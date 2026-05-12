package org.metaeffekt.kontinuum.models.shared;

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

        generatePipeline();
    }

    private void generatePipeline() {
        addAssetIndependentProcessors();
    }

    /**
     * This method determines the order in which processor are added to the processors list.
     */
    private void addAssetIndependentProcessors() {
        for (AssetPlan assetPlan : assetPlans) {
            addInspectImageProcessor(assetPlan);
            addScanDirectoryProcessor(assetPlan);
            addEnrichWithReferenceProcessor(assetPlan);
            addResolveProcessor(assetPlan);
            addScanProcessor(assetPlan);
        }
    }

    private void addInspectImageProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireContainerInspect()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("scan-directory");

        processor.setProcessorParameter("output.dir", workspace.getPreparedDirForAsset(asset).toString());
        processor.setProcessorParameter("param.image.id", asset.getContainerResolver().getImage());
        processor.setProcessorParameter("param.image.version", asset.getContainerResolver().getTag());

        processors.add(processor);
    }

    private void addScanDirectoryProcessor(AssetPlan assetPlan) {
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
        processor.setProcessorParameter("param.reference.inventory.dir", asset.getReferenceDir());

        processors.add(processor);
    }

    private void addEnrichWithReferenceProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireReferenceEnrichment()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("enrich-with-reference");

        processor.setProcessorParameter("input.inventory.file", workspace.getAggregatedDirForAsset(asset).appendAssetInventory());

        if (Files.isDirectory(Path.of(asset.getReference()))) {
            processor.setProcessorParameter("param.reference.inventory.dir", asset.getReference());
        } else {
            processor.setProcessorParameter("param.reference.inventory.file", asset.getReference());
        }

        processor.setProcessorParameter("output.inventory.file", workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        processors.add(processor);
    }

    private void addResolveProcessor(AssetPlan assetPlan) {
        if (!assetPlan.isRequireResolve()) { return; }

        PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();

        ProcessorDefinitions.Processor processor = yamlProcessorCatalog.getProcessorById("resolve-inventory");
        processor.setProcessorParameter("input.inventory.file", workspace.getAggregatedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("output.inventory.file", workspace.getResolvedDirForAsset(asset).appendAssetInventory());
        processor.setProcessorParameter("param.artifact.resolver.config.file", environmentConfiguration.ARTIFACT_RESOLVER_CONFIG_FILE);
        processor.setProcessorParameter("param.artifact.resolver.proxy.file", environmentConfiguration.ARTIFACT_RESOLVER_PROXY_FILE);
        processor.setProcessorParameter("env.maven.index.dir", workspace.MAVEN_INDEX_DIR);

        processors.add(processor);
    }

    private void addScanProcessor(AssetPlan assetPlan) {
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
        processor.setProcessorParameter("param.properties.file", environmentConfiguration.SCAN_PROPERTIES_FILE);
        processor.setProcessorParameter("env.kosmos.password", environmentConfiguration.KOSMOS_PASSWORD);
        processor.setProcessorParameter("env.kosmos.userkeys.file", environmentConfiguration.KOSMOS_USERKEYS_FILE);

        processors.add(processor);
    }

    private void addVulnerabilityEnrichmentProcessor(AssetPlan assetPlan) {
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
        processor.setProcessorParameter("param.correlation.dir", environmentConfiguration.getCorrelationDir());
        processor.setProcessorParameter("param.assessment.dirs");
        processor.setProcessorParameter("param.context.dirs");
        processor.setProcessorParameter("param.security.policy.file", );
        processor.setProcessorParameter("param.security.policy.active.ids");
        processor.setProcessorParameter("param.activate.msrc");
        processor.setProcessorParameter("param.activate.nvd");
        processor.setProcessorParameter("param.activate.certfr");
        processor.setProcessorParameter("param.activate.certeu");
        processor.setProcessorParameter("param.activate.certsei");
        processor.setProcessorParameter("param.activate.kev");
        processor.setProcessorParameter("param.activate.epss");
        processor.setProcessorParameter("param.activate.eol");
        processor.setProcessorParameter("param.activate.osv");
        processor.setProcessorParameter("param.activate.csaf");
        processor.setProcessorParameter("param.activate.purl.derivation");
        processor.setProcessorParameter("param.activate.status");
        processor.setProcessorParameter("param.activate.keywords");
        processor.setProcessorParameter("param.activate.validation");
        processor.setProcessorParameter("param.exclude.nvd.equivalent.msrc");
        processor.setProcessorParameter("param.exclude.nvd.equivalent.osv");
        processor.setProcessorParameter("param.vulnerabilities.custom.dir");
        processor.setProcessorParameter("env.vulnerability.mirror.dir");

        processors.add(processor);
    }
}
