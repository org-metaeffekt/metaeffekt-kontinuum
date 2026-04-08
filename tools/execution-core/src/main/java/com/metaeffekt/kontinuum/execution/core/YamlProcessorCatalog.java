package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;
import com.metaeffekt.kontinuum.execution.contract.ProcessorParameterDefinition;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class YamlProcessorCatalog implements ProcessorCatalog {

    private final List<ProcessorDefinition> processors;

    public YamlProcessorCatalog(Path processorsYaml) {
        this.processors = load(processorsYaml);
    }

    @Override
    public List<ProcessorDefinition> list() {
        return Collections.unmodifiableList(processors);
    }

    @Override
    public Optional<ProcessorDefinition> findById(String processorId) {
        return processors.stream().filter(processor -> processor.id().equals(processorId)).findFirst();
    }

    private static List<ProcessorDefinition> load(Path processorsYaml) {
        try (InputStream inputStream = Files.newInputStream(processorsYaml)) {
            Yaml yaml = new Yaml();
            Map<String, Object> root = yaml.load(inputStream);
            List<Map<String, Object>> rawProcessors = castList(root.get("processors"));
            List<ProcessorDefinition> definitions = new ArrayList<>();
            for (Map<String, Object> rawProcessor : rawProcessors) {
                String pom = String.valueOf(rawProcessor.get("pom"));
                String id = pom.replace(".xml", "").substring(pom.indexOf('/') + 1);
                String displayName = String.valueOf(rawProcessor.getOrDefault("name", id));
                String goal = String.valueOf(rawProcessor.getOrDefault("goal", "process-resources"));

                List<Map<String, Object>> rawParameters = castList(rawProcessor.getOrDefault("parameters", List.of()));
                List<ProcessorParameterDefinition> parameters = rawParameters.stream()
                    .map(YamlProcessorCatalog::toParameterDefinition)
                    .toList();

                definitions.add(new ProcessorDefinition(id, displayName, pom, goal, parameters));
            }
            return definitions;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load processors catalog from " + processorsYaml, e);
        }
    }

    private static ProcessorParameterDefinition toParameterDefinition(Map<String, Object> rawParameter) {
        String key = String.valueOf(rawParameter.get("key"));
        boolean required = Boolean.parseBoolean(String.valueOf(rawParameter.getOrDefault("required", false)));
        String description = String.valueOf(rawParameter.getOrDefault("description", ""));
        return new ProcessorParameterDefinition(key, required, description);
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> castList(Object source) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (!(source instanceof List<?> list)) {
            return result;
        }
        for (Object item : list) {
            if (item instanceof Map<?, ?> map) {
                result.add(new LinkedHashMap<>((Map<String, Object>) map));
            }
        }
        return result;
    }
}
