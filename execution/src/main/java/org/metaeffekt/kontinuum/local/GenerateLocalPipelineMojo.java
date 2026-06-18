package org.metaeffekt.kontinuum.local;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.metaeffekt.kontinuum.AbstractGeneratePipelineMojo;

import java.io.File;

@Mojo(name = "generate-local-pipeline", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GenerateLocalPipelineMojo extends AbstractGeneratePipelineMojo {

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Generating local execution pipeline from: " + pipelineConfigPath);

       // TODO: Implement local pipeline generation / execution
    }
}
