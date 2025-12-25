package testcase;

import common.CommonTest;
import org.example.keywords.MobileUI;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;

public class LoginTest extends CommonTest {

    LoginPage loginPage;

    @Override
    protected String getTestName() {
        return "Login Test";
    }

    @Test(priority = 1, description = "LG.01 - Login successfully with valid credentials")
    public void loginSuccessfullyWithValidCredentials() {
        loginPage = new HomePage().clickSignInButton();
        homePage = loginPage.loginExpectSuccess("hpqn1510@gmail.com", "Kikiga18123@");

        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after successful login");
        Assert.assertFalse(homePage.isRegisterButtonDisplayed(),
                "Register button should not be displayed when logged in");
    }

    @Test(priority = 2, description = "LG.02 - Login failed with existing email but wrong password format")
    public void loginFailedWithExistingEmailAndWrongPasswordFormat() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("lytuthat1234@gmail.com", "weak");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message 'We've had a problem, please try again' should be displayed");
    }

    @Test(priority = 3, description = "LG.03 - Login failed with non-existing email and wrong password format")
    public void loginFailedWithNonExistingEmailAndWrongPasswordFormat() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("nonexist@gmail.com", "weak");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message 'We've had a problem, please try again' should be displayed");
    }

    @Test(priority = 4, description = "LG.04 - Login failed with non-existing email but valid password format")
    public void loginFailedWithNonExistingEmailAndValidPasswordFormat() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("nonexist@gmail.com", "ValidPass123@");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message 'We've had a problem, please try again' should be displayed");
    }

    @Test(priority = 5, description = "LG.05 - Login failed with invalid email format but valid password")
    public void loginFailedWithInvalidEmailFormatAndValidPassword() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("invalidemail", "ValidPass123@");

        Assert.assertTrue(loginPage.isEmailInvalidMessageDisplayed(),
                "Error message 'Please enter valid email address' should be displayed");
    }

    @Test(priority = 6, description = "LG.06 - Login failed with both fields empty")
    public void loginFailedWithBothFieldsEmpty() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("", "");

        boolean isEmailErrorDisplayed = loginPage.isEmailEmptyFieldDisplayed();
        boolean isPasswordErrorDisplayed = loginPage.isPasswordEmptyFieldDisplayed();

        Assert.assertTrue(isEmailErrorDisplayed || isPasswordErrorDisplayed,
                "At least one error message should be displayed when both fields are empty");
    }

    @Test(priority = 7, description = "LG.07 - Login failed with empty email field")
    public void loginFailedWithEmptyEmailField() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("", "ValidPass123@");

        Assert.assertTrue(loginPage.isEmailEmptyFieldDisplayed(),
                "Error message 'Please enter your email address' should be displayed");
    }

    @Test(priority = 8, description = "LG.08 - Login failed with empty password field")
    public void loginFailedWithEmptyPasswordField() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("validuser@gmail.com", "");

        Assert.assertTrue(loginPage.isPasswordEmptyFieldDisplayed(),
                "Error message 'Please enter your password' should be displayed");
    }

    // ==================== MEDIUM PRIORITY TEST CASES ====================

    @Test(priority = 9, description = "LG.09 - Verify error modal is displayed for invalid credentials")
    public void verifyErrorModalDisplayedForInvalidCredentials() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("wronguser@gmail.com", "WrongPass123@");

        // Wait for error message to appear
        MobileUI.sleep(1);

        boolean isErrorModalDisplayed = loginPage.isEmailNotExistedErrorDisplayed();
        Assert.assertTrue(isErrorModalDisplayed,
                "Error modal 'We've had a problem, please try again' should be displayed");

        String errorMessage = loginPage.getEmailNotExistedErrorMessage();
        Assert.assertEquals(errorMessage, "We've had a problem, please try again",
                "Error message text should match expected text");
    }

    @Test(priority = 10, description = "LG.10 - Login with email containing special characters")
    public void loginWithEmailContainingSpecialCharacters() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("user+test@gmail.com", "ValidPass123@");

        MobileUI.sleep(1);

        // Should show error if email doesn't exist
        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for non-existing email with special characters");
    }

    @Test(priority = 11, description = "LG.11 - Login with password exactly 8 characters")
    public void loginWithPasswordExactly8Characters() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "Pass123@");

        // Should allow 8 character password that meets requirements
        MobileUI.sleep(1);
        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Should show 'email not exist' error, not password format error");
    }

    @Test(priority = 12, description = "LG.12 - Login with password missing uppercase")
    public void loginWithPasswordMissingUppercase() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "password123@");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for invalid password format");
    }

    @Test(priority = 13, description = "LG.13 - Login with password missing lowercase")
    public void loginWithPasswordMissingLowercase() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "PASSWORD123@");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for invalid password format");
    }

    @Test(priority = 14, description = "LG.14 - Login with password missing special character")
    public void loginWithPasswordMissingSpecialChar() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "Password123");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for invalid password format");
    }

    @Test(priority = 15, description = "LG.15 - Login with password less than 8 characters")
    public void loginWithPasswordLessThan8Characters() {
        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "Pass1@");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for password less than 8 characters");
    }

}
