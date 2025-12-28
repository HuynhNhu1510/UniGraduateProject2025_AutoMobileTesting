package common;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import org.example.constants.ConfigData;
import org.example.drivers.DriverManager;
import org.example.helpers.SystemHelpers;
import org.testng.annotations.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    private AppiumDriverLocalService service;

    @BeforeSuite
    public void runAppiumServer() {
        String host = ConfigData.APPIUM_HOST;
        String port = ConfigData.APPIUM_PORT;
        int timeoutService = Integer.parseInt(ConfigData.TIMEOUT_SERVICE);

        logger.info("Starting Appium server on {}:{}", host, port);

        //Kill process on port
        SystemHelpers.killProcessOnPort(port);

        //Build the Appium service
        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress(host);
        builder.usingPort(Integer.parseInt(port));
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, ConfigData.LOG_LEVEL);
        builder.withTimeout(Duration.ofSeconds(timeoutService));

        // Start the server with the builder
        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        if (service.isRunning()) {
            logger.info("##### Appium server started on {}:{}", host, port);
        } else {
            logger.error("Failed to start Appium server.");
        }
    }

    @BeforeTest
    public void setUpDriver() {
        String host = ConfigData.APPIUM_HOST;
        String port = ConfigData.APPIUM_PORT;

        AppiumDriver driver;
        UiAutomator2Options options = new UiAutomator2Options();

        System.out.println("***SERVER ADDRESS: " + host);
        System.out.println("***SERVER PORT: " + port);

        options.setPlatformName(ConfigData.PLATFORM_NAME);
        options.setPlatformVersion(ConfigData.PLATFORM_VERSION);
        options.setAutomationName(ConfigData.AUTOMATION_NAME);
        options.setDeviceName(ConfigData.DEVICE_NAME);
        options.setAppPackage(ConfigData.APP_PACKAGE);
        options.setAppActivity(ConfigData.APP_ACTIVITY);
        options.setNoReset(Boolean.parseBoolean(ConfigData.NO_RESET));
        options.setFullReset(Boolean.parseBoolean(ConfigData.FULL_RESET));

        try {
            driver = new AppiumDriver(new URL("http://" + host + ":" + port), options);
            DriverManager.setDriver(driver);
            logger.info("Driver initialized with EXPLICIT WAIT strategy only");
        } catch (MalformedURLException e) {
            logger.error("Failed to initialize driver", e);
            throw new RuntimeException(e);
        }
    }

    @AfterTest
    public void tearDownDriver() {
        if (DriverManager.getDriver() != null) {
            logger.info("Quitting driver...");
            DriverManager.quitDriver();
        }
    }

    @AfterSuite
    public void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            logger.info("##### Appium server stopped.");
        }
        DriverManager.cleanup();
    }
}
