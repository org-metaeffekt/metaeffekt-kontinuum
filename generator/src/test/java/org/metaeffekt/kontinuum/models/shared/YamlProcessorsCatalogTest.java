package org.metaeffekt.kontinuum.models.shared;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class YamlProcessorsCatalogTest {

    /**
     * If this test method fails, this means there were changes in the processors.yaml file parsed by
     * {@link YamlProcessorCatalog}. Ensure that changed processor ids are reflected in the rest of the code-base
     *  as they are usually hard coded as Strings.
     */
    @Test
    public void testExpectedProcessorsExistInYaml() {
        YamlProcessorCatalog processorCatalog = new YamlProcessorCatalog();
        List<String> expectedProcessorIds = new ArrayList<String>();

        expectedProcessorIds.add("attach-metadata");
        expectedProcessorIds.add("create-dashboard");
        expectedProcessorIds.add("enrich-inventory");
        expectedProcessorIds.add("enrich-with-reference");
        expectedProcessorIds.add("cyclonedx-to-inventory");
        expectedProcessorIds.add("inventory-to-cyclonedx");
        expectedProcessorIds.add("inventory-to-spdx");
        expectedProcessorIds.add("copy-pom-dependencies");
        expectedProcessorIds.add("save-inspect-image");
        expectedProcessorIds.add("download-data-sources");
        expectedProcessorIds.add("download-index");
        expectedProcessorIds.add("download-asset");
        expectedProcessorIds.add("update-index");
        expectedProcessorIds.add("update-index_external");
        expectedProcessorIds.add("copy-resources");
        expectedProcessorIds.add("create-overview");
        expectedProcessorIds.add("scan-directory");
        expectedProcessorIds.add("aggregate-annex-folders");
        expectedProcessorIds.add("create-annex-archive");
        expectedProcessorIds.add("create-document");
        expectedProcessorIds.add("resolve-inventory");
        expectedProcessorIds.add("scan-inventory");
        expectedProcessorIds.add("aggregate-sources");
        expectedProcessorIds.add("convert-assessments");
        expectedProcessorIds.add("copy-inventories");
        expectedProcessorIds.add("create-diff");
        expectedProcessorIds.add("enrich-advisors");
        expectedProcessorIds.add("generate-report-svg");
        expectedProcessorIds.add("merge-advisors");
        expectedProcessorIds.add("merge-assessments");
        expectedProcessorIds.add("merge-inventories");
        expectedProcessorIds.add("portfolio-download");
        expectedProcessorIds.add("portfolio-download-jars");
        expectedProcessorIds.add("portfolio-upload");
        expectedProcessorIds.add("transform-inventories");
        expectedProcessorIds.add("validate-reference-inventory");

        List<String> actualProcessorIds = processorCatalog.getProcessors().stream()
                .map(ProcessorDefinitions.Processor::getId)
                .toList();

        for (String expectedId : expectedProcessorIds) {
            assertTrue(actualProcessorIds.contains(expectedId),
                    "Expected processor id [" + expectedId + "] is not present in processorCatalog.getProcessors()");
        }
    }

}
