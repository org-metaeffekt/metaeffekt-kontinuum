package org.metaeffekt.kontinuum.generator.gitlab;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.metaeffekt.kontinuum.models.gitlab.IncludedComponent;
import org.metaeffekt.kontinuum.models.gitlab.Job;
import org.metaeffekt.kontinuum.models.gitlab.PipelineConfiguration;
import org.metaeffekt.kontinuum.models.gitlab.PipelineConfigurationLoader;
import org.metaeffekt.kontinuum.models.shared.AssetPlan;
import org.metaeffekt.kontinuum.models.shared.Stage;
import org.metaeffekt.kontinuum.models.shared.Workspace;


import java.io.File;
import java.util.List;

@Slf4j
public class PipelineGenerator implements Generator {

    @Getter
    private final StringBuilder pipelineDocument = new StringBuilder();
    private final GeneratorConfig config;

    private final Workspace workspace;

    private final PipelineConfiguration pipelineConfiguration;

    public PipelineGenerator(GeneratorConfig config) {
        this.config = config;

        File pipelineConfigFile = new File(config.getPipelineConfigPath());
        PipelineConfigurationLoader pipelineConfigurationLoader = new PipelineConfigurationLoader(pipelineConfigFile);
        this.pipelineConfiguration = pipelineConfigurationLoader.readConfig();

        this.workspace = new Workspace(config.getMountedVolume(), pipelineConfiguration.getProjectProperties().getProject().toString());
    }

    @Override
    public void generate() {
        PipelinePlanner pipelinePlanner = new PipelinePlanner(pipelineConfiguration);
        generatePipelineStagesSection(pipelinePlanner);
        generatePipelineVariablesSection();
        generatePipelineDefaultSection();
        generateIncludedComponentsSection(pipelinePlanner);
        generateStandaloneJobs(pipelinePlanner);
    }

    private void generatePipelineStagesSection(PipelinePlanner pipelinePlanner) {
        StringBuilder pipelineStages = new StringBuilder();
        pipelineStages.append("stages:").append(System.lineSeparator());

        pipelinePlanner.getRequiredStages().stream()
                .sorted()
                .forEach(s -> pipelineStages.append("  - ")
                        .append(s.name())
                        .append(System.lineSeparator()));

        pipelineDocument.append(pipelineStages).append(System.lineSeparator());
    }

    private void generatePipelineVariablesSection() {
        StringBuilder pipelineVariables = new StringBuilder();
        pipelineVariables.append("variables:").append(System.lineSeparator())
                .append("  GIT_DEPTH: ").append(config.getGitDepth()).append(System.lineSeparator())
                .append("  GIT_STRATEGY: ").append(config.getGitStrategy()).append(System.lineSeparator())
                .append("  CONTAINER_IMAGE: ").append(config.getContainerImage()).append(System.lineSeparator());

        pipelineDocument.append(pipelineVariables).append(System.lineSeparator());
    }

    private void generatePipelineDefaultSection() {
        StringBuilder defaultContent = new StringBuilder();

        if (StringUtils.isNotBlank(config.getRunnerTag())) {
            defaultContent.append("  tags:").append(System.lineSeparator())
                    .append("    - ").append(config.getRunnerTag()).append(System.lineSeparator());
        }

        if (!defaultContent.isEmpty()) {
            pipelineDocument.append("default:").append(System.lineSeparator()).append(defaultContent).append(System.lineSeparator());
        }
    }

    private void generateIncludedComponentsSection(PipelinePlanner pipelinePlanner) {
        StringBuilder includedComponents = new StringBuilder();
        includedComponents.append("include:").append(System.lineSeparator());

        List<PipelineConfiguration.ProjectProperties.Asset> assetsRequiringPrepare = getAssetsForStage(pipelinePlanner, Stage.PREPARE);
        if (!assetsRequiringPrepare.isEmpty()) {
            includedComponents.append(generateIncludedPreparedComponents(assetsRequiringPrepare));
        }

        List<PipelineConfiguration.ProjectProperties.Asset> assetsRequiringAggregate = getAssetsForStage(pipelinePlanner, Stage.AGGREGATE);
        if (!assetsRequiringPrepare.isEmpty()) {
            includedComponents.append(generateIncludedAggregatedComponents(assetsRequiringAggregate));
        }

        List<PipelineConfiguration.ProjectProperties.Asset> assetsRequiringResolve = getAssetsForStage(pipelinePlanner, Stage.RESOLVE);
        if (!assetsRequiringResolve.isEmpty()) {
            includedComponents.append(generateIncludedResolvedComponents(assetsRequiringResolve));
        }

        List<AssetPlan> assetPlansForAssetsRequiringAdvise = pipelinePlanner.getAssetPlans()
                .stream().filter(a -> a.getRequiredStages().contains(Stage.ADVISE)).toList();

        if (!assetPlansForAssetsRequiringAdvise.isEmpty()) {
            includedComponents.append(generateIncludedAdvisedComponents(assetPlansForAssetsRequiringAdvise));
        }

        pipelineDocument.append(includedComponents.append(System.lineSeparator()));
    }

