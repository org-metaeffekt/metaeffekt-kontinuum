package org.metaeffekt.kontinuum.models.shared;

import lombok.Getter;
import org.metaeffekt.kontinuum.models.gitlab.PipelineConfiguration;

public class Workspace {

    @Getter
    String workspaceDir;

    public Workspace(String mountedVolume, String productId) {
        StringBuilder sb = new StringBuilder();
        sb.append(mountedVolume);

        if (mountedVolume.endsWith("/")) {
            sb.append("workspace/");
        } else {
            sb.append("/workspace/");
        }

        workspaceDir = sb.append(productId).append("/").toString();
    }

    public String getAdditionalDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/xx_additional/" + asset + "/";
    }

    public String getFetchedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/00_fetched/" + asset + "/";
    }

    public String getExtractedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/01_extracted/" + asset + "/";
    }

    public String getPreparedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/02_prepared/" + asset + "/";
    }

    public String getAggregatedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/03_aggregated/" + asset + "/";
    }

    public String getResolvedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/04_resolved/" + asset + "/";
    }

    public String getScannedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/05_scanned/" + asset + "/";
    }

    public String getAdvisedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/06_advised/" + asset + "/";
    }

    public String getGroupedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/07_grouped/" + asset + "/";
    }

    public String getReportedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/08_reported/" + asset + "/";
    }

    public String getSummarizedDirForAsset(PipelineConfiguration.ProjectProperties.Asset asset) {
        return workspaceDir + asset + "/09_summarized/" + asset + "/";
    }
}
