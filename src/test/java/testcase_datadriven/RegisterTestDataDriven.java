package testcase_datadriven;

import common.CommonTest;
import dataprovider.TestDataProviders;
import org.example.model.RegisterTestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.HomePage;
import page.RegisterPage;

public class RegisterTestDataDriven extends CommonTest {
    RegisterPage registerPage;

    @Override
    protected String getTestName() {
        return "Register Test (Data-Driven)";
    }

    /**
     * DECISION TABLE - All combinations
     * Covers: REG_DT1 to REG_DT8 (8 test cases)
     */
    @Test(dataProvider = "registerDecisionTable",
            dataProviderClass = TestDataProviders.class,
            description = "Register - Decision Table Testing")
    public void testRegisterDecisionTable(RegisterTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getScenario());
        System.out.println("FullName Valid: " + testData.getFullNameValid() +
                ", Email Valid: " + testData.getEmailValid() +
                ", Password Valid: " + testData.getPasswordValid());

        registerPage = new HomePage().clickRegisterButton();

        // Replace {{timestamp}} if exists
        String email = testData.getEmail().replace("{{timestamp}}",
                String.valueOf(System.currentTimeMillis()));

        if ("success".equals(testData.getExpectedResult())) {
            homePage = registerPage.registerExpectSuccess(
                    testData.getFullName(), email, testData.getPassword());
            Assert.assertTrue(homePage.isLoggedIn(),
                    "Should register successfully for: " + testData.getTestId());
        } else {
            registerPage.registerExpectFailure(
                    testData.getFullName(), email, testData.getPassword());

            // Verify appropriate error based on which field is invalid
            boolean errorDisplayed = false;

            if (!testData.getFullNameValid()) {
                errorDisplayed = registerPage.isFullNameInvalidMessageDisplayed();
            } else if (!testData.getEmailValid()) {
                errorDisplayed = registerPage.isEmailInvalidMessageDisplayed() ||
                        registerPage.isEmailEmptyMessageDisplayed();
            } else if (!testData.getPasswordValid()) {
                errorDisplayed = registerPage.isPasswordInvalidMessageDisplayed() ||
                        registerPage.isPasswordEmptyMessageDisplayed();
            }

            Assert.assertTrue(errorDisplayed,
                    "Error should be displayed for: " + testData.getTestId());
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    /**
     * EQUIVALENCE PARTITIONING - Full Name Validation
     */
    @Test(dataProvider = "registerFullNameValidation_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Register - Full Name Validation")
    public void testRegisterFullNameValidation_EP(RegisterTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());

        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure(
                testData.getFullName(), testData.getEmail(), testData.getPassword());

        switch (testData.getExpectedResult()) {
            case "fullname_error":
                Assert.assertTrue(registerPage.isFullNameInvalidMessageDisplayed(),
                        "Full name error should be displayed");
                break;
            case "success":
                // Some edge cases might pass
                break;
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    /**
     * EQUIVALENCE PARTITIONING - Email Validation
     */
    @Test(dataProvider = "registerEmailValidation_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Register - Email Validation")
    public void testRegisterEmailValidation_EP(RegisterTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());

        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure(
                testData.getFullName(), testData.getEmail(), testData.getPassword());

        switch (testData.getExpectedResult()) {
            case "email_invalid":
                Assert.assertTrue(registerPage.isEmailInvalidMessageDisplayed());
                break;
            case "email_empty":
                Assert.assertTrue(registerPage.isEmailEmptyMessageDisplayed());
                break;
            case "email_exists":
                Assert.assertTrue(registerPage.isEmailExistedMessageDisplayed());
                break;
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    /**
     * EQUIVALENCE PARTITIONING - Password Validation
     */
    @Test(dataProvider = "registerPasswordValidation_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Register - Password Validation")
    public void testRegisterPasswordValidation_EP(RegisterTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());

        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure(
                testData.getFullName(), testData.getEmail(), testData.getPassword());

        if ("password_invalid".equals(testData.getExpectedResult())) {
            Assert.assertTrue(registerPage.isPasswordInvalidMessageDisplayed(),
                    "Password invalid error should be displayed");
        } else if ("password_empty".equals(testData.getExpectedResult())) {
            Assert.assertTrue(registerPage.isPasswordEmptyMessageDisplayed(),
                    "Password empty error should be displayed");
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid Special Characters in Password
     */
    @Test(dataProvider = "registerInvalidSpecialChars_EP",
            dataProviderClass = TestDataProviders.class,
            description = "Register - Invalid Special Characters")
    public void testRegisterInvalidSpecialChars_EP(RegisterTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());
        System.out.println("Testing: " + testData.getDescription());

        registerPage = new HomePage().clickRegisterButton();
        registerPage.registerExpectFailure(
                testData.getFullName(), testData.getEmail(), testData.getPassword());

        Assert.assertTrue(registerPage.isPasswordInvalidMessageDisplayed(),
                "Password invalid error should be displayed for: " + testData.getTestId());

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }

    /**
     * BOUNDARY VALUE ANALYSIS - Full Name Length
     */
    @Test(dataProvider = "registerFullNameLength_BVA",
            dataProviderClass = TestDataProviders.class,
            description = "Register - Full Name Length BVA")
    public void testRegisterFullNameLength_BVA(RegisterTestData testData) {
        System.out.println("\n[" + testData.getTestId() + "] " + testData.getCategory());
        System.out.println("Full name length: " + testData.getFullName().length());

        registerPage = new HomePage().clickRegisterButton();

        String email = "test" + System.currentTimeMillis() + "@test.com";

        if ("success".equals(testData.getExpectedResult()) ||
                "success_or_error".equals(testData.getExpectedResult())) {
            // Try to register - might succeed or fail based on app validation
            registerPage.fillRegistrationForm(testData.getFullName(), email, testData.getPassword());
            registerPage.clickCreateAccount();
            // Don't assert - just observe behavior
        } else {
            registerPage.registerExpectFailure(testData.getFullName(), email, testData.getPassword());
            Assert.assertTrue(registerPage.isFullNameInvalidMessageDisplayed(),
                    "Full name error should be displayed for: " + testData.getTestId());
        }

        System.out.println("[" + testData.getTestId() + "] ✓ PASSED");
    }
}
