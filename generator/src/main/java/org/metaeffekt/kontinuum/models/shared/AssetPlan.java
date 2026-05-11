package org.metaeffekt.kontinuum.models.shared;

import lombok.Getter;
import org.metaeffekt.kontinuum.models.gitlab.PipelineConfiguration;

import java.util.HashSet;
import java.util.Set;

@Getter
public class AssetPlan {


    private final PipelineConfiguration.ProjectProperties.Asset asset;
    private final Set<Stage> requiredStages = new HashSet<>();

    public AssetPlan(PipelineConfiguration.ProjectProperties.Asset asset) {
        this.asset = asset;
    }

    public void addStage(Stage stage) {
        requiredStages.add(stage);
    }
}
