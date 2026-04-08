package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.BackendType;
import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorRequest;
import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;
import com.metaeffekt.kontinuum.execution.contract.ProcessorParameterDefinition;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AllProcessorsDryRunExecutionTest {

    private GuidedProcessorExecutor executor;
    private ProcessorCatalog catalog;
    private Path repoRoot;

    @BeforeAll
    void setUp() {
        repoRoot = TestWorkspace.locateRepoRoot();
        catalog = new YamlProcessorCatalog(repoRoot.resolve("processors/processors.yaml"));
        executor = new GuidedProcessorExecutor(catalog, new RequiredPropertyValidator(), new MavenCommandPlanner());
    }

    Stream<ProcessorDefinition> processors() {
        return catalog.list().stream();
    }

    @ParameterizedTest(name = "dry-run plan for {0}")
    @MethodSource("processors")
    void createsExecutablePlanForEveryProcessor(ProcessorDefinition processor) throws Exception {
        Map<String, String> properties = buildRequiredProperties(processor.parameters());
        ExecuteProcessorRequest request = new ExecuteProcessorRequest(processor.id(), properties, repoRoot, true);

        ExecuteProcessorResult result = executor.execute(request, new PlanOnlyBackend());

        assertEquals(0, result.exitCode());
        assertTrue(result.effectiveCommand().contains("mvn -f "));
        assertTrue(result.effectiveCommand().contains(processor.pomPath()));
    }

    private Map<String, String> buildRequiredProperties(List<ProcessorParameterDefinition> parameters) {
        Map<String, String> values = new LinkedHashMap<>();
        for (ProcessorParameterDefinition parameter : parameters) {
            if (!parameter.required()) {
                continue;
            }
            values.put(parameter.key(), defaultValue(parameter.key()));
        }
        return values;
    }

    private String defaultValue(String propertyKey) {
        if (propertyKey.endsWith(".dir")) {
            return repoRoot.resolve("target/test-workspace").toString();
        }
        if (propertyKey.endsWith(".file") || propertyKey.endsWith(".yaml") || propertyKey.endsWith(".xml")) {
            return repoRoot.resolve("target/test-workspace").resolve("placeholder.txt").toString();
        }
        if (propertyKey.startsWith("env.")) {
            return "TEST_ENV_VALUE";
        }
        return "test-value";
    }

    private static final class PlanOnlyBackend implements ProcessorExecutionBackend {

        @Override
        public BackendType backendType() {
            return BackendType.LOCAL_MAVEN;
        }

        @Override
        public ExecuteProcessorResult execute(ExecutionPlan plan) {
            return new ExecuteProcessorResult(0, String.join(" ", plan.command()), List.of("planned"), Map.of());
        }
    }
}
