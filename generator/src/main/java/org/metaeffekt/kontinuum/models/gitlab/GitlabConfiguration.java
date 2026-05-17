package org.metaeffekt.kontinuum.models.gitlab;

import java.io.File;

import org.metaeffekt.kontinuum.models.shared.EnvironmentConfiguration;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GitlabConfiguration extends EnvironmentConfiguration {
    
    @Builder.Default
    public final int GIT_DEPTH = 1;
    
    @Builder.Default
    public final String GIT_STRATEGY = "CLONE";
    
    public final String CONTAINER_IMAGE;
    public final String RUNNER_TAG;
}

