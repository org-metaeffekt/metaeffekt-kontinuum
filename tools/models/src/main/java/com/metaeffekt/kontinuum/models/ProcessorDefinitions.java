package com.metaeffekt.kontinuum.models;


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
    }

    @Data
    public static class ProcessorParameter {
        String key;
        String description;
        Boolean required;
        String value;
    }

}
