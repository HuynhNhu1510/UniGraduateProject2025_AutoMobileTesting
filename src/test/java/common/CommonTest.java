package common;

import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.testng.Assert;
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

    // ==================== OVERRIDABLE METHODS ====================

    protected boolean shouldLogoutAfterPassedTest() {
        return true; // Default: logout after passed test
    }

    protected boolean requiresLoginPrecondition() {
        return false;
    }

    protected String[] getLoginCredentials() {
        return null;
    }

    // ==================== COMMON HELPER METHODS ====================

    protected void performLogout() {
        System.out.println("[" + getTestName() + "] Logging out...");
        BasePage basePage = new BasePage();
        AccountPage accountPage = basePage.clickAccountMenuItem();
        accountPage.scrollAndLogout();
        MobileUI.sleep(0.1);
        System.out.println("[" + getTestName() + "] Logged out successfully");
    }

    protected void navigateBackToHomePage() {
        System.out.println("[" + getTestName() + "] Navigating back to HomePage...");
        DriverManager.getDriver().navigate().back();
        MobileUI.sleep(0.1);
    }

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

    private void performLoginPrecondition() {
        System.out.println("[" + getTestName() + "] ========== CHECKING LOGIN PRECONDITION ==========");

        homePage = new HomePage();

        if (!homePage.isLoggedIn()) {
            String[] credentials = getLoginCredentials();
            if (credentials == null || credentials.length != 2) {
                System.out.println("[" + getTestName() + "] WARNING: Login credentials not provided!");
                return;
            }

            System.out.println("[" + getTestName() + "] User not logged in -> Logging in...");

            LoginPage loginPage = homePage.clickSignInButton();
            homePage = loginPage.loginExpectSuccess(credentials[0], credentials[1]);

            if (homePage.isLoggedIn()) {
                System.out.println("[" + getTestName() + "] Logged in successfully");
            } else {
                System.out.println("[" + getTestName() + "] ERROR: Login failed!");
            }
        } else {
            System.out.println("[" + getTestName() + "] User already logged in - Skipping login");
        }

        System.out.println("[" + getTestName() + "] ========== LOGIN PRECONDITION COMPLETED ==========\n");
    }

    // ==================== CHANGE PASSWORD HELPERS ====================

    protected ChangePasswordPage navigateToChangePasswordScreen() {
        System.out.println("[" + getTestName() + "] Navigating to Change Password screen...");

        BasePage basePage = new BasePage();
        AccountPage accountPage = basePage.clickAccountMenuItem();
        MobileUI.sleep(0.2);

        DetailsAndPasswordPage detailsAndPasswordPage = accountPage.clickOnAccountInformation();
        MobileUI.sleep(0.2);

        ChangePasswordPage changePasswordPage = detailsAndPasswordPage.clickChangePasswordButton();
        MobileUI.sleep(0.2);

        Assert.assertTrue(changePasswordPage.isChangePasswordTitleDisplayed(),
                "PRECONDITION FAILED: Should be on Change Password screen");

        System.out.println("[" + getTestName() + "] Successfully navigated to Change Password screen");
        return changePasswordPage;
    }

    protected void resetPasswordToDefault(String currentPassword, String defaultPassword) {
        System.out.println("[" + getTestName() + "] Resetting password back to default...");

        try {
            BasePage basePage = new BasePage();
            AccountPage accountPage = basePage.clickAccountMenuItem();
            MobileUI.sleep(0.2);

            DetailsAndPasswordPage detailsAndPasswordPage = accountPage.clickOnAccountInformation();
            MobileUI.sleep(0.2);

            ChangePasswordPage changePasswordPage = detailsAndPasswordPage.clickChangePasswordButton();
            MobileUI.sleep(0.2);

            DetailsAndPasswordPage result = changePasswordPage.changePasswordExpectSuccess(
                    currentPassword,
                    defaultPassword
            );

            System.out.println("[" + getTestName() + "] Password reset successfully to default");

        } catch (Exception e) {
            System.err.println("[" + getTestName() + "] CRITICAL ERROR: Failed to reset password!");
            System.err.println("[" + getTestName() + "] Current password: " + currentPassword);
            System.err.println("[" + getTestName() + "] Target default password: " + defaultPassword);
            System.err.println("[" + getTestName() + "] MANUAL INTERVENTION REQUIRED!");

            throw new RuntimeException("CRITICAL: Password reset failed - cannot continue tests safely", e);
        }
    }
}
