package com.metaeffekt.kontinuum.shell;

import com.metaeffekt.kontinuum.execution.MavenProcessorExecutor;
import com.metaeffekt.kontinuum.execution.ProcessorExecution;
import com.metaeffekt.kontinuum.execution.ProcessorExecutionBackend;
import com.metaeffekt.kontinuum.models.ConfigurationFileManager;
import com.metaeffekt.kontinuum.models.ProcessorDefinitions;
import com.metaeffekt.kontinuum.models.YamlProcessorCatalog;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
public class ProcessorCommands {

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String DIM = "\u001B[2m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String RED = "\u001B[31m";

    private final YamlProcessorCatalog yamlProcessorCatalog;

    private final ProcessorExecutionBackend processorExecutionBackend;

    private final ConfigurationFileManager configurationFileManager;

    public ProcessorCommands(
        YamlProcessorCatalog yamlProcessorCatalog,
        MavenProcessorExecutor mavenProcessorExecutor,
        ConfigurationFileManager configurationFileManager
    ) {
        this.yamlProcessorCatalog = yamlProcessorCatalog;
        this.processorExecutionBackend = mavenProcessorExecutor;
        this.configurationFileManager = configurationFileManager;
    }

    @ShellMethod(key = "processor list", value = "List all known processors.")
    public String list() {
        List<ProcessorDefinitions.Processor> processors = yamlProcessorCatalog.getProcessors();
        StringBuilder sb = new StringBuilder();

        sb.append(System.lineSeparator());
        sb.append(BOLD).append("  Available Processors").append(RESET).append(System.lineSeparator());
        sb.append(DIM).append("  ").append("─".repeat(60)).append(RESET).append(System.lineSeparator());
        sb.append(System.lineSeparator());

        int processorIndex = 0;
        for (ProcessorDefinitions.Processor processor : processors) {
            sb.append("  ").append(BLUE).append(String.format("[%d]", processorIndex)).append(RESET);
            sb.append(" ").append(BOLD).append(processor.getName()).append(RESET);
            sb.append(System.lineSeparator());
            sb.append(DIM).append("      ").append(processor.getId()).append(RESET);
            if (processor.getDescription() != null && !processor.getDescription().isEmpty()) {
                String desc = processor.getDescription().trim();
                if (desc.length() > 70) {
                    desc = desc.substring(0, 67) + "...";
                }
                sb.append(System.lineSeparator());
                sb.append(DIM).append("      ").append(desc).append(RESET);
            }
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
            processorIndex++;
        }

        sb.append(DIM).append("  ").append("─".repeat(60)).append(RESET).append(System.lineSeparator());
        sb.append("  ").append(GREEN).append(Integer.toString(processorIndex)).append(RESET).append(" processors available").append(System.lineSeparator());
        sb.append("  ").append(DIM).append("Use 'processor show --processor-id <id>' to see details").append(RESET).append(System.lineSeparator());
        sb.append(System.lineSeparator());

        return sb.toString();
    }

    @ShellMethod(key = "processor show", value = "Show required and optional parameters for one processor.")
    public String show(@ShellOption String processorId) {
        ProcessorDefinitions.Processor processor = getProcessorByIdOrIndex(processorId);

        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator());

        // Header
        builder.append(BOLD).append("  ").append(processor.getName()).append(RESET).append(System.lineSeparator());
        builder.append(DIM).append("  ").append("─".repeat(60)).append(RESET).append(System.lineSeparator());
        builder.append(System.lineSeparator());

        // Info section
        builder.append("  ").append(CYAN).append("POM Location:").append(RESET).append("  processors/").append(processor.getPomLocation()).append(System.lineSeparator());
        builder.append("  ").append(CYAN).append("Maven Goal:  ").append(RESET).append(processor.getGoal()).append(System.lineSeparator());

        if (processor.getDescription() != null && !processor.getDescription().isEmpty()) {
            builder.append(System.lineSeparator());
            builder.append("  ").append(CYAN).append("Description:").append(RESET).append(System.lineSeparator());
            for (String line : processor.getDescription().trim().split("\n")) {
                builder.append("    ").append(line.trim()).append(System.lineSeparator());
            }
        }

        // Parameters section
        List<ProcessorDefinitions.ProcessorParameter> required = processor.getParameters().stream()
            .filter(parameter -> Boolean.TRUE.equals(parameter.getRequired()))
            .toList();
        List<ProcessorDefinitions.ProcessorParameter> optional = processor.getParameters().stream()
            .filter(parameter -> !Boolean.TRUE.equals(parameter.getRequired()))
            .toList();

        if (!required.isEmpty()) {
            builder.append(System.lineSeparator());
            appendParameterSection(builder, "Required Parameters", required);
        }

        if (!optional.isEmpty()) {
            builder.append(System.lineSeparator());
            appendParameterSection(builder, "Optional Parameters", optional);
        }

        // Footer with run command hint
        builder.append(System.lineSeparator());
        builder.append(DIM).append("  ").append("─".repeat(60)).append(RESET).append(System.lineSeparator());
        builder.append("  ").append(GREEN).append("Run with:").append(RESET).append(" processor run --processor-id ").append(processor.getId()).append(System.lineSeparator());
        builder.append(System.lineSeparator());

        return builder.toString();
    }

