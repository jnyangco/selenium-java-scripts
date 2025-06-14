package com.automation.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Reader utility class
 * Reads configuration properties from files and system properties
 *
 * Example:
 *     String browser = ConfigReader.getProperty("browser");
 *     String baseUrl = ConfigReader.getProperty("saucedemo.base.url");
 */
public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config/config.properties";
    private static final String DATA_FILE_PATH = "src/test/resources/config/data.properties";

    static {
        loadProperties();
    }

    /**
     * Load properties from configuration files
     */
    private static void loadProperties() {
        properties = new Properties();

        try {
            // Load main configuration
            FileInputStream configFile = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(configFile);
            configFile.close();
            logger.info("Loaded configuration from: {}", CONFIG_FILE_PATH);

            // Load test data configuration
            FileInputStream dataFile = new FileInputStream(DATA_FILE_PATH);
            properties.load(dataFile);
            dataFile.close();
            logger.info("Loaded test data from: {}", DATA_FILE_PATH);

        } catch (IOException e) {
            logger.error("Failed to load configuration files: {}", e.getMessage());
            throw new RuntimeException("Configuration files not found", e);
        }
    }

    /**
     * Get property value by key
     * @param key Property key
     * @return Property value
     */
    public static String getProperty(String key) {
        // First check system properties (for command line overrides)
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            logger.debug("Using system property for {}: {}", key, systemProperty);
            return systemProperty;
        }

        // Then check loaded properties
        String property = properties.getProperty(key);
        if (property != null) {
            logger.debug("Using config property for {}: {}", key, property);
            return property;
        }

        logger.warn("Property not found: {}", key);
        return null;
    }

    /**
     * Get property value with default fallback
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get property as integer
     * @param key Property key
     * @param defaultValue Default value if property not found or invalid
     * @return Property value as integer
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            String value = getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer property {}: {}", key, getProperty(key));
            return defaultValue;
        }
    }

    /**
     * Get property as boolean
     * @param key Property key
     * @param defaultValue Default value if property not found
     * @return Property value as boolean
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Get browser configuration
     */
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    /**
     * Get environment configuration
     */
    public static String getEnvironment() {
        return getProperty("environment", "local");
    }

    /**
     * Get base URL for specific application
     */
    public static String getBaseUrl(String application) {
        return getProperty(application + ".base.url");
    }

    /**
     * Get username for specific application
     */
    public static String getUsername(String application) {
        return getProperty(application + ".username");
    }

    /**
     * Get password for specific application
     */
    public static String getPassword(String application) {
        return getProperty(application + ".password");
    }

    /**
     * Check if headless mode is enabled
     */
    public static boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }

    /**
     * Get implicit wait timeout
     */
    public static int getImplicitWait() {
        return getIntProperty("implicit.wait", 10);
    }

    /**
     * Get explicit wait timeout
     */
    public static int getExplicitWait() {
        return getIntProperty("explicit.wait", 10);
    }

    /**
     * Reload properties (useful for dynamic configuration changes)
     */
    public static void reloadProperties() {
        logger.info("Reloading configuration properties");
        loadProperties();
    }
}