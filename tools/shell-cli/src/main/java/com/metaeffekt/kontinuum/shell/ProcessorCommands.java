package com.metaeffekt.kontinuum.shell;

import com.metaeffekt.kontinuum.execution.container.ContainerWrapperBackend;
import com.metaeffekt.kontinuum.data.models.ExecuteProcessorRequest;
import com.metaeffekt.kontinuum.data.models.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.data.models.ProcessorDefinition;
import com.metaeffekt.kontinuum.data.models.ProcessorParameterDefinition;
import com.metaeffekt.kontinuum.execution.core.GuidedProcessorExecutor;
import com.metaeffekt.kontinuum.execution.local.LocalMavenBackend;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
public class ProcessorCommands {

    private final GuidedProcessorExecutor guidedProcessorExecutor;
    private final LocalMavenBackend localMavenBackend;
    private final ContainerWrapperBackend containerWrapperBackend;

    public ProcessorCommands(
        GuidedProcessorExecutor guidedProcessorExecutor,
        LocalMavenBackend localMavenBackend,
        ContainerWrapperBackend containerWrapperBackend
    ) {
        this.guidedProcessorExecutor = guidedProcessorExecutor;
        this.localMavenBackend = localMavenBackend;
        this.containerWrapperBackend = containerWrapperBackend;
    }

    @ShellMethod(key = "processor list", value = "List all known processors.")
    public String list() {
        return guidedProcessorExecutor.listProcessors().stream()
            .map(processor -> processor.id() + " - " + processor.displayName())
            .collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(key = "processor show", value = "Show required and optional parameters for one processor.")
    public String show(@ShellOption String processorId) {
        ProcessorDefinition processor = findProcessor(processorId);

        StringBuilder builder = new StringBuilder();
        builder.append(processor.id()).append(System.lineSeparator());
        builder.append("pom: processors/").append(processor.pomPath()).append(System.lineSeparator());
        builder.append("goal: ").append(processor.goal()).append(System.lineSeparator());
        builder.append("parameters:").append(System.lineSeparator());
        processor.parameters().forEach(parameter -> builder
            .append(" - ")
            .append(parameter.key())
            .append(" [")
            .append(parameter.required() ? "required" : "optional")
            .append("]")
            .append(System.lineSeparator()));
        return builder.toString().trim();
    }

    @ShellMethod(key = "processor run", value = "Execute a processor in local or container mode.")
    public String run(
        @ShellOption(value = "processor-id") String processorId,
        @ShellOption(defaultValue = ShellOption.NULL) String[] properties,
        @ShellOption(defaultValue = "local") String backend,
        @ShellOption(defaultValue = "false") boolean interactive,
        @ShellOption(value = "dry-run", defaultValue = "false") boolean dryRun
    ) throws Exception {
        ProcessorDefinition processor = findProcessor(processorId);
        Map<String, String> propertiesMap = parseProperties(properties);

        if (interactive || propertiesMap.isEmpty()) {
            propertiesMap = promptForProperties(processor, propertiesMap);
        }

        ExecuteProcessorRequest request = new ExecuteProcessorRequest(processorId, propertiesMap, Path.of("."), dryRun);

        ExecuteProcessorResult result;
        if ("container".equalsIgnoreCase(backend)) {
            result = guidedProcessorExecutor.execute(request, containerWrapperBackend);
        } else {
            result = guidedProcessorExecutor.execute(request, localMavenBackend);
        }

        return "exitCode=" + result.exitCode() + System.lineSeparator()
            + "command=" + result.effectiveCommand() + System.lineSeparator()
            + "diagnostics=" + result.diagnostics() + System.lineSeparator();
    }

    private Map<String, String> parseProperties(String[] define) {
        if (define == null || define.length == 0) {
            return Map.of();
        }

        Map<String, String> properties = new LinkedHashMap<>();
        Arrays.stream(define).forEach(entry -> {
            int separator = entry.indexOf('=');
            if (separator <= 0) {
                throw new IllegalArgumentException("Invalid --properties entry: " + entry + " (expected key=value)");
            }
            String key = entry.substring(0, separator).trim();
            String value = entry.substring(separator + 1).trim();
            properties.put(key, value);
        });

        return properties;
    }

    private ProcessorDefinition findProcessor(String processorId) {
        return guidedProcessorExecutor.listProcessors().stream()
            .filter(candidate -> candidate.id().equals(processorId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown processor id: " + processorId));
    }

    private Map<String, String> promptForProperties(ProcessorDefinition processor, Map<String, String> initialProperties)
        throws IOException {
        Map<String, String> properties = new LinkedHashMap<>(initialProperties);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (ProcessorParameterDefinition parameter : processor.parameters()) {
            String key = parameter.key();
            String existingValue = properties.get(key);

            while (true) {
                String prompt = buildPrompt(parameter, existingValue);
                System.out.print(prompt);
                String input = reader.readLine();

                if (input == null) {
                    throw new IllegalStateException("Interactive input is not available.");
                }

                String value = input.trim();
                if (value.isEmpty()) {
                    if (existingValue != null && !existingValue.isBlank()) {
                        break;
                    }
                    if (!parameter.required()) {
                        break;
                    }
                    System.out.println("Value is required for " + key + ".");
                    continue;
                }

                properties.put(key, value);
                break;
            }
        }

        return properties;
    }

    private String buildPrompt(ProcessorParameterDefinition parameter, String existingValue) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("[")
                .append(parameter.key())
                .append("]")
                .append(String.format("%n"))
                .append(parameter.description())
                .append(String.format("%n"))
                .append("(")
                .append(parameter.required() ? "required" : "optional, leave empty to skip")
                .append(")");

        if (existingValue != null && !existingValue.isBlank()) {
            promptBuilder.append(" (default: ").append(existingValue).append(")");
        }
        promptBuilder.append(": ");
        return promptBuilder.toString();
    }
}
