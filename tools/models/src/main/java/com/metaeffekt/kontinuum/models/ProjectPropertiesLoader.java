package com.metaeffekt.kontinuum.models;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Loads project.properties from the tools directory and sets them as system properties.
 * This ensures the properties are available regardless of how the application is run.
 */
@Slf4j
public class ProjectPropertiesLoader {

    private static final String PROPERTIES_FILE = "project.properties";
    private static final String[] PROPERTY_KEYS = {
        "kontinuum.dir",
        "workbench.dir",
        "ae.core.version",
        "ae.artifact.analysis.version"
    };

    private final Properties properties;

    public ProjectPropertiesLoader() {
        this.properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        // Try multiple strategies to locate the properties file
        Path propertiesFile = locatePropertiesFile();

        if (propertiesFile != null && Files.exists(propertiesFile)) {
            log.info("Loading project properties from: {}", propertiesFile.toAbsolutePath());
            try (InputStream input = Files.newInputStream(propertiesFile)) {
                properties.load(input);
                applyAsSystemProperties();
                log.info("Loaded {} project properties", properties.size());
            } catch (IOException e) {
                log.warn("Failed to load project.properties: {}", e.getMessage());
            }
        } else {
            log.warn("project.properties not found, using defaults from environment");
        }
    }

    private Path locatePropertiesFile() {
        // Strategy 1: Relative to current working directory
        Path current = Path.of("").toAbsolutePath().normalize();
        while (current != null) {
            Path candidate = current.resolve("tools").resolve(PROPERTIES_FILE);
            if (Files.exists(candidate)) {
                return candidate;
            }
            // Also check if we're already in the tools directory
            candidate = current.resolve(PROPERTIES_FILE);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }

        // Strategy 2: Check classpath (for packaged JAR)
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (stream != null) {
                // Copy to temp file for loading
                Path tempFile = Files.createTempFile("project-properties", ".properties");
                Files.copy(stream, tempFile);
                tempFile.toFile().deleteOnExit();
                return tempFile;
            }
        } catch (IOException e) {
            // Ignore
        }

        return null;
    }

    private void applyAsSystemProperties() {
        for (String key : PROPERTY_KEYS) {
            String value = properties.getProperty(key);
            if (value != null && !value.isEmpty()) {
                // Only set if not already set
                if (System.getProperty(key) == null) {
                    System.setProperty(key, value);
                    log.debug("Set system property: {}={}", key, value);
                }
            }
        }
    }

    /**
     * Get a property value.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get a property value with a default.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get all loaded properties.
     */
    public Properties getProperties() {
        return new Properties(properties);
    }

    /**
     * Check if a property exists.
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
}
