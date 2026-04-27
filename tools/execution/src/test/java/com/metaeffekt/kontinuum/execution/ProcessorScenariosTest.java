package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.models.ProcessorDefinitions;
import com.metaeffekt.kontinuum.models.ProjectProperties;
import com.metaeffekt.kontinuum.models.ProjectPropertiesLoader;
import org.junit.jupiter.api.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class ProcessorScenariosTest {

    private static final String TEST_VALUE = "test-value";
    private static String BASE_DIR;
    private static String TEST_RESOURCES_DIR;
    private static String INPUT_INVENTORY;
    private static String INVENTORY_DIR;
    private static String SECURITY_POLICY_FILE;

    private final MavenProcessorExecutor mavenProcessorExecutor = new MavenProcessorExecutor();

    @BeforeAll
    public static void setup() {
        new ProjectPropertiesLoader();
        BASE_DIR = KontinuumManagementUtils.locateRepoRoot().toString();
        TEST_RESOURCES_DIR = BASE_DIR + "/tools/execution/src/test/resources";
        INPUT_INVENTORY = TEST_RESOURCES_DIR + "/inventories/inventory.xlsx";
        INVENTORY_DIR = TEST_RESOURCES_DIR + "/inventories";
        SECURITY_POLICY_FILE = TEST_RESOURCES_DIR + "/security-policies/security-policy.json";
    }

    private void executeProcessor(ProcessorExecution execution) {
        List<String> command = mavenProcessorExecutor.buildExecutionCommand(execution);

        Assertions.assertEquals("mvn", command.get(0));
        Assertions.assertTrue(command.contains("-f"));
        Assertions.assertTrue(command.contains(execution.goal()));
        Assertions.assertTrue(mavenProcessorExecutor.execute(command));
    }

    // ADVISE PROCESSORS

    @Test
    void testAttachMetadata() {
        executeProcessor(createExecution("advise/advise_attach-metadata.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", BASE_DIR + "/target/attach-metadata/inventory.xlsx",
                "param.metadata.asset.id", "example-asset-id"
        )));
    }

    @Test
    void testCreateDashboard() {
        executeProcessor(createExecution("advise/advise_create-dashboard.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.dashboard.file", BASE_DIR + "/target/create-dashboard/dashboard.html",
                "param.security.policy.file", SECURITY_POLICY_FILE,
                "param.tenant.id", "metaeffekt",
                "param.asset.id", "example-asset-id",
                "param.assessment.context", "local",
                "env.vulnerability.mirror.dir", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_DIR.getPropertyKey(), TEST_VALUE) + "/.database"
        )));
    }

    @Test
    void testEnrichInventory() {
        executeProcessor(createExecution("advise/advise_enrich-inventory.xml", "process-resources", params(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", BASE_DIR + "/target/enrich-inventory/inventory.xlsx",
                "output.tmp.dir", BASE_DIR + "/target/enrich-inventory/tmp",
                "param.correlation.dir", TEST_VALUE,
                "param.assessment.dirs", TEST_VALUE,
                "param.context.dirs", TEST_VALUE,
                "param.security.policy.file", SECURITY_POLICY_FILE,
                "env.vulnerability.mirror.dir", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_DIR.getPropertyKey(), TEST_VALUE) + "/.database"
        )));
    }

    @Test
    void testEnrichWithReference() {
        executeProcessor(createExecution("advise/advise_enrich-with-reference.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.dir", BASE_DIR + "/target/enrich-with-reference",
                "output.inventory.path", "inventory.xlsx",
                "param.reference.inventory.dir", INVENTORY_DIR
        )));
    }

    // CONVERT PROCESSORS

    @Test
    void testCyclonedxToInventory() {
        executeProcessor(createExecution("convert/convert_cyclonedx-to-inventory.xml", "process-resources", Map.of(
                "input.bom.file", TEST_RESOURCES_DIR + "/boms/keycloak-cyclonedx.json",
                "output.inventory.file", BASE_DIR + "/target/cyclonedx-to-inventory/inventory.xlsx"
        )));
    }

    @Test
    void testInventoryToCyclonedx() {
        executeProcessor(createExecution("convert/convert_inventory-to-cyclonedx.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.bom.file", BASE_DIR + "/target/inventory-to-cyclonedx/bom.json",
                "param.document.name", TEST_VALUE,
                "param.document.organization", TEST_VALUE,
                "param.document.organization.url", TEST_VALUE
        )));
    }

    @Test
    void testInventoryToSpdx() {
        executeProcessor(createExecution("convert/convert_inventory-to-spdx.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.bom.file", BASE_DIR + "/target/inventory-to-spdx/bom.spdx",
                "param.document.name", TEST_VALUE,
                "param.document.organization", TEST_VALUE,
                "param.document.organization.url", TEST_VALUE
        )));
    }

    // EXTRACT PROCESSORS

    @Test
    void testCopyPomDependencies() {
        executeProcessor(createExecution("extract/extract_copy-pom-dependencies.xml", "process-resources", Map.of(
                "output.dependencies.dir", BASE_DIR + "/target/copy-pom-dependencies",
                "param.group.id", "org.metaeffekt.core",
                "param.artifact.id", "ae-core-inventory",
                "param.version", "HEAD-SNAPSHOT",
                "param.exclude.transitive.enabled", "false"
        )));
    }

    @Test
    @Tag("ExternalDependencies")
    void testSaveInspectImage() {
        executeProcessor(createExecution("extract/extract_save-inspect-image.xml", "process-resources", Map.of(
                "output.dir", BASE_DIR + "/target/save-inspect-image",
                "param.image.id", "nginx",
                "param.image.version", "1.29"
        )));
    }

    // MIRROR PROCESSORS

    @Test
    @Tag("Slow")
    @Disabled
    void testDownloadDataSources() {
        executeProcessor(createExecution("mirror/mirror_download-data-sources.xml", "process-resources", Map.of(
                "env.mirror.dir", BASE_DIR + "/target/download-data-sources",
                "env.nvd.apikey", TEST_VALUE
        )));
    }

    @Test
    void testDownloadIndex() {
        executeProcessor(createExecution("mirror/mirror_download-index.xml", "process-resources", Map.of(
                "param.mirror.archive.url", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_URL.getPropertyKey()),
                "env.vulnerability.mirror.dir", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_DIR.getPropertyKey(), BASE_DIR + "/target/download-index")
        )));
    }

    @Test
    void testUpdateIndex() {
        executeProcessor(createExecution("mirror/mirror_update-index.xml", "process-resources", Map.of(
                "env.mirror.dir", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_DIR.getPropertyKey(), BASE_DIR + "/target/update-index") + "/.database"
        )));
    }

    @Test
    void testUpdateIndexExternal() {
        executeProcessor(createExecution("mirror/mirror_update-index_external.xml", "process-resources", Map.of(
                "env.mirror.dir", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_DIR.getPropertyKey(), BASE_DIR + "/target/update-index-external") + "/.database"
        )));
    }

    // PORTFOLIO PROCESSORS

    @Test
    void testCopyResources() {
        executeProcessor(createExecution("portfolio/portfolio_copy-resources.xml", "process-resources", Map.of(
                "input.inventories.dir", INVENTORY_DIR,
                "input.dashboards.dir", TEST_RESOURCES_DIR + "/dashboards",
                "input.reports.dir", TEST_RESOURCES_DIR + "/reports",
                "input.advisor.inventories.dir", INVENTORY_DIR,
                "output.resources.dir", BASE_DIR + "/target/copy-resources"
        )));
    }

    @Test
    void testCreateOverview() {
        executeProcessor(createExecution("portfolio/portfolio_create-overview.xml", "process-resources", Map.of(
                "input.inventory.dir", BASE_DIR + "/target/copy-resources",
                "input.inventory.path", "source-inventories",
                "input.advisor.inventories.dir", "advisor-inventories",
                "input.dashboards.dir", "assessment-dashboards",
                "input.reports.dir", "vulnerability-reports",
                "output.overview.file", BASE_DIR + "/target/create-overview/overview.html",
                "param.security.policy.file", SECURITY_POLICY_FILE
        )));
    }

    // PREPARE PROCESSORS

    @Test
    void testScanDirectory() {
        executeProcessor(createExecution("prepare/prepare_scan-directory.xml", "process-resources", Map.of(
                "input.extract.dir", INVENTORY_DIR,
                "output.scan.dir", BASE_DIR + "/target/scan-directory/scan",
                "output.inventory.file", BASE_DIR + "/target/scan-directory/inventory.xlsx",
                "param.reference.inventory.dir", INVENTORY_DIR
        )));
    }

    // REPORT PROCESSORS

    @Test
    void testAggregateAnnexFolders() {
        executeProcessor(createExecution("report/report_aggregate-annex-folders.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "param.reference.inventory.dir", INVENTORY_DIR
        )));
    }

    @Test
    void testCreateAnnexArchive() {
        executeProcessor(createExecution("report/report_create-annex-archive.xml", "process-resources", Map.of(
                "input.document.pdf.file", TEST_RESOURCES_DIR + "/reports/example-report.pdf",
                "output.annex.archive.file", BASE_DIR + "/target/create-annex-archive/annex-archive.zip"
        )));
    }

    @Test
    void testCreateDocument() {
        executeProcessor(createExecution("report/report_create-document.xml", "process-resources", params(
                "input.inventory.dir", INVENTORY_DIR,
                "output.document.file", BASE_DIR + "/target/create-document/report.pdf",
                "param.document.type", "VR",
                "param.asset.id", TEST_VALUE,
                "param.asset.name", TEST_VALUE,
                "param.asset.version", TEST_VALUE,
                "param.product.name", TEST_VALUE,
                "param.product.version", TEST_VALUE,
                "param.product.watermark", TEST_VALUE,
                "param.property.selector.organization", "metaeffekt",
                "param.asset.descriptor.file", TEST_RESOURCES_DIR + "/descriptors/asset-descriptor_GENERIC-vulnerability-report.yaml",
                "env.kontinuum.dir", System.getProperty(ProjectProperties.KONTINUUM_DIR.getPropertyKey(), BASE_DIR)
        )));
    }

    // RESOLVE PROCESSORS

    @Test
    void testResolveInventory() {
        executeProcessor(createExecution("resolve/resolve_resolve-inventory.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", BASE_DIR + "/target/resolve-inventory/inventory.xlsx",
                "param.artifact.resolver.config.file", TEST_RESOURCES_DIR + "/resolver/artifact-resolver-config.yaml",
                "param.artifact.resolver.proxy.file", TEST_RESOURCES_DIR + "/resolver/artifact-resolver-proxy.yaml",
                "env.maven.index.dir", BASE_DIR + "/target/maven-index"
        )));
    }

    // SCAN PROCESSORS

    @Test
    void testScanInventory() {
        executeProcessor(createExecution("scan/scan_scan-inventory.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", BASE_DIR + "/target/scan-inventory/inventory.xlsx",
                "input.output.analysis.base.dir", BASE_DIR + "/target/scan-inventory/analysis",
                "param.properties.file", TEST_RESOURCES_DIR + "/scanner/scan-control.properties",
                "env.kosmos.password", "EuBsVvcjIElWdXVVtHmPJdsE",
                "env.kosmos.userkeys.file", TEST_RESOURCES_DIR + "/kosmos/kosmos.consumer.keys"
        )));
    }

    // UTIL PROCESSORS

    @Test
    void testConvertAssessments() {
        executeProcessor(createExecution("util/util_convert-assessments.xml", "process-resources", Map.of(
                "input.assessment.dir", TEST_RESOURCES_DIR + "/assessments",
                "output.assessment.dir", BASE_DIR + "/target/convert-assessments",
                "param.output.mode", "IN_PLACE",
                "param.output.format", "PRESERVE"
        )));
    }

    @Test
    void testCopyInventories() {
        executeProcessor(createExecution("util/util_copy-inventories.xml", "process-resources", Map.of(
                "output.inventories.dir", BASE_DIR + "/target/copy-inventories",
                "param.inventories.list", INPUT_INVENTORY
        )));
    }

    @Test
    void testCreateDiff() {
        executeProcessor(createExecution("util/util_create-diff.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "input.inventory.compare.file", INPUT_INVENTORY,
                "output.inventory.dir", BASE_DIR + "/target/create-diff",
                "param.inventory.version", TEST_VALUE,
                "param.inventory.compare.version", TEST_VALUE,
                "param.security.policy.file", SECURITY_POLICY_FILE
        )));
    }

    @Test
    void testEnrichAdvisors() {
        executeProcessor(createExecution("util/util_enrich-advisors.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", BASE_DIR + "/target/enrich-advisors/inventory.xlsx",
                "param.security.policy.file", SECURITY_POLICY_FILE,
                "env.vulnerability.mirror.dir", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_DIR.getPropertyKey()) + "/.database"
        )));
    }

    @Test
    void testGenerateReportSvg() {
        executeProcessor(createExecution("util/util_generate-report-svg.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.svg.dir", BASE_DIR + "/target/generate-report-svg",
                "param.security.policy.file", SECURITY_POLICY_FILE
        )));
    }

    @Test
    void testMergeAdvisors() {
        executeProcessor(createExecution("util/util_merge-advisors.xml", "process-resources", Map.of(
                "input.inventory.dir", INVENTORY_DIR,
                "output.inventory.file", BASE_DIR + "/target/merge-advisors/inventory.xlsx",
                "param.security.policy.file", SECURITY_POLICY_FILE
        )));
    }

    @Test
    void testMergeAssessments() {
        executeProcessor(createExecution("util/util_merge-assessments.xml", "process-resources", Map.of(
                "input.inventory.dir", INVENTORY_DIR,
                "output.inventory.file", BASE_DIR + "/target/merge-assessments/inventory.xlsx"
        )));
    }

    @Test
    void testMergeInventories() {
        executeProcessor(createExecution("util/util_merge-inventories.xml", "process-resources", Map.of(
                "input.inventory.dir", INVENTORY_DIR,
                "output.inventory.file", BASE_DIR + "/target/merge-inventories/inventory.xlsx"
        )));
    }

    @Test
    @Tag("ExternalDependencies")
    void testPortfolioDownload() {
        executeProcessor(createExecution("util/util_portfolio-download.xml", "process-resources", params(
                "output.inventory.dir", BASE_DIR + "/target/portfolio-download",
                "param.portfolio.manager.url", TEST_VALUE,
                "param.portfolio.manager.token", TEST_VALUE,
                "param.product.name", TEST_VALUE,
                "param.product.version", TEST_VALUE,
                "param.product.artifact.id", TEST_VALUE,
                "param.keystore.password", TEST_VALUE,
                "param.truststore.password", TEST_VALUE,
                "param.keystore.config.file", TEST_VALUE,
                "param.truststore.config.file", TEST_VALUE
        )));
    }

    @Test
    void testPortfolioDownloadJars() {
        executeProcessor(createExecution("util/util_portfolio-download-jars.xml", "process-resources", Map.of(
                "input.cli.dir", BASE_DIR + "/target/portfolio-download-jars"
        )));
    }

    @Test
    @Tag("ExternalDependencies")
    void testPortfolioUpload() {
        executeProcessor(createExecution("util/util_portfolio-upload.xml", "process-resources", params(
                "input.file", TEST_VALUE,
                "param.portfolio.manager.url", TEST_VALUE,
                "param.portfolio.manager.token", TEST_VALUE,
                "param.product.name", TEST_VALUE,
                "param.product.version", TEST_VALUE,
                "param.product.artifact.id", TEST_VALUE,
                "param.keystore.config.file", TEST_VALUE,
                "param.truststore.config.file", TEST_VALUE,
                "param.keystore.password", TEST_VALUE,
                "param.truststore.password", TEST_VALUE
        )));
    }

    @Test
    void testTransformInventories() {
        executeProcessor(createExecution("util/util_transform-inventories.xml", "process-resources", Map.of(
                "input.inventory.dir", INVENTORY_DIR,
                "output.inventory.dir", BASE_DIR + "/target/transform-inventories",
                "param.kotlin.script.file", TEST_RESOURCES_DIR + "/kotlin-scripts/aggregate.kts"
        )));
    }

    @Test
    void testValidateReferenceInventory() {
        executeProcessor(createExecution("util/util_validate-reference-inventory.xml", "process-resources", Map.of(
                "input.inventory.dir", INVENTORY_DIR
        )));
    }

    // HELPER METHODS

    private static ProcessorExecution createExecution(String pomLocation, String goal, Map<String, String> parameters) {
        Map<ProcessorDefinitions.ProcessorParameter, String> paramMap = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            ProcessorDefinitions.ProcessorParameter param = new ProcessorDefinitions.ProcessorParameter();
            param.setKey(entry.getKey());
            paramMap.put(param, entry.getValue());
        }

        return new ProcessorExecution(pomLocation, goal, paramMap, false, false);
    }

    private static Map<String, String> params(String... keyValuePairs) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            map.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }
        return map;
    }
}
