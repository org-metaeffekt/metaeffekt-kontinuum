package com.metaeffekt.kontinuum.execution.contract;

import java.util.List;
import java.util.Map;

public record ExecuteProcessorResult(
    int exitCode,
    String effectiveCommand,
    List<String> logs,
    Map<String, String> diagnostics
) {
}
