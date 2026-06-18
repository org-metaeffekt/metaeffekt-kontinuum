package org.metaeffekt.kontinuum.models.shared;

import java.io.File;

import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.metaeffekt.kontinuum.util.KontinuumUtils;

@SuperBuilder
public abstract class EnvironmentConfiguration {
    
    @Builder.Default
    public final String WORKBENCH_DIR = "./workbench/";

    @Builder.Default
    public final String WORKSPACE_DIR = "./workspace/";

    @Builder.Default
    public final String VULNERABILITY_MIRROR_DIR = "./mirror/";

    public final String VULNERABILITY_MIRROR_URL;
    public final String ARTIFACT_RESOLVER_CONFIG_FILE;
    public final String ARTIFACT_RESOLVER_PROXY_FILE;
    public final String SCAN_PROPERTIES_FILE;
    public final String KOSMOS_PASSWORD;
    public final String KOSMOS_USERKEYS_FILE;
    public final File SETUP_COMMAND;
    private final String KONTINUUM_DIR;

    public String getCorrelationDir() {
        return getWorkbenchDirNormalized() + "correlations/";
    }

    public String getAssessmentsDir() {
        return getWorkbenchDirNormalized() + "assessments/";
    }

    public String getWorkbenchDirNormalized() {
        return KontinuumUtils.normalizeDir(WORKBENCH_DIR);
    }

    public String getKontinuumDirNormalized() {
        return KontinuumUtils.normalizeDir(KONTINUUM_DIR);
    }

    public String getKontinuumProcessorsDirNormalized() {
        return getKontinuumDirNormalized() + "processors/";
    }

    public abstract String getWorkspaceDirNormalized();
}


