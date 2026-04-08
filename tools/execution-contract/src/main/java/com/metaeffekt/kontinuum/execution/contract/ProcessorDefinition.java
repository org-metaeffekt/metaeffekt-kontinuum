package com.metaeffekt.kontinuum.execution.contract;

import java.util.List;

public record ProcessorDefinition(
    String id,
    String displayName,
    String pomPath,
    String goal,
    List<ProcessorParameterDefinition> parameters
) {
}