    private void generateStandaloneJobs(PipelinePlanner pipelinePlanner) {
        StringBuilder standaloneComponents = new StringBuilder();

        List<AssetPlan> assetPlansForAssetsRequiringFetch = pipelinePlanner.getAssetPlans()
                .stream()
                .filter(a -> a.getRequiredStages().contains(Stage.FETCH))
                .toList();

        if (!assetPlansForAssetsRequiringFetch.isEmpty()) {
            standaloneComponents.append(generateFetchJob(assetPlansForAssetsRequiringFetch));
        }

        List<AssetPlan> assetPlansForAssetsRequiringScan = pipelinePlanner.getAssetPlans()
                .stream()
                .filter(a -> a.getRequiredStages().contains(Stage.SCAN))
                .toList();

        if (!assetPlansForAssetsRequiringScan.isEmpty()) {
            log.warn("License and Copyright scan is currently not available in GitLab pipelines. Inventories from the " +
                    "AGGREGATE stage will be used for all designated reports requiring scanned inventories.");
            standaloneComponents.append(generateCopyToScanJob(assetPlansForAssetsRequiringScan));
        }

        List<AssetPlan> assetPlansForAssetsRequiringAdvise = pipelinePlanner.getAssetPlans()
                        .stream()
                                .filter(a -> a.getRequiredStages().contains(Stage.ADVISE))
                                        .toList();

        if (!assetPlansForAssetsRequiringAdvise.isEmpty()) {
            standaloneComponents.append(generateAdvisePreJob(assetPlansForAssetsRequiringAdvise));
        }

        standaloneComponents.append(generateGroupJob(pipelinePlanner.getAssetPlans()));

        pipelineDocument.append(standaloneComponents);
    }

    private String generateIncludedPreparedComponents(List<PipelineConfiguration.ProjectProperties.Asset> assetsRequiringPrepare) {
        StringBuilder includedPreparedComponents = new StringBuilder();

        for (PipelineConfiguration.ProjectProperties.Asset asset : assetsRequiringPrepare) {
            if (StringUtils.isBlank(asset.getReference())) {
                throw new IllegalStateException("Asset " + asset + "requires a reference inventory to be set for the defined pipeline.");
            }

            IncludedComponent includedComponent = new IncludedComponent(IncludedComponent.IncludeScope.LOCAL,
                    ".metaeffekt-components/templates/prepare_scan-directory.yml");
            addJobInputsForIncludedComponent(includedComponent);
            includedComponent.addInput("job_stage", Stage.PREPARE.name());
            includedComponent.addInput("job_prefix", "prepare-" + asset);
            includedComponent.addInput("param_reference-inventory-dir", "$CI_PROJECT_DIR/" + asset.getReference());
            includedComponent.addInput("input_extract-dir", workspace.getFetchedDirForAsset(asset));
            includedComponent.addInput("output_dir", workspace.getPreparedDirForAsset(asset) + "scan/");
            includedComponent.addInput("output_inventory-file", workspace.getPreparedDirForAsset(asset) + asset + ".xlsx");
            includedPreparedComponents.append(includedComponent.toYaml(2));
        }

        return includedPreparedComponents.toString();
    }

    private String generateIncludedAggregatedComponents(List<PipelineConfiguration.ProjectProperties.Asset> assetsRequiringAggregate) {
        StringBuilder includedAggregatedComponents = new StringBuilder();

        for (PipelineConfiguration.ProjectProperties.Asset asset : assetsRequiringAggregate) {
            IncludedComponent includedComponent = new IncludedComponent(IncludedComponent.IncludeScope.LOCAL,
                    ".metaeffekt-components/templates/util_transform-inventories.yml");
            addJobInputsForIncludedComponent(includedComponent);
            includedComponent.addInput("job_stage", Stage.AGGREGATE.name());
            includedComponent.addInput("job_prefix", "aggregate-" + asset);
            includedComponent.addInput("input_inventory-dir", workspace.getPreparedDirForAsset(asset));
            includedComponent.addInput("output_inventory-dir", workspace.getAggregatedDirForAsset(asset));
            includedComponent.addInput("param_kotlin-script-file", "$CI_PROJECT_DIR/scripts/aggregate.kts");
            includedComponent.addInput("param_filter-preset", "simple-copy"); // FIXME: Enable other options to enable asset-parts
            includedComponent.addInput("param_asset-name", asset.toString());

            includedAggregatedComponents.append(includedComponent.toYaml(2));
        }

        return includedAggregatedComponents.toString();
    }

