package com.metaeffekt.kontinuum.execution.environment;

import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;
import com.metaeffekt.kontinuum.execution.core.ProcessorCatalog;
import com.metaeffekt.kontinuum.execution.core.YamlProcessorCatalog;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ProcessorScenarioCoverageTest {

    /**
     * This test ensures that all processors which are listed in the YAML catalog, have at least one test scenario,
     * ensuring that the processors run successfully. This does not verify whether the processor correctly executes
     * its underlying processes.
     */
    @Test
    void allCatalogProcessorsHaveTestScenarios() {
        Path repoRoot = TestWorkspace.locateRepoRoot();
        ProcessorCatalog catalog = new YamlProcessorCatalog(repoRoot.resolve("processors/processors.yaml"));

        Set<String> catalogIds = catalog.list().stream().map(ProcessorDefinition::id).collect(Collectors.toSet());
        Set<String> scenarioIds = catalog.list().stream()
            .map(ProcessorExecutionScenarioFactory::forProcessor)
            .map(scenario -> scenario.processor().id())
            .collect(Collectors.toSet());

        assertFalse(catalogIds.isEmpty());
        assertEquals(catalogIds, scenarioIds);
    }
}
