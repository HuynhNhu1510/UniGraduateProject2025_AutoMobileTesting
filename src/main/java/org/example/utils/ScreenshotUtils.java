package org.example.utils;

import io.appium.java_client.AppiumDriver;
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
            // NULL CHECK - Best practice from Appium community
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) {
                logger.error("Driver is null, cannot take screenshot");
                return null;
            }

            // Create screenshot directory if not exists
            File screenshotDir = new File(SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                boolean created = screenshotDir.mkdirs();
                if (!created) {
                    logger.error("Failed to create screenshot directory: {}", SCREENSHOT_PATH);
                    return null;
                }
            }

            // Generate timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String fullPath = SCREENSHOT_PATH + File.separator + fileName;

            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(fullPath);

            // Copy file
            FileUtils.copyFile(source, destination);

            logger.info("Screenshot saved: {}", fullPath);
            return fullPath;

        } catch (IOException e) {
            logger.error("Failed to take screenshot (IO error): {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Failed to take screenshot: {}", e.getMessage(), e);
            return null;
        }
    }

    @Attachment(value = "{screenshotName}", type = "image/png")
    public static byte[] takeScreenshotForAllure(String screenshotName) {
        try {
            // NULL CHECK - Best practice from Appium community
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) {
                logger.error("Driver is null, cannot take screenshot for Allure");
                return null;
            }

            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            logger.debug("Screenshot attached to Allure: {}", screenshotName);
            return screenshot;
        } catch (Exception e) {
            logger.error("Failed to take screenshot for Allure: {}", e.getMessage(), e);
            // Return null instead of empty byte array - Best practice from Allure GitHub
            return null;
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
