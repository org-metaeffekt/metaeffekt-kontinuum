package org.metaeffekt.kontinuum.models.shared;

import java.io.File;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class EnvironmentConfiguration {
    
    @Builder.Default
    public final String WORKBENCH_DIR = "./workbench/";

    @Builder.Default
    public final String VULNERABILITY_MIRROR_DIR = "./mirror/";

    public final String VULNERABILITY_MIRROR_URL;
    public final String ARTIFACT_RESOLVER_CONFIG_FILE;
    public final String ARTIFACT_RESOLVER_PROXY_FILE;
    public final String SCAN_PROPERTIES_FILE;
    public final String KOSMOS_PASSWORD;
    public final String KOSMOS_USERKEYS_FILE;
    public final File SETUP_COMMAND;


    public String getCorrelationDir() {
        return getWorkbenchDirNormalized() + "correlations/";
    }

    private String getAssessmentsDir() {
        return getWorkbenchDirNormalized() + "assessments/";
    }

    private String getWorkbenchDirNormalized() {
        if (WORKBENCH_DIR.endsWith("/")) {
            return WORKBENCH_DIR;
        } else {
            return WORKBENCH_DIR + "/";
        }
    }
}


