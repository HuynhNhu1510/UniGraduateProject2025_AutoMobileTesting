package org.example.constants;

import org.example.helpers.PropertiesHelpers;
import org.example.helpers.SystemHelpers;

public class ConfigData {

    private ConfigData() {
        // Ngăn chặn khởi tạo class
    }

    // Load all properties files
    static {
        PropertiesHelpers.loadAllFiles();
    }

    public static final String PROJECT_PATH = SystemHelpers.getCurrentDir();
    public static final String EXCEL_DATA_FILE_PATH = PropertiesHelpers.getValue("EXCEL_DATA_FILE_PATH");
    public static final String JSON_DATA_FILE_PATH = PropertiesHelpers.getValue("JSON_DATA_FILE_PATH");
    public static final String JSON_CONFIG_FILE_PATH = PropertiesHelpers.getValue("JSON_CONFIG_FILE_PATH");
    public static final String TEST_DATA_FOLDER_PATH = PropertiesHelpers.getValue("TEST_DATA_FOLDER_PATH");
    public static final String LOCATE = PropertiesHelpers.getValue("LOCATE");
    public static final String TIMEOUT_SERVICE = PropertiesHelpers.getValue("TIMEOUT_SERVICE");
    public static final String TIMEOUT_EXPLICIT_DEFAULT = PropertiesHelpers.getValue("TIMEOUT_EXPLICIT_DEFAULT");
    public static final String APPIUM_DRIVER_LOCAL_SERVICE = PropertiesHelpers.getValue("APPIUM_DRIVER_LOCAL_SERVICE");
}
