package com.metaeffekt.kontinuum.execution.container;

import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.execution.core.ExecutionPlan;

import java.io.IOException;

public interface ContainerCommandClient {

    ExecuteProcessorResult submit(ExecutionPlan executionPlan) throws IOException;
}
