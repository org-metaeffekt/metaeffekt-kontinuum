package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessorCatalogContractTest {

    @Test
    void catalogEntriesReferenceExistingPomFiles() {
        Path repoRoot = TestWorkspace.locateRepoRoot();
        ProcessorCatalog catalog = new YamlProcessorCatalog(repoRoot.resolve("processors/processors.yaml"));

        List<ProcessorDefinition> processors = catalog.list();
        assertFalse(processors.isEmpty(), "No processors were loaded from processors.yaml");

        for (ProcessorDefinition processor : processors) {
            Path pom = repoRoot.resolve("processors").resolve(processor.pomPath());
            assertTrue(Files.exists(pom), "Missing pom for processor " + processor.id() + ": " + pom);
        }
    }

    @Test
    void processorIdsAreUnique() {
        Path repoRoot = TestWorkspace.locateRepoRoot();
        ProcessorCatalog catalog = new YamlProcessorCatalog(repoRoot.resolve("processors/processors.yaml"));

        Set<String> ids = new HashSet<>();
        for (ProcessorDefinition processor : catalog.list()) {
            assertTrue(ids.add(processor.id()), "Duplicate processor id detected: " + processor.id());
        }
    }
}
