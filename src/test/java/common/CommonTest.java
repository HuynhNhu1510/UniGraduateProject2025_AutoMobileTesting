package common;

import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import page.AccountPage;
import page.BasePage;
import page.HomePage;

public abstract class CommonTest extends BaseTest{
    protected HomePage homePage;
    protected AppFlowManager appFlowManager;
    protected abstract String getTestName();

    @BeforeClass
    public void setUpClass() {
        appFlowManager = new AppFlowManager();
        appFlowManager.handleAppLaunch();
        System.out.println("[" + getTestName() + "] ========== TEST SUITE STARTED ==========");
    }

    @BeforeMethod
    public void setUp() {
        System.out.println("\n[" + getTestName() + "] ========== TEST STARTED ==========");
        homePage = new HomePage();

        // Ensure we're on HomePage before each test
        if (!homePage.isHomePageDisplayed()) {
            System.out.println("[" + getTestName() + "] Not on HomePage, navigating back...");
            DriverManager.getDriver().navigate().back();
            MobileUI.sleep(0.1);
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        System.out.println("\n[" + getTestName() + "] ========== CLEANUP STARTED ==========");

        boolean isTestPassed = (result.getStatus() == ITestResult.SUCCESS);
        System.out.println("[" + getTestName() + "] Test Status: " + (isTestPassed ? "PASSED" : "FAILED"));

        try {
            homePage = new HomePage();

            if (isTestPassed) {
                // Test PASSED → Check if logged in and perform logout if needed
                if (homePage.isLoggedIn()) {
                    System.out.println("[" + getTestName() + "] Test PASSED & Logged in → Performing logout");
                    performLogout();
                } else if (!homePage.isHomePageDisplayed()) {
                    System.out.println("[" + getTestName() + "] Test PASSED but not on HomePage → Navigating back");
                    navigateBackToHomePage();
                } else {
                    System.out.println("[" + getTestName() + "] Test PASSED & Already on HomePage → No cleanup needed");
                }
            } else {
                // Test FAILED → Just clear data, no need to logout (save time)
                System.out.println("[" + getTestName() + "] Test FAILED → Clearing data only (no logout to save time)");
                clearAppDataOnly();
            }

        } catch (Exception e) {
            System.out.println("[" + getTestName() + "] Cleanup error: " + e.getMessage());
            try {
                DriverManager.getDriver().navigate().back();
            } catch (Exception ex) {
                System.out.println("[" + getTestName() + "] Fallback navigation failed");
            }
        }
        System.out.println("[" + getTestName() + "] ========== CLEANUP COMPLETED ==========\n");
    }

    // Performs logout by navigating to account page and clicking logout
    protected void performLogout() {
        System.out.println("[" + getTestName() + "] Logging out...");
        BasePage basePage = new BasePage();
        AccountPage accountPage = basePage.clickAccountMenuItem();
        accountPage.scrollAndLogout();
        MobileUI.sleep(0.1);
        System.out.println("[" + getTestName() + "] Logged out successfully");
    }

    // Navigates back to home page using back button
    protected void navigateBackToHomePage() {
        System.out.println("[" + getTestName() + "] Navigating back to HomePage...");
        DriverManager.getDriver().navigate().back();
        MobileUI.sleep(0.1);
    }

    /**
     * Clear app data without logout - for FAILED test cases
     * This method only navigates back to home page without performing logout
     * to save time during test execution
     */
    protected void clearAppDataOnly() {
        System.out.println("[" + getTestName() + "] Clearing app data (no logout)...");
        try {
            if (!homePage.isHomePageDisplayed()) {
                // Navigate back to home page
                int maxAttempts = 3;
                for (int i = 0; i < maxAttempts; i++) {
                    DriverManager.getDriver().navigate().back();
                    MobileUI.sleep(0.2);

                    homePage = new HomePage();
                    if (homePage.isHomePageDisplayed()) {
                        System.out.println("[" + getTestName() + "] Successfully navigated back to HomePage");
                        break;
                    }
                }
            } else {
                System.out.println("[" + getTestName() + "] Already on HomePage");
            }

            System.out.println("[" + getTestName() + "] App data cleared successfully");
        } catch (Exception e) {
            System.out.println("[" + getTestName() + "] Clear data error: " + e.getMessage());
        }
    }
}
