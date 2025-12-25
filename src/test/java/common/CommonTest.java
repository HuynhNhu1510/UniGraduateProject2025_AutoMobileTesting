package common;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
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
    protected HomePage homePage;
    protected AppFlowManager appFlowManager;

    protected abstract String getTestName();

    @BeforeClass
    public void setUpClass() {
        appFlowManager = new AppFlowManager();
        appFlowManager.handleAppLaunch();
        System.out.println("[" + getTestName() + "] ========== TEST SUITE STARTED ==========");

        // Perform login precondition nếu test class cần
        if (requiresLoginPrecondition()) {
            performLoginPrecondition();
        }
    }

    @BeforeMethod
    public void setUp() {
        System.out.println("\n[" + getTestName() + "] ========== TEST STARTED ==========");
        homePage = new HomePage();

        String currentState = homePage.getCurrentState();
        System.out.println("[" + getTestName() + "] Current state: " + currentState);

        if (!homePage.isHomePageDisplayed()) {
            System.out.println("[" + getTestName() + "] Not on HomePage (state: " + currentState + "), navigating back...");
            DriverManager.getDriver().navigate().back();

            homePage.waitForHomePageToLoad(1);

            if (homePage.isHomePageDisplayed()) {
                System.out.println("[" + getTestName() + "] Successfully navigated back to HomePage");
            } else {
                System.out.println("[" + getTestName() + "] WARNING: Still not on HomePage after navigation!");
            }
        } else {
            System.out.println("[" + getTestName() + "] Already on HomePage (state: " + currentState + ")");
        }
    }

    public void refresh() {
        // Re-initialize elements if needed
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        System.out.println("\n[" + getTestName() + "] ========== CLEANUP STARTED ==========");

        boolean isTestPassed = (result.getStatus() == ITestResult.SUCCESS);
        System.out.println("[" + getTestName() + "] Test Status: " + (isTestPassed ? "PASSED" : "FAILED"));

        try {
            // Reuse existing homePage instance instead of creating new
            if (homePage == null) {
                homePage = new HomePage();
            }

            if (isTestPassed) {
                // Test PASSED - Check nếu cần logout
                if (homePage.isLoggedIn() && shouldLogoutAfterPassedTest()) {
                    System.out.println("[" + getTestName() + "] Test PASSED & Logged in -> Performing logout");
                    performLogout();
                } else if (homePage.isLoggedIn() && !shouldLogoutAfterPassedTest()) {
                    System.out.println("[" + getTestName() + "] Test PASSED & Logged in -> Keeping session (no logout needed)");
                    navigateBackToHomePage();
                } else if (!homePage.isHomePageDisplayed()) {
                    System.out.println("[" + getTestName() + "] Test PASSED but not on HomePage -> Navigating back");
                    navigateBackToHomePage();
                } else {
                    System.out.println("[" + getTestName() + "] Test PASSED & Already on HomePage -> No cleanup needed");
                }
            } else {
                // Test FAILED - Không logout, chỉ clear data
                System.out.println("[" + getTestName() + "] Test FAILED -> Clearing data only (no logout to save time)");
                clearAppDataOnly();
            }

        } catch (NoSuchElementException e) {
            System.out.println("[" + getTestName() + "] Element not found during cleanup: " + e.getMessage());
            navigateBackFallback();
        } catch (WebDriverException e) {
            System.out.println("[" + getTestName() + "] WebDriver error during cleanup: " + e.getMessage());
            e.printStackTrace();
            navigateBackFallback();
        } catch (Exception e) {
            System.out.println("[" + getTestName() + "] Unexpected cleanup error: " + e.getClass().getName());
            e.printStackTrace();
            navigateBackFallback();
        }
        System.out.println("[" + getTestName() + "] ========== CLEANUP COMPLETED ==========\n");
    }

    protected boolean shouldLogoutAfterPassedTest() {
        return true; // Default: logout after passed test
    }

    protected boolean requiresLoginPrecondition() {
        return false;
    }

    protected String[] getLoginCredentials() {
        return null;
    }

    protected void performLogout() {
        System.out.println("[" + getTestName() + "] Logging out...");
        BasePage basePage = new BasePage();
        AccountPage accountPage = basePage.clickAccountMenuItem();
        accountPage.scrollAndLogout();

        homePage = new HomePage();
        homePage.waitForHomePageToLoad(1);

        System.out.println("[" + getTestName() + "] Logged out successfully");

    }

    protected void navigateBackToHomePage() {
        System.out.println("[" + getTestName() + "] Navigating back to HomePage...");
        DriverManager.getDriver().navigate().back();

        if (homePage == null) {
            homePage = new HomePage();
        }
        homePage.waitForHomePageToLoad(1);
        System.out.println("[" + getTestName() + "] Navigated back to HomePage");
    }

    protected void clearAppDataOnly() {
        System.out.println("[" + getTestName() + "] Clearing app data (no logout)...");
        try {
            // Reuse homePage instance
            if (homePage == null) {
                homePage = new HomePage();
            }

            if (!homePage.isHomePageDisplayed()) {
                // Navigate back to home page with retry
                int maxAttempts = 3;
                for (int i = 0; i < maxAttempts; i++) {
                    System.out.println("[" + getTestName() + "] Navigate back attempt " + (i+1) + "/" + maxAttempts);
                    DriverManager.getDriver().navigate().back();

                    // Wait for page to load instead of fixed sleep
                    boolean loaded = homePage.waitForHomePageToLoad(2);
                    if (loaded && homePage.isHomePageDisplayed()) {
                        System.out.println("[" + getTestName() + "] Successfully navigated back to HomePage");
                        break;
                    }

                    if (i == maxAttempts - 1) {
                        System.out.println("[" + getTestName() + "] WARNING: Failed to navigate back after " + maxAttempts + " attempts");
                    }
                }
            } else {
                System.out.println("[" + getTestName() + "] Already on HomePage");
            }

            System.out.println("[" + getTestName() + "] App data cleared successfully");
        } catch (NoSuchElementException e) {
            System.out.println("[" + getTestName() + "] Element not found during clear: " + e.getMessage());
        } catch (WebDriverException e) {
            System.out.println("[" + getTestName() + "] WebDriver error during clear: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[" + getTestName() + "] Clear data error: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private void performLoginPrecondition() {
        System.out.println("[" + getTestName() + "] ========== CHECKING LOGIN PRECONDITION ==========");

        // Reuse homePage instance if exists
        if (homePage == null) {
            homePage = new HomePage();
        }

        if (!homePage.isLoggedIn()) {
            String[] credentials = getLoginCredentials();
            if (credentials == null || credentials.length != 2) {
                String errorMsg = "Login precondition required but credentials not provided in " + getTestName();
                System.out.println("[" + getTestName() + "] ERROR: " + errorMsg);
                throw new IllegalStateException(errorMsg);
            }

            System.out.println("[" + getTestName() + "] User not logged in -> Logging in...");

            try {
                LoginPage loginPage = homePage.clickSignInButton();
                homePage = loginPage.loginExpectSuccess(credentials[0], credentials[1]);

                if (!homePage.isLoggedIn()) {
                    throw new RuntimeException("Login succeeded but user state shows not logged in");
                }

                System.out.println("[" + getTestName() + "] Logged in successfully");

            } catch (Exception e) {
                String errorMsg = "Login precondition failed for " + getTestName();
                System.out.println("[" + getTestName() + "] ERROR: " + errorMsg);
                e.printStackTrace();
                throw new RuntimeException(errorMsg, e);
            }
        } else {
            System.out.println("[" + getTestName() + "] User already logged in - Skipping login");
        }

        System.out.println("[" + getTestName() + "] ========== LOGIN PRECONDITION COMPLETED ==========\n");
    }

    private void navigateBackFallback() {
        try {
            System.out.println("[" + getTestName() + "] Attempting fallback navigation...");
            DriverManager.getDriver().navigate().back();
            MobileUI.sleep(0.3);
            System.out.println("[" + getTestName() + "] Fallback navigation completed");
        } catch (Exception ex) {
            System.out.println("[" + getTestName() + "] Fallback navigation also failed: " + ex.getMessage());
        }
    }
}
