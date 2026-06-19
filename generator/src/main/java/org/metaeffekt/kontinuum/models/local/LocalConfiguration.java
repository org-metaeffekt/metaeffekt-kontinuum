package org.metaeffekt.kontinuum.models.local;

import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.metaeffekt.kontinuum.models.shared.EnvironmentConfiguration;
import org.metaeffekt.kontinuum.util.KontinuumUtils;

@SuperBuilder
public class LocalConfiguration extends EnvironmentConfiguration {

    @Builder.Default
    ExecutionEnvironment executionEnvironment = ExecutionEnvironment.UNIX;

    @Override
    public String getWorkspaceDirNormalized() {
        return KontinuumUtils.normalizeDir(WORKSPACE_DIR);
    }

    public enum ExecutionEnvironment {
        UNIX,
        WINDOWS_NT
    }
}

