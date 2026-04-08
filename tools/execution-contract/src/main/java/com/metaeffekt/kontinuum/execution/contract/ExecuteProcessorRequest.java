package com.metaeffekt.kontinuum.execution.contract;

import java.nio.file.Path;
import java.util.Map;

public record ExecuteProcessorRequest(
    String processorId,
    Map<String, String> properties,
    Path workingDirectory,
    boolean dryRun
) {
}
