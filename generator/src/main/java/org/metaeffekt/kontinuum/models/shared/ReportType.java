package org.metaeffekt.kontinuum.models.shared;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ReportType {
    CERT_REPORT("CR", "cert-report"),
    CUSTOM_ANNEX("CA", "custom-annex"),
    INITIAL_LICENSE_DOCUMENTATION("ILD", "initial-license-documentation"),
    LICENSE_DOCUMENTATION("LD", "license-documentation"),
    SOFTWARE_DISTRIBUTION_ANNEX("SDA", "software-distribution-annex"),
    VULNERABILITY_REPORT("VR", "vulnerability-report"),
    VULNERABILITY_SUMMARY_REPORT("VSR", "vulnerability-summary-report");

    @Getter
    private final String key;

    @Getter
    private final String workspaceFolder;
    ReportType(String key, String workspaceFolder) {
        this.key = key;
        this.workspaceFolder = workspaceFolder;
    }

    public static Set<String> allKeys() {
        return Arrays.stream(values()).map(ReportType::getKey).collect(Collectors.toSet());
    }
}
