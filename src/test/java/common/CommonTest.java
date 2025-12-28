package common;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import page.*;

public abstract class CommonTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(CommonTest.class);

    protected HomePage homePage;
    protected AppFlowManager appFlowManager;

    protected abstract String getTestName();

    @BeforeClass
    public void setUpClass() {
        appFlowManager = new AppFlowManager();
        appFlowManager.handleAppLaunch();
        logger.info("[{}] ========== TEST SUITE STARTED ==========", getTestName());

        if (requiresLoginPrecondition()) {
            performLoginPrecondition();
        }
    }

    @BeforeMethod
    public void setUp() {
        logger.info("\n[{}] ========== TEST STARTED ==========", getTestName());
        homePage = new HomePage();

        String currentState = homePage.getCurrentState();
        logger.info("[{}] Current state: {}", getTestName(), currentState);

        if (!homePage.isHomePageDisplayed()) {
            logger.debug("[{}] Not on HomePage (state: {}), navigating back...", getTestName(), currentState);
            DriverManager.getDriver().navigate().back();

            homePage.waitForHomePageToLoad();  // ← SỬA: Loại bỏ parameter

            if (homePage.isHomePageDisplayed()) {
                logger.info("[{}] Successfully navigated back to HomePage", getTestName());
            } else {
                logger.warn("[{}] WARNING: Still not on HomePage after navigation!", getTestName());
            }
        } else {
            logger.info("[{}] Already on HomePage (state: {})", getTestName(), currentState);
        }
    }

    public void refresh() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        logger.info("\n[{}] ========== CLEANUP STARTED ==========", getTestName());

        boolean isTestPassed = (result.getStatus() == ITestResult.SUCCESS);
        logger.info("[{}] Test Status: {}", getTestName(), (isTestPassed ? "PASSED" : "FAILED"));

        try {
            if (homePage == null) {
                homePage = new HomePage();
            }

            if (isTestPassed) {
                if (!homePage.isHomePageDisplayed()) {
                    logger.debug("[{}] Not on HomePage, auto-navigating back...", getTestName());
                    navigateBackToHomePageWithRetry();
                }

                if (homePage.isLoggedIn() && shouldLogoutAfterPassedTest()) {
                    logger.info("[{}] Test PASSED & Logged in -> Performing logout", getTestName());
                    performLogout();
                } else if (homePage.isLoggedIn() && !shouldLogoutAfterPassedTest()) {
                    logger.info("[{}] Test PASSED & Logged in -> Keeping session (no logout needed)", getTestName());
                } else {
                    logger.info("[{}] Test PASSED & Already on HomePage -> No cleanup needed", getTestName());
                }
            } else {
                logger.info("[{}] Test FAILED -> Clearing data only (no logout to save time)", getTestName());
                clearAppDataOnly();
            }

        } catch (NoSuchElementException e) {
            logger.error("[{}] Element not found during cleanup: {}", getTestName(), e.getMessage());
            navigateBackFallback();
        } catch (WebDriverException e) {
            logger.error("[{}] WebDriver error during cleanup: {}", getTestName(), e.getMessage());
            navigateBackFallback();
        } catch (Exception e) {
            logger.error("[{}] Unexpected cleanup error: {}", getTestName(), e.getClass().getName());
            navigateBackFallback();
        }
        logger.info("[{}] ========== CLEANUP COMPLETED ==========\n", getTestName());
    }

    protected boolean shouldLogoutAfterPassedTest() {
        return true;
    }

    protected boolean requiresLoginPrecondition() {
        return false;
    }

    protected String[] getLoginCredentials() {
        return null;
    }

    protected void performLogout() {
        logger.info("[{}] Logging out...", getTestName());
        BasePage basePage = new BasePage();
        AccountPage accountPage = basePage.clickAccountMenuItem();
        accountPage.scrollAndLogout();

        homePage = new HomePage();
        homePage.waitForHomePageToLoad();  // ← SỬA: Loại bỏ parameter

        logger.info("[{}] Logged out successfully", getTestName());
    }

    protected void clearAppDataOnly() {
        logger.info("[{}] Clearing app data (no logout)...", getTestName());
        try {
            if (homePage == null) {
                homePage = new HomePage();
            }

            if (!homePage.isHomePageDisplayed()) {
                logger.debug("[{}] Not on HomePage, navigating back...", getTestName());
                navigateBackToHomePageWithRetry();
            } else {
                logger.debug("[{}] Already on HomePage", getTestName());
            }

            logger.info("[{}] App data cleared successfully", getTestName());
        } catch (NoSuchElementException e) {
            logger.error("[{}] Element not found during clear: {}", getTestName(), e.getMessage());
        } catch (WebDriverException e) {
            logger.error("[{}] WebDriver error during clear: {}", getTestName(), e.getMessage());
        } catch (Exception e) {
            logger.error("[{}] Clear data error: {} - {}", getTestName(), e.getClass().getName(), e.getMessage());
        }
    }

    private void performLoginPrecondition() {
        logger.info("[{}] ========== CHECKING LOGIN PRECONDITION ==========", getTestName());

        if (homePage == null) {
            homePage = new HomePage();
        }

        if (!homePage.isLoggedIn()) {
            String[] credentials = getLoginCredentials();
            if (credentials == null || credentials.length != 2) {
                String errorMsg = "Login precondition required but credentials not provided in " + getTestName();
                logger.error("[{}] ERROR: {}", getTestName(), errorMsg);
                throw new IllegalStateException(errorMsg);
            }

            logger.info("[{}] User not logged in -> Logging in...", getTestName());

            try {
                LoginPage loginPage = homePage.clickSignInButton();
                homePage = loginPage.loginExpectSuccess(credentials[0], credentials[1]);

                if (!homePage.isLoggedIn()) {
                    throw new RuntimeException("Login succeeded but user state shows not logged in");
                }

                logger.info("[{}] Logged in successfully", getTestName());

            } catch (Exception e) {
                String errorMsg = "Login precondition failed for " + getTestName();
                logger.error("[{}] ERROR: {}", getTestName(), errorMsg);
                throw new RuntimeException(errorMsg, e);
            }
        } else {
            logger.info("[{}] User already logged in - Skipping login", getTestName());
        }

        logger.info("[{}] ========== LOGIN PRECONDITION COMPLETED ==========\n", getTestName());
    }

    private void navigateBackToHomePageWithRetry() {
        int maxAttempts = 5;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                homePage = new HomePage();
                if (homePage.isHomePageDisplayed()) {
                    logger.debug("[{}] Successfully on HomePage (attempt {})", getTestName(), (i+1));
                    return;
                }

                logger.debug("[{}] Navigate back attempt {}/{}", getTestName(), (i+1), maxAttempts);
                DriverManager.getDriver().navigate().back();

                homePage.waitForHomePageToLoad();  // ← SỬA: Loại bỏ parameter và return value

                if (homePage.isHomePageDisplayed()) {
                    logger.info("[{}] Reached HomePage successfully", getTestName());
                    return;
                }

            } catch (NoSuchElementException e) {
                logger.debug("[{}] Element not found on attempt {}, continuing...", getTestName(), (i+1));
            } catch (WebDriverException e) {
                logger.debug("[{}] WebDriver error on attempt {}: {}", getTestName(), (i+1), e.getMessage());
            } catch (Exception e) {
                logger.debug("[{}] Error on attempt {}: {}", getTestName(), (i+1), e.getClass().getName());
            }

            if (i < maxAttempts - 1) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        try {
            homePage = new HomePage();
            if (!homePage.isHomePageDisplayed()) {
                logger.warn("[{}] WARNING: Failed to reach HomePage after {} attempts", getTestName(), maxAttempts);
                logger.warn("[{}] Current state: {}", getTestName(), homePage.getCurrentState());
            }
        } catch (Exception e) {
            logger.warn("[{}] WARNING: Cannot verify final state: {}", getTestName(), e.getMessage());
        }
    }

    private void navigateBackFallback() {
        try {
            logger.debug("[{}] Attempting fallback navigation...", getTestName());
            DriverManager.getDriver().navigate().back();
            MobileUI.sleep(0.3);
            logger.debug("[{}] Fallback navigation completed", getTestName());
        } catch (Exception ex) {
            logger.error("[{}] Fallback navigation also failed: {}", getTestName(), ex.getMessage());
        }
    }
}
