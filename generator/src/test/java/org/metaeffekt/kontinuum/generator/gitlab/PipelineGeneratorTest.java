package org.metaeffekt.kontinuum.generator.gitlab;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PipelineGeneratorTest {

    @Test
    public void testValidPipelineGeneration() throws IOException {
        GeneratorConfig generatorConfig = new GeneratorConfig();
        generatorConfig.setRunnerTag("gpu");
        generatorConfig.setContainerImage("metaeffekt/metaeffekt-kontinuum-runtime:2.3.2_0.156.x");
        generatorConfig.setPipelineConfigPath("src/test/resources/valid-pipeline-config.yaml");
        PipelineGenerator pipelineGenerator = new PipelineGenerator(generatorConfig);
        pipelineGenerator.generate();

        Path outputPath = Path.of("target/generator/valid-pipeline.yml");
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, pipelineGenerator.getPipelineDocument());
    }

}
