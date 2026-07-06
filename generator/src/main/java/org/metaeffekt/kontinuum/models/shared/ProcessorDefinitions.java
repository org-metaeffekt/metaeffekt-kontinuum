package org.metaeffekt.kontinuum.models.shared;


import lombok.AccessLevel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
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

        public void setProcessorParameter(ProcessorParameterKey key, String value) {
            if (parameters.stream().noneMatch(p -> p.key == key)) {
                throw new IllegalStateException("The key " + key + " for processor " + id + " required during pipeline " +
                        "creation does not exist. Consider checking with the processors.yaml.");
            }

            for (ProcessorParameter processorParameter : parameters) {
                if (processorParameter.key == key) {
                    if (value == null && !processorParameter.required) {
                        return;
                    }
                    processorParameter.setValue(value);
                }
            }
        }
        // TODO: Implement this instead of having logic in the gitlab pipeline
        public String buildMavenCall() {
            StringBuilder mavenCall = new StringBuilder();
            mavenCall.append("mvn ")
            .append("-f ").append(pomLocation).append(" ")
            .append(goal).append(" ");
            
            for (ProcessorParameter parameter : parameters) {
                if (StringUtils.isNotBlank(parameter.value)) {
                    mavenCall.append("-D").append(parameter.getKey()).append("=")
                    .append("'").append(parameter.getValue()).append("' ");
                }
            }

            return mavenCall.toString();
        }
    }

    @Data
    public static class ProcessorParameter {
        ProcessorParameterKey key;
        String description;
        Boolean required;
        String value;
    }
}
