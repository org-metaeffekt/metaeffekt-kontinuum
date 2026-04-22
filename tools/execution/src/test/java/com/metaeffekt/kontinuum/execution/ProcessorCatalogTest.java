package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.models.ProcessorDefinitions;
import com.metaeffekt.kontinuum.models.ProjectPropertiesLoader;
import com.metaeffekt.kontinuum.models.YamlProcessorCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProcessorCatalogTest {

    private YamlProcessorCatalog yamlProcessorCatalog;
    private ProjectPropertiesLoader propertiesLoader;

    @BeforeEach
    public void setup() {
        // Load project.properties before running tests
        propertiesLoader = new ProjectPropertiesLoader();
        yamlProcessorCatalog = new YamlProcessorCatalog();
    }

    /**
     * Tests whether all processors listed in the processors.yaml exist as actual maven processors.
     */
    @Test
    public void testAllProcessorsInYamlExist() {
        Path repoRoot = KontinuumManagementUtils.locateRepoRoot();
        Path processorsDir = repoRoot.resolve("processors");

        List<String> yamlProcessorPomLocations = yamlProcessorCatalog.getProcessors()
                .stream()
                .map(ProcessorDefinitions.Processor::getPomLocation)
                .toList();

        for (String pomLocation : yamlProcessorPomLocations) {
            Path fullPomPath = processorsDir.resolve(pomLocation);
            assertTrue(Files.exists(fullPomPath),
                "Processor POM not found: " + fullPomPath);
        }
    }

    /**
     * Tests whether all maven processors in the kontinuum repository are listed in the processors.yaml.
     */
    @Test
    public void testAllProcessorsAreContainedInYaml() throws IOException {
        Path repoRoot = KontinuumManagementUtils.locateRepoRoot();
        Path processorsDir = repoRoot.resolve("processors");

        // Collect all POM locations from the YAML
        Set<String> yamlPomLocations = new HashSet<>();
        for (ProcessorDefinitions.Processor processor : yamlProcessorCatalog.getProcessors()) {
            yamlPomLocations.add(processor.getPomLocation());
        }

        // Find all POM files in the processors directory (excluding _common_, configurations, and target)
        Set<String> fileSystemPomLocations = new HashSet<>();
        try (Stream<Path> paths = Files.walk(processorsDir)) {
            paths.filter(p -> p.toString().endsWith(".xml"))
                 .filter(p -> !p.toString().contains("_common_"))
                 .filter(p -> !p.toString().contains("configurations"))
                 .filter(p -> !p.toString().contains("/target/"))
                 .forEach(p -> {
                     // Get relative path from processors directory
                     String relativePath = processorsDir.relativize(p).toString();
                     fileSystemPomLocations.add(relativePath);
                 });
        }

        // Find POMs that exist on file system but not in YAML
        Set<String> missingFromYaml = new HashSet<>(fileSystemPomLocations);
        missingFromYaml.removeAll(yamlPomLocations);

        if (!missingFromYaml.isEmpty()) {
            fail("The following processor POMs are not listed in processors.yaml:\n" +
                String.join("\n", missingFromYaml));
        }
    }
}
