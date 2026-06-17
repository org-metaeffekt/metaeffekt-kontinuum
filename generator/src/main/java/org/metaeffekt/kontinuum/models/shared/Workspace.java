package org.metaeffekt.kontinuum.models.shared;

import java.util.List;
import java.util.stream.Collectors;

import org.metaeffekt.kontinuum.models.shared.PipelineConfiguration.ProjectProperties.Asset;

public class Workspace {

    public final String WORKSPACE_DIR;
    public final String MAVEN_INDEX_DIR;

    public Workspace(PipelineConfiguration pipelineConfiguration, EnvironmentConfiguration environmentConfiguration) {
        WORKSPACE_DIR = environmentConfiguration.getWorkspaceDirNormalized() + pipelineConfiguration.getProjectProperties().getProject() + "/";
        MAVEN_INDEX_DIR = "workspace/maven-index/";
    }

    public AssetPath getFetchedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "00_fetched/" + asset + "/", asset);
    }
    public AssetPath getExtractedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "01_extracted/" + asset + "/", asset);
    }

    public AssetPath getPreparedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "02_prepared/" + asset + "/", asset);
    }

    public AssetPath getAggregatedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "03_aggregated/" + asset + "/", asset);
    }

    public AssetPath getResolvedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "04_resolved/" + asset + "/", asset);
    }

    public AssetPath getScannedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "05_scanned/" + asset + "/", asset);
    }

    public AssetPath getAdvisedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "06_advised/" + asset + "/", asset);
    }

    public String getGroupedDir(ReportType reportType) {
        return WORKSPACE_DIR + "07_grouped/" + reportType.getWorkspaceFolder() + "/";
    }

    public AssetPath getReportedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "08_reported/" + asset + "/", asset);
    }

    public String getReportedDirWithInventory(List<Asset> assetsPartOfReport, ReportType reportType) {
        StringBuilder reportPath = new StringBuilder();
        reportPath.append(WORKSPACE_DIR).append("08_reported/");
        assetsPartOfReport.forEach(a -> reportPath.append(a).append("-"));

        reportPath.append(reportType.getKey()).append(".pdf");

        return reportPath.toString();
    }
        

    public AssetPath getSummarizedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + "09_summarized/" + asset + "/", asset);
    }

    public record AssetPath(String dir, PipelineConfiguration.ProjectProperties.Asset assetName) {

        public String appendAssetInventory() {
            return dir + assetName + ".xlsx";
        }

        public String appenDashboardFile() {
            return dir + assetName + ".html";
        }


        @Override
        public String toString() {
            return dir;
        }
    }

}
