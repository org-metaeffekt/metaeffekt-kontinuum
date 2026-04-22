package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.models.ProjectProperties;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class KontinuumManagementUtils {
    public static Path locateRepoRoot() {
        String kontinuumDir = System.getProperty(ProjectProperties.KONTINUUM_DIR.getPropertyKey());

        Path kontinuumPath = Path.of(kontinuumDir);
        if (Files.exists(kontinuumPath)) {
            return kontinuumPath;
        } else {
            throw new IllegalStateException("The path to the metaeffekt-kontinuum specified in the properties file does not exist.");
        }
    }

    public static Path getAbsolutePomPath(String pomPath) {
        Path repoRoot = locateRepoRoot();
        StringBuilder sb = new StringBuilder();

        if (!repoRoot.endsWith("/")) { sb.append("/"); }
        sb.append("processors");
        if (!pomPath.startsWith("/")) { sb.append("/"); }

        return new File(repoRoot + sb.toString() + pomPath).toPath();
    }
}
