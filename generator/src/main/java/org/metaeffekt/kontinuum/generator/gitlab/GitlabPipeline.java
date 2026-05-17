package org.metaeffekt.kontinuum.generator.gitlab;

import org.apache.commons.lang3.StringUtils;
import org.metaeffekt.kontinuum.generator.shared.Pipeline;
import org.metaeffekt.kontinuum.models.gitlab.GitlabConfiguration;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration;
import org.metaeffekt.kontinuum.models.shared.Stage;
import org.metaeffekt.kontinuum.models.shared.ProcessorDefinitions.Processor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
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
            
            if (generateBeforeScriptSection() != null) {
                job.append(generateBeforeScriptSection());
            }
            
            job.append("  ").append("script: ").append(System.lineSeparator());
            job.append("    - |").append(System.lineSeparator());
            job.append("      ").append(processor.buildMavenCall()).append(System.lineSeparator());
            gitlabPipelineDocument.append(job).append(System.lineSeparator());
        }
    }

    private String generateBeforeScriptSection() {
 
        if (gitlabConfiguration.SETUP_COMMAND == null) {
            return null;
        }

        if (!gitlabConfiguration.SETUP_COMMAND.exists()) {
            throw new IllegalStateException("The designated 'before script' file " + gitlabConfiguration.SETUP_COMMAND.getAbsolutePath() + " does not exist.");
        }

        StringBuilder beforeScriptBuilder = new StringBuilder();


        beforeScriptBuilder.append("  ").append("before_script: ").append(System.lineSeparator());
        beforeScriptBuilder.append("    - |").append(System.lineSeparator());
        
        try {
            List<String> scriptLines = Files.readAllLines(gitlabConfiguration.SETUP_COMMAND.toPath());
            for (String line : scriptLines) {
                beforeScriptBuilder.append("      ").append(line).append(System.lineSeparator());
            }
            beforeScriptBuilder.append(System.lineSeparator());
            return beforeScriptBuilder.toString();
        } catch (IOException e) {
            log.error("Could not read lines from 'before script' file. The pipeline will still be generated but the before script section will be left empty!", e);
            return "echo 'Before script was left empty as the contents of the provided before-script file could not be read.'";
        }

    }
 }
