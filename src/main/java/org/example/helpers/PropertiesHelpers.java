package org.example.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Helper class to read and write properties files
 */
public class PropertiesHelpers {

    private static Properties properties;
    private static String linkFile;
    private static FileInputStream file;
    private static final String DEFAULT_PROPERTIES_FILE = String.join(File.separator,
            "src", "main", "resources", "config", "config.properties");

    /**
     * Load all properties files
     * You can add multiple properties files here
     */
    public static void loadAllFiles() {
        LinkedList<String> files = new LinkedList<>();

        // Add all properties files here
        files.add(String.join(File.separator,
                "src", "main", "resources", "config", "config.properties"));
        try {
            properties = new Properties();

            for (String f : files) {
                Properties tempProp = new Properties();
                linkFile = SystemHelpers.getCurrentDir() + f;
                System.out.println("[PropertiesHelpers] Loading properties file: " + linkFile);
                file = new FileInputStream(linkFile);
                tempProp.load(file);
                properties.putAll(tempProp);
                file.close();
            }

            System.out.println("[PropertiesHelpers] Successfully loaded " + files.size() + " properties file(s)");

        } catch (IOException ioe) {
            System.err.println("[PropertiesHelpers] Error loading properties files: " + ioe.getMessage());
            ioe.printStackTrace();
            new Properties();
        }
    }

    /**
     * Set specific properties file to read
     *
     * @param relPropertiesFilePath Relative path to properties file
     */
    public static void setFile(String relPropertiesFilePath) {
        properties = new Properties();
        try {
            linkFile = SystemHelpers.getCurrentDir() + relPropertiesFilePath;
            System.out.println("[PropertiesHelpers] Setting properties file: " + linkFile);
            file = new FileInputStream(linkFile);
            properties.load(file);
            file.close();
        } catch (Exception e) {
            System.err.println("[PropertiesHelpers] Error setting properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set default properties file (config.properties)
     */
    public static void setDefaultFile() {
        properties = new Properties();
        try {
            linkFile = SystemHelpers.getCurrentDir() + DEFAULT_PROPERTIES_FILE;
            System.out.println("[PropertiesHelpers] Loading default properties file: " + linkFile);
            file = new FileInputStream(linkFile);
            properties.load(file);
            file.close();
        } catch (Exception e) {
            System.err.println("[PropertiesHelpers] Error loading default properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get property value by key
     *
     * @param key Property key
     * @return Property value or null if not found
     */
    public static String getValue(String key) {
        String value = null;
        try {
            if (file == null) {
                properties = new Properties();
                linkFile = SystemHelpers.getCurrentDir() + DEFAULT_PROPERTIES_FILE;
                file = new FileInputStream(linkFile);
                properties.load(file);
                file.close();
            }

            // Get value from properties
            value = properties.getProperty(key);

            if (value == null) {
                System.out.println("[PropertiesHelpers] Property key '" + key + "' not found");
            }

        } catch (Exception e) {
            System.err.println("[PropertiesHelpers] Error getting value for key '" + key + "': " + e.getMessage());
        }
        return value;
    }

    /**
     * Get property value with default fallback
     *
     * @param key          Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public static String getValue(String key, String defaultValue) {
        String value = getValue(key);
        return (value != null) ? value : defaultValue;
    }

    /**
     * Set value to current properties file
     * WARNING: Use with caution, should not modify config.properties
     *
     * @param key      Property key
     * @param keyValue Property value
     */
    public static void setValue(String key, String keyValue) {
        try {
            if (file == null) {
                properties = new Properties();
                linkFile = SystemHelpers.getCurrentDir() + DEFAULT_PROPERTIES_FILE;
                file = new FileInputStream(linkFile);
                properties.load(file);
                file.close();
            }

            // Write to same properties file
            FileOutputStream out = new FileOutputStream(linkFile);
            System.out.println("[PropertiesHelpers] Setting value '" + keyValue + "' for key '" + key + "' in file: " + linkFile);
            properties.setProperty(key, keyValue);
            properties.store(out, null);
            out.close();

        } catch (Exception e) {
            System.err.println("[PropertiesHelpers] Error setting value: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set value to specific properties file
     * Recommended: Use this to write to NEW properties file, not config.properties
     *
     * @param filePropertiesRelativePath Relative path to properties file
     * @param key                        Property key
     * @param keyValue                   Property value
     */
    public static void setValue(String filePropertiesRelativePath, String key, String keyValue) {
        try {
            String fullPath = SystemHelpers.getCurrentDir() + filePropertiesRelativePath;

            Properties properties = new Properties();
            FileInputStream file = new FileInputStream(fullPath);
            properties.load(file);
            file.close();

            properties.setProperty(key, keyValue);
            FileOutputStream out = new FileOutputStream(fullPath);
            properties.store(out, null);
            out.close();

            System.out.println("[PropertiesHelpers] Set value '" + keyValue + "' to file " + fullPath);

        } catch (Exception e) {
            System.err.println("[PropertiesHelpers] Error setting value to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reload properties from file
     */
    public static void reload() {
        System.out.println("[PropertiesHelpers] Reloading properties files...");
        loadAllFiles();
    }
}
