package org.metaeffekt.kontinuum.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class KontinuumUtils {

    public static Properties getLocalProperties() {
        File localPropertiesFile = new File(".project.properties");
        if (!localPropertiesFile.exists()) {
            localPropertiesFile = new File("../.project.properties");
        }
        if (!localPropertiesFile.exists()) {
            localPropertiesFile = new File("../../.project.properties");
        }
        if (!localPropertiesFile.exists()) {
            localPropertiesFile = new File("../../../.project.properties");
        }
        if (!localPropertiesFile.exists()) {
            throw new IllegalStateException("Provide .project.properties file.");
        }

        Properties properties = new Properties();
        if (localPropertiesFile.exists()) {
            try (FileInputStream in = new FileInputStream(localPropertiesFile)) {
                properties.load(in);
            } catch (IOException e) {
                log.error("Cannot load '{}'.", localPropertiesFile, e);
            }
        }
        return properties;
    }

    public static String normalizeDir(String... path) {
        StringBuilder sb = new StringBuilder();
        for (String part : path) {
            if (part == null || part.isEmpty()) {
                continue;
            }
            if (!sb.isEmpty() && sb.charAt(sb.length() - 1) != '/') {
                sb.append('/');
            }
            if (!sb.isEmpty() && part.startsWith("/")) {
                part = part.substring(1);
            }
            sb.append(part);
        }
        String result = sb.toString().replaceAll("/{2,}", "/");
        if (!result.endsWith("/")) {
            result += "/";
        }
        return result;
    }
}
