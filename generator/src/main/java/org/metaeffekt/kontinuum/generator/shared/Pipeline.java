package org.metaeffekt.kontinuum.generator.shared;

import org.metaeffekt.kontinuum.models.shared.*;
import org.metaeffekt.kontinuum.util.MissingValueCollector;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Pipeline {

    List<AssetPlan> assetPlans = new ArrayList<>();
    List<ProcessorDefinitions.Processor> processors = new ArrayList<>();
    private final PipelineConfiguration pipelineConfiguration;

    private final Workspace workspace;

    private final EnvironmentConfiguration environmentConfiguration;
    private final YamlProcessorCatalog yamlProcessorCatalog = new YamlProcessorCatalog();

    public Pipeline(PipelineConfiguration pipelineConfiguration, EnvironmentConfiguration environmentConfiguration) {
        this.environmentConfiguration = environmentConfiguration;
        this.pipelineConfiguration = pipelineConfiguration;
        this.workspace = new Workspace(pipelineConfiguration);

        pipelineConfiguration.getProjectProperties().getAssets()
                .forEach(a -> assetPlans.add(new AssetPlan(a, pipelineConfiguration)));
    }

    public List<ProcessorDefinitions.Processor> generatePipeline() {
        MissingValueCollector collector = new MissingValueCollector();
        addAssetIndependentProcessors(collector);
        addReportProcessors(collector);
        collector.check();
        return processors;
    }

    private void addAssetIndependentProcessors(MissingValueCollector collector) {
        for (AssetPlan assetPlan : assetPlans) {
            addInspectImageProcessor(assetPlan, collector);
            addScanDirectoryProcessor(assetPlan, collector);
            addEnrichWithReferenceProcessor(assetPlan, collector);
            addResolveProcessor(assetPlan, collector);
            addScanProcessor(assetPlan, collector);
        }
    }

    private void addReportProcessors(MissingValueCollector collector) {
        for (AssetPlan assetPlan : assetPlans) {
            addVulnerabilityEnrichmentProcessor(assetPlan, collector);
        }
    }

    private void addInspectImageProcessor(AssetPlan assetPlan, MissingValueCollector collector) {
        if (!assetPlan.isRequireContainerInspect()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("scan-directory");

        processor.setProcessorParameter("output.dir", workspace.getPreparedDirForAsset(asset).toString());
        processor.setProcessorParameter("param.image.id",
            collector.require("asset[" + asset + "].containerResolver.image", () -> asset.getContainerResolver().getImage()));
        processor.setProcessorParameter("param.image.version",
            collector.require("asset[" + asset + "].containerResolver.tag", () -> asset.getContainerResolver().getTag()));

        processors.add(processor);
    }

    private void addScanDirectoryProcessor(AssetPlan assetPlan, MissingValueCollector collector) {
        if (!assetPlan.isRequireExtract()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("scan-directory");

        if (assetPlan.isRequireFetch()) {
            processor.setProcessorParameter("input.extract.dir", workspace.getFetchedDirForAsset(asset).toString());
        } else if (assetPlan.isRequireContainerInspect()) {
            processor.setProcessorParameter("input.extract.dir", workspace.getExtractedDirForAsset(asset).toString());
        }

        processor.setProcessorParameter("output.scan.dir", workspace.getPreparedDirForAsset(asset).toString() + "scan/");
        processor.setProcessorParameter("output.inventory.file", workspace.getPreparedDirForAsset(asset).appendAssetInventory());

        String referenceDir = collector.require("asset[" + asset + "].referenceDir", () -> asset.getReferenceDir());
        if (referenceDir != null) {
            processor.setProcessorParameter("param.reference.inventory.dir", referenceDir);
        }

        processors.add(processor);
    }

    private void addEnrichWithReferenceProcessor(AssetPlan assetPlan, MissingValueCollector collector) {
        if (!assetPlan.isRequireReferenceEnrichment()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("enrich-with-reference");

        processor.setProcessorParameter("input.inventory.file", workspace.getAggregatedDirForAsset(asset).appendAssetInventory());

        String reference = collector.require("asset[" + asset + "].reference", () -> asset.getReference());
        if (reference != null) {
            if (Files.isDirectory(Path.of(reference))) {
                processor.setProcessorParameter("param.reference.inventory.dir", reference);
            } else {
                processor.setProcessorParameter("param.reference.inventory.file", reference);
            }
        }

        processor.setProcessorParameter("output.inventory.file", workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        processors.add(processor);
    }

    private void addResolveProcessor(AssetPlan assetPlan, MissingValueCollector collector) {
        if (!assetPlan.isRequireResolve()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();

        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("resolve-inventory");
        processor.setProcessorParameter("input.inventory.file", workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("output.inventory.file", workspace.getResolvedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("param.artifact.resolver.config.file",
            collector.require("environment.ARTIFACT_RESOLVER_CONFIG_FILE", () -> environmentConfiguration.ARTIFACT_RESOLVER_CONFIG_FILE));
        processor.setProcessorParameter("param.artifact.resolver.proxy.file",
            collector.require("environment.ARTIFACT_RESOLVER_PROXY_FILE", () -> environmentConfiguration.ARTIFACT_RESOLVER_PROXY_FILE));
        processor.setProcessorParameter("env.maven.index.dir", workspace.MAVEN_INDEX_DIR);

        processors.add(processor);
    }

    private void addScanProcessor(AssetPlan assetPlan, MissingValueCollector collector) {
        if (!assetPlan.isRequireLicenseScan()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("scan-inventory");

        if (assetPlan.isRequireResolve()) {
            processor.setProcessorParameter("input.inventory.file", workspace.getResolvedDirForAsset(asset).appendAssetInventory());
        } else {
            processor.setProcessorParameter("input.inventory.file", workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        }

        processor.setProcessorParameter("output.inventory.file", workspace.getScannedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("input.output.analysis.base.dir", workspace.getScannedDirForAsset(asset) + "analysis/");
        processor.setProcessorParameter("param.properties.file",
            collector.require("environment.SCAN_PROPERTIES_FILE", () -> environmentConfiguration.SCAN_PROPERTIES_FILE));
        processor.setProcessorParameter("env.kosmos.password",
            collector.require("environment.KOSMOS_PASSWORD", () -> environmentConfiguration.KOSMOS_PASSWORD));
        processor.setProcessorParameter("env.kosmos.userkeys.file",
            environmentConfiguration.KOSMOS_USERKEYS_FILE);

        processors.add(processor);
    }

    private void addVulnerabilityEnrichmentProcessor(AssetPlan assetPlan, MissingValueCollector collector) {
        if (!assetPlan.isRequireVulnerabilityEnrichment()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("enrich-inventory");

        if (assetPlan.isRequireResolve()) {
            processor.setProcessorParameter("input.inventory.file", workspace.getResolvedDirForAsset(asset).appendAssetInventory());
        } else {
            processor.setProcessorParameter("input.inventory.file", workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        }

        processor.setProcessorParameter("output.inventory.file", workspace.getAdvisedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("output.tmp.dir", workspace.getAdvisedDirForAsset(asset) + "tmp/");
        processor.setProcessorParameter("param.correlation.dir",
            collector.require("environment.correlationDir", environmentConfiguration::getCorrelationDir));

        PipelineConfiguration.Options.EnrichmentOptions enrichment = pipelineConfiguration.getOptions().getEnrichment();

        processor.setProcessorParameter("param.security.policy.file",
            collector.require("enrichment.securityPolicyFile", enrichment::getSecurityPolicyFile));
        processor.setProcessorParameter("param.security.policy.active.ids",
            collector.require("enrichment.securityPolicyActiveIds",
                () -> enrichment.getSecurityPolicyActiveIds() != null ? String.join(",", enrichment.getSecurityPolicyActiveIds()) : null));

        processor.setProcessorParameter("param.activate.msrc", String.valueOf(enrichment.getActivateMsrc()));
        processor.setProcessorParameter("param.activate.nvd", String.valueOf(enrichment.getActivateNvd()));
        processor.setProcessorParameter("param.activate.certfr", String.valueOf(enrichment.getActivateCertFr()));
        processor.setProcessorParameter("param.activate.certeu", String.valueOf(enrichment.getActivateCertEu()));
        processor.setProcessorParameter("param.activate.certsei", String.valueOf(enrichment.getActivateCertSei()));
        processor.setProcessorParameter("param.activate.kev", String.valueOf(enrichment.getActivateKev()));
        processor.setProcessorParameter("param.activate.epss", String.valueOf(enrichment.getActivateEpss()));
        processor.setProcessorParameter("param.activate.eol", String.valueOf(enrichment.getActivateEol()));
        processor.setProcessorParameter("param.activate.osv", String.valueOf(enrichment.getActivateOsv()));
        processor.setProcessorParameter("param.activate.csaf", String.valueOf(enrichment.getActivateCsaf()));

        processor.setProcessorParameter("env.vulnerability.mirror.dir",
            collector.require("environment.vulnerabilityMirrorDir", () -> null));
        processor.setProcessorParameter("param.assessment.dirs",
            collector.require("pipeline.reports.assessmentDirs", () -> null));
        processor.setProcessorParameter("param.context.dirs",
            collector.require("pipeline.reports.contextDirs", () -> null));

        processors.add(processor);
    }
}
