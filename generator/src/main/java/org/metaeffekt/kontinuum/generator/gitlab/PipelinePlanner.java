package org.metaeffekt.kontinuum.generator.gitlab;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.metaeffekt.kontinuum.models.gitlab.PipelineConfiguration;
import org.metaeffekt.kontinuum.models.shared.AssetPlan;
import org.metaeffekt.kontinuum.models.shared.ReportType;
import org.metaeffekt.kontinuum.models.shared.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PipelinePlanner {

    private final PipelineConfiguration pipelineConfiguration;

    @Getter
    private final List<AssetPlan> assetPlans = new ArrayList<>();

    @Getter
    private final Set<Stage> requiredStages = new HashSet<>();

    public PipelinePlanner(PipelineConfiguration pipelineConfiguration) {
        this.pipelineConfiguration = pipelineConfiguration;
        generatePipelinePlan();
    }

    private void generatePipelinePlan() {
        for (PipelineConfiguration.ProjectProperties.Asset asset : pipelineConfiguration.getProjectProperties().getAssets()) {
            generatePipelinePlanForAsset(asset.getId(), asset);
        }

        assetPlans.forEach(a -> requiredStages.addAll(a.getRequiredStages()));
    }

    private void generatePipelinePlanForAsset(String assetId, PipelineConfiguration.ProjectProperties.Asset asset) {
        AssetPlan assetPlan = new AssetPlan(asset);
        if (StringUtils.isNotBlank(asset.getUrlResolver().getUrl())) {
            assetPlan.addStage(Stage.FETCH);
            assetPlan.addStage(Stage.PREPARE);
        }

        assetPlan.addStage(Stage.AGGREGATE);

        if (pipelineConfiguration.getOptions().getGlobal().getEnableResolve()) {
            assetPlan.addStage(Stage.RESOLVE);
        }

        addReportStagesForAsset(assetId, assetPlan);
        addDashboardStagesForAsset(assetId, assetPlan);

        if (pipelineConfiguration.getReports().stream().anyMatch(r -> r.getAssets().contains(assetId))
        && pipelineConfiguration.getDashboards().stream().anyMatch(r -> r.getAssets().contains(assetId))) {
            assetPlan.addStage(Stage.SUMMARIZE);
        }
        assetPlans.add(assetPlan);
    }

    private void addReportStagesForAsset(String assetId, AssetPlan assetPlan) {
        List<PipelineConfiguration.Report> reportsContainingAsset = pipelineConfiguration.getReports()
                .stream()
                .filter(r -> r.getAssets().contains(assetId))
                .toList();

        for (PipelineConfiguration.Report report : reportsContainingAsset) {
            assetPlan.addStage(Stage.GROUP);
            String type = report.getType();
            if (type.equals(ReportType.VULNERABILITY_REPORT.getKey())
            || type.equals(ReportType.VULNERABILITY_SUMMARY_REPORT.getKey())
            || type.equals(ReportType.CERT_REPORT.getKey())) {
                assetPlan.addStage(Stage.ADVISE);
            }

            if (type.equals(ReportType.INITIAL_LICENSE_DOCUMENTATION.getKey())
                    || type.equals(ReportType.LICENSE_DOCUMENTATION.getKey())) {
                assetPlan.addStage(Stage.SCAN);
            }
        }
    }

    private void addDashboardStagesForAsset(String assetId, AssetPlan assetPlan) {
        List<PipelineConfiguration.Dashboard> dashboardContainingAsset = pipelineConfiguration.getDashboards()
                .stream()
                .filter(d -> d.getAssets().contains(assetId))
                .toList();

        for (PipelineConfiguration.Dashboard dashboard : dashboardContainingAsset) {
            if (dashboard.getAssets().contains(assetId)) {
                assetPlan.addStage(Stage.ADVISE);
            }
        }
    }
}
