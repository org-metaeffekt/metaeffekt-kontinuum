package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.data.models.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.execution.core.ExecutionPlan;

import java.io.IOException;

public interface ContainerCommandClient {

    ExecuteProcessorResult submit(ExecutionPlan executionPlan) throws IOException;
}
