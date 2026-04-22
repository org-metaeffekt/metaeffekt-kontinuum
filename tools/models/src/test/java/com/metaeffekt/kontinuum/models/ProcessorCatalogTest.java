package com.metaeffekt.kontinuum.models;

import com.metaeffekt.kontinuum.execution.KontinuumManagementUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertThat;

public class ProcessorCatalogTest {

    YamlProcessorCatalog yamlProcessorCatalog;

    @Before
    public void setup() {
        yamlProcessorCatalog = new YamlProcessorCatalog();
    }

    /**
     * Tests whether all processors listed in the processors.yaml exist as actual maven processors.
     */
    @Test
    public void testAllProcessorsInYamlExist() {
        List<String> yamlProcessorPomLocations =  yamlProcessorCatalog.getProcessors()
                .stream()
                .map(p -> p.pomLocation)
                .toList();

        for (String pomLocation : yamlProcessorPomLocations) {
            Path fullPomPath = Path.of(KontinuumManagementUtils.locateRepoRoot().toString(), pomLocation);
            assertThat(new File(fullPomPath)).exists();
        }
    }

    /**
     * Tests whether all maven processors in the kontinuum repository are listed in the procesors.yaml
     */
    @Test
    public void testAllProcessorsAreContainedInYaml() {
        // IMPLEMENT THIS
    }
}
