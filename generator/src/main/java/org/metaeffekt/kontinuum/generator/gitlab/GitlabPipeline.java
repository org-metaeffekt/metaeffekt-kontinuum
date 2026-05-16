package org.metaeffekt.kontinuum.generator.gitlab;

import org.metaeffekt.kontinuum.generator.shared.Pipeline;
import org.metaeffekt.kontinuum.models.shared.EnvironmentConfiguration;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration;
import org.metaeffekt.kontinuum.models.shared.ProcessorDefinitions;

import java.util.List;

public class GitlabPipeline {

    List<ProcessorDefinitions.Processor> processors;

    StringBuilder gitlabPipelineDocument = new StringBuilder();

    public GitlabPipeline(PipelineConfiguration pipelineConfiguration, EnvironmentConfiguration environmentConfiguration) {
        Pipeline pipeline = new Pipeline(pipelineConfiguration, environmentConfiguration);
        processors = pipeline.generatePipeline();
    }

    public void generatePipeline() {
        generateStagesSection();
    }

    public void generateStagesSection() {
        StringBuilder stagesSection = new StringBuilder();
        stagesSection.append("stages:").append(System.lineSeparator());
        for (ProcessorDefinitions.Processor processor : processors) {
            processor.get
        }
    }
}
