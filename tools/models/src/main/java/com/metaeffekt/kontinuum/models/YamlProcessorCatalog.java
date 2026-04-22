package com.metaeffekt.kontinuum.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class YamlProcessorCatalog implements ProcessorCatalog {

    public final List<ProcessorDefinitions.Processor> catalog;

    public YamlProcessorCatalog() {
        File processorsYamlFile = locateProcessorsYaml();
        this.catalog = load(processorsYamlFile);
    }

    @Override
    public List<ProcessorDefinitions.Processor> getProcessors() {
        return catalog;
    }

    @Override
    public ProcessorDefinitions.Processor getProcessorById(String processorId) {
        List<ProcessorDefinitions.Processor> foundProcessors = catalog.stream()
                .filter(p -> p.id.equals(processorId))
                .toList();

        if (foundProcessors.size() > 1) {
            log.error("More than one processor found with id {}, continuing with processor {}", processorId, foundProcessors.get(0).id);
        }

        return foundProcessors.get(0);
    }

    private List<ProcessorDefinitions.Processor> load(File processorsYamlFile) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            ProcessorDefinitions processorDefinitions = objectMapper.readValue(processorsYamlFile, ProcessorDefinitions.class);
            return processorDefinitions.processors.stream().sorted(Comparator.comparing(p -> p.name)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File locateProcessorsYaml() {
        String kontinuumDir = System.getProperty(ProjectProperties.KONTINUUM_DIR.getPropertyKey());
        StringBuilder sb = new StringBuilder().append(kontinuumDir);
        if (kontinuumDir.endsWith("/")) {
            sb.append("procesors/processors.yaml");
        } else {
            sb.append("/procesors/processors.yaml");
        }

        if (Files.exists(Path.of(sb.toString()))) {
            return new File(sb.toString());
        } else {
            throw new IllegalStateException("Unable to locate repository root containing processors/processors.yaml");
        }
    }
}
