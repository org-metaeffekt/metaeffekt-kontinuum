package org.metaeffekt.kontinuum.generator.gitlab;

import org.apache.commons.lang3.StringUtils;
import org.metaeffekt.kontinuum.generator.shared.Pipeline;
import org.metaeffekt.kontinuum.models.gitlab.GitlabConfiguration;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration;
import org.metaeffekt.kontinuum.models.shared.Stage;
import org.metaeffekt.kontinuum.models.shared.ProcessorDefinitions.Processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GitlabPipeline {

    List<Processor> processors;

    StringBuilder gitlabPipelineDocument = new StringBuilder();

    GitlabConfiguration gitlabConfiguration;

    public GitlabPipeline(PipelineConfiguration pipelineConfiguration, GitlabConfiguration gitlabConfiguration) {
        this.gitlabConfiguration = gitlabConfiguration;
        Pipeline pipeline = new Pipeline(pipelineConfiguration, gitlabConfiguration);
        processors = pipeline.generatePipeline();
    }

    public String generatePipeline() {
        generateStagesSection();
        generateVariablesSection();
        generateDefaultSection();
        generateJobsSection();
        return gitlabPipelineDocument.toString();
    }

    public void generateStagesSection() {
        StringBuilder stagesSection = new StringBuilder();
        stagesSection.append("stages:").append(System.lineSeparator());
        Set<Stage> requiredStages = new HashSet<>();
        
        processors.forEach(p -> requiredStages.add(p.getStage()));
        
        for (Stage stage : requiredStages) {
            stagesSection.append("  ").append(stage).append(System.lineSeparator());
        }

        gitlabPipelineDocument.append(stagesSection.append(System.lineSeparator()));
    }

    public void generateVariablesSection() {
        StringBuilder variablesSection = new StringBuilder();
        variablesSection.append("variables:").append(System.lineSeparator())
                .append("  GIT_DEPTH: ").append(gitlabConfiguration.GIT_DEPTH).append(System.lineSeparator())
                .append("  GIT_STRATEGY: ").append(gitlabConfiguration.GIT_STRATEGY).append(System.lineSeparator())
                .append("  CONTAINER_IMAGE: ").append(gitlabConfiguration.CONTAINER_IMAGE).append(System.lineSeparator());

        gitlabPipelineDocument.append(variablesSection).append(System.lineSeparator());
    }

    public void generateDefaultSection() {
        StringBuilder defaultContent = new StringBuilder();

        if (StringUtils.isNotBlank(gitlabConfiguration.RUNNER_TAG)) {
            defaultContent.append("  tags:").append(System.lineSeparator())
                    .append("    - ").append(gitlabConfiguration.RUNNER_TAG).append(System.lineSeparator());
        }

        if (!defaultContent.isEmpty()) {
            gitlabPipelineDocument.append("default:").append(System.lineSeparator()).append(defaultContent).append(System.lineSeparator());
        }
    }

    public void generateJobsSection() {
        for (Processor processor : processors) {
            StringBuilder job = new StringBuilder();
            job.append(processor.getId()).append(System.lineSeparator());
            job.append("  ").append("stage: ").append(processor.getStage()).append(System.lineSeparator());
            job.append("  ").append("image: ").append(gitlabConfiguration.CONTAINER_IMAGE).append(System.lineSeparator());
            job.append("  ").append("before_script: ").append(System.lineSeparator());
            job.append("    - |").append(System.lineSeparator());
            job.append("      ").append(gitlabConfiguration.BEFORE_SCRIPT).append(System.lineSeparator());
            job.append("  ").append("script: ").append(System.lineSeparator());
            job.append("    - |").append(System.lineSeparator());
            job.append("      ").append(processor.buildMavenCall()).append(System.lineSeparator());
            gitlabPipelineDocument.append(job).append(System.lineSeparator());
        }
    }
 }
