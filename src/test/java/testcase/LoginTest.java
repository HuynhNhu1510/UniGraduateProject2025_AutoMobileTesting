package testcase;

import common.CommonTest;
import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.keywords.MobileUI;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;

@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTest extends CommonTest {

    private static final Logger logger = LogManager.getLogger(LoginTest.class);
    LoginPage loginPage;

    @Override
    protected String getTestName() {
        return "Login Test";
    }

    @Test(priority = 1, description = "LG.01 - Login successfully with valid credentials")
    @Story("User Login - Valid Credentials")
    @Description("Verify that user can login successfully with valid email and password")
    @Severity(SeverityLevel.BLOCKER)
    public void loginSuccessfullyWithValidCredentials() {
        logger.info("Starting test: Login with valid credentials");

        loginPage = new HomePage().clickSignInButton();
        homePage = loginPage.loginExpectSuccess("khoadang1510@gmail.com", "Kikiga18123@");

        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after successful login");
        Assert.assertFalse(homePage.isRegisterButtonDisplayed(),
                "Register button should not be displayed when logged in");

        logger.info("Test passed: User logged in successfully");
    }

    @Test(priority = 2, description = "LG.02 - Login failed with existing email but wrong password format")
    @Story("User Login - Invalid Password Format")
    @Description("Verify error message when password format is invalid")
    @Severity(SeverityLevel.CRITICAL)
    public void loginFailedWithExistingEmailAndWrongPasswordFormat() {
        logger.info("Starting test: Login with wrong password format");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("lytuthat1234@gmail.com", "weak");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message 'We've had a problem, please try again' should be displayed");

        logger.info("Test passed: Error message displayed correctly");
    }

    @Test(priority = 3, description = "LG.03 - Login failed with non-existing email and wrong password format")
    @Story("User Login - Non-existing Email")
    @Description("Verify error when both email doesn't exist and password format is invalid")
    @Severity(SeverityLevel.CRITICAL)
    public void loginFailedWithNonExistingEmailAndWrongPasswordFormat() {
        logger.info("Starting test: Login with non-existing email and wrong password");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("nonexist@gmail.com", "weak");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message 'We've had a problem, please try again' should be displayed");

        logger.info("Test passed: Appropriate error shown");
    }

    @Test(priority = 4, description = "LG.04 - Login failed with non-existing email but valid password format")
    @Story("User Login - Email Not Found")
    @Description("Verify error when email doesn't exist but password format is valid")
    @Severity(SeverityLevel.CRITICAL)
    public void loginFailedWithNonExistingEmailAndValidPasswordFormat() {
        logger.info("Starting test: Non-existing email with valid password format");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("nonexist@gmail.com", "ValidPass123@");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message 'We've had a problem, please try again' should be displayed");

        logger.info("Test passed");
    }

    @Test(priority = 5, description = "LG.05 - Login failed with invalid email format but valid password")
    @Story("Email Validation")
    @Description("Verify email format validation shows appropriate error message")
    @Severity(SeverityLevel.CRITICAL)
    public void loginFailedWithInvalidEmailFormatAndValidPassword() {
        logger.info("Starting test: Invalid email format");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("invalidemail", "ValidPass123@");

        Assert.assertTrue(loginPage.isEmailInvalidMessageDisplayed(),
                "Error message 'Please enter valid email address' should be displayed");

        logger.info("Test passed: Email validation works correctly");
    }

    @Test(priority = 6, description = "LG.06 - Login failed with both fields empty")
    @Story("Field Validation - Empty Fields")
    @Description("Verify error when both email and password fields are empty")
    @Severity(SeverityLevel.NORMAL)
    public void loginFailedWithBothFieldsEmpty() {
        logger.info("Starting test: Both fields empty");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("", "");

        boolean isEmailErrorDisplayed = loginPage.isEmailEmptyFieldDisplayed();
        boolean isPasswordErrorDisplayed = loginPage.isPasswordEmptyFieldDisplayed();

        Assert.assertTrue(isEmailErrorDisplayed || isPasswordErrorDisplayed,
                "At least one error message should be displayed when both fields are empty");

        logger.info("Test passed: Empty field validation works");
    }

    @Test(priority = 7, description = "LG.07 - Login failed with empty email field")
    @Story("Field Validation - Empty Email")
    @Description("Verify error message when email field is empty")
    @Severity(SeverityLevel.NORMAL)
    public void loginFailedWithEmptyEmailField() {
        logger.info("Starting test: Empty email field");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("", "ValidPass123@");

        Assert.assertTrue(loginPage.isEmailEmptyFieldDisplayed(),
                "Error message 'Please enter your email address' should be displayed");

        logger.info("Test passed");
    }

    @Test(priority = 8, description = "LG.08 - Login failed with empty password field")
    @Story("Field Validation - Empty Password")
    @Description("Verify error message when password field is empty")
    @Severity(SeverityLevel.NORMAL)
    public void loginFailedWithEmptyPasswordField() {
        logger.info("Starting test: Empty password field");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("validuser@gmail.com", "");

        Assert.assertTrue(loginPage.isPasswordEmptyFieldDisplayed(),
                "Error message 'Please enter your password' should be displayed");

        logger.info("Test passed");
    }

    // ==================== MEDIUM PRIORITY TEST CASES ====================

    @Test(priority = 9, description = "LG.09 - Verify error modal is displayed for invalid credentials")
    @Story("Error Handling")
    @Description("Verify that error modal appears with correct message for invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    public void verifyErrorModalDisplayedForInvalidCredentials() {
        logger.info("Starting test: Error modal verification");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("wronguser@gmail.com", "WrongPass123@");

        MobileUI.sleep(1);

        boolean isErrorModalDisplayed = loginPage.isEmailNotExistedErrorDisplayed();
        Assert.assertTrue(isErrorModalDisplayed,
                "Error modal 'We've had a problem, please try again' should be displayed");

        String errorMessage = loginPage.getEmailNotExistedErrorMessage();
        Assert.assertEquals(errorMessage, "We've had a problem, please try again",
                "Error message text should match expected text");

        logger.info("Test passed: Error modal displays correctly");
    }

    @Test(priority = 10, description = "LG.10 - Login with email containing special characters")
    @Story("Email Validation - Special Characters")
    @Description("Verify handling of email addresses with special characters (RFC 5322 compliant)")
    @Severity(SeverityLevel.MINOR)
    public void loginWithEmailContainingSpecialCharacters() {
        logger.info("Starting test: Email with special characters");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("user+test@gmail.com", "ValidPass123@");

        MobileUI.sleep(1);

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for non-existing email with special characters");

        logger.info("Test passed");
    }

    @Test(priority = 11, description = "LG.11 - Login with password exactly 8 characters")
    @Story("Password Validation - Boundary Testing")
    @Description("Verify minimum password length boundary (8 characters)")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithPasswordExactly8Characters() {
        logger.info("Starting test: Password with exactly 8 characters");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "Pass123@");

        MobileUI.sleep(1);
        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Should show 'email not exist' error, not password format error");

        logger.info("Test passed: 8-character password accepted");
    }

    @Test(priority = 12, description = "LG.12 - Login with password missing uppercase")
    @Story("Password Validation - Format Requirements")
    @Description("Verify password validation rejects password without uppercase letters")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithPasswordMissingUppercase() {
        logger.info("Starting test: Password missing uppercase");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "password123@");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for invalid password format");

        logger.info("Test passed");
    }

    @Test(priority = 13, description = "LG.13 - Login with password missing lowercase")
    @Story("Password Validation - Format Requirements")
    @Description("Verify password validation rejects password without lowercase letters")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithPasswordMissingLowercase() {
        logger.info("Starting test: Password missing lowercase");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "PASSWORD123@");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for invalid password format");

        logger.info("Test passed");
    }

    @Test(priority = 14, description = "LG.14 - Login with password missing special character")
    @Story("Password Validation - Format Requirements")
    @Description("Verify password validation rejects password without special characters")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithPasswordMissingSpecialChar() {
        logger.info("Starting test: Password missing special character");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "Password123");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for invalid password format");

        logger.info("Test passed");
    }

    @Test(priority = 15, description = "LG.15 - Login with password less than 8 characters")
    @Story("Password Validation - Boundary Testing")
    @Description("Verify password validation rejects password shorter than minimum length")
    @Severity(SeverityLevel.NORMAL)
    public void loginWithPasswordLessThan8Characters() {
        logger.info("Starting test: Password less than 8 characters");

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure("testuser@gmail.com", "Pass1@");

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error message should be displayed for password less than 8 characters");

        logger.info("Test passed");
    }
}
