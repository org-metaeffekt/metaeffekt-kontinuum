package org.metaeffekt.kontinuum.models.shared;

public class Workspace {

    public final String WORKSPACE_DIR;
    public final String MAVEN_INDEX_DIR;

    public Workspace(PipelineConfiguration pipelineConfiguration) {
        WORKSPACE_DIR = "workspace/" + pipelineConfiguration.getProjectProperties().getProject() + "/";
        MAVEN_INDEX_DIR = "workspace/maven-index/";
    }

    public AssetPath getFetchedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/00_fetched/" + asset + "/", asset);
    }
    public AssetPath getExtractedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/01_extracted/" + asset + "/", asset);
    }

    public AssetPath getPreparedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/02_prepared/" + asset + "/", asset);
    }

    public AssetPath getAggregatedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/03_aggregated/" + asset + "/", asset);
    }

    public AssetPath getResolvedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/04_resolved/" + asset + "/", asset);
    }

    public AssetPath getScannedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/05_scanned/" + asset + "/", asset);
    }

    public AssetPath getAdvisedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/06_advised/" + asset + "/", asset);
    }

    public AssetPath getGroupedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset, ReportType reportType) {
        return new AssetPath(WORKSPACE_DIR + asset + "/07_grouped/" + asset + "/" + reportType.getWorkspaceFolder() + "/", asset);
    }

    public AssetPath getReportedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/08_reported/" + asset + "/", asset);
    }

    public AssetPath getSummarizedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return new AssetPath(WORKSPACE_DIR + asset + "/09_summarized/" + asset + "/", asset);
    }

    public record AssetPath(String dir, PipelineConfiguration.ProjectProperties.Asset assetName) {

        public String appendAssetInventory() {
            return dir + assetName + ".xlsx";
        }


        @Override
        public String toString() {
            return dir;
        }
    }

}
