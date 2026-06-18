package org.metaeffekt.kontinuum.models.gitlab;

import org.metaeffekt.kontinuum.models.shared.EnvironmentConfiguration;

import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.metaeffekt.kontinuum.util.KontinuumUtils;

@SuperBuilder
public class GitlabConfiguration extends EnvironmentConfiguration {
    
    @Builder.Default
    public final int GIT_DEPTH = 1;
    
    @Builder.Default
    public final String GIT_STRATEGY = "CLONE";
    
    public final String CONTAINER_IMAGE;
    public final String RUNNER_TAG;

    @Override
    public String getWorkspaceDirNormalized() {
        return KontinuumUtils.normalizeDir(WORKSPACE_DIR);
    }
}

