package org.metaeffekt.kontinuum.models.shared;


import lombok.Data;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
        Stage stage;
        List<ProcessorParameter> parameters;
        int executionOrder;

        public void setProcessorParameter(String key, String value) {
            if (value == null) return;

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

        public String buildMavenCall() {
            StringBuilder mavenCall = new StringBuilder();
            mavenCall.append("mvn ")
            .append("-f ").append(pomLocation).append(" ")
            .append(goal).append(" ").append(System.lineSeparator());
            
            for (ProcessorParameter parameter : parameters) {
                if (StringUtils.isNotBlank(parameter.value)) {
                    mavenCall.append("-D").append(parameter.getKey()).append("=")
                    .append("'").append(parameter.getValue()).append("'").append(System.lineSeparator());
                }
            }

            return mavenCall.toString();
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
