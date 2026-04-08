package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorRequest;
import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;

import java.io.IOException;
import java.util.List;

public class GuidedProcessorExecutor {

    private final ProcessorCatalog processorCatalog;
    private final RequiredPropertyValidator requiredPropertyValidator;
    private final MavenCommandPlanner mavenCommandPlanner;

    public GuidedProcessorExecutor(
        ProcessorCatalog processorCatalog,
        RequiredPropertyValidator requiredPropertyValidator,
        MavenCommandPlanner mavenCommandPlanner
    ) {
        this.processorCatalog = processorCatalog;
        this.requiredPropertyValidator = requiredPropertyValidator;
        this.mavenCommandPlanner = mavenCommandPlanner;
    }

    public List<ProcessorDefinition> listProcessors() {
        return processorCatalog.list();
    }

    public ExecuteProcessorResult execute(ExecuteProcessorRequest request, ProcessorExecutionBackend backend)
        throws IOException, InterruptedException {
        ProcessorDefinition processor = processorCatalog.findById(request.processorId())
            .orElseThrow(() -> new IllegalArgumentException("Unknown processor id: " + request.processorId()));

        List<String> missing = requiredPropertyValidator.missing(processor, request.properties());
        if (!missing.isEmpty()) {
            throw new IllegalArgumentException("Missing required properties: " + String.join(", ", missing));
        }

        ExecutionPlan plan = mavenCommandPlanner.plan(processor, request);
        return backend.execute(plan);
    }
}
