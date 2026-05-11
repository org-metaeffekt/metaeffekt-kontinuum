package org.metaeffekt.kontinuum.gitlab;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.metaeffekt.kontinuum.generator.gitlab.GeneratorConfig;
import org.metaeffekt.kontinuum.generator.gitlab.PipelineGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Mojo(name = "generate-pipeline", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GeneratePipelineMojo extends AbstractMojo {

    @Parameter(property = "pipeline.configPath", required = true)
    private String pipelineConfigPath;

    @Parameter(property = "pipeline.outputFile", defaultValue = "${project.build.directory}/generated-pipeline/.gitlab-ci.yml")
    private File outputFile;

    @Parameter(property = "pipeline.containerImage", defaultValue = "metaeffekt/metaeffekt-kontinuum-runtime:2.3.2_0.156.x")
    private String containerImage;

    @Parameter(property = "pipeline.gitDepth", defaultValue = "1")
    private int gitDepth;

    @Parameter(property = "pipeline.gitStrategy", defaultValue = "clone")
    private String gitStrategy;

    @Parameter(property = "runner.defaultTag")
    private String runnerTag;

    @Parameter(property = "runner.mountedVolume", defaultValue = "/shared")
    private String mountedVolume;

    @Parameter(property = "job.setupCommand")
    private String setupCommand;

    @Parameter(property = "job.mavenCliOpts")
    private String mavenCliOpts;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Generating GitLab CI pipeline from: " + pipelineConfigPath);

        GeneratorConfig config = new GeneratorConfig();
        config.setPipelineConfigPath(pipelineConfigPath);
        config.setContainerImage(containerImage);
        config.setGitDepth(gitDepth);
        config.setGitStrategy(gitStrategy);
        config.setRunnerTag(runnerTag);
        config.setMountedVolume(mountedVolume);
        config.setMavenCliOpts(mavenCliOpts);
        config.setSetupCommand(setupCommand);

        PipelineGenerator generator = new PipelineGenerator(config);
        generator.generate();

        try {
            outputFile.getParentFile().mkdirs();
            Files.writeString(outputFile.toPath(), generator.getPipelineDocument());
            getLog().info("Pipeline written to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to write pipeline file: " + outputFile, e);
        }
    }
}
