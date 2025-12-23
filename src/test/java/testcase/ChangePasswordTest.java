package testcase;

import common.CommonTest;
import org.example.keywords.MobileUI;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import page.*;

public class ChangePasswordTest extends CommonTest {

    ChangePasswordPage changePasswordPage;
    DetailsAndPasswordPage detailsAndPasswordPage;
    AccountPage accountPage;
    LoginPage loginPage;

    // Valid credentials for testing
    private static final String VALID_EMAIL = "lytuthat1234@gmail.com";
    private static final String VALID_CURRENT_PASSWORD = "Nhu151003@";

    @Override
    protected String getTestName() {
        return "Change Password Test";
    }

    @BeforeMethod
    public void setupPreconditionAndNavigateToChangePasswordScreen() {
        System.out.println("[" + getTestName() + "] ========== SETTING UP PRECONDITION ==========");

        // Step 1: Ensure user is logged in
        if (!homePage.isLoggedIn()) {
            System.out.println("[" + getTestName() + "] User not logged in → Logging in...");
            loginPage = homePage.clickSignInButton();
            homePage = loginPage.loginExpectSuccess(VALID_EMAIL, VALID_CURRENT_PASSWORD);

            // Verify login successful
            Assert.assertTrue(homePage.isLoggedIn(),
                    "PRECONDITION FAILED: User should be logged in before testing Change Password");
            System.out.println("[" + getTestName() + "] ✓ User logged in successfully");
        } else {
            System.out.println("[" + getTestName() + "] ✓ User already logged in");
        }

        // Step 2: Navigate to Change Password screen
        System.out.println("[" + getTestName() + "] Navigating to Change Password screen...");

        BasePage basePage = new BasePage();
        accountPage = basePage.clickAccountMenuItem();
        MobileUI.sleep(0.2);

        detailsAndPasswordPage = accountPage.clickOnAccountInformation();
        MobileUI.sleep(0.2);

        changePasswordPage = detailsAndPasswordPage.clickChangePasswordButton();
        MobileUI.sleep(0.3);

        // Verify we are on Change Password screen
        Assert.assertTrue(changePasswordPage.isChangePasswordTitleDisplayed(),
                "PRECONDITION FAILED: Should be on Change Password screen");

        System.out.println("[" + getTestName() + "] ✓ Successfully navigated to Change Password screen");
        System.out.println("[" + getTestName() + "] ========== PRECONDITION SETUP COMPLETED ==========\n");
    }

    // ==================== HIGH PRIORITY TEST CASES ====================

    @Test(priority = 1, description = "CP.01 - Change password successfully with valid current and new password")
    public void changePasswordSuccessfullyWithValidCredentials() {
        System.out.println("[Test CP.01] Testing successful password change...");

        detailsAndPasswordPage = changePasswordPage.changePasswordExpectSuccess(
                VALID_CURRENT_PASSWORD,
                "NewPass123@"
        );

        // Verify navigation to Details & password page
        Assert.assertTrue(detailsAndPasswordPage.isDetailsAndPasswordPageDisplayed(),
                "Should navigate to 'Details & password' page after successful password change");

        String pageTitle = detailsAndPasswordPage.getPageTitle();
        Assert.assertEquals(pageTitle, "Details & password",
                "Page title should be 'Details & password'");

        System.out.println("[Test CP.01] ✓ Password changed successfully and navigated to correct page");
    }

    @Test(priority = 2, description = "CP.02 - Cannot save with empty current password and valid new password")
    public void cannotSaveWithEmptyCurrentPasswordAndValidNewPassword() {
        System.out.println("[Test CP.02] Testing with empty current password...");

        changePasswordPage.changePasswordExpectFailure("", "NewPass123@");

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "Error message 'Current password is invalid' should be displayed");

        String errorMessage = changePasswordPage.getCurrentPasswordErrorMessage();
        Assert.assertEquals(errorMessage, "Current password is invalid",
                "Error message text should match");

