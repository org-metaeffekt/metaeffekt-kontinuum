package com.metaeffekt.kontinuum.execution;

import com.metaeffekt.kontinuum.models.ProcessorDefinitions;
import com.metaeffekt.kontinuum.models.ProjectProperties;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProcessorScenariosTest {

    private static final String TEST_VALUE = "test-value";
    private static final String INPUT_INVENTORY = "src/test/resources/inventories/inventory.xlsx";
    private static final String REFERENCE_INVENTORY_DIR = "src/test/resources/inventories";
    private static final String SECURITY_POLICY_FILE = "src/test/resources/security-policies/security-policy.json";


    @ParameterizedTest
    @MethodSource("getProcessorExecutions")
    public void testProcessorBuildsCommand(ProcessorExecution processorExecution) {
        MavenProcessorExecutor mavenProcessorExecutor = new MavenProcessorExecutor();
        List<String> command = mavenProcessorExecutor.buildExecutionCommand(processorExecution);

        // Verify command starts with mvn
        assert command.get(0).equals("mvn") : "Command should start with mvn";

        // Verify command includes -f flag and pom location
        assert command.contains("-f") : "Command should contain -f flag";

        // Verify command includes the goal
        assert command.contains(processorExecution.goal()) : "Command should contain the goal";
    }

    static List<ProcessorExecution> getProcessorExecutions() {
        List<ProcessorExecution> executions = new ArrayList<>();

        executions.add(attachMetadata());
        executions.add(createDashboard());
        executions.add(enrichInventory());
        executions.add(enrichWithReference());
        executions.add(cyclonedxToInventory());
        executions.add(inventoryToCyclonedx());
        executions.add(inventoryToSpdx());
        executions.add(copyPomDependencies());
        executions.add(saveInspectImage());
        executions.add(downloadDataSources());
        executions.add(downloadIndex());
        executions.add(updateIndex());
        executions.add(updateIndexExternal());
        executions.add(copyResources());
        executions.add(createOverview());
        executions.add(scanDirectory());
        executions.add(aggregateAnnexFolders());
        executions.add(createAnnexArchive());
        executions.add(createDocument());
        executions.add(resolveInventory());
        executions.add(scanInventory());
        executions.add(convertAssessments());
        executions.add(copyInventories());
        executions.add(createDiff());
        executions.add(enrichAdvisors());
        executions.add(generateReportSvg());
        executions.add(mergeAdvisors());
        executions.add(mergeAssessments());
        executions.add(mergeInventories());
        executions.add(portfolioDownload());
        executions.add(portfolioDownloadJars());
        executions.add(portfolioUpload());
        executions.add(transformInventories());
        executions.add(validateReferenceInventory());

        return executions;
    }

    private static ProcessorExecution attachMetadata() {
        return createExecution("advise/advise_attach-metadata.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", "target/attach-metadata/inventory.xlsx",
                "param.metadata.asset.id", "example-asset-id"
        ));
    }

    private static ProcessorExecution createDashboard() {
        return createExecution("advise/advise_create-dashboard.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.dashboard.file", "target/create-dashboard/dashboard.html",
                "param.security.policy.file", SECURITY_POLICY_FILE,
                "param.tenant.id", "metaeffekt",
                "param.asset.id", "example-asset-id",
                "param.assessment.context", "local",
                "env.vulnerability.mirror.dir", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_URL.getPropertyKey())
        ));
    }

    private static ProcessorExecution enrichInventory() {
        return createExecution("advise/advise_enrich-inventory.xml", "process-resources", params(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", "target/enrich-inventory/inventory.xlsx",
                "output.tmp.dir", "target/enrich-inventory/tmp",
                "param.correlation.dir", TEST_VALUE,
                "param.assessment.dirs", TEST_VALUE,
                "param.context.dirs", TEST_VALUE,
                "param.security.policy.file", SECURITY_POLICY_FILE,
                "env.vulnerability.mirror.dir", System.getProperty(ProjectProperties.VULNERABILITY_MIRROR_DIR.getPropertyKey())
        ));
    }

    private static ProcessorExecution enrichWithReference() {
        return createExecution("advise/advise_enrich-with-reference.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.dir", "target/enrich-with-reference",
                "output.inventory.path", "inventory.xlsx",
                "param.reference.inventory.dir", REFERENCE_INVENTORY_DIR
        ));
    }

    private static ProcessorExecution cyclonedxToInventory() {
        return createExecution("convert/convert_cyclonedx-to-inventory.xml", "process-resources", Map.of(
                "input.bom.file", "src/test/resources/boms/keycloak-cyclonedx.json",
                "output.inventory.file", "target/cyclonedx-to-inventory/inventory.xlsx"
        ));
    }

    private static ProcessorExecution inventoryToCyclonedx() {
        return createExecution("convert/convert_inventory-to-cyclonedx.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.bom.file", "target/inventory-to-cyclonedx/inventory.xlsx",
                "param.document.name", TEST_VALUE,
                "param.document.organization", TEST_VALUE,
                "param.document.organization.url", TEST_VALUE
        ));
    }

    private static ProcessorExecution inventoryToSpdx() {
        return createExecution("convert/convert_inventory-to-spdx.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.bom.file", "target/inventory-to-spdx/inventory.xlsx",
                "param.document.name", TEST_VALUE,
                "param.document.organization", TEST_VALUE,
                "param.document.organization.url", TEST_VALUE
        ));
    }

    private static ProcessorExecution copyPomDependencies() {
        return createExecution("extract/extract_copy-pom-dependencies.xml", "process-resources", Map.of(
                "output.dependencies.dir", "target/copy-pom-dependencies",
                "param.group.id", TEST_VALUE,
                "param.artifact.id", TEST_VALUE,
                "param.version", TEST_VALUE,
                "param.exclude.transitive.enabled", "false"
        ));
    }

    private static ProcessorExecution saveInspectImage() {
        return createExecution("extract/extract_save-inspect-image.xml", "process-resources", Map.of(
                "output.dir", "target/save-inspect-image",
                "param.image.id", "nginx",
                "param.image.version", "1.29"
        ));
    }

    private static ProcessorExecution downloadDataSources() {
        return createExecution("mirror/mirror_download-data-sources.xml", "process-resources", Map.of(
                "env.mirror.dir", "target/download-data-sources",
                "env.nvd.apikey", System.getProperty(ProjectProperties.NVD_API_KEY.getPropertyKey())
        ));
    }

    private static ProcessorExecution downloadIndex() {
        return createExecution("mirror/mirror_download-index.xml", "process-resources", Map.of(
                "param.mirror.archive.url", TEST_VALUE,
                "env.vulnerability.mirror.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution updateIndex() {
        return createExecution("mirror/mirror_update-index.xml", "process-resources", Map.of(
                "env.mirror.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution updateIndexExternal() {
        return createExecution("mirror/mirror_update-index_external.xml", "process-resources", Map.of(
                "env.mirror.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution copyResources() {
        return createExecution("portfolio/portfolio_copy-resources.xml", "process-resources", Map.of(
                "input.inventories.dir", TEST_VALUE,
                "input.dashboards.dir", TEST_VALUE,
                "input.reports.dir", TEST_VALUE,
                "input.advisor.inventories.dir", TEST_VALUE,
                "output.resources.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution createOverview() {
        return createExecution("portfolio/portfolio_create-overview.xml", "process-resources", Map.of(
                "input.inventory.dir", TEST_VALUE,
                "input.inventory.path", TEST_VALUE,
                "input.advisor.inventories.dir", TEST_VALUE,
                "input.dashboards.dir", TEST_VALUE,
                "input.reports.dir", TEST_VALUE,
                "output.overview.file", TEST_VALUE,
                "param.security.policy.file", SECURITY_POLICY_FILE
        ));
    }

    private static ProcessorExecution scanDirectory() {
        return createExecution("prepare/prepare_scan-directory.xml", "process-resources", Map.of(
                "input.extract.dir", TEST_VALUE,
                "output.scan.dir", "target/scan-directory/scan",
                "output.inventory.file", "target/scan-directory/inventory.xlsx",
                "param.reference.inventory.dir", REFERENCE_INVENTORY_DIR
        ));
    }

    private static ProcessorExecution aggregateAnnexFolders() {
        return createExecution("report/report_aggregate-annex-folders.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "param.reference.inventory.dir", REFERENCE_INVENTORY_DIR
        ));
    }

    private static ProcessorExecution createAnnexArchive() {
        return createExecution("report/report_create-annex-archive.xml", "process-resources", Map.of(
                "input.document.pdf.file", TEST_VALUE,
                "output.annex.archive.file", TEST_VALUE
        ));
    }

    private static ProcessorExecution createDocument() {
        return createExecution("report/report_create-document.xml", "process-resources", params(
                "input.inventory.dir", TEST_VALUE,
                "output.document.file", TEST_VALUE,
                "param.document.type", TEST_VALUE,
                "param.asset.id", TEST_VALUE,
                "param.asset.name", TEST_VALUE,
                "param.asset.version", TEST_VALUE,
                "param.product.name", TEST_VALUE,
                "param.product.version", TEST_VALUE,
                "param.product.watermark", TEST_VALUE,
                "param.property.selector.organization", TEST_VALUE,
                "param.asset.descriptor.file", TEST_VALUE,
                "env.kontinuum.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution resolveInventory() {
        return createExecution("resolve/resolve_resolve-inventory.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", "target/resolve-inventory/inventory.xlsx",
                "param.artifact.resolver.config.file", TEST_VALUE,
                "param.artifact.resolver.proxy.file", TEST_VALUE,
                "env.maven.index.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution scanInventory() {
        return createExecution("scan/scan_scan-inventory.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", "target/scan-inventory/inventory.xlsx",
                "input.output.analysis.base.dir", TEST_VALUE,
                "param.properties.file", TEST_VALUE
        ));
    }

    private static ProcessorExecution convertAssessments() {
        return createExecution("util/util_convert-assessments.xml", "process-resources", Map.of(
                "input.assessment.dir", TEST_VALUE,
                "output.assessment.dir", TEST_VALUE,
                "param.output.mode", TEST_VALUE,
                "param.output.format", TEST_VALUE
        ));
    }

    private static ProcessorExecution copyInventories() {
        return createExecution("util/util_copy-inventories.xml", "process-resources", Map.of(
                "output.inventories.dir", "target/copy-inventories",
                "param.inventories.list", TEST_VALUE
        ));
    }

    private static ProcessorExecution createDiff() {
        return createExecution("util/util_create-diff.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "input.inventory.compare.file", TEST_VALUE,
                "output.inventory.dir", "target/create-diff",
                "param.inventory.version", TEST_VALUE,
                "param.inventory.compare.version", TEST_VALUE,
                "param.security.policy.file", SECURITY_POLICY_FILE
        ));
    }

    private static ProcessorExecution enrichAdvisors() {
        return createExecution("util/util_enrich-advisors.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.inventory.file", "target/enrich-advisors/inventory.xlsx",
                "param.security.policy.file", SECURITY_POLICY_FILE,
                "env.vulnerability.mirror.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution generateReportSvg() {
        return createExecution("util/util_generate-report-svg.xml", "process-resources", Map.of(
                "input.inventory.file", INPUT_INVENTORY,
                "output.svg.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution mergeAdvisors() {
        return createExecution("util/util_merge-advisors.xml", "process-resources", Map.of(
                "input.inventory.dir", TEST_VALUE,
                "output.inventory.file", "target/merge-advisors/inventory.xlsx",
                "param.security.policy.file", SECURITY_POLICY_FILE
        ));
    }

    private static ProcessorExecution mergeAssessments() {
        return createExecution("util/util_merge-assessments.xml", "process-resources", Map.of(
                "input.inventory.dir", TEST_VALUE,
                "output.inventory.file", "target/merge-assessments/inventory.xlsx"
        ));
    }

    private static ProcessorExecution mergeInventories() {
        return createExecution("util/util_merge-inventories.xml", "process-resources", Map.of(
                "input.inventory.dir", TEST_VALUE,
                "output.inventory.file", "target/merge-inventories/inventory.xlsx"
        ));
    }

    private static ProcessorExecution portfolioDownload() {
        return createExecution("util/util_portfolio-download.xml", "process-resources", params(
                "output.inventory.dir", "target/portfolio-download",
                "param.portfolio.manager.url", TEST_VALUE,
                "param.portfolio.manager.token", TEST_VALUE,
                "param.product.name", TEST_VALUE,
                "param.product.version", TEST_VALUE,
                "param.product.artifact.id", TEST_VALUE,
                "param.keystore.password", TEST_VALUE,
                "param.truststore.password", TEST_VALUE,
                "param.keystore.config.file", TEST_VALUE,
                "param.truststore.config.file", TEST_VALUE
        ));
    }

    private static ProcessorExecution portfolioDownloadJars() {
        return createExecution("util/util_portfolio-download-jars.xml", "process-resources", Map.of(
                "input.cli.dir", TEST_VALUE
        ));
    }

    private static ProcessorExecution portfolioUpload() {
        return createExecution("util/util_portfolio-upload.xml", "process-resources", params(
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
        ));
    }

    private static ProcessorExecution transformInventories() {
        return createExecution("util/util_transform-inventories.xml", "process-resources", Map.of(
                "input.inventory.dir", TEST_VALUE,
                "output.inventory.dir", "target/transform-inventories",
                "param.kotlin.script.file", TEST_VALUE
        ));
    }

    private static ProcessorExecution validateReferenceInventory() {
        return createExecution("util/util_validate-reference-inventory.xml", "process-resources", Map.of(
                "input.inventory.dir", TEST_VALUE
        ));
    }

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
