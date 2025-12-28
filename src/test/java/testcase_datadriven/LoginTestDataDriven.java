package testcase_datadriven;

import common.CommonTest;
import dataprovider.TestDataProviders;
import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.LoginTestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;

@Epic("Authentication")
@Feature("Login Functionality - Data Driven")
public class LoginTestDataDriven extends CommonTest {
    private static final Logger logger = LogManager.getLogger(LoginTestDataDriven.class);

    LoginPage loginPage;

    @Override
    protected String getTestName() {
        return "Login Test - Data Driven";
    }

    @Test(dataProvider = "loginInvalidEmailFormat_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Login with invalid email formats - Equivalence Partitioning")
    @Story("Login - Email Validation (Equivalence Partitioning)")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginInvalidEmailFormat_EP(LoginTestData testData) {
        logger.info("\n[{}] {}", testData.getTestId(), testData.getCategory());
        logger.debug("Description: {}", testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        if (testData.getExpectedResult().equals("email_invalid")) {
            Assert.assertTrue(loginPage.isEmailInvalidMessageDisplayed(),
                    "Email invalid message should be displayed for: " + testData.getTestId());

            String actualMsg = loginPage.getEmailInvalidMessage();
            Assert.assertEquals(actualMsg, testData.getExpectedMessage(),
                    "Error message should match for: " + testData.getTestId());
        } else {
            Assert.fail("Unknown expected result: " + testData.getExpectedResult());
        }
        logger.info("[{}] ✓ PASSED", testData.getTestId());
    }

    @Test(dataProvider = "loginInvalidPasswordFormat_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Login with invalid password formats - Equivalence Partitioning")
    @Story("Login - Password Validation (Equivalence Partitioning)")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginInvalidPasswordFormat_EP(LoginTestData testData) {
        logger.info("\n[{}] {}", testData.getTestId(), testData.getCategory());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        if ("credential_error".equals(testData.getExpectedResult())) {
            Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                    "Credential error should be displayed for: " + testData.getTestId());

            String actualMsg = loginPage.getEmailNotExistedErrorMessage();
            Assert.assertEquals(actualMsg, testData.getExpectedMessage(),
                    "Error message should match for: " + testData.getTestId());
        }

        logger.info("[{}] ✓ PASSED", testData.getTestId());
    }

    @Test(dataProvider = "loginInvalidSpecialChars_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Login with invalid special characters in password")
    @Story("Login - Special Characters Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginInvalidSpecialCharacters_EP(LoginTestData testData) {
        logger.info("\n[{}] {}", testData.getTestId(), testData.getCategory());
        logger.debug("Testing: {}", testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Error should be displayed for invalid special char: " + testData.getTestId());

        logger.info("[{}] ✓ PASSED", testData.getTestId());
    }

    @Test(dataProvider = "loginEmptyFields_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Login with empty fields - Equivalence Partitioning")
    @Story("Login - Empty Fields Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginEmptyFields_EP(LoginTestData testData) {
        logger.info("\n[{}] {}", testData.getTestId(), testData.getCategory());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

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
                boolean hasError = loginPage.isEmailEmptyFieldDisplayed() ||
                        loginPage.isPasswordEmptyFieldDisplayed();
                Assert.assertTrue(hasError, "At least one empty field error should be displayed");
        }

        logger.info("[{}] ✓ PASSED", testData.getTestId());
    }

    @Test(dataProvider = "loginPasswordLength_BVA",
            dataProviderClass = TestDataProviders.class,
            description = "Login password length - Boundary Value Analysis")
    @Story("Login - Password Length (Boundary Value Analysis)")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginPasswordLength_BVA(LoginTestData testData) {
        logger.info("\n[{}] {}", testData.getTestId(), testData.getCategory());
        logger.debug("Password length: {} - {}", testData.getPassword().length(), testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        Assert.assertTrue(loginPage.isEmailNotExistedErrorDisplayed(),
                "Credential error should be displayed for: " + testData.getTestId());

        logger.info("[{}] ✓ PASSED", testData.getTestId());
    }

    @Test(dataProvider = "loginEmailLength_BVA",
            dataProviderClass = TestDataProviders.class,
            description = "Login email length - Boundary Value Analysis")
    @Story("Login - Email Length (Boundary Value Analysis)")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginEmailLength_BVA(LoginTestData testData) {
        logger.info("\n[{}] {}", testData.getTestId(), testData.getCategory());
        logger.debug("Email length: {} - {}", testData.getEmail().length(), testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

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

        logger.info("[{}] ✓ PASSED", testData.getTestId());
    }

    @Test(dataProvider = "loginSpecialCases",
            dataProviderClass = TestDataProviders.class,
            description = "Login special cases - Security & Edge Cases")
    @Story("Login - Special Cases")
    @Severity(SeverityLevel.MINOR)
    public void testLoginSpecialCases(LoginTestData testData) {
        logger.info("\n[{}] {}", testData.getTestId(), testData.getCategory());
        logger.debug("Testing: {}", testData.getDescription());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

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

        logger.info("[{}] ✓ PASSED", testData.getTestId());
    }

    @Test(dataProvider = "loginCombinationTests",
            dataProviderClass = TestDataProviders.class,
            description = "Login combination testing")
    @Story("Login - Combination Testing")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginCombinations(LoginTestData testData) {
        logger.info("\n[{}] {}", testData.getTestId(), testData.getCategory());

        loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

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

        logger.info("[{}] ✓ PASSED", testData.getTestId());
    }
}
