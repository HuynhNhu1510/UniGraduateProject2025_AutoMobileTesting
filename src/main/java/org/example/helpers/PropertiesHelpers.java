package org.example.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

public class PropertiesHelpers {

    private static Properties properties;
    private static String linkFile;
    private static FileInputStream file;
    private static final String DEFAULT_PROPERTIES_FILE = String.join(File.separator,
            "src", "main", "resources", "config", "config.properties");

    public static void loadAllFiles() {
        LinkedList<String> files = new LinkedList<>();

        files.add(String.join(File.separator,
                "src", "main", "resources", "config", "config.properties"));

        properties = new Properties();

        for (String f : files) {
            Properties tempProp = new Properties();
            String fullPath = SystemHelpers.getCurrentDir() + f;
            System.out.println("[PropertiesHelpers] Loading properties file: " + fullPath);

            try (FileInputStream fis = new FileInputStream(fullPath)) {
                tempProp.load(fis);
                properties.putAll(tempProp);
            } catch (IOException ioe) {
                System.err.println("[PropertiesHelpers] Error loading file " + fullPath + ": " + ioe.getMessage());
                ioe.printStackTrace();
            }
        }
        System.out.println("[PropertiesHelpers] Successfully loaded " + files.size() + " properties file(s)");
    }

    public static void setFile(String relPropertiesFilePath) {
        properties = new Properties();
        String fullPath = SystemHelpers.getCurrentDir() + relPropertiesFilePath;

        System.out.println("[PropertiesHelpers] Setting properties file: " + fullPath);

        try (FileInputStream fis = new FileInputStream(fullPath)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("[PropertiesHelpers] Error setting properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setDefaultFile() {
        properties = new Properties();
        String fullPath = SystemHelpers.getCurrentDir() + DEFAULT_PROPERTIES_FILE;

        System.out.println("[PropertiesHelpers] Loading default properties file: " + fullPath);

        try (FileInputStream fis = new FileInputStream(fullPath)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("[PropertiesHelpers] Error loading default properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        String value = null;
        try {
            if (file == null) {  // ← Có thể xóa biến 'file' static field
                properties = new Properties();
                String fullPath = SystemHelpers.getCurrentDir() + DEFAULT_PROPERTIES_FILE;

                try (FileInputStream fis = new FileInputStream(fullPath)) {
                    properties.load(fis);
                }
            }

            value = properties.getProperty(key);

            if (value == null) {
                System.out.println("[PropertiesHelpers] Property key '" + key + "' not found");
            }

        } catch (IOException e) {
            System.err.println("[PropertiesHelpers] Error getting value for key '" + key + "': " + e.getMessage());
        }
        return value;
    }

    public static String getValue(String key, String defaultValue) {
        String value = getValue(key);
        return (value != null) ? value : defaultValue;
    }

    public static void setValue(String key, String keyValue) {
        try {
            if (file == null) {
                properties = new Properties();
                String fullPath = SystemHelpers.getCurrentDir() + DEFAULT_PROPERTIES_FILE;

                try (FileInputStream fis = new FileInputStream(fullPath)) {
                    properties.load(fis);
                }

                linkFile = fullPath;  // Update linkFile for writing
            }

            // Write to same properties file
            try (FileOutputStream out = new FileOutputStream(linkFile)) {
                System.out.println("[PropertiesHelpers] Setting value '" + keyValue + "' for key '" + key + "' in file: " + linkFile);
                properties.setProperty(key, keyValue);
                properties.store(out, null);
            }

        } catch (IOException e) {
            System.err.println("[PropertiesHelpers] Error setting value: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void setValue(String filePropertiesRelativePath, String key, String keyValue) {
        String fullPath = SystemHelpers.getCurrentDir() + filePropertiesRelativePath;

        try {
            Properties tempProperties = new Properties();

            // Load existing properties
            try (FileInputStream fis = new FileInputStream(fullPath)) {
                tempProperties.load(fis);
            }

            // Update and save
            tempProperties.setProperty(key, keyValue);

            try (FileOutputStream out = new FileOutputStream(fullPath)) {
                tempProperties.store(out, null);
            }

            System.out.println("[PropertiesHelpers] Set value '" + keyValue + "' to file " + fullPath);

        } catch (IOException e) {
            System.err.println("[PropertiesHelpers] Error setting value to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void reload() {
        System.out.println("[PropertiesHelpers] Reloading properties files...");
        loadAllFiles();
    }
}
