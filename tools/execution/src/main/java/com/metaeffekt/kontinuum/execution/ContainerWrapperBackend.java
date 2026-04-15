package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.data.models.BackendType;
import com.metaeffekt.kontinuum.data.models.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.execution.core.ExecutionPlan;
import com.metaeffekt.kontinuum.execution.core.ProcessorExecutionBackend;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ContainerWrapperBackend implements ProcessorExecutionBackend {

    private final ContainerCommandClient commandClient;

    public ContainerWrapperBackend(ContainerCommandClient commandClient) {
        this.commandClient = commandClient;
    }

    @Override
    public BackendType backendType() {
        return BackendType.CONTAINER_WRAPPER;
    }

    @Override
    public ExecuteProcessorResult execute(ExecutionPlan plan) throws IOException {
        if (plan.dryRun()) {
            return new ExecuteProcessorResult(
                0,
                String.join(" ", plan.command()),
                List.of("Dry-run mode.", "Container submission skipped."),
                Map.of("backend", backendType().name())
            );
        }
        return commandClient.submit(plan);
    }
}
