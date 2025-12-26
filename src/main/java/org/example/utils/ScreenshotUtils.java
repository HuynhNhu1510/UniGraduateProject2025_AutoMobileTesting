package org.example.utils;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.drivers.DriverManager;
import org.example.helpers.PropertiesHelpers;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_PATH = PropertiesHelpers.getValue("SCREENSHOT_PATH");

    public static String takeScreenshot(String screenshotName) {
        try {
            // Create screenshot directory if not exists
            File screenshotDir = new File(SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String fullPath = SCREENSHOT_PATH + File.separator + fileName;

            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(fullPath);

            // Copy file
            FileUtils.copyFile(source, destination);

            logger.info("Screenshot saved: {}", fullPath);
            return fullPath;

        } catch (IOException e) {
            logger.error("Failed to take screenshot: {}", e.getMessage());
            return null;
        }
    }

    @Attachment(value = "{screenshotName}", type = "image/png")
    public static byte[] takeScreenshotForAllure(String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            logger.debug("Screenshot attached to Allure: {}", screenshotName);
            return screenshot;
        } catch (Exception e) {
            logger.error("Failed to take screenshot for Allure: {}", e.getMessage());
            return new byte[0];
        }
    }

    public static void takeScreenshotIfEnabled(String screenshotName, boolean testPassed) {
        boolean screenshotAll = Boolean.parseBoolean(
                PropertiesHelpers.getValue("SCREENSHOT_ALL")
        );
        boolean screenshotPass = Boolean.parseBoolean(
                PropertiesHelpers.getValue("SCREENSHOT_PASS")
        );
        boolean screenshotFail = Boolean.parseBoolean(
                PropertiesHelpers.getValue("SCREENSHOT_FAIL")
        );

        if (screenshotAll || (testPassed && screenshotPass) || (!testPassed && screenshotFail)) {
            takeScreenshot(screenshotName);
            takeScreenshotForAllure(screenshotName);
        }
    }
}
