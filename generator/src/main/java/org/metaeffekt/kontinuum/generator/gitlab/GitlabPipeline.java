package org.metaeffekt.kontinuum.generator.gitlab;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.metaeffekt.kontinuum.generator.shared.Pipeline;
import org.metaeffekt.kontinuum.models.gitlab.GitlabConfiguration;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.ProjectProperties.Asset;
import org.metaeffekt.kontinuum.models.shared.ProcessorDefinitions.Processor;
import org.metaeffekt.kontinuum.models.shared.ProcessorDefinitions.ProcessorParameter;
import org.metaeffekt.kontinuum.models.shared.Stage;

/**
 * This class generates a gitlab pipeline from the given configuration files to include as a
 * downstream pipeline in a gitlab project.
 */
@Slf4j
public class GitlabPipeline {

    Map<Asset, List<Processor>> assetProcessorsMap;

    StringBuilder gitlabPipelineDocument = new StringBuilder();

    GitlabConfiguration gitlabConfiguration;

    public GitlabPipeline(PipelineConfiguration pipelineConfiguration, GitlabConfiguration gitlabConfiguration) {
        this.gitlabConfiguration = gitlabConfiguration;
        Pipeline pipeline = new Pipeline(pipelineConfiguration, gitlabConfiguration);
        assetProcessorsMap = pipeline.generatePipeline();
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
        
        assetProcessorsMap.values().stream()
            .flatMap(List::stream)
            .forEach(p -> requiredStages.add(p.getStage()));
        
        for (Stage stage : requiredStages.stream().sorted().toList()) {
            stagesSection.append("  - ").append(stage).append(System.lineSeparator());
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
        for (Map.Entry<Asset, List<Processor>> entry : assetProcessorsMap.entrySet()) {
            for (Processor processor : entry.getValue()) {
                StringBuilder job = new StringBuilder();
                job.append(generateJobName(processor , entry.getKey().toString())).append(System.lineSeparator());
                job.append("  ").append("stage: ").append(processor.getStage()).append(System.lineSeparator());
                job.append("  ").append("image: ").append(gitlabConfiguration.CONTAINER_IMAGE).append(System.lineSeparator());
                
                if (generateBeforeScriptBlock() != null) {
                    job.append(generateBeforeScriptBlock());
                }
                
                job.append("  ").append("script: ").append(System.lineSeparator());
                job.append("    - |").append(System.lineSeparator());
                job.append(generateMavenScriptBlock(processor));
                gitlabPipelineDocument.append(job).append(System.lineSeparator());
            }
        }
    }

    private String generateMavenScriptBlock(Processor processor) {
        StringBuilder script = new StringBuilder();
        script.append("      mvn -f ")
                .append(gitlabConfiguration.getKontinuumProcessorsDirNormalized())
                .append(processor.getPomLocation()).append(" ")
                .append(processor.getGoal()).append(" \\").append(System.lineSeparator());
        
        List<ProcessorParameter> nonBlankParams = processor.getParameters().stream()
            .filter(p -> StringUtils.isNotBlank(p.getValue()))
            .toList();
        
        for (int i = 0; i < nonBlankParams.size(); i++) {
            ProcessorParameter param = nonBlankParams.get(i);
            script.append("      -D").append(param.getKey()).append("='").append(param.getValue()).append("'");
            if (i < nonBlankParams.size() - 1) {
                script.append(" \\");
            }
            script.append(System.lineSeparator());
        }
        return script.toString();
    }

    private String generateBeforeScriptBlock() {
 
        if (gitlabConfiguration.SETUP_COMMAND == null) {
            return null;
        }

        if (!gitlabConfiguration.SETUP_COMMAND.exists()) {
            throw new IllegalStateException("The designated 'before script' file " 
                + gitlabConfiguration.SETUP_COMMAND.getAbsolutePath() + " does not exist.");
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

    private String generateJobName(Processor processor, String assetName) {
        StringBuilder processorName =  new StringBuilder()
                .append(processor.getId())
                .append("-");

        if (processor.getId().equals("create-document")) {
            Optional<ProcessorParameter> processorParameter = processor.getParameters()
                    .stream()
                    .filter(p -> p.getKey().equals("param.document.type"))
                    .findFirst();

            processorParameter.ifPresent(parameter -> processorName.append(parameter.getValue()).append("-"));
        }

        processorName.append(assetName).append(":");
        return processorName.toString();
    }
 }
