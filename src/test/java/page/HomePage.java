package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);

    public HomePage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
        logger.debug("HomePage initialized");
    }

    @AndroidFindBy(accessibility = "Register")
    private WebElement registerButton;

    @AndroidFindBy(accessibility = "Sign in")
    private WebElement signInButton;

    @AndroidFindBy(accessibility = "Sign up for an improved experience and insider pricing. " +
            "If you already have an account, sign in instead.")
    private WebElement contentDescHomePage;

    @AndroidFindBy(accessibility = "Shop Recently Added")
    private WebElement shopRecentlyButton;

    // ===== Action Methods =====

    @Step("Click Register button")
    public RegisterPage clickRegisterButton() {
        logger.info("Clicking Register button");
        MobileUI.clickElement(registerButton);
        logger.debug("Navigated to Register page");
        return new RegisterPage();
    }

    @Step("Click Sign In button")
    public LoginPage clickSignInButton() {
        logger.info("Clicking Sign In button");
        MobileUI.clickElement(signInButton);
        logger.debug("Navigated to Login page");
        return new LoginPage();
    }

    @Step("Check if Register button is displayed")
    public boolean isRegisterButtonDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(registerButton);
        logger.debug("Register button displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Check if user is NOT logged in")
    public boolean isNotLoggedIn() {
        boolean notLoggedIn = MobileUI.isElementPresentAndDisplayed(contentDescHomePage);
        logger.debug("User not logged in: {}", notLoggedIn);
        return notLoggedIn;
    }

    @Step("Check if user is logged in")
    public boolean isLoggedIn() {
        boolean loggedIn = MobileUI.isElementPresentAndDisplayed(shopRecentlyButton);
        logger.debug("User logged in: {}", loggedIn);
        return loggedIn;
    }

    @Step("Check if HomePage is displayed")
    public boolean isHomePageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(shopRecentlyButton)
                || MobileUI.isElementPresentAndDisplayed(contentDescHomePage);
        logger.debug("HomePage displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Get current state of HomePage")
    public String getCurrentState() {
        if (isLoggedIn()) {
            logger.debug("Current state: LOGGED_IN");
            return "LOGGED_IN";
        } else if (isNotLoggedIn()) {
            logger.debug("Current state: NOT_LOGGED_IN");
            return "NOT_LOGGED_IN";
        }
        logger.warn("Current state: UNKNOWN");
        return "UNKNOWN";
    }

    @Step("Wait for HomePage to load")
    public void waitForHomePageToLoad() {
        logger.info("Waiting for HomePage to load...");
        try {
            // Sử dụng vòng lặp polling với MobileUI.isElementPresentAndDisplayed
            int maxAttempts = 20; // 10 seconds với mỗi lần 500ms
            int attempts = 0;
            boolean loaded = false;

            while (attempts < maxAttempts && !loaded) {
                loaded = MobileUI.isElementPresentAndDisplayed(shopRecentlyButton)
                        || MobileUI.isElementPresentAndDisplayed(contentDescHomePage);
                if (!loaded) {
                    MobileUI.sleep(0.5);
                    attempts++;
                }
            }

            if (loaded) {
                logger.info("HomePage loaded successfully - State: {}", getCurrentState());
            } else {
                logger.error("HomePage failed to load within timeout");
                throw new RuntimeException("HomePage failed to load within 10 seconds");
            }
        } catch (Exception e) {
            logger.error("HomePage failed to load - Exception: {}", e.getMessage());
            throw e;
        }
    }

/*    public RegisterPage clickRegisterButton() {
        MobileUI.clickElement(registerButton);
        System.out.println("[HomePage] Clicked Register button");
        return new RegisterPage();
    }

    public LoginPage clickSignInButton() {
        MobileUI.clickElement(signInButton);
        System.out.println("[HomePage] Clicked Sign In button");
        return new LoginPage();
    }

    public void navigateToCreateAccount() {
        clickRegisterButton();
    }

    public void navigateToSignIn() {
        clickSignInButton();
    }

    public boolean isHomePageDisplayed() {
        return isElementDisplayed(contentDescHomePage) || isElementDisplayed(shopRecentlyButton);
    }

    public boolean isNotLoggedIn() {
        return isElementDisplayed(contentDescHomePage);
    }

    public boolean isLoggedIn() {
        return isElementDisplayed(shopRecentlyButton);
    }

    public String getCurrentState() {
        if (isNotLoggedIn()) {
            return "NOT_LOGGED_IN";
        } else if (isLoggedIn()) {
            return "LOGGED_IN";
        } else {
            return "UNKNOWN";
        }
    }

    public boolean isRegisterButtonDisplayed() {
        return isElementDisplayed(registerButton);
    }

    public boolean isSignInButtonDisplayed() {
        return isElementDisplayed(signInButton);
    }

    public boolean waitForHomePageToLoad() {
        return waitForHomePageToLoad(1);
    }

    public boolean waitForHomePageToLoad(int timeoutSeconds) {
        int attempts = 0;
        int maxAttempts = timeoutSeconds;

        while (attempts < maxAttempts) {
            if (isHomePageDisplayed()) {
                System.out.println("[HomePage] Home page loaded after " + attempts + " seconds");
                return true;
            }
            MobileUI.sleep(0.3);
            attempts++;
        }
        System.out.println("[HomePage] Home page did not load within " + timeoutSeconds + " seconds");
        return false;
    }

    private boolean isElementDisplayed(WebElement element) {
        return MobileUI.isElementPresentAndDisplayed(element);
    }*/
}
