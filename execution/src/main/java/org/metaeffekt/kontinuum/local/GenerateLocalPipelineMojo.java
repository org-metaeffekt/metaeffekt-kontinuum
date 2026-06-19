package org.metaeffekt.kontinuum.local;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.metaeffekt.kontinuum.AbstractGeneratePipelineMojo;
import org.metaeffekt.kontinuum.models.local.LocalConfiguration;

import java.io.File;

@Mojo(name = "generate-local-pipeline", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GenerateLocalPipelineMojo extends AbstractGeneratePipelineMojo {

    @Parameter(property = "executionEnvironment", defaultValue = LocalConfiguration.ExecutionEnvironment.UNIX)
    private LocalConfiguration.ExecutionEnvironment executionEnvironment;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Generating local execution pipeline from: " + pipelineConfigPath);

        File pipelineConfigFile = new File(pipelineConfigPath);

        if (!pipelineConfigFile.exists()) {
            throw new MojoExecutionException("The pipeline configuration file " + pipelineConfigFile.getAbsolutePath() + " does not exist.");
        }

        LocalConfiguration localConfiguration = LocalConfiguration.builder()
                .executionEnvironment(executionEnvironment).build();

    }
}
