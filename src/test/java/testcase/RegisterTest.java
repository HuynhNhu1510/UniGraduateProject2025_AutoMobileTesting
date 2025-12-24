package testcase;

import common.CommonTest;
import page.HomePage;
import page.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class RegisterTest extends CommonTest {
    RegisterPage registerPage;

    @Override
    protected String getTestName() {
        return "Register Test";
    }

    @Test(priority = 2, description = "RG.02 - Register successfully with valid information")
    public void registerSuccessfullyWithValidData() {
        registerPage = new HomePage().clickRegisterButton();
        homePage = registerPage.registerExpectSuccess("Huynh Alice", "danthuy18123@gmail.com", "Kikiga18@");
        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after successful registration");
        Assert.assertFalse(homePage.isRegisterButtonDisplayed(),
                "Register button should not be displayed when logged in");
    }

    @Test(priority = 3, description = "RG.03 - Register failed with email already existed")
    public void registerFailedWithEmailAlreadyExisted() {
        registerPage = new HomePage().clickRegisterButton();
        System.out.println("[Test] Navigated to Register Page");
        registerPage.registerExpectFailure("Huynh Alice", "lytuthat1234@gmail.com", "Kikiga18123@");

        //Verify error message (đã có wait bên trong method)
        System.out.println("[Test] Verifying error message...");
        boolean isErrorDisplayed = registerPage.isEmailExistedMessageDisplayed();

        Assert.assertTrue(isErrorDisplayed,
                "ERROR: 'User already exists' message should be displayed but was not found after 5 seconds wait");

        System.out.println("[Test] Test PASSED: Error message displayed correctly");
    }

    @Test(priority = 4, description = "RG.04 - Register failed with empty full name")
    public void registerFailedWithEmptyFullName() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("", "validtest@gmail.com", "Valid123@");

        Assert.assertTrue(registerPage.isFullNameInvalidMessageDisplayed(),
                "Error message 'Please enter a full name' should be displayed");
    }

    @Test(priority = 5, description = "RG.05 - Register failed with empty email")
    public void registerFailedWithEmptyEmail() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("Test User", "", "Valid123@");

        Assert.assertTrue(registerPage.isEmailEmptyMessageDisplayed(),
                "Error message 'Please enter your email address' should be displayed");
    }

    @Test(priority = 6, description = "RG.06 - Register failed with empty password")
    public void registerFailedWithEmptyPassword() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("Test User", "validtest@gmail.com", "");

        Assert.assertTrue(registerPage.isPasswordEmptyMessageDisplayed(),
                "Error message 'Please enter your password' should be displayed");
    }

    @Test(priority = 7, description = "RG.07 - Register failed with invalid full name (single word)")
    public void registerFailedWithInvalidFullName() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("kiki", "validtest@gmail.com", "Valid123@");

        Assert.assertTrue(registerPage.isFullNameInvalidMessageDisplayed(),
                "Error message 'Please enter a full name' should be displayed for single word name");
    }

    @Test(priority = 8, description = "RG.08 - Register failed with invalid email format")
    public void registerFailedWithInvalidEmailFormat() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("Test User", "invalidemail", "Valid123@");

        Assert.assertTrue(registerPage.isEmailInvalidMessageDisplayed(),
                "Error message 'Please enter valid email address' should be displayed");
    }

    @Test(priority = 9, description = "RG.09 - Register failed with password less than 8 characters")
    public void registerFailedWithShortPassword() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("Test User", "validtest@gmail.com", "Val1@");

        Assert.assertTrue(registerPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password requirements should be displayed");
    }

    @Test(priority = 10, description = "RG.10 - Register failed with password missing uppercase")
    public void registerFailedWithPasswordMissingUppercase() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("Test User", "validtest@gmail.com", "valid123@");

        Assert.assertTrue(registerPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password requirements should be displayed");
    }

    @Test(priority = 11, description = "RG.11 - Register failed with password missing lowercase")
    public void registerFailedWithPasswordMissingLowercase() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("Test User", "validtest@gmail.com", "VALID123@");

        Assert.assertTrue(registerPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password requirements should be displayed");
    }

    @Test(priority = 12, description = "RG.12 - Register failed with password missing special character")
    public void registerFailedWithPasswordMissingSpecialChar() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("Test User", "validtest@gmail.com", "Valid1234");

        Assert.assertTrue(registerPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password requirements should be displayed");
    }

    @Test(priority = 13, description = "RG.13 - Register failed with all fields empty")
    public void registerFailedWithAllFieldsEmpty() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("", "", "");

        // Check if any error message is displayed (usually full name error appears first)
        boolean isAnyErrorDisplayed = registerPage.isFullNameInvalidMessageDisplayed() ||
                registerPage.isEmailEmptyMessageDisplayed() ||
                registerPage.isPasswordEmptyMessageDisplayed();

        Assert.assertTrue(isAnyErrorDisplayed,
                "At least one error message should be displayed when all fields are empty");
    }

    // ==================== MEDIUM PRIORITY TEST CASES ====================

    @Test(priority = 14, description = "RG.14 - Register successfully with full name containing special characters")
    public void registerSuccessfullyWithSpecialCharactersInName() {
        registerPage = new HomePage().clickRegisterButton();
        homePage = registerPage.registerExpectSuccess("Nguyễn Văn", "specialchar@gmail.com", "Valid123@");

        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after successful registration with special characters in name");
    }

    @Test(priority = 15, description = "RG.15 - Register successfully with full name containing numbers")
    public void registerSuccessfullyWithNumbersInName() {
        registerPage = new HomePage().clickRegisterButton();
        homePage = registerPage.registerExpectSuccess("kiki 1", "numbertest@gmail.com", "Valid123@");

        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after successful registration with numbers in name");
    }

    @Test(priority = 16, description = "RG.21 - Register failed with password missing number")
    public void registerFailedWithPasswordMissingNumber() {
        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure("Test User", "validtest@gmail.com", "ValidPass@");

        Assert.assertTrue(registerPage.isPasswordInvalidMessageDisplayed(),
                "Error message about password requirements should be displayed");
    }

    @Test(priority = 17, description = "RG.22 - Register with minimum valid full name")
    public void registerWithMinimumValidFullName() {
        registerPage = new HomePage().clickRegisterButton();
        homePage = registerPage.registerExpectSuccess("A B", "minname@gmail.com", "Valid123@");

        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in with minimal full name (2 characters)");
    }
}
