package common;

import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.testng.Assert;
import page.*;

public class ChangePasswordTestHelper {

    private String testName;

    public ChangePasswordTestHelper(String testName) {
        this.testName = testName;
    }

    public ChangePasswordPage navigateToChangePasswordScreen() {
        System.out.println("[" + testName + "] Navigating to Change Password screen...");

        BasePage basePage = new BasePage();
        AccountPage accountPage = basePage.clickAccountMenuItem();
        MobileUI.sleep(0.2);

        DetailsAndPasswordPage detailsAndPasswordPage = accountPage.clickOnAccountInformation();
        MobileUI.sleep(0.2);

        ChangePasswordPage changePasswordPage = detailsAndPasswordPage.clickChangePasswordButton();
        MobileUI.sleep(0.2);

        Assert.assertTrue(changePasswordPage.isChangePasswordTitleDisplayed(),
                "PRECONDITION FAILED: Should be on Change Password screen");

        System.out.println("[" + testName + "] Successfully navigated to Change Password screen");
        return changePasswordPage;
    }

    public void resetPasswordToDefault(String currentPassword, String defaultPassword) {
        System.out.println("[" + testName + "] Resetting password back to default...");

        try {
            // FIX: Từ DetailsAndPasswordPage, click trực tiếp vào ChangePasswordButton
            // KHÔNG cần navigate qua AccountPage nữa
            DetailsAndPasswordPage detailsAndPasswordPage = new DetailsAndPasswordPage();

            ChangePasswordPage changePasswordPage = detailsAndPasswordPage.clickChangePasswordButton();
            MobileUI.sleep(0.2);

            // Submit để reset password về default
            DetailsAndPasswordPage result = changePasswordPage.changePasswordExpectSuccess(
                    currentPassword,
                    defaultPassword
            );

            System.out.println("[" + testName + "] Password reset successfully to default");

            // FIX: Navigate về HomePage với VERIFICATION trong loop
            System.out.println("[" + testName + "] Navigating back to HomePage after reset...");
            navigateBackToHomePageWithRetry();

        } catch (Exception e) {
            System.err.println("[" + testName + "] CRITICAL ERROR: Failed to reset password!");
            System.err.println("[" + testName + "] Current password: " + currentPassword);
            System.err.println("[" + testName + "] Target default password: " + defaultPassword);
            System.err.println("[" + testName + "] MANUAL INTERVENTION REQUIRED!");

            throw new RuntimeException("CRITICAL: Password reset failed - cannot continue tests safely", e);
        }
    }

    private void navigateBackToHomePageWithRetry() {
        int maxAttempts = 5;
        HomePage homePage = new HomePage();

        // Check nếu đã ở HomePage rồi thì không cần navigate
        if (homePage.isHomePageDisplayed()) {
            System.out.println("[" + testName + "] Already on HomePage, no navigation needed");
            return;
        }

        // Retry loop với verification
        for (int i = 0; i < maxAttempts; i++) {
            System.out.println("[" + testName + "] Navigate back attempt " + (i + 1) + "/" + maxAttempts);

            DriverManager.getDriver().navigate().back();
            MobileUI.sleep(0.3);

            // VERIFICATION
            homePage = new HomePage();

            if (homePage.isHomePageDisplayed()) {
                System.out.println("[" + testName + "] Successfully navigated back to HomePage after " + (i + 1) + " attempts");
                return;
            }
        }

        // FINAL CHECK - Nếu sau maxAttempts vẫn chưa về thì warning
        if (!homePage.isHomePageDisplayed()) {
            System.out.println("[" + testName + "] WARNING: Failed to navigate back to HomePage after " + maxAttempts + " attempts");
            System.out.println("[" + testName + "] This may cause next test to fail");
        }
    }

    public void ensureOnHomePageBeforeNavigation() {
        HomePage homePage = new HomePage();

        if (!homePage.isHomePageDisplayed()) {
            System.out.println("[" + testName + "] ERROR: Not on HomePage before navigation!");
            System.out.println("[" + testName + "] setUp() should have ensured we're on HomePage");
            throw new RuntimeException("PRECONDITION FAILED: Not on HomePage. This should not happen if setUp() works correctly.");
        }

        System.out.println("[" + testName + "] Verified: Currently on HomePage");
    }
}
