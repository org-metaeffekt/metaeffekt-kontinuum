package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public record ExecutionPlan(
    ProcessorDefinition processor,
    List<String> command,
    Map<String, String> normalizedProperties,
    Path workingDirectory,
    boolean dryRun
) {
}