        System.out.println("[Test CP.02] ✓ Error displayed correctly");
    }

    @Test(priority = 3, description = "CP.03 - Cannot save with valid current password and empty new password")
    public void cannotSaveWithValidCurrentPasswordAndEmptyNewPassword() {
        System.out.println("[Test CP.03] Testing with empty new password...");

        changePasswordPage.changePasswordExpectFailure(VALID_CURRENT_PASSWORD, "");

        Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                "Error message 'Please enter your password' should be displayed");

        String errorMessage = changePasswordPage.getPasswordEmptyMessage();
        Assert.assertEquals(errorMessage, "Please enter your password",
                "Error message text should match");

        System.out.println("[Test CP.03] ✓ Error displayed correctly");
    }

    @Test(priority = 4, description = "CP.04 - Cannot save with both fields empty")
    public void cannotSaveWithBothFieldsEmpty() {
        System.out.println("[Test CP.04] Testing with both fields empty...");

        changePasswordPage.changePasswordExpectFailure("", "");

        Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                "Error message 'Please enter your password' should be displayed for new password field");

        System.out.println("[Test CP.04] ✓ Error displayed correctly");
    }

    @Test(priority = 5, description = "CP.05 - Show error when current password is incorrect")
    public void showErrorWhenCurrentPasswordIsIncorrect() {
        System.out.println("[Test CP.05] Testing with incorrect current password...");

        changePasswordPage.changePasswordExpectFailure("WrongPass123@", "NewPass123@");

        Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                "Error message 'Current password is invalid' should be displayed");

        System.out.println("[Test CP.05] ✓ Error displayed correctly");
    }

    @Test(priority = 6, description = "CP.06 - Show format error when new password is invalid (missing uppercase)")
    public void showFormatErrorWhenNewPasswordMissingUppercase() {
        System.out.println("[Test CP.06] Testing new password without uppercase...");

        changePasswordPage.changePasswordExpectFailure(VALID_CURRENT_PASSWORD, "newpass123@");

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password format should be displayed");

        System.out.println("[Test CP.06] ✓ Format error displayed correctly");
    }

    @Test(priority = 7, description = "CP.07 - Show format error when new password is less than 8 characters")
    public void showFormatErrorWhenNewPasswordLessThan8Characters() {
        System.out.println("[Test CP.07] Testing new password with less than 8 characters...");

        changePasswordPage.changePasswordExpectFailure(VALID_CURRENT_PASSWORD, "Pass1@");

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password format should be displayed");

        System.out.println("[Test CP.07] ✓ Format error displayed correctly");
    }

    @Test(priority = 8, description = "CP.08 - Show format error when new password missing special character")
    public void showFormatErrorWhenNewPasswordMissingSpecialCharacter() {
        System.out.println("[Test CP.08] Testing new password without special character...");

        changePasswordPage.changePasswordExpectFailure(VALID_CURRENT_PASSWORD, "Newpass123");

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password format should be displayed");

        System.out.println("[Test CP.08] ✓ Format error displayed correctly");
    }

    // ==================== MEDIUM PRIORITY TEST CASES ====================

    @Test(priority = 9, description = "CP.09 - Show format error when current password empty and new password invalid")
    public void showFormatErrorWhenCurrentEmptyAndNewPasswordInvalid() {
        System.out.println("[Test CP.09] Testing with empty current and invalid new password...");

        changePasswordPage.changePasswordExpectFailure("", "weak");

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password format should be displayed");

        System.out.println("[Test CP.09] ✓ Format error displayed correctly");
    }

    @Test(priority = 10, description = "CP.10 - Show format error when new password missing lowercase")
    public void showFormatErrorWhenNewPasswordMissingLowercase() {
        System.out.println("[Test CP.10] Testing new password without lowercase...");

        changePasswordPage.changePasswordExpectFailure(VALID_CURRENT_PASSWORD, "NEWPASS123@");

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password format should be displayed");

        System.out.println("[Test CP.10] ✓ Format error displayed correctly");
    }

    @Test(priority = 11, description = "CP.11 - Show format error when new password missing number")
    public void showFormatErrorWhenNewPasswordMissingNumber() {
        System.out.println("[Test CP.11] Testing new password without number...");

        changePasswordPage.changePasswordExpectFailure(VALID_CURRENT_PASSWORD, "NewPassword@");

        Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password format should be displayed");

        System.out.println("[Test CP.11] ✓ Format error displayed correctly");
    }

    @Test(priority = 12, description = "CP.12 - Verify new password with exactly 8 characters is accepted and navigates correctly")
    public void verifyNewPasswordWithExactly8CharactersIsAcceptedAndNavigatesCorrectly() {
        System.out.println("[Test CP.12] Testing with 8-character password...");

        detailsAndPasswordPage = changePasswordPage.changePasswordExpectSuccess(
                VALID_CURRENT_PASSWORD,
                "Pass123@"
        );

        // Verify navigation to Details & password page
        Assert.assertTrue(detailsAndPasswordPage.isDetailsAndPasswordPageDisplayed(),
                "Should navigate to 'Details & password' page with 8-character password");

        String pageTitle = detailsAndPasswordPage.getPageTitle();
        Assert.assertEquals(pageTitle, "Details & password",
                "Page title should be 'Details & password'");

        System.out.println("[Test CP.12] ✓ 8-character password accepted and navigated correctly");
    }

    @Test(priority = 13, description = "CP.13 - Show password empty error when current password is incorrect and new password is empty")
    public void showPasswordEmptyErrorWhenCurrentIncorrectAndNewPasswordEmpty() {
        System.out.println("[Test CP.13] Testing with incorrect current password and empty new password...");

        changePasswordPage.changePasswordExpectFailure("WrongPass123@", "");

        Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                "Error message 'Please enter your password' should be displayed for empty new password");

        String errorMessage = changePasswordPage.getPasswordEmptyMessage();
        Assert.assertEquals(errorMessage, "Please enter your password",
                "Error message text should match");

        System.out.println("[Test CP.13] ✓ Error displayed correctly");
    }
}
