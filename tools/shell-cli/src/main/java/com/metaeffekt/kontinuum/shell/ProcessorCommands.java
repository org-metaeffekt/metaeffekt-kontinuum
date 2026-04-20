package com.metaeffekt.kontinuum.shell;

import com.metaeffekt.kontinuum.execution.MavenProcessorExecutor;
import com.metaeffekt.kontinuum.execution.ProcessorExecution;
import com.metaeffekt.kontinuum.execution.ProcessorExecutionBackend;
import com.metaeffekt.kontinuum.models.ProcessorDefinitions;
import com.metaeffekt.kontinuum.models.YamlProcessorCatalog;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
public class ProcessorCommands {

    private final YamlProcessorCatalog yamlProcessorCatalog;

    private final ProcessorExecutionBackend processorExecutionBackend;

    public ProcessorCommands(
        YamlProcessorCatalog yamlProcessorCatalog,
        MavenProcessorExecutor mavenProcessorExecutor
    ) {
        this.yamlProcessorCatalog = yamlProcessorCatalog;
        this.processorExecutionBackend = mavenProcessorExecutor;
    }

    @ShellMethod(key = "processor list", value = "List all known processors.")
    public String list() {
        StringBuilder sb = new StringBuilder();
        int processorIndex = 0;
        for (ProcessorDefinitions.Processor processor : yamlProcessorCatalog.getProcessors()) {
            sb.append("[").append(processorIndex).append("] ")
                    .append(processor.getName())
                    .append(" - ")
                    .append(processor.getId())
                    .append(System.lineSeparator());
            processorIndex++;
        }
        return sb.toString();
    }

    @ShellMethod(key = "processor show", value = "Show required and optional parameters for one processor.")
    public String show(@ShellOption String processorId) {
        ProcessorDefinitions.Processor processor = getProcessorByIdOrIndex(processorId);

        StringBuilder builder = new StringBuilder();
        builder.append(processor.getId()).append(System.lineSeparator());
        builder.append("pom: processors/").append(processor.getPomLocation()).append(System.lineSeparator());
        builder.append("goal: ").append(processor.getGoal()).append(System.lineSeparator());
        appendParameterSection(
            builder,
            "required:",
            processor.getParameters().stream()
                .filter(parameter -> Boolean.TRUE.equals(parameter.getRequired()))
                .toList()
        );
        appendParameterSection(
            builder,
            "optional:",
            processor.getParameters().stream()
                .filter(parameter -> !Boolean.TRUE.equals(parameter.getRequired()))
                .toList()
        );
        return builder.toString().trim();
    }

    private static void appendParameterSection(
        StringBuilder builder,
        String sectionTitle,
        List<ProcessorDefinitions.ProcessorParameter> parameters
    ) {
        builder.append(System.lineSeparator());
        builder.append(sectionTitle).append(System.lineSeparator());

        if (parameters.isEmpty()) {
            builder.append(" - none").append(System.lineSeparator());
            return;
        }

        parameters.forEach(parameter -> builder
            .append(" - ")
            .append(parameter.getKey())
            .append(System.lineSeparator())
            .append("   ")
            .append(parameter.getDescription())
            .append(System.lineSeparator()));
    }

    @ShellMethod(key = "processor run", value = "Execute a processor in local or container mode.")
    public void run(
        @ShellOption(value = "processor-id") String processorId,
        @ShellOption(value = "dry-run", defaultValue = "false") boolean dryRun
    ) throws Exception {
        ProcessorDefinitions.Processor processor = getProcessorByIdOrIndex(processorId);
        Map<ProcessorDefinitions.ProcessorParameter, String> parameterToValueMap = promptForProcessorParameters(processor);
        ProcessorExecution processorExecution = new ProcessorExecution(processor.getPomLocation(), processor.getGoal(), parameterToValueMap ,dryRun);
        processorExecutionBackend.execute(processorExecutionBackend.buildExecutionCommand(processorExecution));
    }

    private Map<ProcessorDefinitions.ProcessorParameter, String> promptForProcessorParameters(ProcessorDefinitions.Processor processor) {
        Map<ProcessorDefinitions.ProcessorParameter, String> parameterToValueMap = new LinkedHashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (ProcessorDefinitions.ProcessorParameter parameter : processor.getParameters()) {
            while (true) {
                String requirement = Boolean.TRUE.equals(parameter.getRequired())
                    ? "required"
                    : "optional, press enter to skip";

                System.out.println("[" + parameter.getKey() + "]");
                System.out.println(parameter.getDescription());
                System.out.print("(" + requirement + "): ");

                String input;
                try {
                    input = reader.readLine();
                } catch (IOException e) {
                    throw new IllegalStateException("Failed to read interactive input", e);
                }

                if (input == null) {
                    throw new IllegalStateException("Interactive input is not available.");
                }

                String value = input.trim();
                if (value.isEmpty()) {
                    if (Boolean.TRUE.equals(parameter.getRequired())) {
                        System.out.println("Value is required for " + parameter.getKey() + ".");
                        continue;
                    }
                    break;
                }

                parameterToValueMap.put(parameter, value);
                break;
            }
        }

        return parameterToValueMap;
    }

    private ProcessorDefinitions.Processor getProcessorByIdOrIndex(String idOrIndex) {
        try {
            int processorIndex = Integer.parseInt(idOrIndex);
            return yamlProcessorCatalog.getProcessors().get(processorIndex);
        } catch (NumberFormatException e) {
            return yamlProcessorCatalog.getProcessorById(idOrIndex);
        }
    }
}
