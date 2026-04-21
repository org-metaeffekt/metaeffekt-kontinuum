package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.models.ProcessorDefinitions;

import java.util.Map;

public record ProcessorExecution(
    String pomLocation,
    String goal,
    Map<ProcessorDefinitions.ProcessorParameter, String> parameterToValueMap,
    boolean dryRun,
    boolean debug
) { }
