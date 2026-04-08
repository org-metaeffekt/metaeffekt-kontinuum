package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorRequest;
import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;
import com.metaeffekt.kontinuum.execution.contract.ProcessorParameterDefinition;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MavenCommandPlannerTest {

    @Test
    void plansDeterministicMavenCommand() {
        ProcessorDefinition processor = new ProcessorDefinition(
            "advise/advise_enrich-inventory",
            "Enrich Inventory",
            "advise/advise_enrich-inventory.xml",
            "process-resources",
            List.of(new ProcessorParameterDefinition("input.inventory.file", true, ""))
        );

        ExecuteProcessorRequest request = new ExecuteProcessorRequest(
            processor.id(),
            Map.of("input.inventory.file", "in.xlsx", "output.inventory.file", "out.xlsx"),
            Path.of("."),
            true
        );

        ExecutionPlan plan = new MavenCommandPlanner().plan(processor, request);

        assertEquals("mvn", plan.command().get(0));
        assertTrue(plan.command().get(2).endsWith("processors/advise/advise_enrich-inventory.xml"));
        assertEquals("process-resources", plan.command().get(3));
        assertEquals("-Dinput.inventory.file=in.xlsx", plan.command().get(4));
        assertEquals("-Doutput.inventory.file=out.xlsx", plan.command().get(5));
    }
}
