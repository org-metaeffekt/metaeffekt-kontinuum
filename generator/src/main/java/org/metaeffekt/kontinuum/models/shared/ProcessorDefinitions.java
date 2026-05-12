package org.metaeffekt.kontinuum.models.shared;


import lombok.Data;

import java.util.List;

@Data
public class ProcessorDefinitions {
    List<Processor> processors;

    @Data
    public static class Processor{
        String id;
        String name;
        String description;
        String pomLocation;
        String goal;
        List<ProcessorParameter> parameters;
        int executionOrder;

        public void setProcessorParameter(String key, String value) {
            if (parameters.stream().noneMatch(p -> p.key.equals(key))) {
                throw new IllegalStateException("The key " + key + " for processor " + id + " required during pipeline " +
                        "creation does not exist. Consider checking with the processors.yaml.");
            }

            for (ProcessorParameter processorParameter : parameters) {
                if (processorParameter.key.equals(key)) {
                    processorParameter.setValue(value);
                }
            }
        }
    }

    @Data
    public static class ProcessorParameter {
        String key;
        String description;
        Boolean required;
        String value;
    }

}
