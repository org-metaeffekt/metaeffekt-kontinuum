package org.metaeffekt.kontinuum.models.shared;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ReportType {
    CERT_REPORT("CR"),
    CUSTOM_ANNEX("CA"),
    INITIAL_LICENSE_DOCUMENTATION("ILD"),
    LICENSE_DOCUMENTATION("LD"),
    SOFTWARE_DISTRIBUTION_ANNEX("SDA"),
    VULNERABILITY_REPORT("VR"),
    VULNERABILITY_SUMMARY_REPORT("VSR");

    @Getter
    private final String key;

    ReportType(String key) {
        this.key = key;
    }

    public static Set<String> allKeys() {
        return Arrays.stream(values()).map(ReportType::getKey).collect(Collectors.toSet());
    }
}
