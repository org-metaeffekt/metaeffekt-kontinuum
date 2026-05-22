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

@Mojo(name = "generate-pipeline", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GeneratePipelineMojo extends AbstractMojo {

    @Parameter(property = "pipeline.config.path", required = true)
    private String pipelineConfigPath;

    @Parameter(property = "pipeline.output.file", defaultValue = "${project.build.directory}/generated-pipeline/.gitlab-ci.yml")
    private File outputFile;

    @Parameter(property = "pipeline.container.image", defaultValue = "metaeffekt/metaeffekt-kontinuum-runtime:2.3.2_0.156.x")
    private String containerImage;

    @Parameter(property = "pipeline.git.depth", defaultValue = "1")
    private int gitDepth;

    @Parameter(property = "pipeline.git.strategy", defaultValue = "clone")
    private String gitStrategy;

    @Parameter(property = "runner.default.tag")
    private String runnerTag;

    @Parameter(property = "runner.mounted.volume", defaultValue = "/shared")
    private String mountedVolume;

    @Parameter(property = "job.maven.cli.opts")
    private String mavenCliOpts;
    
    @Parameter(property = "job.kosmos.password", required = false)
    private String kosmosPassword;

    @Parameter(property = "job.kosmos.userkeys.file", required = false)
    private String userkeysFile;

    @Parameter(property = "job.artifact.resolver.config.file", required = false)
    private String artifactResolverConfigFile;

    @Parameter(property = "job.artifact.resolver.proxy.file", required = false)
    private String artifactResolverProxyFile;

    @Parameter(property = "job.setup.command.file", required = false)
    private String setupCommandFile;

    @Parameter(property = "job.scan.properties.file", required = false)
    private String scanPropertiesFile;

    @Parameter(property = "job.vulnerability.mirror.dir", required = false)
    private String vulnerabilityMirrorDir;

    @Parameter(property = "job.vulnerability.mirror.url", required = false)
    private String vulnerabilityMirrorUrl;

    @Parameter(property = "job.workbench.dir", required = false)
    private String workbenchDir;


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
            .WORKBENCH_DIR(workbenchDir);
       
        PipelineConfiguration pipelineConfiguration = PipelineConfigurationLoader.readConfig(pipelineConfigFile);
        
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
