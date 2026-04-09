package com.metaeffekt.kontinuum.execution.environment;

import com.metaeffekt.kontinuum.execution.contract.ProcessorDefinition;
import com.metaeffekt.kontinuum.execution.contract.ProcessorParameterDefinition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class ProcessorExecutionScenarioFactory {

    private static final String WORKSPACE_TOKEN = "${workspace}";

    private ProcessorExecutionScenarioFactory() {
    }

    static ProcessorExecutionScenario forProcessor(ProcessorDefinition processor) {
        if ("portfolio_copy-resources".equals(processor.id())) {
            return portfolioCopyResourcesScenario(processor);
        }
        return new ProcessorExecutionScenario(
            processor,
            ProcessorExecutionMode.PLAN_ONLY,
            defaultTemplates(processor.parameters()),
            List.of(),
            workspaceDir -> {
            }
        );
    }

    static String workspaceToken() {
        return WORKSPACE_TOKEN;
    }

    private static ProcessorExecutionScenario portfolioCopyResourcesScenario(ProcessorDefinition processor) {
        Map<String, String> properties = new LinkedHashMap<>();
        properties.put("input.inventories.dir", WORKSPACE_TOKEN + "/input/inventories");
        properties.put("input.dashboards.dir", WORKSPACE_TOKEN + "/input/dashboards");
        properties.put("input.reports.dir", WORKSPACE_TOKEN + "/input/reports");
        properties.put("input.advisor.inventories.dir", WORKSPACE_TOKEN + "/input/advisor-inventories");
        properties.put("output.resources.dir", WORKSPACE_TOKEN + "/output/resources");

        List<String> expectedOutputs = List.of(
            WORKSPACE_TOKEN + "/output/resources/source-inventories/source.xlsx",
            WORKSPACE_TOKEN + "/output/resources/assessment-dashboards/dashboard.html",
            WORKSPACE_TOKEN + "/output/resources/vulnerability-reports/report.pdf",
            WORKSPACE_TOKEN + "/output/resources/advisor-inventories/advisor.xlsx"
        );

        return new ProcessorExecutionScenario(
            processor,
            ProcessorExecutionMode.EXECUTE,
            properties,
            expectedOutputs,
            ProcessorExecutionScenarioFactory::preparePortfolioCopyResourcesFixture
        );
    }

    private static Map<String, String> defaultTemplates(List<ProcessorParameterDefinition> parameters) {
        Map<String, String> values = new LinkedHashMap<>();
        for (ProcessorParameterDefinition parameter : parameters) {
            if (!parameter.required()) {
                continue;
            }
            values.put(parameter.key(), defaultTemplate(parameter.key()));
        }
        return values;
    }

    private static String defaultTemplate(String propertyKey) {
        if (propertyKey.endsWith(".dir")) {
            return WORKSPACE_TOKEN + "/input/default-dir";
        }
        if (propertyKey.endsWith(".file") || propertyKey.endsWith(".yaml") || propertyKey.endsWith(".xml")) {
            return WORKSPACE_TOKEN + "/input/default-file.txt";
        }
        if (propertyKey.startsWith("env.")) {
            return "TEST_ENV_VALUE";
        }
        return "test-value";
    }

    private static void preparePortfolioCopyResourcesFixture(Path workspaceDir) throws IOException {
        Path inventoriesDir = workspaceDir.resolve("input/inventories");
        Path dashboardsDir = workspaceDir.resolve("input/dashboards");
        Path reportsDir = workspaceDir.resolve("input/reports");
        Path advisorInventoriesDir = workspaceDir.resolve("input/advisor-inventories");

        Files.createDirectories(inventoriesDir);
        Files.createDirectories(dashboardsDir);
        Files.createDirectories(reportsDir);
        Files.createDirectories(advisorInventoriesDir);

        Files.writeString(inventoriesDir.resolve("source.xlsx"), "xlsx-placeholder");
        Files.writeString(dashboardsDir.resolve("dashboard.html"), "<html><body>dashboard</body></html>");
        Files.writeString(reportsDir.resolve("report.pdf"), "%PDF-1.0 placeholder");
        Files.writeString(advisorInventoriesDir.resolve("advisor.xlsx"), "xlsx-placeholder");
    }
}
