package org.metaeffekt.kontinuum.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class MissingValueCollector {

    private final List<String> missing = new ArrayList<>();

    public String require(String label, Supplier<String> supplier) {
        try {
            String value = supplier.get();
            if (value == null || value.isBlank()) {
                missing.add(label);
            }
            return value;
        } catch (NullPointerException e) {
            missing.add(label);
            return null;
        }
    }

    public String require(String label, String value) {
        if (StringUtils.isBlank(value)) {
            missing.add(label);
        }
        return value;
    }

    public void check() {
        if (missing.isEmpty()) return;
        String msg = "Missing required configuration values:\n" +
            missing.stream().map(s -> "  - " + s).collect(Collectors.joining("\n"));
        System.err.println(msg);
        throw new IllegalStateException(msg);
    }
}
