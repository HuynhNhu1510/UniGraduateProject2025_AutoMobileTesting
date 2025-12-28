package testcase;

import common.CommonTest;
import io.qameta.allure.*;
import org.example.drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.keywords.MobileUI;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.*;

@Epic("User Management")
@Feature("Change Password Functionality")
public class ChangePasswordTest extends CommonTest {

    private static final Logger logger = LogManager.getLogger(ChangePasswordTest.class);

    private static final String TEST_EMAIL = "khoadang1510@gmail.com";
    private static final String ORIGINAL_PASSWORD = "Kikiga18123@";

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
        return false;
    }

    @Step("Navigate to Change Password page")
    private void navigateToChangePassword() {
        logger.info("Navigating to Change Password page...");
        BasePage basePage = new BasePage();
        accountPage = basePage.clickAccountMenuItem();
        MobileUI.sleep(0.3);

        detailsPage = accountPage.clickOnAccountInformation();
        changePasswordPage = detailsPage.clickChangePasswordButton();

        Assert.assertTrue(changePasswordPage.isChangePasswordPageDisplayed(),
                "Change Password page should be displayed");
        logger.info("Successfully navigated to Change Password page");
    }

    @Step("Navigate back to HomePage manually")
    private void navigateBackToHomePageManually() {
        logger.debug("Manually navigating back to HomePage...");
        int maxAttempts = 3;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                HomePage hp = new HomePage();
                if (hp.isHomePageDisplayed()) {
                    logger.debug("Already on HomePage");
                    return;
                }

                DriverManager.getDriver().navigate().back();
                MobileUI.sleep(0.3);

            } catch (Exception e) {
                logger.debug("Navigate attempt {} exception: {}", (i+1), e.getMessage());
            }
        }

        HomePage hp = new HomePage();
        if (!hp.isHomePageDisplayed()) {
            logger.warn("WARNING: Not on HomePage after manual navigation");
        }
    }

    @Test(priority = 1, description = "CP.01 - Change password success with valid credentials")
    @Story("Change Password - Valid Data")
    @Description("Verify user can change password successfully with valid current and new password")
    @Severity(SeverityLevel.CRITICAL)
    public void testChangePasswordSuccess() {
        logger.info("Starting test: CP.01 - Change password success");
        String newPassword = "NewPass@456";

        navigateToChangePassword();

        logger.info("[CP.01] Step 1: Changing password from original to new...");
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, newPassword);

        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page after successful password change");
        logger.info("[CP.01] Password changed successfully to new password");

        navigateBackToHomePageManually();

        logger.info("[CP.01] Step 2: Verifying new password by re-login...");
        performLogout();
        MobileUI.sleep(0.5);

        HomePage homePage = new HomePage();
        LoginPage loginPage = homePage.clickSignInButton();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        Assert.assertTrue(homePage.isLoggedIn(),
                "Should be able to login with new password");
        logger.info("[CP.01] Successfully logged in with new password");

        logger.info("[CP.01] Step 3: Changing password back to original (cleanup)...");
        navigateToChangePassword();
        detailsPage = changePasswordPage.changePasswordExpectSuccess(newPassword, ORIGINAL_PASSWORD);

        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page after changing back");
        logger.info("[CP.01] Password restored to original - Test cleanup completed");
    }

    @Test(priority = 2, description = "CP.02 - Invalid new password format")
    @Story("Change Password - Invalid Password Format")
    @Description("Verify error message when new password doesn't meet requirements")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidNewPasswordFormat() {
        logger.info("Starting test: CP.02 - Invalid new password format");
        String invalidPassword = "abc12345";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(ORIGINAL_PASSWORD, invalidPassword);

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Password format error message should be displayed");

        String expectedError = "Password must have at least 8 characters that include at least 1 lowercase character, " +
                "1 uppercase character, 1 number, and 1 special character in (!@#$%^&*)";
        String actualError = changePasswordPage.getPasswordInvalidMessage();
        Assert.assertEquals(actualError, expectedError,
                "Error message should match expected password rule message");

        logger.info("Test PASSED: Invalid password error displayed correctly");
    }

    @Test(priority = 3, description = "CP.03 - Invalid current password")
    @Story("Change Password - Invalid Current Password")
    @Description("Verify error when current password is incorrect")
    @Severity(SeverityLevel.CRITICAL)
    public void testInvalidCurrentPassword() {
        logger.info("Starting test: CP.03 - Invalid current password");
        String wrongCurrentPassword = "Wrong@123";
        String validNewPassword = "NewPass@456";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(wrongCurrentPassword, validNewPassword);

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed");

        String actualError = changePasswordPage.getCurrentPasswordErrorMessage();
        Assert.assertEquals(actualError, "Current password is invalid",
                "Error message should indicate current password is invalid");

        logger.info("Test PASSED: Current password error displayed correctly");
    }

    @Test(priority = 4, description = "CP.04 - New password empty")
    @Story("Change Password - Empty Fields")
    @Description("Verify error when new password field is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testNewPasswordEmpty() {
        logger.info("Starting test: CP.04 - New password empty");

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(ORIGINAL_PASSWORD, "");

        Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                "Empty password error should be displayed");

        String actualError = changePasswordPage.getPasswordEmptyMessage();
        Assert.assertEquals(actualError, "Please enter your password",
                "Error message should ask to enter password");

        logger.info("Test PASSED: Empty password error displayed correctly");
    }

    @Test(priority = 5, description = "CP.05 - Both fields empty")
    @Story("Change Password - Empty Fields")
    @Description("Verify error when both password fields are empty")
    @Severity(SeverityLevel.NORMAL)
    public void testBothFieldsEmpty() {
        logger.info("Starting test: CP.05 - Both fields empty");

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure("", "");

        boolean isErrorDisplayed = changePasswordPage.isPasswordEmptyMessageDisplayed() ||
                changePasswordPage.isCurrentPasswordErrorDisplayed();

        Assert.assertTrue(isErrorDisplayed,
                "At least one error message should be displayed when both fields are empty");

        logger.info("Test PASSED: Error displayed for empty fields");
    }

    @Test(priority = 6, description = "CP.06 - Current password empty")
    @Story("Change Password - Empty Fields")
    @Description("Verify error when current password field is empty")
    @Severity(SeverityLevel.NORMAL)
    public void testCurrentPasswordEmpty() {
        logger.info("Starting test: CP.06 - Current password empty");
        String validNewPassword = "NewPass@456";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure("", validNewPassword);

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed for empty current password");

        logger.info("Test PASSED: Empty current password error displayed");
    }

    @Test(priority = 7, description = "CP.07 - Invalid current format + empty new")
    @Story("Change Password - Combined Validation")
    @Description("Verify error priority when current password has invalid format and new is empty")
    @Severity(SeverityLevel.MINOR)
    public void testInvalidCurrentFormatAndEmptyNew() {
        logger.info("Starting test: CP.07 - Invalid current + empty new");
        String invalidCurrentPassword = "abc";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(invalidCurrentPassword, "");

        Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                "Empty new password error should be displayed");

        logger.info("Test PASSED: Empty new password takes priority");
    }

    @Test(priority = 8, description = "CP.08 - Invalid current format with valid new")
    @Story("Change Password - Invalid Current Password")
    @Description("Verify error when current password format is invalid but new is valid")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidCurrentFormat() {
        logger.info("Starting test: CP.08 - Invalid current format");
        String invalidCurrentPassword = "abc";
        String validNewPassword = "NewPass@456";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(invalidCurrentPassword, validNewPassword);

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed");

        logger.info("Test PASSED: Invalid current password error displayed");
    }

    @Test(priority = 9, description = "CP.09 - New password equals current")
    @Story("Change Password - Edge Cases")
    @Description("Verify behavior when new password equals current password")
    @Severity(SeverityLevel.MINOR)
    public void testNewPasswordEqualsCurrent() {
        logger.info("Starting test: CP.09 - New equals current");

        navigateToChangePassword();
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, ORIGINAL_PASSWORD);

        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page when new password equals current (treated as success)");

        logger.info("Test PASSED: Same password change handled correctly");
    }

    @Test(priority = 10, description = "CP.10 - Login with new password after change")
    @Story("Change Password - Authentication Verification")
    @Description("Verify old password becomes invalid and new password works after change")
    @Severity(SeverityLevel.BLOCKER)
    public void testLoginWithNewPasswordAfterChange() {
        logger.info("Starting test: CP.10 - Verify password change affects authentication");
        String newPassword = "TempPass@456";

        navigateToChangePassword();

        logger.info("[CP.10] Step 1: Changing password...");
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, newPassword);
        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page");
        navigateBackToHomePageManually();

        logger.info("[CP.10] Step 2: Logging out...");
        performLogout();
        MobileUI.sleep(0.5);

        logger.info("[CP.10] Step 3: Testing login with NEW password...");
        HomePage homePage = new HomePage();
        LoginPage loginPage = homePage.clickSignInButton();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        Assert.assertTrue(homePage.isLoggedIn(),
                "Should be able to login with NEW password");
        logger.info("[CP.10] Login with new password successful");

        logger.info("[CP.10] Step 4: Testing login with OLD password (should fail)...");
        performLogout();
        MobileUI.sleep(0.5);

        homePage = new HomePage();
        loginPage = homePage.clickSignInButton();
        loginPage.loginExpectFailure(TEST_EMAIL, ORIGINAL_PASSWORD);

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Should NOT be able to login with OLD password");
        logger.info("[CP.10] Login with old password failed as expected");

        logger.info("[CP.10] Step 5: Restoring original password (cleanup)...");
        loginPage.clearAllFields();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectSuccess(newPassword, ORIGINAL_PASSWORD);

        logger.info("[CP.10] Password restored to original - Test cleanup completed");
    }

    @Test(priority = 11, description = "CP.11 - Invalid special character")
    @Story("Change Password - Password Validation")
    @Description("Verify error when password contains invalid special character")
    @Severity(SeverityLevel.NORMAL)
    public void testInvalidSpecialCharacter() {
        logger.info("Starting test: CP.11 - Invalid special character");
        String passwordWithInvalidChar = "NewPass@123?";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(ORIGINAL_PASSWORD, passwordWithInvalidChar);

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Password rule error should be displayed for invalid special character");

        logger.info("Test PASSED: Invalid special character rejected");
    }

    @Test(priority = 12, description = "CP.12 - Min length boundary (8 characters)")
    @Story("Change Password - Boundary Value")
    @Description("Verify password with exactly 8 characters is accepted")
    @Severity(SeverityLevel.NORMAL)
    public void testMinLengthBoundary() {
        logger.info("Starting test: CP.12 - Minimum length boundary");
        String minLengthPassword = "A@b1cdef";

        navigateToChangePassword();

        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, minLengthPassword);
        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should accept password with exactly 8 characters meeting all requirements");

        navigateBackToHomePageManually();

        logger.info("CLEANUP: Changing back to original");
        navigateToChangePassword();
        changePasswordPage.changePasswordExpectSuccess(minLengthPassword, ORIGINAL_PASSWORD);
        logger.info("[CP.12] Password restored to original");
    }

    @Test(priority = 13, description = "CP.13 - Case-sensitive current password")
    @Story("Change Password - Case Sensitivity")
    @Description("Verify current password check is case-sensitive")
    @Severity(SeverityLevel.NORMAL)
    public void testCaseSensitiveCurrentPassword() {
        logger.info("Starting test: CP.13 - Case-sensitive current password");
        String wrongCasePassword = "kikiga18123@";
        String validNewPassword = "NewPass@456";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(wrongCasePassword, validNewPassword);

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed (password is case-sensitive)");

        logger.info("Test PASSED: Password case-sensitivity verified");
    }

    /*@Override
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
        System.out.println("[ChangePasswordTest] Navigating to Change Password page...");
        BasePage basePage = new BasePage();
        accountPage = basePage.clickAccountMenuItem();
        MobileUI.sleep(0.3);

        detailsPage = accountPage.clickOnAccountInformation();
        changePasswordPage = detailsPage.clickChangePasswordButton();

        Assert.assertTrue(changePasswordPage.isChangePasswordPageDisplayed(),
                "Change Password page should be displayed");
        System.out.println("[ChangePasswordTest] Successfully navigated to Change Password page");
    }

    private void navigateBackToHomePageManually() {
        System.out.println("[ChangePasswordTest] Manually navigating back to HomePage...");
        int maxAttempts = 3;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                HomePage hp = new HomePage();
                if (hp.isHomePageDisplayed()) {
                    System.out.println("[ChangePasswordTest] Already on HomePage");
                    return;
                }

                DriverManager.getDriver().navigate().back();
                MobileUI.sleep(0.3);

            } catch (Exception e) {
                System.out.println("[ChangePasswordTest] Navigate attempt " + (i+1) + " exception: " + e.getMessage());
            }
        }

        HomePage hp = new HomePage();
        if (!hp.isHomePageDisplayed()) {
            System.out.println("[ChangePasswordTest] WARNING: Not on HomePage after manual navigation");
        }
    }

    @Test(priority = 1, description = "CP.01 - Change password success with valid credentials")
    public void testChangePasswordSuccess() {
        String newPassword = "NewPass@123";

        navigateToChangePassword();

        //Change password from ORIGINAL → NEW
        System.out.println("[CP.01] Step 1: Changing password from original to new...");
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, newPassword);

        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page after successful password change");
        System.out.println("[CP.01] Password changed successfully to new password");

        // Navigate to HomePage before logout
        navigateBackToHomePageManually();

        // Verify new password works (logout and login with new password)
        System.out.println("[CP.01] Step 2: Verifying new password by re-login...");
        performLogout();
        MobileUI.sleep(0.5);

        HomePage homePage = new HomePage();
        LoginPage loginPage = homePage.clickSignInButton();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        Assert.assertTrue(homePage.isLoggedIn(),
                "Should be able to login with new password");
        System.out.println("[CP.01] Successfully logged in with new password");

        // Change password BACK to original for test independence
        System.out.println("[CP.01] Step 3: Changing password back to original (cleanup)...");
        navigateToChangePassword();
        detailsPage = changePasswordPage.changePasswordExpectSuccess(newPassword, ORIGINAL_PASSWORD);

        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page after changing back");
        System.out.println("[CP.01] Password restored to original - Test cleanup completed");
    }

    @Test(priority = 2, description = "CP.02 - Invalid new password format")
    public void testInvalidNewPasswordFormat() {
        String invalidPassword = "abc12345";

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
        String invalidCurrentPassword = "abc";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(invalidCurrentPassword, "");

        // Should show error for empty new password (higher priority)
        Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                "Empty new password error should be displayed");
    }

    @Test(priority = 8, description = "CP.08 - Invalid current format with valid new")
    public void testInvalidCurrentFormat() {
        String invalidCurrentPassword = "abc";
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

        //Change password
        System.out.println("[CP.10] Step 1: Changing password...");
        detailsPage = changePasswordPage.changePasswordExpectSuccess(ORIGINAL_PASSWORD, newPassword);
        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page");
        navigateBackToHomePageManually();

        //Logout
        System.out.println("[CP.10] Step 2: Logging out...");
        performLogout();
        MobileUI.sleep(0.5);

        //Try login with NEW password (should succeed)
        System.out.println("[CP.10] Step 3: Testing login with NEW password...");
        HomePage homePage = new HomePage();
        LoginPage loginPage = homePage.clickSignInButton();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        Assert.assertTrue(homePage.isLoggedIn(),
                "Should be able to login with NEW password");
        System.out.println("[CP.10] Login with new password successful");

        //Logout and try OLD password (should fail)
        System.out.println("[CP.10] Step 4: Testing login with OLD password (should fail)...");
        performLogout();
        MobileUI.sleep(0.5);

        homePage = new HomePage();
        loginPage = homePage.clickSignInButton();
        loginPage.loginExpectFailure(TEST_EMAIL, ORIGINAL_PASSWORD);

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Should NOT be able to login with OLD password");
        System.out.println("[CP.10] Login with old password failed as expected");

        //Restore original password
        System.out.println("[CP.10] Step 5: Restoring original password (cleanup)...");
        loginPage.clearAllFields();
        homePage = loginPage.loginExpectSuccess(TEST_EMAIL, newPassword);

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectSuccess(newPassword, ORIGINAL_PASSWORD);

        System.out.println("[CP.10] Password restored to original - Test cleanup completed");
    }

    @Test(priority = 11, description = "CP.11 - Invalid special character")
    public void testInvalidSpecialCharacter() {
        String passwordWithInvalidChar = "NewPass@123?";

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

        // Navigate back to HomePage before next navigation
        navigateBackToHomePageManually();

        // CLEANUP: Change back to original
        navigateToChangePassword();
        changePasswordPage.changePasswordExpectSuccess(minLengthPassword, ORIGINAL_PASSWORD);
        System.out.println("[CP.12] Password restored to original");
    }

    @Test(priority = 13, description = "CP.13 - Case-sensitive current password")
    public void testCaseSensitiveCurrentPassword() {
        // Original password: Kikiga18123@
        // Try with different case: kikiga18123@
        String wrongCasePassword = "kikiga18123@"; // All lowercase 'k'
        String validNewPassword = "NewPass@123";

        navigateToChangePassword();
        changePasswordPage.changePasswordExpectFailure(wrongCasePassword, validNewPassword);

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "'Current password is invalid' error should be displayed (password is case-sensitive)");
    }*/
}
