package org.example.keywords;

import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.constants.ConfigData;
import org.example.drivers.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;

import static org.example.drivers.DriverManager.getDriver;

public class MobileUI {
    private static final Logger logger = LogManager.getLogger(MobileUI.class);

    private static final int DEFAULT_TIMEOUT = ConfigData.TIMEOUT_EXPLICIT_DEFAULT != null
            ? Integer.parseInt(ConfigData.TIMEOUT_EXPLICIT_DEFAULT)
            : 1;
    private static final int POLLING_INTERVAL_MS = 100;
    private static final boolean DEBUG_MODE = false;

    public static void sleep(double second) {
        logger.debug("Sleeping for {} seconds", second);
        try {
            Thread.sleep((long) (1000 * second));
        } catch (InterruptedException e) {
            logger.error("Sleep interrupted", e);
            throw new RuntimeException(e);
        }
    }

    public static void swipe(int startX, int startY, int endX, int endY, int durationMillis) {
        logger.debug("Executing swipe from ({},{}) to ({},{}) with duration {}ms",
                startX, startY, endX, endY, durationMillis);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(0));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMillis), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(0));
        getDriver().perform(Collections.singletonList(swipe));
    }

    public static void swipeLeft() {
        logger.debug("Executing swipeLeft");
        Dimension size = getDriver().manage().window().getSize();
        int startX = (int) (size.width * 0.8);
        int startY = (int) (size.height * 0.3);
        int endX = (int) (size.width * 0.2);
        int endY = startY;
        int duration = 100;
        swipe(startX, startY, endX, endY, duration);
    }

    public static void swipeRight() {
        logger.debug("Executing swipeRight");
        Dimension size = getDriver().manage().window().getSize();
        int startX = (int) (size.width * 0.2);
        int startY = (int) (size.height * 0.3);
        int endX = (int) (size.width * 0.8);
        int endY = startY;
        int duration = 200;
        swipe(startX, startY, endX, endY, duration);
    }

    private static Point getCenterOfElement(Point location, Dimension size) {
        return new Point(location.getX() + size.getWidth() / 2,
                location.getY() + size.getHeight() / 2);
    }

    public static void tap(WebElement element) {
        logger.debug("Executing tap on element: {}", element);
        Point location = element.getLocation();
        Dimension size = element.getSize();
        Point centerOfElement = getCenterOfElement(location, size);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(500)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        getDriver().perform(Collections.singletonList(sequence));
    }

    public static void tap(int x, int y) {
        logger.debug("Executing tap at coordinates ({},{}) with 200ms pause", x, y);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(200)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        getDriver().perform(List.of(tap));
    }

    public static void tap(int x, int y, int milliSecondDuration) {
        logger.debug("Executing tap at coordinates ({},{}) with pause {}ms", x, y, milliSecondDuration);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(milliSecondDuration)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        getDriver().perform(Collections.singletonList(tap));
    }

    public static void zoom(WebElement element, double scale) {
        logger.debug("Executing zoom on element: {} with approximate scale factor: {}", element, scale);
        int centerX = element.getLocation().getX() + element.getSize().getWidth() / 2;
        int centerY = element.getLocation().getY() + element.getSize().getHeight() / 2;
        int distance = 100;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence zoom = new Sequence(finger1, 1);
        zoom.addAction(finger1.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX - distance, centerY));
        zoom.addAction(finger1.createPointerDown(0));

        Sequence zoom2 = new Sequence(finger2, 1);
        zoom2.addAction(finger2.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), centerX + distance, centerY));
        zoom2.addAction(finger2.createPointerDown(0));

        int moveDuration = 50;
        int steps = 10;
        int startDist1X = centerX - distance;
        int startDist2X = centerX + distance;
        int endDist1X, endDist2X;

        if (scale > 1) {
            logger.trace("Zooming In");
            endDist1X = centerX - (int) (distance * scale);
            endDist2X = centerX + (int) (distance * scale);
        } else {
            logger.trace("Zooming Out");
            endDist1X = centerX - (int) (distance * scale);
            endDist2X = centerX + (int) (distance * scale);
        }

        for (int i = 1; i <= steps; i++) {
            int currentX1 = startDist1X + (endDist1X - startDist1X) * i / steps;
            int currentX2 = startDist2X + (endDist2X - startDist2X) * i / steps;
            zoom.addAction(finger1.createPointerMove(Duration.ofMillis(moveDuration), PointerInput.Origin.viewport(), currentX1, centerY));
            zoom2.addAction(finger2.createPointerMove(Duration.ofMillis(moveDuration), PointerInput.Origin.viewport(), currentX2, centerY));
        }

        zoom.addAction(finger1.createPointerUp(0));
        zoom2.addAction(finger2.createPointerUp(0));

        getDriver().perform(Arrays.asList(zoom, zoom2));
    }

    public static void scroll(int startX, int startY, int endX, int endY, int durationMillis) {
        logger.debug("Executing scroll from ({},{}) to ({},{}) with duration {}ms",
                startX, startY, endX, endY, durationMillis);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(0));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(durationMillis), PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(0));
        getDriver().perform(Collections.singletonList(swipe));
    }

    public static void scrollGestureCommand() {
        Map<String, Object> scrollParams = new HashMap<>();
        scrollParams.put("left", 670);
        scrollParams.put("top", 500);
        scrollParams.put("width", 200);
        scrollParams.put("height", 2000);
        scrollParams.put("direction", "down");
        scrollParams.put("percent", 1);

        logger.debug("Executing scrollGesture command with params: {}", scrollParams);
        getDriver().executeScript("mobile: scrollGesture", scrollParams);
    }

    @Step("Click element")
    public static void clickElement(By locator, int second) {
        logger.debug("Clicking element located by: {} within {}s", locator, second);
        waitForElementToBeClickable(locator, second).click();
    }

    @Step("Click element")
    public static void clickElement(By locator) {
        logger.debug("Clicking element located by: {} within default timeout ({}s)", locator, DEFAULT_TIMEOUT);
        waitForElementToBeClickable(locator).click();
    }

    @Step("Click element")
    public static void clickElement(WebElement element, int second) {
        logger.debug("Clicking element: {} within {}s", element, second);
        waitForElementToBeClickable(element, second).click();
    }

    @Step("Click element")
    public static void clickElement(WebElement element) {
        logger.debug("Clicking element: {} within default timeout ({}s)", element, DEFAULT_TIMEOUT);
        waitForElementToBeClickable(element).click();
    }

    @Step("Set text: {text}")
    public static void setText(By locator, String text) {
        logger.debug("Setting text '{}' on element located by: {} with default timeout", text, locator);
        WebElement element = waitForElementVisibe(locator);
        element.click();
        element.clear();
        element.sendKeys(text);
        logger.trace("Set text completed for locator: {}", locator);
    }

    @Step("Set text: {text}")
    public static void setText(By locator, String text, int second) {
        logger.debug("Setting text '{}' on element located by: {} with timeout {}s", text, locator, second);
        WebElement element = waitForElementVisibe(locator, second);
        element.click();
        element.clear();
        element.sendKeys(text);
        logger.trace("Set text completed for locator: {}", locator);
    }

    @Step("Set text: {text}")
    public static void setText(WebElement element, String text) {
        logger.debug("Setting text '{}' on element: {} with default timeout", text, element);
        WebElement elm = waitForElementVisibe(element);
        elm.click();
        elm.clear();
        elm.sendKeys(text);
        logger.trace("Set text completed for element");
    }

    @Step("Set text: {text}")
    public static void setText(WebElement element, String text, int second) {
        logger.debug("Setting text '{}' on element: {} with timeout {}s", text, element, second);
        WebElement elm = waitForElementVisibe(element, second);
        elm.click();
        elm.clear();
        elm.sendKeys(text);
        logger.trace("Set text completed for element");
    }

    @Step("Clear text")
    public static void clearText(By locator) {
        logger.debug("Clearing text on element located by: {} with default timeout", locator);
        WebElement element = waitForElementVisibe(locator);
        element.click();
        element.clear();
        logger.trace("Clear text completed for locator: {}", locator);
    }

    @Step("Clear text")
    public static void clearText(By locator, int second) {
        logger.debug("Clearing text on element located by: {} with timeout {}s", locator, second);
        WebElement element = waitForElementVisibe(locator, second);
        element.click();
        element.clear();
        logger.trace("Clear text completed for locator: {}", locator);
    }

    @Step("Clear text")
    public static void clearText(WebElement element) {
        logger.debug("Clearing text on element: {} with default timeout", element);
        WebElement elm = waitForElementVisibe(element);
        elm.click();
        elm.clear();
        logger.trace("Clear text completed for element");
    }

    @Step("Clear text")
    public static void clearText(WebElement element, int second) {
        logger.debug("Clearing text on element: {} with timeout {}s", element, second);
        WebElement elm = waitForElementVisibe(element, second);
        elm.click();
        elm.clear();
        logger.trace("Clear text completed for element");
    }

    public static String getElementText(By locator) {
        logger.debug("Getting text from element located by: {} with default timeout", locator);
        WebElement element = waitForElementVisibe(locator);
        String text = element.getText();
        logger.trace("Retrieved text: '{}'", text);
        return text;
    }

    public static String getElementText(By locator, int second) {
        logger.debug("Getting text from element located by: {} with timeout {}s", locator, second);
        WebElement element = waitForElementVisibe(locator, second);
        String text = element.getText();
        logger.trace("Retrieved text: '{}'", text);
        return text;
    }

    public static String getElementText(WebElement element) {
        logger.debug("Getting text from element: {} with default timeout", element);
        WebElement elm = waitForElementVisibe(element);
        String text = elm.getText();
        logger.trace("Retrieved text: '{}'", text);
        return text;
    }

    public static String getElementText(WebElement element, int second) {
        logger.debug("Getting text from element: {} with timeout {}s", element, second);
        WebElement elm = waitForElementVisibe(element, second);
        String text = elm.getText();
        logger.trace("Retrieved text: '{}'", text);
        return text;
    }

    public static String getElementAttribute(By locator, String attribute) {
        logger.debug("Getting attribute '{}' from element located by: {} with default timeout", attribute, locator);
        WebElement element = waitForElementVisibe(locator);
        String value = element.getAttribute(attribute);
        logger.trace("Retrieved attribute value: '{}'", value);
        return value;
    }

    public static String getElementAttribute(By locator, String attribute, int second) {
        logger.debug("Getting attribute '{}' from element located by: {} with timeout {}s", attribute, locator, second);
        WebElement element = waitForElementVisibe(locator, second);
        String value = element.getAttribute(attribute);
        logger.trace("Retrieved attribute value: '{}'", value);
        return value;
    }

    public static String getElementAttribute(WebElement element, String attribute) {
        logger.debug("Getting attribute '{}' from element: {} with default timeout", attribute, element);
        WebElement elm = waitForElementVisibe(element);
        String value = elm.getAttribute(attribute);
        logger.trace("Retrieved attribute value: '{}'", value);
        return value;
    }

    public static String getElementAttribute(WebElement element, String attribute, int second) {
        logger.debug("Getting attribute '{}' from element: {} with timeout {}s", attribute, element, second);
        WebElement elm = waitForElementVisibe(element, second);
        String value = elm.getAttribute(attribute);
        logger.trace("Retrieved attribute value: '{}'", value);
        return value;
    }

    public static boolean isElementPresentAndDisplayed(WebElement element) {
        logger.trace("Checking if element is present and displayed: {}", element);
        boolean result;
        try {
            result = element != null && element.isDisplayed();
            logger.trace("Element present and displayed check result: {}", result);
            return result;
        } catch (NoSuchElementException e) {
            logger.trace("Element not found during presence/display check: {} - {}", element, e.getMessage());
            return false;
        } catch (org.openqa.selenium.WebDriverException e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("socket hang up") ||
                    errorMsg.contains("instrumentation process is not running") ||
                    errorMsg.contains("session not created"))) {
                logger.error("CRITICAL ERROR: WebDriver session issue detected!");
                logger.error("Error: {}", errorMsg);
                logger.error("This may require session restart or emulator restart!");
                throw e;
            } else {
                logger.trace("WebDriver error (non-critical): {}", errorMsg);
                return false;
            }
        } catch (Exception e) {
            logger.trace("An error occurred checking presence/display for element: {} - {}", element, e.getMessage());
            return false;
        }
    }

    public static boolean isElementPresentAndDisplayed(By locator) {
        logger.trace("Checking if element is present and displayed: {}", locator);
        try {
            WebElement element = getDriver().findElement(locator);
            boolean result = element.isDisplayed();
            logger.trace("Element present and displayed check result: {} for locator: {}", result, locator);
            return result;
        } catch (NoSuchElementException e) {
            logger.trace("Element not found during presence/display check: {} - {}", locator, e.getMessage());
            return false;
        } catch (org.openqa.selenium.WebDriverException e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && (errorMsg.contains("socket hang up") ||
                    errorMsg.contains("instrumentation process is not running") ||
                    errorMsg.contains("session not created"))) {
                logger.error("CRITICAL ERROR: WebDriver session issue detected!");
                logger.error("Error: {}", errorMsg);
                logger.error("This may require session restart or emulator restart!");
                throw e;
            } else {
                logger.trace("WebDriver error (non-critical): {}", errorMsg);
                return false;
            }
        } catch (Exception e) {
            logger.trace("An error occurred checking presence/display for element: {} - {}", locator, e.getMessage());
            return false;
        }
    }

    public static boolean isElementEnabled(WebElement element) {
        logger.trace("Checking if element is enabled: {}", element);
        boolean result;
        try {
            result = element != null && element.isEnabled();
            logger.trace("Element enabled check result: {}", result);
            return result;
        } catch (Exception e) {
            logger.trace("An error occurred checking enabled status for element: {} - {}", element, e.getMessage());
            return false;
        }
    }

    public static boolean isElementEnabled(By locator) {
        logger.trace("Checking if element is enabled: {}", locator);
        boolean result;
        try {
            WebElement element = waitForElementVisibe(locator);
            result = element != null && element.isEnabled();
            logger.trace("Element enabled check result: {} for locator: {}", result, locator);
            return result;
        } catch (Exception e) {
            logger.trace("An error occurred checking enabled status for locator: {} - {}", locator, e.getMessage());
            return false;
        }
    }

    public static boolean isElementSelected(WebElement element) {
        logger.trace("Checking if element is selected: {}", element);
        boolean result;
        try {
            result = element != null && element.isSelected();
            logger.trace("Element selected check result: {}", result);
            return result;
        } catch (Exception e) {
            logger.trace("An error occurred checking selected status for element: {} - {}", element, e.getMessage());
            return false;
        }
    }

    public static boolean isElementSelected(By locator) {
        logger.trace("Checking if element is selected: {}", locator);
        boolean result;
        try {
            WebElement element = waitForElementVisibe(locator);
            result = element != null && element.isSelected();
            logger.trace("Element selected check result: {} for locator: {}", result, locator);
            return result;
        } catch (Exception e) {
            logger.trace("An error occurred checking selected status for locator: {} - {}", locator, e.getMessage());
            return false;
        }
    }


    // Các hàm verify (sử dụng Assert và gọi lại các hàm is)

    public static void verifyElementPresentAndDisplayed(WebElement element, String message) {
        logger.debug("Verifying element is present and displayed: {}. Message if failed: {}", element, message);
        Assert.assertTrue(isElementPresentAndDisplayed(element), message);
    }

    public static void verifyElementPresentAndDisplayed(By locator, String message) {
        logger.debug("Verifying element is present and displayed: {}. Message if failed: {}", locator, message);
        Assert.assertTrue(isElementPresentAndDisplayed(locator), message);
    }

    public static void verifyElementEnabled(WebElement element, String message) {
        logger.debug("Verifying element is enabled: {}. Message if failed: {}", element, message);
        Assert.assertTrue(isElementEnabled(element), message);
    }

    public static void verifyElementEnabled(By locator, String message) {
        logger.debug("Verifying element is enabled: {}. Message if failed: {}", locator, message);
        Assert.assertTrue(isElementEnabled(locator), message);
    }

    public static void verifyElementSelected(WebElement element, String message) {
        logger.debug("Verifying element is selected: {}. Message if failed: {}", element, message);
        Assert.assertTrue(isElementSelected(element), message);
    }

    public static void verifyElementSelected(By locator, String message) {
        logger.debug("Verifying element is selected: {}. Message if failed: {}", locator, message);
        Assert.assertTrue(isElementSelected(locator), message);
    }

    public static void verifyElementText(WebElement element, String expectedText, String message) {
        logger.debug("Verifying text of element: {} equals '{}'. Message if failed: {}", element, expectedText, message);
        Assert.assertEquals(getElementText(element), expectedText, message);
    }

    public static void verifyElementText(By locator, String expectedText, String message) {
        logger.debug("Verifying text of element: {} equals '{}'. Message if failed: {}", locator, expectedText, message);
        Assert.assertEquals(getElementText(locator), expectedText, message);
    }

    public static void verifyElementAttribute(WebElement element, String attribute, String expectedValue, String message) {
        logger.debug("Verifying attribute '{}' of element: {} equals '{}'. Message if failed: {}",
                attribute, element, expectedValue, message);
        Assert.assertEquals(getElementAttribute(element, attribute), expectedValue, message);
    }

    public static void verifyElementAttribute(By locator, String attribute, String expectedValue, String message) {
        logger.debug("Verifying attribute '{}' of element: {} equals '{}'. Message if failed: {}",
                attribute, locator, expectedValue, message);
        Assert.assertEquals(getElementAttribute(locator, attribute), expectedValue, message);
    }

    public static void assertTrueCondition(boolean condition, String message) {
        logger.debug("Asserting condition: {}. Message if failed: {}", condition, message);
        Assert.assertTrue(condition, message);
        if (DEBUG_MODE) {
            logger.trace("Assertion passed");
        }
    }


    // --- Wait Methods ---

    public static WebElement waitForElementToBeClickable(By locator, int timeout) {
        logger.trace("Waiting up to {}s for element to be clickable: {}", timeout, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementToBeClickable(By locator) {
        logger.trace("Waiting up to {}s (default) for element to be clickable: {}", DEFAULT_TIMEOUT, locator);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForElementToBeClickable(WebElement element, int timeout) {
        logger.trace("Waiting up to {}s for element to be clickable: {}", timeout, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForElementToBeClickable(WebElement element) {
        logger.trace("Waiting up to {}s (default) for element to be clickable: {}", DEFAULT_TIMEOUT, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForElementVisibe(By locator, int timeout) {
        logger.trace("Waiting up to {}s for element to be visible: {}", timeout, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementVisibe(By locator) {
        logger.trace("Waiting up to {}s (default) for element to be visible: {}", DEFAULT_TIMEOUT, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        wait.pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForElementVisibe(WebElement element, int timeout) {
        logger.trace("Waiting up to {}s for element to be visible: {}", timeout, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForElementVisibe(WebElement element) {
        logger.trace("Waiting up to {}s (default) for element to be visible: {}", DEFAULT_TIMEOUT, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static boolean waitForElementInvisibe(By locator, int timeout) {
        logger.trace("Waiting up to {}s for element to be invisible: {}", timeout, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForElementInvisibe(By locator) {
        logger.trace("Waiting up to {}s (default) for element to be invisible: {}", DEFAULT_TIMEOUT, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForElementInvisibe(WebElement element, int timeout) {
        logger.trace("Waiting up to {}s for element to be invisible: {}", timeout, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public static boolean waitForElementInvisibe(WebElement element) {
        logger.trace("Waiting up to {}s (default) for element to be invisible: {}", DEFAULT_TIMEOUT, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public static WebElement waitForElementPresent(By locator, int timeout) {
        logger.trace("Waiting up to {}s for element to be present in DOM: {}", timeout, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static WebElement waitForElementPresent(By locator) {
        logger.trace("Waiting up to {}s (default) for element to be present in DOM: {}", DEFAULT_TIMEOUT, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForTextToBePresent(By locator, String text, int timeout) {
        logger.trace("Waiting up to {}s for text '{}' to be present in element: {}", timeout, text, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForTextToBePresent(By locator, String text) {
        logger.trace("Waiting up to {}s (default) for text '{}' to be present in element: {}", DEFAULT_TIMEOUT, text, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForTextToBePresent(WebElement element, String text, int timeout) {
        logger.trace("Waiting up to {}s for text '{}' to be present in element: {}", timeout, text, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public static boolean waitForTextToBePresent(WebElement element, String text) {
        logger.trace("Waiting up to {}s (default) for text '{}' to be present in element: {}", DEFAULT_TIMEOUT, text, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public static boolean waitForAttributeToBe(By locator, String attribute, String value, int timeout) {
        logger.trace("Waiting up to {}s for attribute '{}' to be '{}' in element: {}", timeout, attribute, value, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    public static boolean waitForAttributeToBe(By locator, String attribute, String value) {
        logger.trace("Waiting up to {}s (default) for attribute '{}' to be '{}' in element: {}",
                DEFAULT_TIMEOUT, attribute, value, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    public static boolean waitForAttributeToBe(WebElement element, String attribute, String value, int timeout) {
        logger.trace("Waiting up to {}s for attribute '{}' to be '{}' in element: {}", timeout, attribute, value, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public static boolean waitForAttributeToBe(WebElement element, String attribute, String value) {
        logger.trace("Waiting up to {}s (default) for attribute '{}' to be '{}' in element: {}",
                DEFAULT_TIMEOUT, attribute, value, element);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }

    public static List<WebElement> waitForNumberOfElements(By locator, int expectedCount, int timeout) {
        logger.trace("Waiting up to {}s for number of elements to be {} for locator: {}", timeout, expectedCount, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
    }

    public static List<WebElement> waitForNumberOfElements(By locator, int expectedCount) {
        logger.trace("Waiting up to {}s (default) for number of elements to be {} for locator: {}",
                DEFAULT_TIMEOUT, expectedCount, locator);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
    }

    public static boolean waitForUrlContains(String text, int timeout) {
        logger.trace("Waiting up to {}s for URL to contain: '{}'", timeout, text);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.urlContains(text));
    }

    public static boolean waitForUrlContains(String text) {
        logger.trace("Waiting up to {}s (default) for URL to contain: '{}'", DEFAULT_TIMEOUT, text);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.urlContains(text));
    }

    public static boolean waitForNumberOfWindows(int expectedWindows, int timeout) {
        logger.trace("Waiting up to {}s for number of windows to be: {}", timeout, expectedWindows);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.numberOfWindowsToBe(expectedWindows));
    }

    public static boolean waitForNumberOfWindows(int expectedWindows) {
        logger.trace("Waiting up to {}s (default) for number of windows to be: {}", DEFAULT_TIMEOUT, expectedWindows);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.numberOfWindowsToBe(expectedWindows));
    }
}