    private static void appendParameterSection(
        StringBuilder builder,
        String sectionTitle,
        List<ProcessorDefinitions.ProcessorParameter> parameters
    ) {
        builder.append("  ").append(BOLD).append(sectionTitle).append(RESET).append(System.lineSeparator());

        if (parameters.isEmpty()) {
            builder.append("    ").append(DIM).append("(none)").append(RESET).append(System.lineSeparator());
            return;
        }

        for (ProcessorDefinitions.ProcessorParameter parameter : parameters) {
            builder.append("    ").append(YELLOW).append(parameter.getKey()).append(RESET).append(System.lineSeparator());
            if (parameter.getDescription() != null && !parameter.getDescription().isEmpty()) {
                String desc = parameter.getDescription().trim();
                if (desc.length() > 65) {
                    desc = desc.substring(0, 62) + "...";
                }
                builder.append("      ").append(DIM).append(desc).append(RESET).append(System.lineSeparator());
            }
            if (parameter.getValue() != null && !parameter.getValue().isEmpty()) {
                builder.append("      ").append(DIM).append("(default: ").append(parameter.getValue()).append(")").append(RESET).append(System.lineSeparator());
            }
        }
    }

    @ShellMethod(key = "processor run", value = "Execute a processor in local or container mode.")
    public void run(
        @ShellOption(value = "processor-id", defaultValue = "") String processorId,
        @ShellOption(value = "dry-run", defaultValue = "false") boolean dryRun,
        @ShellOption(value = "debug", defaultValue = "false") boolean debug,
        @ShellOption(value = "configuration", defaultValue = "") String configuration
    ) throws Exception {
        ProcessorDefinitions.Processor processor;
        Map<ProcessorDefinitions.ProcessorParameter, String> parameterToValueMap;

        System.out.println();
        System.out.println(BOLD + "  Processor Execution" + RESET);
        System.out.println(DIM + "  " + "─".repeat(60) + RESET);
        System.out.println();

        if (!configuration.isEmpty()) {
            // Load from configuration file
            ConfigurationFileManager.ConfigurationData configData = configurationFileManager.loadConfiguration(configuration);
            processor = configData.getProcessor();
            parameterToValueMap = mergeParameterValues(processor, configData.getParameterValues());

            System.out.println("  " + GREEN + "✓" + RESET + " Loaded configuration from: " + CYAN + configuration + RESET);
            System.out.println("  " + GREEN + "✓" + RESET + " Processor: " + BOLD + processor.getName() + RESET + " (" + processor.getId() + ")");
        } else {
            // Interactive mode
            processor = getProcessorByIdOrIndex(processorId);

            System.out.println("  " + CYAN + "Processor:" + RESET + " " + BOLD + processor.getName() + RESET);
            System.out.println("  " + CYAN + "Description:" + RESET + " " + (processor.getDescription() != null ? processor.getDescription().trim().split("\n")[0] : "N/A"));
            System.out.println();

            parameterToValueMap = promptForProcessorParameters(processor);
        }

        // Show execution info
        System.out.println();
        System.out.println("  " + DIM + "Configuration:" + RESET);
        if (dryRun) {
            System.out.println("    " + YELLOW + "● Dry run mode enabled" + RESET);
        }
        if (debug) {
            System.out.println("    " + YELLOW + "● Debug mode enabled (-X flag)" + RESET);
        }
        System.out.println("    " + CYAN + "POM:" + RESET + " processors/" + processor.getPomLocation());
        System.out.println("    " + CYAN + "Goal:" + RESET + " " + processor.getGoal());
        System.out.println();

        // Build and show command
        ProcessorExecution processorExecution = new ProcessorExecution(processor.getPomLocation(), processor.getGoal(), parameterToValueMap, dryRun, debug);
        List<String> command = processorExecutionBackend.buildExecutionCommand(processorExecution);

        System.out.println("  " + DIM + "Command:" + RESET);
        System.out.println("    " + String.join(" ", command));
        System.out.println();

        // Execute
        System.out.println("  " + BOLD + "Executing..." + RESET);
        System.out.println();

        long startTime = System.currentTimeMillis();
        boolean success = processorExecutionBackend.execute(command);
        long duration = System.currentTimeMillis() - startTime;

        System.out.println();
        if (success) {
            System.out.println("  " + GREEN + "✓ Execution completed successfully" + RESET + " (" + duration + "ms)");
        } else {
            System.out.println("  " + RED + "✗ Execution failed" + RESET + " (" + duration + "ms)");
        }

        // Save configuration
        if (success && configuration.isBlank()) {
            Map<String, String> simpleParamValues = new LinkedHashMap<>();
            for (Map.Entry<ProcessorDefinitions.ProcessorParameter, String> entry : parameterToValueMap.entrySet()) {
                simpleParamValues.put(entry.getKey().getKey(), entry.getValue());
            }
            Path savedConfig = configurationFileManager.saveConfiguration(processor, simpleParamValues, dryRun, debug);
            System.out.println("  " + GREEN + "✓" + RESET + " Configuration saved to: " + savedConfig);
        }
        System.out.println();
    }

