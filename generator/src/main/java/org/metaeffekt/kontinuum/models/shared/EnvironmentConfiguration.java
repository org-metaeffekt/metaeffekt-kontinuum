package org.metaeffekt.kontinuum.models.shared;

import lombok.Builder;
import lombok.Getter;

@Builder
public class EnvironmentConfiguration {
    public final String ARTIFACT_RESOLVER_CONFIG_FILE;
    public final String ARTIFACT_RESOLVER_PROXY_FILE;
    public final String SCAN_PROPERTIES_FILE;
    public final String KOSMOS_PASSWORD;
    public final String KOSMOS_USERKEYS_FILE;
    private final String WORKBENCH_DIR;

    public String getCorrelationDir() {
        return getWorkbenchDirNormalized() + "correlations/";
    }

    private String getWorkbenchDirNormalized() {
        if (WORKBENCH_DIR.endsWith("/")) {
            return WORKBENCH_DIR;
        } else {
            return WORKBENCH_DIR + "/";
        }
    }
}


