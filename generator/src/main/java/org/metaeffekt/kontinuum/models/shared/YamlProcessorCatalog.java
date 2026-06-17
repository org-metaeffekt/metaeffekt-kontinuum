package org.metaeffekt.kontinuum.models.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class YamlProcessorCatalog implements ProcessorCatalog {

    public final List<ProcessorDefinitions.Processor> catalog;

    public YamlProcessorCatalog() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("processors.yaml");
        if (is == null) {
            throw new IllegalStateException("processors.yaml not found on classpath");
        }
        this.catalog = load(is);
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

        ProcessorDefinitions.Processor original = foundProcessors.get(0);
        ProcessorDefinitions.Processor copy = new ProcessorDefinitions.Processor();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setDescription(original.getDescription());
        copy.setPomLocation(original.getPomLocation());
        copy.setGoal(original.getGoal());
        copy.setStage(original.getStage());
        copy.setExecutionOrder(original.getExecutionOrder());
        List<ProcessorDefinitions.ProcessorParameter> copiedParameters = new ArrayList<>();
        for (ProcessorDefinitions.ProcessorParameter parameter : original.getParameters()) {
            ProcessorDefinitions.ProcessorParameter parameterCopy = new ProcessorDefinitions.ProcessorParameter();
            parameterCopy.setKey(parameter.getKey());
            parameterCopy.setDescription(parameter.getDescription());
            parameterCopy.setRequired(parameter.getRequired());
            parameterCopy.setValue(parameter.getValue());
            copiedParameters.add(parameterCopy);
        }
        copy.setParameters(copiedParameters);
        return copy;
    }

    private List<ProcessorDefinitions.Processor> load(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            ProcessorDefinitions processorDefinitions = objectMapper.readValue(inputStream, ProcessorDefinitions.class);
            return processorDefinitions.processors.stream().sorted(Comparator.comparing(p -> p.name)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
