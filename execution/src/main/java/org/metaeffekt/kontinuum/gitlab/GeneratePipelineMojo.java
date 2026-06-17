package org.metaeffekt.kontinuum.gitlab;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.metaeffekt.kontinuum.generator.gitlab.GitlabPipeline;
import org.metaeffekt.kontinuum.generator.shared.PipelineConfigurationLoader;
import org.metaeffekt.kontinuum.models.gitlab.GitlabConfiguration;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Mojo(name = "generate-gitlab-pipeline", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GeneratePipelineMojo extends AbstractMojo {

    @Parameter(property = "pipelineConfigPath", required = true)
    private String pipelineConfigPath;

    @Parameter(property = "outputFile", defaultValue = "${project.build.directory}/generated-pipeline/.gitlab-ci.yml")
    private File outputFile;

    @Parameter(property = "containerImage", required = true)
    private String containerImage;

    @Parameter(property = "gitDepth", defaultValue = "1")
    private int gitDepth;

    @Parameter(property = "gitStrategy", defaultValue = "clone")
    private String gitStrategy;

    @Parameter(property = "runnerTag")
    private String runnerTag;

    @Parameter(property = "mountedVolume", defaultValue = "/shared")
    private String mountedVolume;

    @Parameter(property = "mavenCliOpts")
    private String mavenCliOpts;

    @Parameter(property = "kosmosPassword")
    private String kosmosPassword;

    @Parameter(property = "userkeysFile")
    private String userkeysFile;

    @Parameter(property = "artifactResolverConfigFile")
    private String artifactResolverConfigFile;

    @Parameter(property = "artifactResolverProxyFile")
    private String artifactResolverProxyFile;

    @Parameter(property = "setupCommandFile")
    private String setupCommandFile;

    @Parameter(property = "scanPropertiesFile")
    private String scanPropertiesFile;

    @Parameter(property = "vulnerabilityMirrorDir")
    private String vulnerabilityMirrorDir;

    @Parameter(property = "vulnerabilityMirrorUrl")
    private String vulnerabilityMirrorUrl;

    @Parameter(property = "workbenchDir", required = true)
    private String workbenchDir;

    @Parameter(property = "workspaceDir", required = true)
    private String workspaceDir;

    @Parameter(property = "kontinuumDir", defaultValue = "/usr/src/metaeffekt-kontinuum/")
    private String kontinuumDir;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Generating GitLab CI pipeline from: " + pipelineConfigPath);

        File pipelineConfigFile = new File(pipelineConfigPath);

        if (!pipelineConfigFile.exists()) {
            throw new MojoExecutionException("The pipeline configuration file " + pipelineConfigFile.getAbsolutePath() + " does not exist.");
        }

        GitlabConfiguration.GitlabConfigurationBuilder gitlabConfiguration = GitlabConfiguration.builder()
                .ARTIFACT_RESOLVER_CONFIG_FILE(artifactResolverConfigFile)
                .ARTIFACT_RESOLVER_PROXY_FILE(artifactResolverProxyFile);

        if (StringUtils.isNotBlank(setupCommandFile)) {
            File setupCommand = new File(setupCommandFile);

            if (setupCommand.exists()) {
                gitlabConfiguration.SETUP_COMMAND(setupCommand);
            }
        }

        gitlabConfiguration.CONTAINER_IMAGE(containerImage)
                .GIT_DEPTH(gitDepth)
                .GIT_STRATEGY(gitStrategy)
                .KOSMOS_PASSWORD(kosmosPassword)
                .KOSMOS_USERKEYS_FILE(userkeysFile)
                .SCAN_PROPERTIES_FILE(scanPropertiesFile)
                .VULNERABILITY_MIRROR_DIR(vulnerabilityMirrorDir)
                .VULNERABILITY_MIRROR_URL(vulnerabilityMirrorUrl)
                .WORKBENCH_DIR(workbenchDir)
                .KONTINUUM_DIR(kontinuumDir)
                .WORKSPACE_DIR(workspaceDir);

        PipelineConfiguration pipelineConfiguration = new PipelineConfigurationLoader().readConfig(pipelineConfigFile);

        GitlabPipeline gitlabPipeline = new GitlabPipeline(pipelineConfiguration, gitlabConfiguration.build());

        try {
            outputFile.getParentFile().mkdirs();
            Files.writeString(outputFile.toPath(), gitlabPipeline.generatePipeline());
            getLog().info("Pipeline written to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write pipeline file: " + outputFile, e);
        }
    }
}
