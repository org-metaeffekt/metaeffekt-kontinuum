package com.metaeffekt.kontinuum.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class YamlProcessorCatalog implements ProcessorCatalog{

    public final List<ProcessorDefinitions.Processor> catalog;

    public YamlProcessorCatalog(File processorsYamlFile) {
        this.catalog = load(processorsYamlFile);
    }

    @Override
    public List<ProcessorDefinitions.Processor> getProcessors() {
        return List.of();
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

    private static List<ProcessorDefinitions.Processor> load(File processorsYamlFile) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            ProcessorDefinitions processorDefinitions = objectMapper.readValue(processorsYamlFile, ProcessorDefinitions.class);
            return processorDefinitions.processors;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
