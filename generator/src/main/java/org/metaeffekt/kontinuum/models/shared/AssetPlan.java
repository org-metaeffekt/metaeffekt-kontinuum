package org.metaeffekt.kontinuum.models.shared;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Getter
public class AssetPlan {

    private boolean requireFetch = false;

    private boolean requireContainerInspect = false;

    private boolean requireExtract = false;

    private boolean requireAggregation = false;

    private boolean requireReferenceEnrichment = false;

    private boolean requireResolve = false;

    private boolean requireLicenseScan = false;

    private boolean requireVulnerabilityEnrichment = false;

    private boolean requireSpdx = false;

    private boolean requireCycloneDx = false;

    private boolean requireDashboardGeneration = false;

    private boolean requireReportGeneration = false;

    private PipelineConfiguration.ProjectProperties.Asset asset;

    public AssetPlan(PipelineConfiguration.ProjectProperties.Asset asset, PipelineConfiguration pipelineConfiguration) {
        this.asset = asset;
        requireSpdx = pipelineConfiguration.getOptions().getGlobal().getEnableSpdxBom();
        requireCycloneDx = pipelineConfiguration.getOptions().getGlobal().getEnableCycloneDxBom();
        requireResolve = pipelineConfiguration.getOptions().getGlobal().getEnableResolve();
        requireReferenceEnrichment = StringUtils.isNotBlank(asset.getReference());

        if (pipelineConfiguration.getDashboards().stream().anyMatch(d -> d.getAssets().contains(asset.getId()))) {
            requireVulnerabilityEnrichment = true;
            requireDashboardGeneration = true;
        }

        try {
            URL assetUrl = new URL(asset.getUrlResolver().getUrl());

            requireAggregation = true;
            if (!"file".equals(assetUrl.getProtocol())) {
                requireFetch = true;
                requireExtract = true;
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateException( "The URLResolver for asset " + asset + " must contain a valid URL.",e);
        }

        evaluateReports(asset.getId(), pipelineConfiguration);

    }

    private void evaluateReports(String assetId, PipelineConfiguration pipelineConfiguration) {
        List<PipelineConfiguration.Report> reportsForAsset = pipelineConfiguration.getReports()
                .stream()
                .filter(r -> r.getAssets().contains(assetId))
                .toList();

        if (!reportsForAsset.isEmpty()) {
            requireReportGeneration = true;
            for (PipelineConfiguration.Report report : reportsForAsset) {
                if (report.getType().equals(ReportType.CERT_REPORT.getKey())
                        || report.getType().equals(ReportType.VULNERABILITY_REPORT.getKey())
                        || report.getType().equals(ReportType.VULNERABILITY_SUMMARY_REPORT.getKey())) {

                    requireVulnerabilityEnrichment = true;

                } else if (report.getType().equals(ReportType.INITIAL_LICENSE_DOCUMENTATION.getKey())
                        || report.getType().equals(ReportType.LICENSE_DOCUMENTATION.getKey())) {
                    requireLicenseScan = true;
                }
            }
        }
    }
}