    private String generateIncludedResolvedComponents(List<PipelineConfiguration.ProjectProperties.Asset> assetsRequiringResolve) {
        StringBuilder includedResolvedComponents = new StringBuilder();

        for (PipelineConfiguration.ProjectProperties.Asset asset : assetsRequiringResolve) {
            IncludedComponent includedComponent = new IncludedComponent(IncludedComponent.IncludeScope.LOCAL,
                    ".metaeffekt-components/templates/resolve_resolve-inventory.yml");
            addJobInputsForIncludedComponent(includedComponent);
            includedComponent.addInput("job_stage", Stage.RESOLVE.name());
            includedComponent.addInput("job_prefix", "resolve-" + asset);
            includedComponent.addInput("input_inventory-file", workspace.getAggregatedDirForAsset(asset) + asset + ".xlsx");
            includedComponent.addInput("output_inventory-file", workspace.getResolvedDirForAsset(asset) + asset + ".xlsx");
            includedComponent.addInput("param_artifact-resolver-config-file", "$CI_PROJECT_DIR/config/resolver/artifact-resolver-config.yaml");
            includedComponent.addInput("param_artifact-resolver-proxy-file", "$CI_PROJECT_DIR/config/resolver/artifact-resolver-proxy.yaml");

            includedResolvedComponents.append(includedComponent.toYaml(2));
        }

        return includedResolvedComponents.toString();
    }

    private String generateIncludedAdvisedComponents(List<AssetPlan> assetPlansForAssetsRequiringAdvise) {
        StringBuilder includedAdvisedComponents = new StringBuilder();
        PipelineConfiguration.Options.EnrichmentOptions enrichmentOptions = pipelineConfiguration.getOptions().getEnrichment();

        for (AssetPlan assetPlan : assetPlansForAssetsRequiringAdvise) {
            PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
            IncludedComponent includedComponent = new IncludedComponent(IncludedComponent.IncludeScope.LOCAL,
                    ".metaeffekt-components/templates/advise_enrich-inventory.yml");
            addJobInputsForIncludedComponent(includedComponent);
            includedComponent.addInput("job_stage", Stage.ADVISE.name());
            includedComponent.addInput("job_prefix", "advise-" + asset);
            includedComponent.addInput("param_security-policy-file", "$CI_PROJECT_DIR/" + enrichmentOptions.getSecurityPolicyFile());
            includedComponent.addInput("param_security-policy-active-ids", String.join(",", enrichmentOptions.getSecurityPolicyActiveIds()));
            includedComponent.addInput("param_correlation-dir", "$CI_PROJECT_DIR/correlations");
            includedComponent.addInput("param_processor-temp-dir", workspace.getAdditionalDirForAsset(asset) + "temp");
            includedComponent.addInput("output_inventory-file", workspace.getAdvisedDirForAsset(asset) + asset + ".xlsx");
            includedComponent.addInput("param_activate-msrc", enrichmentOptions.getActivateMsrc());
            includedComponent.addInput("param_activate-nvd", enrichmentOptions.getActivateNvd());
            includedComponent.addInput("param_activate-certfr", enrichmentOptions.getActivateCertFr());
            includedComponent.addInput("param_activate-certeu", enrichmentOptions.getActivateCertEu());
            includedComponent.addInput("param_activate-certsei", enrichmentOptions.getActivateCertSei());
            includedComponent.addInput("param_activate-osv", enrichmentOptions.getActivateOsv());
            includedComponent.addInput("param_activate-kev", enrichmentOptions.getActivateKev());
            includedComponent.addInput("param_activate-epss", enrichmentOptions.getActivateEpss());
            includedComponent.addInput("param_activate-eol", enrichmentOptions.getActivateEol());
            includedComponent.addInput("param_activate-csaf", enrichmentOptions.getActivateCsaf());

            includedComponent.addInput("param_context-dirs", "$CI_PROJECT_DIR/assessments/"
                    + pipelineConfiguration.getProjectProperties().getProject().toString() + "/" + asset.getAssessmentId()
            + "/context");

            includedComponent.addInput("param_assessment-dirs", "$CI_PROJECT_DIR/assessments/"
                    + pipelineConfiguration.getProjectProperties().getProject().toString() + "/" + asset.getAssessmentId()
            + "/assessments/generic");

            if (assetPlan.getRequiredStages().contains(Stage.RESOLVE)
                    && pipelineConfiguration.getOptions().getGlobal().getEnableResolve()) {
                includedComponent.addInput("input_inventory-file", workspace.getResolvedDirForAsset(asset) + asset + ".xlsx");
            } else {
                includedComponent.addInput("input_inventory-file", workspace.getAggregatedDirForAsset(asset) + asset + ".xlsx");
            }
            includedAdvisedComponents.append(includedComponent.toYaml(2));
        }

        return includedAdvisedComponents.toString();
    }

