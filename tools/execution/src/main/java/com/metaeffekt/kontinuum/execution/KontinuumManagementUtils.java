package com.metaeffekt.kontinuum.execution;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class KontinuumManagementUtils {
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

    static Path getAbsolutePomPath(String pomPath) {
        Path repoRoot = locateRepoRoot();
        StringBuilder sb = new StringBuilder();

        if (!repoRoot.endsWith("/")) { sb.append("/"); }
        sb.append("processors");
        if (!pomPath.startsWith("/")) { sb.append("/"); }

        return new File(repoRoot + sb.toString() + pomPath).toPath();
    }

    static File getProcessorsYaml() {
        return new File(locateRepoRoot() + "/processors/processors.yaml");
    }
}
