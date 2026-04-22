package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.models.ProjectPropertiesLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KontinuumManagementUtilsTest {

    @BeforeEach
    public void setup() {
        // Load project.properties before running tests
        new ProjectPropertiesLoader();
    }

    @Test
    void testLocateRepoRoot() {
        Path repoRoot = KontinuumManagementUtils.locateRepoRoot();
        assertTrue(Files.exists(repoRoot));
    }

    @Test
    void testGetAbsolutePomPath() {
        Path repoRoot = KontinuumManagementUtils.locateRepoRoot();
        Path expectedPomPath = repoRoot.resolve("processors").resolve("advise/advise_attach-metadata.xml").normalize();

        Path absolutePomPath = KontinuumManagementUtils.getAbsolutePomPath("advise/advise_attach-metadata.xml").normalize();

        assertEquals(expectedPomPath, absolutePomPath);
    }

}
