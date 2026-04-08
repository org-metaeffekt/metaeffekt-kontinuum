package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;

import java.util.List;
import java.util.Map;

public class RequiredPropertyValidator {

    public List<String> missing(ProcessorDefinition processor, Map<String, String> providedProperties) {
        return processor.parameters().stream()
            .filter(parameter -> parameter.required() && !providedProperties.containsKey(parameter.key()))
            .map(parameter -> parameter.key())
            .toList();
    }
}
