package com.metaeffekt.kontinuum.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
public class ConfigurationFileManager {

    private static final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final Path configurationsDir;

    public ConfigurationFileManager() {
        this.configurationsDir = locateConfigurationsDir();
    }

    /**
     * Save a processor configuration to a YAML file with a timestamp.
     */
    public Path saveConfiguration(ProcessorDefinitions.Processor processor, Map<String, String> parameterValues, boolean dryRun, boolean debug) throws IOException {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String filename = String.format("%s-%s.yaml", processor.getId(), timestamp);
        Path configFile = configurationsDir.resolve(filename);

        ProcessorDefinitions config = createConfiguration(processor, parameterValues, dryRun, debug);
        yamlMapper.writeValue(configFile.toFile(), config);

        log.info("Configuration saved to: {}", configFile);
        return configFile;
    }

    /**
     * Load a configuration file and return the processor and parameter values.
     */
    public ConfigurationData loadConfiguration(String configFilePath) throws IOException {
        Path configFile = Paths.get(configFilePath);
        if (!Files.exists(configFile)) {
            // Try to resolve relative to configurations directory
            configFile = configurationsDir.resolve(configFilePath);
            if (!Files.exists(configFile)) {
                throw new IOException("Configuration file not found: " + configFilePath);
            }
        }

        ProcessorDefinitions config = yamlMapper.readValue(configFile.toFile(), ProcessorDefinitions.class);
        if (config.getProcessors() == null || config.getProcessors().isEmpty()) {
            throw new IOException("Configuration file contains no processors");
        }

        ProcessorDefinitions.Processor processor = config.getProcessors().get(0);
        Map<String, String> parameterValues = extractParameterValues(processor);

        return new ConfigurationData(processor, parameterValues);
    }

    private ProcessorDefinitions createConfiguration(ProcessorDefinitions.Processor processor, Map<String, String> parameterValues, boolean dryRun, boolean debug) {
        ProcessorDefinitions config = new ProcessorDefinitions();
        ProcessorDefinitions.Processor configProcessor = new ProcessorDefinitions.Processor();

        configProcessor.setId(processor.getId());
        configProcessor.setName(processor.getName());
        configProcessor.setDescription(processor.getDescription());
        configProcessor.setPomLocation(processor.getPomLocation());
        configProcessor.setGoal(processor.getGoal());
        configProcessor.setExecutionOrder(processor.getExecutionOrder());

        // Create parameters with the used values
        List<ProcessorDefinitions.ProcessorParameter> configParameters = processor.getParameters().stream()
            .map(param -> {
                ProcessorDefinitions.ProcessorParameter configParam = new ProcessorDefinitions.ProcessorParameter();
                configParam.setKey(param.getKey());
                configParam.setDescription(param.getDescription());
                configParam.setRequired(param.getRequired());
                configParam.setValue(parameterValues.get(param.getKey()));
                return configParam;
            })
            .toList();

        configProcessor.setParameters(configParameters);
        config.setProcessors(List.of(configProcessor));

        return config;
    }

    private Map<String, String> extractParameterValues(ProcessorDefinitions.Processor processor) {
        Map<String, String> parameterValues = new java.util.LinkedHashMap<>();
        if (processor.getParameters() != null) {
            for (ProcessorDefinitions.ProcessorParameter param : processor.getParameters()) {
                if (param.getValue() != null && !param.getValue().isEmpty()) {
                    parameterValues.put(param.getKey(), param.getValue());
                }
            }
        }
        return parameterValues;
    }

    private Path locateConfigurationsDir() {
        Path current = Path.of("").toAbsolutePath().normalize();
        while (current != null) {
            Path repoRoot = current.resolve("processors");
            if (Files.exists(repoRoot) && Files.isDirectory(repoRoot)) {
                Path configDir = repoRoot.resolve("configurations");
                if (!Files.exists(configDir)) {
                    try {
                        Files.createDirectories(configDir);
                    } catch (IOException e) {
                        log.warn("Failed to create configurations directory, using current directory", e);
                        return Path.of(".").toAbsolutePath();
                    }
                }
                return configDir;
            }
            current = current.getParent();
        }
        // Fallback to current directory
        return Path.of(".").toAbsolutePath();
    }

    public static class ConfigurationData {
        private final ProcessorDefinitions.Processor processor;
        private final Map<String, String> parameterValues;

        public ConfigurationData(ProcessorDefinitions.Processor processor, Map<String, String> parameterValues) {
            this.processor = processor;
            this.parameterValues = parameterValues;
        }

        public ProcessorDefinitions.Processor getProcessor() {
            return processor;
        }

        public Map<String, String> getParameterValues() {
            return parameterValues;
        }
    }
}
