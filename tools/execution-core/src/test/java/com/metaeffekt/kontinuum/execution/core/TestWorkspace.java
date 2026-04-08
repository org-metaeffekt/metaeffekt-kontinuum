package com.metaeffekt.kontinuum.execution.core;

import java.nio.file.Files;
import java.nio.file.Path;

final class TestWorkspace {

    private TestWorkspace() {
    }

    static Path locateRepoRoot() {
        Path current = Path.of("").toAbsolutePath().normalize();
        while (current != null) {
            if (Files.exists(current.resolve("processors/processors.yaml"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Unable to locate repository root containing processors/processors.yaml");
    }
}
