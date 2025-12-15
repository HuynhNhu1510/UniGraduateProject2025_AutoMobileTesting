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
    public static final String TIMEOUT_PAGE_LOAD = PropertiesHelpers.getValue("TIMEOUT_PAGE_LOAD");
    public static final String STEP_ACTION_TIMEOUT = PropertiesHelpers.getValue("STEP_ACTION_TIMEOUT");

    public static final String APPIUM_DRIVER_LOCAL_SERVICE = PropertiesHelpers.getValue("APPIUM_DRIVER_LOCAL_SERVICE");

    public static final String LOG_LEVEL = PropertiesHelpers.getValue("LOG_LEVEL");
    public static final String LOG_ENABLE_DEBUG = PropertiesHelpers.getValue("LOG_ENABLE_DEBUG");

    public static final String SCREENSHOT_FAIL = PropertiesHelpers.getValue("SCREENSHOT_FAIL");
    public static final String SCREENSHOT_PASS = PropertiesHelpers.getValue("SCREENSHOT_PASS");
    public static final String SCREENSHOT_ALL = PropertiesHelpers.getValue("SCREENSHOT_ALL");
    public static final String SCREENSHOT_PATH = PropertiesHelpers.getValue("SCREENSHOT_PATH");
    public static final String RECORD_VIDEO = PropertiesHelpers.getValue("RECORD_VIDEO");
    public static final String RECORD_VIDEO_PATH = PropertiesHelpers.getValue("RECORD_VIDEO_PATH");

    public static final String EXTENT_REPORT_PATH = PropertiesHelpers.getValue("EXTENT_REPORT_PATH");
    public static final String ALLURE_REPORT_PATH = PropertiesHelpers.getValue("ALLURE_REPORT_PATH");
}
