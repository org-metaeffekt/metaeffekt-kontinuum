package org.metaeffekt.kontinuum.generator.shared;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration;
import org.metaeffekt.kontinuum.models.shared.ReportType;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.Dashboard;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.Report;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.ProjectProperties.Asset;
import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.ProjectProperties.Project;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class PipelineConfigurationLoader {

    private boolean isValid = true;
    private PipelineConfiguration pipelineConfiguration;

    
    public PipelineConfiguration readConfig(File pipelineConfigFile) {
        isValid = true;
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            validatePipelineConfigFile(objectMapper.readValue(pipelineConfigFile, PipelineConfiguration.class));
            return pipelineConfiguration;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read pipeline configuration file.", e);
        }
    }
    

    public void validatePipelineConfigFile(PipelineConfiguration pipelineConfiguration) {
        this.pipelineConfiguration = pipelineConfiguration;
        
        if (pipelineConfiguration == null) {
            log.error("Pipeline configuration is missing.");
            isValid = false;
            return;
        }

        if (pipelineConfiguration.getProjectProperties() == null) {
            log.error("Project properties are missing.");
            isValid = false;
            return;
        }

        validateProject();
        validateAssets();
        validateReports();
        validateDashboards();

        if (!isValid) {
            throw new IllegalStateException("Pipeline configuration contains errors.");
        }
    }

    private void validateProject() {
        Project project = pipelineConfiguration.getProjectProperties().getProject();
        if (project == null) {
            log.error("Project is missing.");
            isValid = false;
            return;
        }

        if (StringUtils.isBlank(project.getName())) {
            log.error("Project name is empty.");
            isValid = false;
        }

        if (StringUtils.isBlank(project.getVersion())) {
            log.error("Project version is empty.");
            isValid = false;
        }

        if (StringUtils.isBlank(project.getTenant()) && assessmentFieldsRequired(null)) {
            log.error("Project tenant is empty but required for reports or dashboards.");
            isValid = false;
        }
    }

    private void validateAssets() {
        List<PipelineConfiguration.ProjectProperties.Asset> assets = pipelineConfiguration.getProjectProperties().getAssets();
        if (assets == null || assets.isEmpty()) {
            log.error("Assets are missing.");
            isValid = false;
            return;
        }

        for (PipelineConfiguration.ProjectProperties.Asset asset : assets) {
            if (asset == null) {
                log.error("Asset entry is null.");
                isValid = false;
                continue;
            }
            if (StringUtils.isBlank(asset.getId())) {
                log.error("Asset {} requires an id to be set.", asset);
                isValid = false;
            }

            if (StringUtils.isBlank(asset.getAssessmentId()) && assessmentFieldsRequired(asset)) {
                log.error("Asset {} requires 'assessmentId' to be set because either reports or dashboards require it.", asset);
                isValid = false;
            }

            if (StringUtils.isBlank(asset.getContext()) && assessmentFieldsRequired(asset)) {
                log.error("Asset {} requires 'context' to be set because either reports or dashboards require it.", asset);
                isValid = false;
            }

            if (StringUtils.isBlank(asset.getReference())) {
                log.error("Asset {} requires 'reference' to be set.", asset);
                isValid = false;
            }
            
            if (asset.getMavenResolver() != null || asset.getContainerResolver() != null) {
                log.warn("The maven resolver and container resolver are not yet implemented and can be removed for asset {}", asset);
            }
            
            validateUrlResolver(asset);
        }
    }

    private void validateUrlResolver(PipelineConfiguration.ProjectProperties.Asset asset) {
        if (asset.getUrlResolver() == null) {
            log.error("Asset {} requires urlResolver to be set.", asset);
            isValid = false;
            return;
        }

        if (StringUtils.isNotBlank(asset.getUrlResolver().getUrl())) {
            try {
                new URL(asset.getUrlResolver().getUrl());
            } catch (MalformedURLException e) {
                isValid = false;
                log.error("Asset {} requires 'urlResolver.url' to contain a valid URL.", asset);
            }
        } else if (StringUtils.isNotBlank(asset.getUrlResolver().getUrlPattern())) {
            String urlPattern = asset.getUrlResolver().getUrlPattern();
            if ((urlPattern.contains("${name}") && StringUtils.isBlank(asset.getName()))
                    || (urlPattern.contains("${version}") && StringUtils.isBlank(asset.getVersion()))) {
                isValid = false;
                log.error("Asset {} requires 'urlResolver.urlPattern' to only contain placeholders which are set.", asset);
            } else {
                if (urlPattern.contains("${name}")) {
                    urlPattern = urlPattern.replace("${name}", asset.getName());
                }
                if (urlPattern.contains("${version}")) {
                    urlPattern = urlPattern.replace("${version}", asset.getVersion());
                }
                asset.getUrlResolver().setUrl(urlPattern);
            }
        } else {
            log.error("Asset {} requires 'urlResolver' to contain either a valid 'url' or 'urlPattern'.", asset);
            isValid = false;
        }
    }

    private void validateReports() {
        List<String> assetIds = getAssets()
            .stream()
            .map(Asset::getId)
            .toList();

        for (PipelineConfiguration.Report report : getReports()) {
            if (report == null) {
                log.error("Report entry is null.");
                isValid = false;
                continue;
            }
            if (StringUtils.isBlank(report.getType())) {
                log.error("A report with 'assetId': {} contains an empty 'type'.", report.getAssetId());
                isValid = false;
                continue;
            }

            if (!ReportType.allKeys().contains(report.getType())) {
                log.error("A report with 'assetId': {} contains an invalid 'type'.", report.getAssetId());
                isValid = false;
                continue;
            }
            if (StringUtils.isBlank(report.getAssetId()) || !assetIds.contains(report.getAssetId())) {
                log.error("A report contains an 'assetId' which does not exist: {}", report.getAssetId());
                isValid = false;
            }

        }
    }

    private void validateDashboards() {
        List<String> assetIds = getAssets()
            .stream()
            .map(Asset::getId)
            .toList();

        for (PipelineConfiguration.Dashboard dashboard : getDashboards()) {
            if (dashboard == null) {
                log.error("Dashboard entry is null.");
                isValid = false;
                continue;
            }
            if (StringUtils.isBlank(dashboard.getAssetId()) || !new HashSet<>(assetIds).contains(dashboard.getAssetId())) {
                log.error("A dashboard contains an 'assetId' which does not exist: {}", dashboard.getAssetId());
                isValid = false;
            }
        }
    }

    private boolean assessmentFieldsRequired(Asset asset) {
        Set<ReportType> reportTypesRequiringAssessmentFields = Set.of(ReportType.CERT_REPORT, ReportType.VULNERABILITY_REPORT, ReportType.VULNERABILITY_SUMMARY_REPORT);
        List<Report> reportsRequiringAssessmentFields = new ArrayList<>();
        List<Dashboard> dashboardsRequiringAssessmentFields = new ArrayList<>();

        if (asset == null) {
            reportsRequiringAssessmentFields = getReports()
                .stream()
                .filter(r -> ReportType.allKeys().contains(r.getType()))
                .filter(r -> reportTypesRequiringAssessmentFields.contains(ReportType.fromKey(r.getType())))
                .toList();
            dashboardsRequiringAssessmentFields.addAll(getDashboards());
        } else {
            reportsRequiringAssessmentFields = getReports()
                .stream()
                .filter(r -> ReportType.allKeys().contains(r.getType()))
                .filter(r -> reportTypesRequiringAssessmentFields.contains(ReportType.fromKey(r.getType())))
                .filter(r -> StringUtils.equals(r.getAssetId(), asset.getId()))
                .toList();
            dashboardsRequiringAssessmentFields.addAll(getDashboards()
                .stream()
                .filter(d -> StringUtils.equals(d.getAssetId(), asset.getId()))
                .toList());
        }
        
        return !reportsRequiringAssessmentFields.isEmpty() || !dashboardsRequiringAssessmentFields.isEmpty();
    }

    private List<Asset> getAssets() {
        PipelineConfiguration.ProjectProperties projectProperties = pipelineConfiguration.getProjectProperties();
        if (projectProperties == null || projectProperties.getAssets() == null) {
            return Collections.emptyList();
        }
        return projectProperties.getAssets();
    }

    private List<Report> getReports() {
        if (pipelineConfiguration.getReports() == null) {
            return Collections.emptyList();
        }
        return pipelineConfiguration.getReports();
    }

    private List<Dashboard> getDashboards() {
        if (pipelineConfiguration.getDashboards() == null) {
            return Collections.emptyList();
        }
        return pipelineConfiguration.getDashboards();
    }
}
