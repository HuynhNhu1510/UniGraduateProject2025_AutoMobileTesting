package listeners;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.drivers.DriverManager;
import org.example.utils.ScreenshotUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Started: {}", context.getName());
        logger.info("========================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Finished: {}", context.getName());
        logger.info("Total Tests Run: {}", context.getAllTestMethods().length);
        logger.info("Passed: {}", context.getPassedTests().size());
        logger.info("Failed: {}", context.getFailedTests().size());
        logger.info("Skipped: {}", context.getSkippedTests().size());
        logger.info("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info(">>> Starting Test: {} <<<", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testClass = result.getTestClass().getRealClass().getSimpleName();
        logger.info("Test PASSED: {}", testName);

        // Best practice: Include class name to avoid conflicts in parallel execution
        String screenshotName = testClass + "." + testName + "_PASSED";

        // Single screenshot call - combines both Allure and file system
        captureAndSaveScreenshot(screenshotName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String testClass = result.getTestClass().getRealClass().getSimpleName();
        logger.error("Test FAILED: {}", testName);

        // Best practice: Include class name to avoid conflicts in parallel execution
        String screenshotName = testClass + "." + testName + "_FAILED";

        // Single screenshot call - combines both Allure and file system
        captureAndSaveScreenshot(screenshotName);

        // Attach error details
        attachErrorDetails(result);
        attachStackTrace(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test SKIPPED: {}", testName);

        if (result.getThrowable() != null) {
            logger.warn("Reason: {}", result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("Test failed but within success percentage: {}", testName);
    }

    @Attachment(value = "Screenshot: {0}", type = "image/png")
    private byte[] captureAndSaveScreenshot(String screenshotName) {
        try {
            // NULL CHECK - Best practice from Appium community
            AppiumDriver driver = DriverManager.getDriver();
            if (driver == null) {
                logger.error("Driver is null, cannot take screenshot: {}", screenshotName);
                return null;
            }

            logger.debug("Capturing screenshot: {}", screenshotName);

            // Take screenshot for Allure (byte array)
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

            // Also save to file system
            ScreenshotUtils.takeScreenshot(screenshotName);

            logger.debug("Screenshot captured and attached: {}", screenshotName);
            return screenshot;

        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage(), e);
            // Return null instead of empty byte array - Best practice from Allure GitHub
            return null;
        }
    }

    @Attachment(value = "Error Details", type = "text/plain")
    private String attachErrorDetails(ITestResult result) {
        StringBuilder errorDetails = new StringBuilder();

        errorDetails.append("Test Name: ").append(result.getMethod().getMethodName()).append("\n");
        errorDetails.append("Test Class: ").append(result.getTestClass().getName()).append("\n");
        errorDetails.append("Status: FAILED\n");
        errorDetails.append("Execution Time: ").append(result.getEndMillis() - result.getStartMillis()).append(" ms\n");

        if (result.getThrowable() != null) {
            errorDetails.append("\nError Message:\n");
            errorDetails.append(result.getThrowable().getMessage()).append("\n");
        }
        String details = errorDetails.toString();
        logger.debug("Error details attached to Allure");
        return details;
    }

    @Attachment(value = "Stack Trace", type = "text/plain")
    private String attachStackTrace(ITestResult result) {
        if (result.getThrowable() != null) {
            StringBuilder stackTrace = new StringBuilder();
            stackTrace.append("Exception: ").append(result.getThrowable().getClass().getName()).append("\n\n");

            for (StackTraceElement element : result.getThrowable().getStackTrace()) {
                stackTrace.append("\tat ").append(element.toString()).append("\n");
            }

            // Include cause if present
            Throwable cause = result.getThrowable().getCause();
            if (cause != null) {
                stackTrace.append("\nCaused by: ").append(cause.getClass().getName()).append("\n");
                stackTrace.append("Message: ").append(cause.getMessage()).append("\n");
                for (StackTraceElement element : cause.getStackTrace()) {
                    stackTrace.append("\tat ").append(element.toString()).append("\n");
                }
            }

            logger.debug("Stack trace attached to Allure");
            return stackTrace.toString();
        }
        return "No stack trace available";
    }
}
