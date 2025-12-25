package testcase;

import common.CommonTest;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.*;

public class ChangePasswordTest extends CommonTest {
    // Test account credentials
    private static final String TEST_EMAIL = "ace2nd@gmail.com";
    private static final String ORIGINAL_PASSWORD = "NewPass@123";

    private ChangePasswordPage changePasswordPage;
    private DetailsAndPasswordPage detailsPage;
    private AccountPage accountPage;

    @Override
    protected String getTestName() {
        return "Change Password Test";
    }

    @Override
    protected boolean requiresLoginPrecondition() {
        return true;
    }

    @Override
    protected String[] getLoginCredentials() {
        return new String[]{TEST_EMAIL, ORIGINAL_PASSWORD};
    }

    @Override
    protected boolean shouldLogoutAfterPassedTest() {
        return false; // Keep logged in to avoid re-login overhead
    }

    private void navigateToChangePassword() {
        int maxBackAttempts = 3;
        for (int i = 0; i < maxBackAttempts; i++) {
            try {
                homePage = new HomePage();
                if (homePage.isHomePageDisplayed()) {
                    System.out.println("[ChangePasswordTest] Reached HomePage");
                    return;
                }
                System.out.println("[ChangePasswordTest] Not on HomePage yet, navigating back...");
                DriverManager.getDriver().navigate().back();
                MobileUI.sleep(0.5);
            } catch (Exception e) {
                System.out.println("[ChangePasswordTest] Error during back navigation: " + e.getMessage());
            }
        }

        // Final check
        homePage = new HomePage();
        if (!homePage.isHomePageDisplayed()) {
            throw new RuntimeException("Failed to navigate back to HomePage after " + maxBackAttempts + " attempts");
        }
    }

    @Test(priority = 1, description = "CP.01 - Change password success with valid credentials")
    public void testChangePasswordSuccess() {
        String newPassword = "Kikiga18123@";
        navigateToChangePassword();

        // Change password from ORIGINAL → NEW
        System.out.println("[CP.01] Step 1: Changing password from original to new...");
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, newPassword);

        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page after successful password change");
        System.out.println("[CP.01] Password changed successfully to new password");


        navigateBackToHomePage();

        // Verify new password works (logout and login with new password)
        System.out.println("[CP.01] Step 2: Verifying new password by re-login...");
        performLogout();
        MobileUI.sleep(0.5);

        homePage = new HomePage();
        LoginPage loginPage = homePage.clickSignInButton();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        Assert.assertTrue(homePage.isLoggedIn(),
                "Should be able to login with new password");
        System.out.println("[CP.01] Successfully logged in with new password");

