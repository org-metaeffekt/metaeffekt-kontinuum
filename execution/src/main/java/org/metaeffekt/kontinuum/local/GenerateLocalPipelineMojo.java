package org.metaeffekt.kontinuum.local;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "generate-local-pipeline", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GenerateLocalPipelineMojo extends AbstractMojo {

    @Parameter(property = "pipeline.configPath", required = true)
    private String pipelineConfigPath;

    @Parameter(property = "pipeline.outputFile", defaultValue = "${project.build.directory}/generated-pipeline/.gitlab-ci.yml")
    private File outputFile;


    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Generating local execution pipeline from: " + pipelineConfigPath);

       // TODO: Implement local pipeline generation / execution
    }
}
