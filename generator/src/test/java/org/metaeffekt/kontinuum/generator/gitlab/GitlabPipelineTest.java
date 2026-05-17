package org.metaeffekt.kontinuum.generator.gitlab;

import org.junit.jupiter.api.Test;
import org.metaeffekt.kontinuum.models.gitlab.GitlabConfiguration;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration;
import org.metaeffekt.kontinuum.models.shared.PipelineConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GitlabPipelineTest {

    private static final File PIPELINE_CONFIG_FILE = new File("src/test/resources/valid-pipeline-config.yaml");

    @Test
    public void testValidPipelineGeneration() throws IOException {
        GitlabConfiguration gitlabConfiguration = GitlabConfiguration.builder()
        .SCAN_PROPERTIES_FILE("config/scan/scan-control.properties")
        .KOSMOS_PASSWORD("EuBsVvcjIElWdXVVtHmPJdsE")
        .RUNNER_TAG("gpu")
        .CONTAINER_IMAGE("metaeffekt/metaeffekt-kontinuum-runtime:2.3.2_0.156.x")
        .build();
        
        PipelineConfiguration pipelineConfiguration = PipelineConfigurationLoader.readConfig(PIPELINE_CONFIG_FILE);
        GitlabPipeline gitlabPipeline = new GitlabPipeline(pipelineConfiguration, gitlabConfiguration);

        Path outputPath = Path.of("target/generator/testValidPipelineGeneration.yml");
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, gitlabPipeline.generatePipeline());
    }
}
