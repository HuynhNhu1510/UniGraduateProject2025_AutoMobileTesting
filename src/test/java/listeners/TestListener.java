package listeners;

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

import java.io.ByteArrayInputStream;

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
        logger.info("✓ Test PASSED: {}", testName);

        captureScreenshotOnSuccess(result);
        ScreenshotUtils.takeScreenshot(testName + "_PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("✗ Test FAILED: {}", testName);

        // Capture screenshot - CHI 1 LAN
        captureScreenshotOnFailure(testName);

        // Attach error details
        attachErrorDetails(result);
        attachStackTrace(result);

        // Save to file system
        ScreenshotUtils.takeScreenshot(testName + "_FAILED");
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

    @Attachment(value = "Test Failed - {0}", type = "image/png")
    private byte[] captureScreenshotOnFailure(String testName) {
        try {
            logger.debug("Capturing screenshot for failed test: {}", testName);

            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

            logger.debug("Screenshot captured and attached to Allure Report");
            return screenshot;

        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }

    @Attachment(value = "Success Screenshot", type = "image/png")
    private byte[] captureScreenshotOnSuccess(ITestResult result) {
        try {
            String testName = result.getMethod().getMethodName();
            logger.debug("Capturing screenshot for passed test: {}", testName);

            TakesScreenshot ts = (TakesScreenshot) DriverManager.getDriver();
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);

            logger.debug("Success screenshot captured");
            return screenshot;

        } catch (Exception e) {
            logger.error("Failed to capture success screenshot: {}", e.getMessage());
            return new byte[0];
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
