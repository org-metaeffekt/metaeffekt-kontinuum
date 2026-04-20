package com.metaeffekt.kontinuum.execution;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessorExecutionTest {

    public static List<ProcessorExecution> processorExecutionList;
    public static MavenProcessorExecutor mavenProcessorExecutor;

    @BeforeAll
    public static void setup() {
        processorExecutionList = ProcessorScenarios.createProcessorScenarios();
        mavenProcessorExecutor = new MavenProcessorExecutor();
    }

    @ParameterizedTest
    @ValueSource(processorExecutionList)
    public void testProcessorExecution(ProcessorExecution processorExecution) {
        List<String> command = mavenProcessorExecutor.buildExecutionCommand(processorExecution);
        assertTrue(mavenProcessorExecutor.execute(command));
    }


}
