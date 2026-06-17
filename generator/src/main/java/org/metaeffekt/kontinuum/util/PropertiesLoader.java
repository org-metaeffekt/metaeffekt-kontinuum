package org.metaeffekt.kontinuum.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertiesLoader {

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
}
