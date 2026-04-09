package com.metaeffekt.kontinuum.execution.environment;

import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorRequest;
import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;
import com.metaeffekt.kontinuum.execution.core.GuidedProcessorExecutor;
import com.metaeffekt.kontinuum.execution.core.ProcessorCatalog;
import com.metaeffekt.kontinuum.execution.core.RequiredPropertyValidator;
import com.metaeffekt.kontinuum.execution.core.MavenCommandPlanner;
import com.metaeffekt.kontinuum.execution.core.YamlProcessorCatalog;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProcessorFunctionalExecutionTest {

    private Path repoRoot;
    private GuidedProcessorExecutor executor;
    private List<ProcessorExecutionScenario> scenarios;

    @BeforeAll
    void setUp() {
        repoRoot = TestWorkspace.locateRepoRoot();
        ProcessorCatalog catalog = new YamlProcessorCatalog(repoRoot.resolve("processors/processors.yaml"));
        executor = new GuidedProcessorExecutor(catalog, new RequiredPropertyValidator(), new MavenCommandPlanner());

        scenarios = new ArrayList<>();
        for (ProcessorDefinition processor : catalog.list()) {
            scenarios.add(ProcessorExecutionScenarioFactory.forProcessor(processor));
        }
    }

    Stream<ProcessorExecutionScenario> scenarios() {
        return scenarios.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("scenarios")
    void executeScenario(ProcessorExecutionScenario scenario, @TempDir Path tempDir) throws Exception {
        Path workspaceDir = tempDir.resolve(scenario.processor().id());
        Files.createDirectories(workspaceDir);
        scenario.workspaceInitializer().initialize(workspaceDir);

        Map<String, String> properties = materializeProperties(scenario.propertyTemplates(), workspaceDir);
        boolean dryRun = scenario.mode() == ProcessorExecutionMode.PLAN_ONLY;
        ExecuteProcessorRequest request = new ExecuteProcessorRequest(
            scenario.processor().id(),
            properties,
            repoRoot,
            dryRun
        );

        ExecuteProcessorResult result = executor.execute(request, new LocalMavenBackend());
        assertEquals(0, result.exitCode(), "Processor failed: " + scenario.processor().id());

        if (scenario.mode() == ProcessorExecutionMode.EXECUTE) {
            for (String expectedOutput : scenario.expectedOutputTemplates()) {
                Path expectedPath = materializePath(expectedOutput, workspaceDir);
                assertTrue(Files.exists(expectedPath), "Expected output missing: " + expectedPath);
            }
        }
    }

    private Map<String, String> materializeProperties(Map<String, String> templates, Path workspaceDir) {
        Map<String, String> materialized = new LinkedHashMap<>();
        templates.forEach((key, value) -> materialized.put(key, materializeValue(value, workspaceDir)));
        return materialized;
    }

    private Path materializePath(String template, Path workspaceDir) {
        return Path.of(materializeValue(template, workspaceDir));
    }

    private String materializeValue(String template, Path workspaceDir) {
        return template.replace(ProcessorExecutionScenarioFactory.workspaceToken(), workspaceDir.toString());
    }
}
