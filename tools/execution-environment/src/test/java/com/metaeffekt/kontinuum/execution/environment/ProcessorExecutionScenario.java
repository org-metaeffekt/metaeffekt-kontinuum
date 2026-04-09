package com.metaeffekt.kontinuum.execution.environment;

import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

record ProcessorExecutionScenario(
    ProcessorDefinition processor,
    ProcessorExecutionMode mode,
    Map<String, String> propertyTemplates,
    List<String> expectedOutputTemplates,
    WorkspaceInitializer workspaceInitializer
) {

    interface WorkspaceInitializer {
        void initialize(Path workspaceDir) throws IOException;
    }
}
