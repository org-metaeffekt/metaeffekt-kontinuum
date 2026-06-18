package org.metaeffekt.kontinuum.models.shared;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
public class AssetPlan {

    private boolean requireFetch = false;

    private boolean requireContainerInspect = false;

    private boolean requireExtract = false;

    private boolean requireAggregation = false;

    private boolean requireReferenceEnrichment = false;

    private boolean requirePortfolioIntegration = false;

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

        if (pipelineConfiguration.getOptions().getGlobal().getEnableResolve()
                && Objects.equals(pipelineConfiguration.getPortfolioManager().getResolve(), Boolean.FALSE)) {
            requireResolve = true;
        }

        requireReferenceEnrichment = StringUtils.isNotBlank(asset.getReference());

        if (pipelineConfiguration.getDashboards().stream().anyMatch(d -> d.getAssetId().equals(asset.getId()))) {
            requireVulnerabilityEnrichment = true;
            requireDashboardGeneration = true;
        }

        if (pipelineConfiguration.getPortfolioManager() != null) {
            requirePortfolioIntegration = true;
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
                .filter(r -> r.getAssetId().equals(assetId))
                .toList();

        boolean enrichmentDoneByPortfolioManager = Optional.ofNullable(pipelineConfiguration.getPortfolioManager())
                .map(PipelineConfiguration.PortfolioManager::getEnrich)
                .orElse(Boolean.FALSE);

        boolean scanDoneByPortfolioManager = Optional.ofNullable(pipelineConfiguration.getPortfolioManager())
                .map(PipelineConfiguration.PortfolioManager::getScan)
                .orElse(Boolean.FALSE);


        if (!reportsForAsset.isEmpty()) {
            requireReportGeneration = true;
            for (PipelineConfiguration.Report report : reportsForAsset) {
                List<String> types = report.getTypes();
                if (types == null) {
                    continue;
                }
                for (String type : types) {
                    if (type == null) {
                        continue;
                    }
                    if ((type.equals(ReportType.CERT_REPORT.getKey())
                            || type.equals(ReportType.VULNERABILITY_REPORT.getKey())
                            || type.equals(ReportType.VULNERABILITY_SUMMARY_REPORT.getKey()))
                    && !enrichmentDoneByPortfolioManager) {

                        requireVulnerabilityEnrichment = true;

                    } else if ((type.equals(ReportType.INITIAL_LICENSE_DOCUMENTATION.getKey())
                            || type.equals(ReportType.LICENSE_DOCUMENTATION.getKey()))
                    && !scanDoneByPortfolioManager) {

                        requireLicenseScan = true;
                    }
                }
            }
        }
    }
}
