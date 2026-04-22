package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.models.ProcessorDefinitions;
import com.metaeffekt.kontinuum.models.ProjectProperties;
import com.metaeffekt.kontinuum.models.ProjectPropertiesLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class MavenProcessorExecutor implements ProcessorExecutionBackend {

    @Override
    public List<String> buildExecutionCommand(ProcessorExecution processorExecution) {
        Objects.requireNonNull(processorExecution, "processorExecution must not be null");

        List<String> command = new ArrayList<>();
        command.add("mvn");
        command.add("-f");
        if (Files.exists(Path.of(processorExecution.pomLocation()))) {
            command.add(requireText(processorExecution.pomLocation(), "pomLocation"));
        } else {
            command.add(requireText(KontinuumManagementUtils.getAbsolutePomPath(processorExecution.pomLocation()).toString(), "pomLocation"));
        }

        if (processorExecution.debug()) {
            command.add("-X");
        }

        String coreVersion = System.getProperty(ProjectProperties.AE_CORE_VERSION.getPropertyKey());
        String artifactAnalysisVersion = System.getProperty(ProjectProperties.AE_ARTIFACT_ANALYSIS_VERSION.getPropertyKey());

        if (!coreVersion.isEmpty()) {
            command.add("-Dae.core.version=" + coreVersion);
        }

        if (!artifactAnalysisVersion.isEmpty()) {
            command.add("-Dae.artifact.analysis.version=" + artifactAnalysisVersion);
        }

        command.add(requireText(processorExecution.goal(), "goal"));

        Map<ProcessorDefinitions.ProcessorParameter, String> parameterToValueMap = processorExecution.parameterToValueMap();
        if (parameterToValueMap != null) {
            for (Map.Entry<ProcessorDefinitions.ProcessorParameter, String> entry : parameterToValueMap.entrySet()) {
                ProcessorDefinitions.ProcessorParameter parameter = Objects.requireNonNull(
                    entry.getKey(),
                    "parameterToValueMap contains a null parameter"
                );

                String parameterKey = requireText(parameter.getKey(), "parameter.key");
                String parameterValue = entry.getValue();

                if (parameterValue == null || parameterValue.isBlank()) {
                    if (Boolean.TRUE.equals(parameter.getRequired())) {
                        throw new IllegalArgumentException("Missing value for required parameter: " + parameterKey);
                    }
                    continue;
                }

                command.add("-D" + parameterKey + "=" + parameterValue);
            }
        }

        return List.copyOf(command);
    }

    @Override
    public boolean execute(List<String> mavenCommand) {
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand);
        processBuilder.redirectErrorStream(true);
        log.info("Executing Maven command: {}", String.join(" ", mavenCommand));

        try {
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("Maven process exited with code {}", exitCode);
                return false;
            }
        } catch (IOException e) {
            log.error("Process execution failed with error message:", e);
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Maven process execution was interrupted", e);
        }
        return true;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }
}
