package com.metaeffekt.kontinuum.models;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum ProjectProperties {

    KONTINUUM_DIR("kontinuum.dir"),
    WORKBENCH_DIR("workbench.dir"),
    AE_CORE_VERSION("ae.core.version"),
    AE_ARTIFACT_ANALYSIS_VERSION("ae.artifact.analysis.version");

    @Getter
    public final String propertyKey;

    @Getter
    public static final List<String> propertyKeys = Arrays.stream(ProjectProperties.values()).map(ProjectProperties::getPropertyKey).toList();

    ProjectProperties(String propertyKey) {
        this.propertyKey = propertyKey;
    }
}
