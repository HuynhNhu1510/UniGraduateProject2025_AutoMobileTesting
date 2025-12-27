package testcase_datadriven;

import common.CommonTest;
import dataprovider.TestDataProviders;
import org.example.model.LoginTestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;

public class LoginTestDataDriven extends CommonTest {

    LoginPage loginPage;

    @Override
    protected String getTestName() {
        return "Login Test - Data Driven";
    }

    // ==================== EQUIVALENCE PARTITIONING TESTS ====================

    @Test(dataProvider = "loginInvalidEmailFormat_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Login with invalid email formats - Equivalence Partitioning")
    public void testLoginInvalidEmailFormat_EP(LoginTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());
        System.out.println("Description: " + testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        // Verify expected result
        switch (testData.getExpectedResult()) {
            case "email_invalid":
                Assert.assertTrue(loginPage.isEmailInvalidMessageDisplayed(),
                        "Email invalid message should be displayed for: " + testData.getTestId());

                String actualMsg = loginPage.getEmailInvalidMessage();
                Assert.assertEquals(actualMsg, testData.getExpectedMessage(),
                        "Error message should match for: " + testData.getTestId());
                break;

            default:
                Assert.fail("Unknown expected result: " + testData.getExpectedResult());
        }
        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    @Test(dataProvider = "loginInvalidPasswordFormat_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Login with invalid password formats - Equivalence Partitioning")
    public void testLoginInvalidPasswordFormat_EP(LoginTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        // All invalid password formats should show credential error
        if ("credential_error".equals(testData.getExpectedResult())) {
            Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                    "Credential error should be displayed for: " + testData.getTestId());

            String actualMsg = loginPage.getEmailNotExistedErrorMessage();
            Assert.assertEquals(actualMsg, testData.getExpectedMessage(),
                    "Error message should match for: " + testData.getTestId());
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    @Test(dataProvider = "loginInvalidSpecialChars_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Login with invalid special characters in password")
    public void testLoginInvalidSpecialCharacters_EP(LoginTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());
        System.out.println("Testing: " + testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error should be displayed for invalid special char: " + testData.getTestId());

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    @Test(dataProvider = "loginEmptyFields_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Login with empty fields - Equivalence Partitioning")
    public void testLoginEmptyFields_EP(LoginTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        // Handle different empty field scenarios
        switch (testData.getExpectedResult()) {
            case "empty_field_error":
            case "email_empty":
                Assert.assertTrue(loginPage.isEmailEmptyFieldDisplayed(),
                        "Email empty error should be displayed");
                break;

            case "password_empty":
                Assert.assertTrue(loginPage.isPasswordEmptyFieldDisplayed(),
                        "Password empty error should be displayed");
                break;

            case "email_invalid":
                Assert.assertTrue(loginPage.isEmailInvalidMessageDisplayed(),
                        "Email invalid error should be displayed");
                break;

            default:
                // For "empty_field_error" - at least one error should show
                boolean hasError = loginPage.isEmailEmptyFieldDisplayed() ||
                        loginPage.isPasswordEmptyFieldDisplayed();
                Assert.assertTrue(hasError, "At least one empty field error should be displayed");
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    @Test(dataProvider = "loginPasswordLength_BVA",
            dataProviderClass = TestDataProviders.class,
            description = "Login password length - Boundary Value Analysis")
    public void testLoginPasswordLength_BVA(LoginTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());
        System.out.println("Password length: " + testData.getPassword().length() + " - " +
                testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        // All BVA password tests should show credential error (since emails don't exist)
        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Credential error should be displayed for: " + testData.getTestId());

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    @Test(dataProvider = "loginEmailLength_BVA",
            dataProviderClass = TestDataProviders.class,
            description = "Login email length - Boundary Value Analysis")
    public void testLoginEmailLength_BVA(LoginTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());
        System.out.println("Email length: " + testData.getEmail().length() + " - " +
                testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        // Check expected result type
        switch (testData.getExpectedResult()) {
            case "email_invalid":
                Assert.assertTrue(loginPage.isEmailInvalidMessageDisplayed(),
                        "Email invalid error should be displayed for: " + testData.getTestId());
                break;

            case "credential_error":
                Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                        "Credential error should be displayed for: " + testData.getTestId());
                break;
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    @Test(dataProvider = "loginSpecialCases",
            dataProviderClass = TestDataProviders.class,
            description = "Login special cases - Security & Edge Cases")
    public void testLoginSpecialCases(LoginTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());
        System.out.println("Testing: " + testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        // Handle different special case results
        switch (testData.getExpectedResult()) {
            case "email_invalid":
                Assert.assertTrue(loginPage.isEmailInvalidMessageDisplayed(),
                        "Email invalid error for: " + testData.getTestId());
                break;

            case "credential_error":
                Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                        "Credential error for: " + testData.getTestId());
                break;
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    @Test(dataProvider = "loginCombinationTests",
            dataProviderClass = TestDataProviders.class,
            description = "Login combination testing")
    public void testLoginCombinations(LoginTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        // Handle combination results
        switch (testData.getExpectedResult()) {
            case "email_invalid":
                Assert.assertTrue(loginPage.isEmailInvalidMessageDisplayed());
                break;
            case "password_empty":
                Assert.assertTrue(loginPage.isPasswordEmptyFieldDisplayed());
                break;
            case "email_empty":
                Assert.assertTrue(loginPage.isEmailEmptyFieldDisplayed());
                break;
            case "credential_error":
                Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed());
                break;
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    /*// ==================== MANUAL TESTS (Keep for specific scenarios) ====================

    @Test(priority = 1, description = "LG.01 - Login successfully with valid credentials")
    public void loginSuccessfully() {
        loginPage = new HomePage().clickSignInButton();
        homePage = loginPage.loginExpectSuccess("hpqn1510@gmail.com", "Kikiga18123@");

        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after successful login");
        Assert.assertFalse(homePage.isRegisterButtonDisplayed(),
                "Register button should not be displayed when logged in");
    }*/
}
