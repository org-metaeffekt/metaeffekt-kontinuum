package com.metaeffekt.kontinuum.execution.core;

import com.metaeffekt.kontinuum.execution.contract.ExecuteProcessorRequest;
import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MavenCommandPlanner {

    public ExecutionPlan plan(ProcessorDefinition processor, ExecuteProcessorRequest request) {
        Map<String, String> normalized = new TreeMap<>(request.properties());
        List<String> command = new ArrayList<>();
        Path pomPath = resolvePomPath(processor, request.workingDirectory());

        command.add("mvn");
        command.add("-f");
        command.add(pomPath.toString());
        command.add(processor.goal());
        normalized.forEach((key, value) -> command.add("-D" + key + "=" + value));

        return new ExecutionPlan(processor, command, normalized, request.workingDirectory(), request.dryRun());
    }

    private Path resolvePomPath(ProcessorDefinition processor, Path workingDirectory) {
        Path normalizedWorkingDirectory = workingDirectory.toAbsolutePath().normalize();
        String relativePomPath = "processors/" + processor.pomPath();

        Path candidate = normalizedWorkingDirectory.resolve(relativePomPath);
        if (Files.exists(candidate)) {
            return candidate;
        }

        Path current = normalizedWorkingDirectory;
        while (current != null) {
            candidate = current.resolve(relativePomPath);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }

        return normalizedWorkingDirectory.resolve(relativePomPath);
    }
}