    private String generateFetchJob(List<AssetPlan> assetPlansForAssetsRequiringFetch) {
        Job fetchJob = new Job("fetch", Stage.FETCH, config.getContainerImage());

        for (AssetPlan assetPlan : assetPlansForAssetsRequiringFetch) {
            fetchJob.addScriptBlockLine("mkdir -p " + workspace.getFetchedDirForAsset(assetPlan.getAsset()));
            fetchJob.addScriptBlockLine("cd " + workspace.getFetchedDirForAsset(assetPlan.getAsset()));
            fetchJob.addScriptBlockLine("curl -L -f -O --retry 2 " + assetPlan.getAsset().getUrlResolver().getUrl());
        }

        return fetchJob.toYaml();
    }

    private String generateCopyToScanJob(List<AssetPlan> assetPlansForAssetsRequiringScan) {
        Job copyToScanJob = new Job("copy-to-scan", Stage.SCAN, config.getContainerImage());

        for (AssetPlan assetPlan : assetPlansForAssetsRequiringScan) {
            copyToScanJob.addScriptBlockLine("mkdir -p " + workspace.getScannedDirForAsset(assetPlan.getAsset()));

            if (assetPlan.getRequiredStages().contains(Stage.RESOLVE)
                    && pipelineConfiguration.getOptions().getGlobal().getEnableResolve()) {
                copyToScanJob.addScriptBlockLine("cp " + workspace.getResolvedDirForAsset(assetPlan.getAsset()) + "*.xlsx "
                        + workspace.getScannedDirForAsset(assetPlan.getAsset()));
            } else {
                copyToScanJob.addScriptBlockLine("cp " + workspace.getAggregatedDirForAsset(assetPlan.getAsset()) + "*.xlsx "
                        + workspace.getScannedDirForAsset(assetPlan.getAsset()));
            }
        }
        return copyToScanJob.toYaml();
    }

    private String generateGroupJob(List<AssetPlan> allAssetPlans) {
        Job groupJob = new Job("group-assets-for-reports", Stage.GROUP, config.getContainerImage());
        for (AssetPlan assetPlan : allAssetPlans) {
            PipelineConfiguration.ProjectProperties.Asset asset = assetPlan.getAsset();
            if (assetPlan.getRequiredStages().contains(Stage.ADVISE)) {
                    groupJob.addScriptBlockLine("cp " + workspace.getAdvisedDirForAsset(asset) + asset + ".xlsx " + workspace.getGroupedDirForAsset(asset) + "vulnerability-report/");
            }
        }

        return groupJob.toYaml();
    }

    /**
     * This is temporary until set-inventory-info creates the required directories by itself.
     * Should be the case for > v0.157.0.
     *
     * @param assetPlansForAssetsRequiringAdvise the asset plans for all assets requiring workspace directory creation.
     * @return the job as a formatted string.
     */
    @Deprecated
    private String generateAdvisePreJob(List<AssetPlan> assetPlansForAssetsRequiringAdvise) {
        Job advisePreJob = new Job("advise-pre", Stage.PRE, config.getContainerImage());

        for (AssetPlan assetPlan : assetPlansForAssetsRequiringAdvise) {
            advisePreJob.addScriptBlockLine("mkdir -p " + workspace.getAdvisedDirForAsset(assetPlan.getAsset()));
        }
        return advisePreJob.toYaml();
    }

    private void addJobInputsForIncludedComponent(IncludedComponent includedComponent) {
        includedComponent.addInput("job_image", config.getContainerImage());
        includedComponent.addInput("job_use-mounted-volume", "true");
        includedComponent.addInput("job_shared-dir", config.getMountedVolume());

        if (StringUtils.isNotBlank(config.getMavenCliOpts())) {
            includedComponent.addInput("job_maven-cli-opts", config.getMavenCliOpts());
        }

        if (StringUtils.isNotBlank(config.getSetupCommand())) {
            includedComponent.addInput("job_setup-command", config.getSetupCommand());
        }
    }

    private List<PipelineConfiguration.ProjectProperties.Asset> getAssetsForStage(PipelinePlanner pipelinePlanner, Stage stage) {
        return pipelinePlanner.getAssetPlans()
                .stream()
                .filter(a -> a.getRequiredStages().contains(stage))
                .map(AssetPlan::getAsset)
                .toList();
    }
}
