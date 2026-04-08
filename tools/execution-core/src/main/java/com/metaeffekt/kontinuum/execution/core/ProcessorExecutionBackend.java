package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.BackendType;
import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorResult;

import java.io.IOException;

public interface ProcessorExecutionBackend {

    BackendType backendType();

    ExecuteProcessorResult execute(ExecutionPlan plan) throws IOException, InterruptedException;
}
