package org.metaeffekt.kontinuum.generator.shared;

import org.metaeffekt.kontinuum.models.shared.*;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.Dashboard;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.Report;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.Options.EnrichmentOptions;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.ProjectProperties.Asset;
import org.metaeffekt.kontinuum.models.shared.ProcessorDefinitions.Processor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pipeline {

    private final List<AssetPlan> assetPlans = new ArrayList<>();
    private final Map<Asset, List<Processor>> assetProcessorsMap = new HashMap<>();

    private final PipelineConfiguration pipelineConfiguration;

    private final Workspace workspace;

    private final EnvironmentConfiguration environmentConfiguration;
    private final YamlProcessorCatalog yamlProcessorCatalog = new YamlProcessorCatalog();

    public Pipeline(PipelineConfiguration pipelineConfiguration,
                    EnvironmentConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;
        this.pipelineConfiguration = pipelineConfiguration;
        this.workspace = new Workspace(pipelineConfiguration, environmentConfiguration);

        if (pipelineConfiguration.getOptions() == null) {
            pipelineConfiguration.setOptions(new PipelineConfiguration.Options());
        }

        pipelineConfiguration.getProjectProperties().getAssets()
                .forEach(a -> assetPlans.add(new AssetPlan(a, pipelineConfiguration)));
    }

    public Map<Asset, List<Processor>> generatePipeline() {
        for (AssetPlan assetPlan : assetPlans) {
            addFetchProcessor(assetPlan);
            addInspectImageProcessor(assetPlan);
            addScanDirectoryProcessor(assetPlan);
            addEnrichWithReferenceProcessor(assetPlan);
            addResolveProcessor(assetPlan);
            addScanProcessor(assetPlan);
            addVulnerabilityEnrichmentProcessor(assetPlan);
            addReportProcessors(assetPlan);
            addDashboardProcessors(assetPlan);
        }

        return assetProcessorsMap;
    }

    private void addDashboardProcessors(AssetPlan assetPlan) {
        List<Dashboard> dashboards = pipelineConfiguration.getDashboards();
        if (dashboards == null || dashboards.isEmpty()) {
            return;
        }

        for (Dashboard dashboard : dashboards) {
            if (!dashboard.getAssetId().equals(assetPlan.getAsset().getId())) {
                continue;
            }

            EnrichmentOptions enrichmentOptions = pipelineConfiguration.getOptions().getEnrichment();
            Processor processor = yamlProcessorCatalog.getProcessorById("create-dashboard");
            PipelineConfiguration.ProjectProperties.Project project = pipelineConfiguration
                    .getProjectProperties()
                    .getProject();

            processor.setProcessorParameter("input.inventory.file",
                    workspace.getAdvisedDirForAsset(assetPlan.getAsset()).appendAssetInventory());
            processor.setProcessorParameter("output.dashboard.file",
                    workspace.getAdvisedDirForAsset(assetPlan.getAsset()).toString() + "dashboard.html");
            processor.setProcessorParameter("param.security.policy.file",
                    enrichmentOptions.getSecurityPolicyFile());
            processor.setProcessorParameter("param.security.policy.active.ids",
                    enrichmentOptions.getSecurityPolicyActiveIds() != null
                            ? String.join(",",
                            pipelineConfiguration.getOptions()
                                    .getEnrichment()
                                    .getSecurityPolicyActiveIds()) : null);
            processor.setProcessorParameter("param.tenant.id",
                    project.getTenant());
            processor.setProcessorParameter("param.asset.id",
                    assetPlan.getAsset().getAssessmentId());
            processor.setProcessorParameter("param.assessment.context",
                    assetPlan.getAsset().getContext());
            processor.setProcessorParameter("env.vulnerability.mirror.dir",
                    environmentConfiguration.VULNERABILITY_MIRROR_DIR);

            assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
        }
    }

    private void addReportProcessors(AssetPlan assetPlan) {
        List<Report> reports = pipelineConfiguration.getReports();
        if (reports == null || reports.isEmpty()) {
            return;
        }

        for (Report report : reports) {
            if (!report.getAssetId().equals(assetPlan.getAsset().getId())) {
                continue;
            }

            List<String> types = report.getTypes();
            if (types == null || types.isEmpty()) {
                continue;
            }

            for (String type : types) {
                if (type == null) {
                    continue;
                }
                Processor processor = yamlProcessorCatalog.getProcessorById("create-document");
                processor.setProcessorParameter("input.inventory.dir",
                        workspace.getGroupedDir(ReportType.fromKey(type)));

                processor.setProcessorParameter("output.document.file",
                        workspace.getReportedDirForAsset(assetPlan.getAsset()).appendAssetInventory());

                processor.setProcessorParameter("param.computed.inventory.dir",
                        workspace.getReportedDirForAsset(assetPlan.getAsset()).appendAssetInventory());
                processor.setProcessorParameter("param.document.type", type);
                processor.setProcessorParameter("param.document.language",
                        pipelineConfiguration.getOptions().getGlobal().getDocumentLanguage());

                processor.setProcessorParameter("param.asset.id", assetPlan.getAsset().getId());
                processor.setProcessorParameter("param.asset.name", assetPlan.getAsset().getName());
                processor.setProcessorParameter("param.asset.version", assetPlan.getAsset().getVersion());

                processor.setProcessorParameter("param.product.name",
                        pipelineConfiguration.getProjectProperties().getProject().getName());
                processor.setProcessorParameter("param.product.version",
                        pipelineConfiguration.getProjectProperties().getProject().getVersion());
                processor.setProcessorParameter("param.product.watermark",
                        pipelineConfiguration.getOptions().getDocument().getWatermark());
                processor.setProcessorParameter("param.overview.advisors",
                        report.getOverviewAdvisors() == null || report.getOverviewAdvisors().isEmpty()
                                ? null
                                : String.join(", ", report.getOverviewAdvisors()));
                processor.setProcessorParameter("param.property.selector.organization",
                        pipelineConfiguration.getOptions().getDocument().getOrganization());
                processor.setProcessorParameter("param.property.selector.classification",
                        pipelineConfiguration.getOptions().getDocument().getClassificationRating());
                processor.setProcessorParameter("param.property.selector.control",
                        pipelineConfiguration.getOptions().getDocument().getControlRating());
                processor.setProcessorParameter("param.security.policy.file",
                        pipelineConfiguration.getOptions().getEnrichment().getSecurityPolicyFile());
                processor.setProcessorParameter("param.asset.descriptor.file",
                        ReportType.fromKey(type).getAssetDescriptorFile());
                processor.setProcessorParameter("param.reference.inventory.dir",
                        assetPlan.getAsset().getReferenceDirNormalized(environmentConfiguration.getWorkbenchDirNormalized()));
                processor.setProcessorParameter("param.reference.license.dir", null);
                processor.setProcessorParameter("param.reference.component.dir", null);
                processor.setProcessorParameter("env.kontinuum.dir",
                        environmentConfiguration.getKontinuumDirNormalized());
                processor.setProcessorParameter("env.kontinuum.processors.dir",
                        environmentConfiguration.getKontinuumProcessorsDirNormalized());
                processor.setProcessorParameter("env.workbench.dir",
                        environmentConfiguration.getWorkbenchDirNormalized());
                processor.setProcessorParameter("env.vulnerability.mirror.dir",
                        environmentConfiguration.VULNERABILITY_MIRROR_DIR);

                assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
            }
        }
    }


    private void addFetchProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireFetch()) {
            return;
        }

        Asset asset = assetPlan.getAsset();
        Processor processor = yamlProcessorCatalog.getProcessorById("download-asset");
        processor.setProcessorParameter("param.asset.url", asset.getUrlResolver().getUrl());
        processor.setProcessorParameter("output.asset.dir", workspace.getFetchedDirForAsset(asset).toString());

        assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
    }

    private void addInspectImageProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireContainerInspect()) {
            return;
        }

        Asset asset = assetPlan.getAsset();
        Processor processor = yamlProcessorCatalog.getProcessorById("save-inspect-image");

        processor.setProcessorParameter("output.dir", workspace.getPreparedDirForAsset(asset).toString());
        processor.setProcessorParameter("param.image.id",
                asset.getContainerResolver().getImage());
        processor.setProcessorParameter("param.image.version",
                asset.getContainerResolver().getTag());

        assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
    }

    private void addScanDirectoryProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireExtract()) {
            return;
        }

        Asset asset = assetPlan.getAsset();
        Processor processor = yamlProcessorCatalog.getProcessorById("scan-directory");

        if (assetPlan.isRequireFetch()) {
            processor.setProcessorParameter("input.extract.dir",
                    workspace.getFetchedDirForAsset(asset).toString());
        } else if (assetPlan.isRequireContainerInspect()) {
            processor.setProcessorParameter("input.extract.dir",
                    workspace.getExtractedDirForAsset(asset).toString());
        }

        processor.setProcessorParameter("output.scan.dir",
                workspace.getPreparedDirForAsset(asset).toString() + "scan/");
        processor.setProcessorParameter("output.inventory.file",
                workspace.getPreparedDirForAsset(asset).appendAssetInventory());

        String referenceDir = asset.getReferenceDirNormalized(environmentConfiguration.getWorkbenchDirNormalized());

        if (referenceDir != null) {
            processor.setProcessorParameter("param.reference.inventory.dir", referenceDir);
        }

        assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
    }

    private void addEnrichWithReferenceProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireReferenceEnrichment()) {
            return;
        }

        Asset asset = assetPlan.getAsset();
        Processor processor = yamlProcessorCatalog
                .getProcessorById("enrich-with-reference");

        processor.setProcessorParameter("input.inventory.file",
                workspace.getAggregatedDirForAsset(asset).appendAssetInventory());

        String reference = asset.getReference();
        if (reference != null) {
            if (Files.isDirectory(Path.of(reference))) {
                processor.setProcessorParameter("param.reference.inventory.dir", reference);
            } else {
                processor.setProcessorParameter("param.reference.inventory.file", reference);
            }
        }

        processor.setProcessorParameter("output.inventory.file",
                workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
    }

    private void addResolveProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireResolve()) {
            return;
        }

        Asset asset = assetPlan.getAsset();
        Processor processor = yamlProcessorCatalog.getProcessorById("resolve-inventory");

        if (assetPlan.isRequireAggregation()) {
            processor.setProcessorParameter("input.inventory.file",
                    workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        } else {
            processor.setProcessorParameter("input.inventory.file",
                    workspace.getPreparedDirForAsset(asset).appendAssetInventory());
        }

        processor.setProcessorParameter("output.inventory.file",
                workspace.getResolvedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("param.artifact.resolver.config.file",
                environmentConfiguration.ARTIFACT_RESOLVER_CONFIG_FILE);
        processor.setProcessorParameter("param.artifact.resolver.proxy.file",
                environmentConfiguration.ARTIFACT_RESOLVER_PROXY_FILE);
        processor.setProcessorParameter("env.maven.index.dir", workspace.MAVEN_INDEX_DIR);

        assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
    }

    private void addScanProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireLicenseScan()) {
            return;
        }

        Asset asset = assetPlan.getAsset();
        Processor processor = yamlProcessorCatalog.getProcessorById("scan-inventory");

        if (assetPlan.isRequireResolve()) {
            processor.setProcessorParameter("input.inventory.file",
                    workspace.getResolvedDirForAsset(asset).appendAssetInventory());
        } else if (assetPlan.isRequireAggregation()) {
            processor.setProcessorParameter("input.inventory.file",
                    workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        } else {
            processor.setProcessorParameter("input.inventory.file",
                    workspace.getPreparedDirForAsset(asset).appendAssetInventory());
        }

        processor.setProcessorParameter("output.inventory.file",
                workspace.getScannedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("input.output.analysis.base.dir",
                workspace.getScannedDirForAsset(asset) + "analysis/");
        processor.setProcessorParameter("param.properties.file",
                environmentConfiguration.SCAN_PROPERTIES_FILE);
        processor.setProcessorParameter("env.kosmos.password",
                environmentConfiguration.KOSMOS_PASSWORD);
        processor.setProcessorParameter("env.kosmos.userkeys.file",
                environmentConfiguration.KOSMOS_USERKEYS_FILE);

        assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
    }

    private void addVulnerabilityEnrichmentProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireVulnerabilityEnrichment()) {
            return;
        }

        Asset asset = assetPlan.getAsset();
        Processor processor = yamlProcessorCatalog.getProcessorById("enrich-inventory");

        if (assetPlan.isRequireResolve()) {
            processor.setProcessorParameter("input.inventory.file",
                    workspace.getResolvedDirForAsset(asset).appendAssetInventory());
        } else if (assetPlan.isRequireAggregation()) {
            processor.setProcessorParameter("input.inventory.file",
                    workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        } else {
            processor.setProcessorParameter("input.inventory.file",
                    workspace.getPreparedDirForAsset(asset).appendAssetInventory());
        }

        processor.setProcessorParameter("output.inventory.file",
                workspace.getAdvisedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("output.tmp.dir", workspace.getAdvisedDirForAsset(asset) + "tmp/");
        processor.setProcessorParameter("param.correlation.dir",
                environmentConfiguration.getCorrelationDir());

        PipelineConfiguration.Options.EnrichmentOptions enrichment = pipelineConfiguration.getOptions()
                .getEnrichment();

        processor.setProcessorParameter("param.security.policy.file", enrichment.getSecurityPolicyFile());
        processor.setProcessorParameter("param.security.policy.active.ids",
                enrichment.getSecurityPolicyActiveIds() != null
                        ? String.join(",", enrichment.getSecurityPolicyActiveIds())
                        : null);

        processor.setProcessorParameter("param.activate.msrc", String.valueOf(enrichment.getActivateMsrc()));
        processor.setProcessorParameter("param.activate.nvd", String.valueOf(enrichment.getActivateNvd()));
        processor.setProcessorParameter("param.activate.certfr",
                String.valueOf(enrichment.getActivateCertFr()));
        processor.setProcessorParameter("param.activate.certeu",
                String.valueOf(enrichment.getActivateCertEu()));
        processor.setProcessorParameter("param.activate.certsei",
                String.valueOf(enrichment.getActivateCertSei()));
        processor.setProcessorParameter("param.activate.kev", String.valueOf(enrichment.getActivateKev()));
        processor.setProcessorParameter("param.activate.epss", String.valueOf(enrichment.getActivateEpss()));
        processor.setProcessorParameter("param.activate.eol", String.valueOf(enrichment.getActivateEol()));
        processor.setProcessorParameter("param.activate.osv", String.valueOf(enrichment.getActivateOsv()));
        processor.setProcessorParameter("param.activate.csaf", String.valueOf(enrichment.getActivateCsaf()));
        PipelineConfiguration.ProjectProperties.Project project = pipelineConfiguration.getProjectProperties()
                .getProject();
        processor.setProcessorParameter("param.assessment.dirs", asset.getAssessmentDir(project, environmentConfiguration.getWorkbenchDirNormalized()));
        processor.setProcessorParameter("param.context.dirs", asset.getContextDir(project, environmentConfiguration.getWorkbenchDirNormalized()));

        processor.setProcessorParameter("env.vulnerability.mirror.dir",
                environmentConfiguration.VULNERABILITY_MIRROR_DIR);

        assetProcessorsMap.computeIfAbsent(assetPlan.getAsset(), k -> new ArrayList<>()).add(processor);
    }
}