    private Map<ProcessorDefinitions.ProcessorParameter, String> mergeParameterValues(
        ProcessorDefinitions.Processor processor,
        Map<String, String> loadedValues
    ) {
        Map<ProcessorDefinitions.ProcessorParameter, String> parameterToValueMap = new LinkedHashMap<>();

        for (ProcessorDefinitions.ProcessorParameter param : processor.getParameters()) {
            String loadedValue = loadedValues.get(param.getKey());
            if (loadedValue != null) {
                parameterToValueMap.put(param, loadedValue);
            }
        }

        return parameterToValueMap;
    }

    private Map<ProcessorDefinitions.ProcessorParameter, String> promptForProcessorParameters(ProcessorDefinitions.Processor processor) {
        Map<ProcessorDefinitions.ProcessorParameter, String> parameterToValueMap = new LinkedHashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        List<ProcessorDefinitions.ProcessorParameter> required = processor.getParameters().stream()
            .filter(parameter -> Boolean.TRUE.equals(parameter.getRequired()))
            .toList();
        List<ProcessorDefinitions.ProcessorParameter> optional = processor.getParameters().stream()
            .filter(parameter -> !Boolean.TRUE.equals(parameter.getRequired()))
            .toList();

        System.out.println("  " + BOLD + "Required Parameters:" + RESET);
        for (ProcessorDefinitions.ProcessorParameter parameter : required) {
            String value = promptForParameter(reader, parameter, true);
            if (value != null && !value.isEmpty()) {
                parameterToValueMap.put(parameter, value);
            }
        }

        System.out.println();
        System.out.println("  " + DIM + "Optional Parameters (press Enter to skip):" + RESET);
        for (ProcessorDefinitions.ProcessorParameter parameter : optional) {
            String value = promptForParameter(reader, parameter, false);
            if (value != null && !value.isEmpty()) {
                parameterToValueMap.put(parameter, value);
            }
        }

        return parameterToValueMap;
    }

    private String promptForParameter(BufferedReader reader, ProcessorDefinitions.ProcessorParameter parameter, boolean isRequired) {
        while (true) {
            String prefilledValue = parameter.getValue() != null && !parameter.getValue().isEmpty()
                ? parameter.getValue()
                : null;

            System.out.println();
            System.out.println("  " + YELLOW + parameter.getKey() + RESET);

            if (parameter.getDescription() != null && !parameter.getDescription().isEmpty()) {
                String desc = parameter.getDescription().trim();
                if (desc.length() > 70) {
                    desc = desc.substring(0, 67) + "...";
                }
                System.out.println("    " + DIM + desc + RESET);
            }

            if (prefilledValue != null) {
                System.out.print("    " + GREEN + "[" + prefilledValue + "]" + RESET + " Enter value: ");
            } else if (isRequired) {
                System.out.print("    " + RED + "(required)" + RESET + " Enter value: ");
            } else {
                System.out.print("    " + DIM + "(optional)" + RESET + " Enter value: ");
            }

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
                if (prefilledValue != null) {
                    System.out.println("    " + GREEN + "→ Using prefilled value" + RESET);
                    return prefilledValue;
                }
                if (isRequired) {
                    System.out.println("    " + RED + "✗ This parameter is required" + RESET);
                    continue;
                }
                return null;
            }

            return value;
        }
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
