package org.metaeffekt.kontinuum.models.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

@Slf4j
public class PipelineConfigurationLoader {

    private static boolean isValid = true;

    public static PipelineConfiguration readConfig(File pipelineConfigFile) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            PipelineConfiguration pipelineConfiguration = objectMapper.readValue(pipelineConfigFile, PipelineConfiguration.class);
            validatePipelineConfigFile(pipelineConfiguration);
            if (!isValid) {
                throw new IllegalStateException("Pipeline configuration file: " + pipelineConfigFile.getAbsolutePath() +
                        " contains errors.");
            }
            return pipelineConfiguration;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read pipeline configuration file.", e);
        }
    }

    private static void validatePipelineConfigFile(PipelineConfiguration pipelineConfiguration) {
        validateProjectProperties(pipelineConfiguration.projectProperties);

        List<String> assetIds = pipelineConfiguration.projectProperties.assets
                .stream()
                .map(PipelineConfiguration.ProjectProperties.Asset::getId)
                .toList();

        validateReports(pipelineConfiguration.reports, assetIds);
        validateDashboards(pipelineConfiguration.dashboards, assetIds);
    }

    private static void validateProjectProperties(PipelineConfiguration.ProjectProperties projectProperties) {
        validateProject(projectProperties.project);
        validateAssets(projectProperties.assets);
    }

    private static void validateProject(PipelineConfiguration.ProjectProperties.Project project) {
        if (StringUtils.isBlank(project.name)) {
            log.error("Project name is empty.");
            isValid = false;
        }

        if (StringUtils.isBlank(project.version)) {
            log.error("Project version is empty.");
            isValid = false;
        }
    }

    private static void validateAssets(List<PipelineConfiguration.ProjectProperties.Asset> assets) {
        for (PipelineConfiguration.ProjectProperties.Asset asset : assets) {
            if (StringUtils.isBlank(asset.id)) {
                log.error("Asset {} requires an id to be set.", asset);
            }

            if (asset.mavenResolver != null) {
                log.warn("The maven resolver is not yet implemented and can be removed for asset {}", asset);
            }

            validateUrlResolver(asset);
        }
    }

    private static void validateUrlResolver(PipelineConfiguration.ProjectProperties.Asset asset) {
        if (asset.urlResolver == null) {
            isValid = false;
            log.error("The URL resolver for asset {} is not defined.", asset);
        }

        if (StringUtils.isNotBlank(asset.urlResolver.url)) {
            try {
                new URL(asset.urlResolver.url);
            } catch (MalformedURLException e) {
                isValid = false;
                log.error("URL defined for asset {} is not a valid URL", asset);
            }
        } else if (StringUtils.isNotBlank(asset.urlResolver.urlPattern)) {
            String urlPattern = asset.urlResolver.urlPattern;
            if ((urlPattern.contains("${name}") && StringUtils.isBlank(asset.name))
                    || (urlPattern.contains("${version}") && StringUtils.isBlank(asset.version))) {
                isValid = false;
                log.error("URL pattern for asset {} contains placeholders which are not set.", asset);
            } else {
                if (urlPattern.contains("${name}")) {
                    urlPattern = urlPattern.replace("${name}", asset.name);
                }
                if (urlPattern.contains("${version}")) {
                    urlPattern = urlPattern.replace("${version}", asset.version);
                }
                asset.urlResolver.setUrl(urlPattern);
            }
        } else {
            log.error("URL resolver for asset {} does not contain a valid URL or URL pattern", asset);
            isValid = false;
        }
    }

    private static void validateReports(List<PipelineConfiguration.Report> reports, List<String> assetIds) {
        for (PipelineConfiguration.Report report : reports) {
            if (report.assets.isEmpty() || !new HashSet<>(assetIds).containsAll(report.assets)) {
                isValid = false;
                log.error("Some assets, {} listed in report, do not exist.", report.assets);
            }

            if (StringUtils.isBlank(report.type) || !ReportType.allKeys().contains(report.type)) {
                isValid = false;
                log.error("Report for assets {} requires a valid report type.", report.assets);
            }
        }
    }

    private static void validateDashboards(List<PipelineConfiguration.Dashboard> dashboards, List<String> assetIds) {
        for (PipelineConfiguration.Dashboard dashboard : dashboards) {
            if (dashboard.assets.isEmpty() || !new HashSet<>(assetIds).containsAll(dashboard.assets)) {
                isValid = false;
                log.error("Some assets, {} listed in dashboard, do not exist.", dashboard.assets);
            }
        }
    }
}

