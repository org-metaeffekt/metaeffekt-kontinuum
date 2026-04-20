package com.metaeffekt.kontinuum.execution;

import java.util.List;

public interface ProcessorExecutionBackend {

    List<String> buildExecutionCommand(ProcessorExecution processorExecution);

    boolean execute(List<String> command);

}