        // Change password back to original
        System.out.println("[CP.01] Step 3: Changing password back to original (cleanup)...");
        navigateToChangePassword();
        detailsPage = changePasswordPage.changePasswordExpectSuccess(newPassword, ORIGINAL_PASSWORD);

        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page after changing back");
        System.out.println("[CP.01] Password restored to original - Test cleanup completed");
    }

    @Test(priority = 2, description = "CP.02 - Invalid new password format")
    public void testInvalidNewPasswordFormat() {
        String invalidPassword = "abc12345"; // Missing uppercase and special char

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(ORIGINAL_PASSWORD, invalidPassword);

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Password format error message should be displayed");

        String expectedError = "Password must have at least 8 characters that include at least 1 lowercase character, " +
                "1 uppercase character, 1 number, and 1 special character in (!@#$%^&*)";
        String actualError = changePasswordPage.getPasswordInvalidMessage();
        Assert.assertEquals(actualError, expectedError,
                "Error message should match expected password rule message");
    }

    @Test(priority = 3, description = "CP.03 - Invalid current password")
    public void testInvalidCurrentPassword() {
        String wrongCurrentPassword = "Wrong@123";
        String validNewPassword = "NewPass@123";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(wrongCurrentPassword, validNewPassword);

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed");

        String actualError = changePasswordPage.getCurrentPasswordErrorMessage();
        Assert.assertEquals(actualError, "Current password is invalid",
                "Error message should indicate current password is invalid");
    }

    @Test(priority = 4, description = "CP.04 - New password empty")
    public void testNewPasswordEmpty() {
        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(ORIGINAL_PASSWORD, "");

        Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                "Empty password error should be displayed");

        String actualError = changePasswordPage.getPasswordEmptyMessage();
        Assert.assertEquals(actualError, "Please enter your password",
                "Error message should ask to enter password");
    }

    @Test(priority = 5, description = "CP.05 - Both fields empty")
    public void testBothFieldsEmpty() {
        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure("", "");

        // Should show error for empty field
        boolean isErrorDisplayed = changePasswordPage.isPasswordEmptyMessageDisplayed() ||
                changePasswordPage.isCurrentPasswordErrorDisplayed();

        Assert.assertTrue(isErrorDisplayed,
                "At least one error message should be displayed when both fields are empty");
    }

    @Test(priority = 6, description = "CP.06 - Current password empty")
    public void testCurrentPasswordEmpty() {
        String validNewPassword = "NewPass@123";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure("", validNewPassword);

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed for empty current password");
    }

    @Test(priority = 7, description = "CP.07 - Invalid current format + empty new")
    public void testInvalidCurrentFormatAndEmptyNew() {
        String invalidCurrentPassword = "abc"; // Invalid format

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(invalidCurrentPassword, "");

        // Should show error for empty new password (higher priority)
        Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                "Empty new password error should be displayed");
    }

    @Test(priority = 8, description = "CP.08 - Invalid current format with valid new")
    public void testInvalidCurrentFormat() {
        String invalidCurrentPassword = "abc"; // Too short, no uppercase, no special char
        String validNewPassword = "NewPass@123";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(invalidCurrentPassword, validNewPassword);

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed");
    }

    @Test(priority = 9, description = "CP.09 - New password equals current")
    public void testNewPasswordEqualsCurrent() {
        navigateToChangePassword();

        // Use same password for both current and new
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, ORIGINAL_PASSWORD);

        // According to test case, this should succeed (no error)
        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page when new password equals current (treated as success)");
    }

    @Test(priority = 10, description = "CP.10 - Login with new password after change")
    public void testLoginWithNewPasswordAfterChange() {
        String newPassword = "TempPass@456";

        navigateToChangePassword();

        // Step 1: Change password
        System.out.println("[CP.10] Step 1: Changing password...");
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, newPassword);
        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page");

        // Step 2: Logout
        System.out.println("[CP.10] Step 2: Logging out...");
        performLogout();
        MobileUI.sleep(0.5);

        // Step 3: Try login with NEW password (should succeed)
        System.out.println("[CP.10] Step 3: Testing login with NEW password...");
        HomePage homePage = new HomePage();
        LoginPage loginPage = homePage.clickSignInButton();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        Assert.assertTrue(homePage.isLoggedIn(),
                "Should be able to login with NEW password");
        System.out.println("[CP.10] Login with new password successful");

        // Step 4: Logout and try OLD password (should fail)
        System.out.println("[CP.10] Step 4: Testing login with OLD password (should fail)...");
        performLogout();
        MobileUI.sleep(0.5);

        homePage = new HomePage();
        loginPage = homePage.clickSignInButton();
        loginPage.loginExpectFailure(TEST_EMAIL, ORIGINAL_PASSWORD);

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Should NOT be able to login with OLD password");
        System.out.println("[CP.10] Login with old password failed as expected");

        // Step 5: CRITICAL - Restore original password for test independence
        System.out.println("[CP.10] Step 5: Restoring original password (cleanup)...");
        // First login with new password
        loginPage.clearAllFields();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        // Then change back to original
        navigateToChangePassword();
        changePasswordPage.changePasswordExpectSuccess(newPassword, ORIGINAL_PASSWORD);
        System.out.println("[CP.10] Password restored to original - Test cleanup completed");
    }

    @Test(priority = 11, description = "CP.11 - Invalid special character")
    public void testInvalidSpecialCharacter() {
        String passwordWithInvalidChar = "NewPass@123?"; // '?' is not in allowed list

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(ORIGINAL_PASSWORD, passwordWithInvalidChar);

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Password rule error should be displayed for invalid special character");
    }

    @Test(priority = 12, description = "CP.12 - Min length boundary (8 characters)")
    public void testMinLengthBoundary() {
        String minLengthPassword = "A@b1cdef";

        navigateToChangePassword();

        // This should succeed
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, minLengthPassword);
        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should accept password with exactly 8 characters meeting all requirements");

        // CLEANUP: Change back to original
        navigateToChangePassword();
        changePasswordPage.changePasswordExpectSuccess(minLengthPassword, ORIGINAL_PASSWORD);
        System.out.println("[CP.12] Password restored to original");
    }

    @Test(priority = 13, description = "CP.13 - Case-sensitive current password")
    public void testCaseSensitiveCurrentPassword() {
        // Original password: Kikiga18123@
        // Try with different case: kikiga18123@
        String wrongCasePassword = "kikiga18123@";
        String validNewPassword = "NewPass@123";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(wrongCasePassword, validNewPassword);
        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed (password is case-sensitive)");
    }
}
