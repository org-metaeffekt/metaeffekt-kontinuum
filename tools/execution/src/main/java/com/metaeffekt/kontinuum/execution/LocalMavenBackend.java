package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.data.models.BackendType;
import com.metaeffekt.kontinuum.data.models.ExecuteProcessorResult;
import com.metaeffekt.kontinuum.execution.core.ExecutionPlan;
import com.metaeffekt.kontinuum.execution.core.ProcessorExecutionBackend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class LocalMavenBackend implements ProcessorExecutionBackend {

    @Override
    public BackendType backendType() {
        return BackendType.LOCAL_MAVEN;
    }

    @Override
    public ExecuteProcessorResult execute(ExecutionPlan plan) throws IOException, InterruptedException {
        String effectiveCommand = String.join(" ", plan.command());
        if (plan.dryRun()) {
            return new ExecuteProcessorResult(0, effectiveCommand, List.of("Dry-run mode."), Map.of());
        }

        ProcessBuilder processBuilder = new ProcessBuilder(plan.command());
        processBuilder.directory(plan.workingDirectory().toFile());
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        return new ExecuteProcessorResult(exitCode, effectiveCommand, null, Map.of("backend", backendType().name()));
    }
}
