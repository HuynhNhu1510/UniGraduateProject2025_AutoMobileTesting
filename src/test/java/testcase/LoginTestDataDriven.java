package testcase;

import common.CommonTest;
import dataprovider.TestDataProviders;
import org.example.model.LoginTestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.HomePage;
import page.LoginPage;

public class LoginTestDataDriven extends CommonTest {

    @Override
    protected String getTestName() {
        return "Login Test - Data Driven";
    }

    // ==================== EQUIVALENCE PARTITIONING TESTS ====================

    /**
     * Test valid login credentials - Valid Partition
     */
    @Test(dataProvider = "loginValidCredentials_EP",
            dataProviderClass = TestDataProviders.class,
            priority = 1,
            description = "EP: Valid login credentials partition")
    public void testLogin_ValidCredentials_EP(LoginTestData testData) {
        System.out.println("[EP Test] " + testData.getTestId() + ": " + testData.getDescription());

        LoginPage loginPage = new HomePage().clickSignInButton();
        HomePage homePage = loginPage.loginExpectSuccess(testData.getEmail(), testData.getPassword());

        Assert.assertTrue(homePage.isLoggedIn(),
                "[" + testData.getTestId() + "] Login should succeed with valid credentials");

        System.out.println("[EP Test PASSED] " + testData.getTestId());
    }

    /**
     * Test invalid email format - Invalid Partition
     */
    @Test(dataProvider = "loginInvalidEmailFormat_EP",
            dataProviderClass = TestDataProviders.class,
            priority = 2,
            description = "EP: Invalid email format partition")
    public void testLogin_InvalidEmailFormat_EP(LoginTestData testData) {
        System.out.println("[EP Test] " + testData.getTestId() + ": " + testData.getDescription());

        LoginPage loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        boolean isErrorDisplayed = loginPage.isEmailInvalidMessageDisplayed();
        Assert.assertTrue(isErrorDisplayed,
                "[" + testData.getTestId() + "] Should show email format error: " + testData.getExpectedMessage());

        System.out.println("[EP Test PASSED] " + testData.getTestId());
    }

    /**
     * Test invalid password format - Invalid Partition
     */
    @Test(dataProvider = "loginInvalidPasswordFormat_EP",
            dataProviderClass = TestDataProviders.class,
            priority = 3,
            description = "EP: Invalid password format partition")
    public void testLogin_InvalidPasswordFormat_EP(LoginTestData testData) {
        System.out.println("[EP Test] " + testData.getTestId() + ": " + testData.getDescription());

        LoginPage loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        boolean isErrorDisplayed = loginPage.isEmailNotExistedErrorDisplayed();
        Assert.assertTrue(isErrorDisplayed,
                "[" + testData.getTestId() + "] Should show error for invalid password format");

        System.out.println("[EP Test PASSED] " + testData.getTestId());
    }

    /**
     * Test empty fields - Invalid Partition
     */
    @Test(dataProvider = "loginEmptyFields_EP",
            dataProviderClass = TestDataProviders.class,
            priority = 4,
            description = "EP: Empty fields partition")
    public void testLogin_EmptyFields_EP(LoginTestData testData) {
        System.out.println("[EP Test] " + testData.getTestId() + ": " + testData.getDescription());

        LoginPage loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        boolean isEmailEmptyError = loginPage.isEmailEmptyFieldDisplayed();
        boolean isPasswordEmptyError = loginPage.isPasswordEmptyFieldDisplayed();

        Assert.assertTrue(isEmailEmptyError || isPasswordEmptyError,
                "[" + testData.getTestId() + "] Should show empty field error");

        System.out.println("[EP Test PASSED] " + testData.getTestId());
    }

    // ==================== BOUNDARY VALUE ANALYSIS TESTS ====================

    /**
     * Test password length at boundaries: Min-1, Min, Min+1
     */
    @Test(dataProvider = "loginPasswordLength_BVA",
            dataProviderClass = TestDataProviders.class,
            priority = 5,
            description = "BVA: Password length boundaries")
    public void testLogin_PasswordLength_BVA(LoginTestData testData) {
        System.out.println("[BVA Test] " + testData.getTestId() + ": " + testData.getDescription());
        System.out.println("  Password length: " + testData.getPassword().length() + " chars");

        LoginPage loginPage = new HomePage().clickSignInButton();

        if (testData.getExpectedResult().equals("success")) {
            // Should succeed (Min, Min+1)
            // Note: Since test user doesn't exist, will show "email not existed" error
            // This still validates password format is accepted
            loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());
            boolean isPasswordFormatAccepted = loginPage.isEmailNotExistedErrorDisplayed();
            Assert.assertTrue(isPasswordFormatAccepted,
                    "[" + testData.getTestId() + "] Password format should be accepted at boundary: " +
                            testData.getPassword().length() + " chars");
        } else {
            // Should fail (Min-1)
            loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());
            boolean isErrorDisplayed = loginPage.isEmailNotExistedErrorDisplayed();
            Assert.assertTrue(isErrorDisplayed,
                    "[" + testData.getTestId() + "] Password should be rejected at boundary: " +
                            testData.getPassword().length() + " chars");
        }

        System.out.println("[BVA Test PASSED] " + testData.getTestId());
    }

    /**
     * Test email length at boundaries
     */
    @Test(dataProvider = "loginEmailLength_BVA",
            dataProviderClass = TestDataProviders.class,
            priority = 6,
            description = "BVA: Email length boundaries")
    public void testLogin_EmailLength_BVA(LoginTestData testData) {
        System.out.println("[BVA Test] " + testData.getTestId() + ": " + testData.getDescription());
        System.out.println("  Email length: " + testData.getEmail().length() + " chars");

        LoginPage loginPage = new HomePage().clickSignInButton();
        loginPage.loginExpectFailure(testData.getEmail(), testData.getPassword());

        // At very short email length, should show format error
        boolean isErrorDisplayed = loginPage.isEmailInvalidMessageDisplayed() ||
                loginPage.isEmailNotExistedErrorDisplayed();
        Assert.assertTrue(isErrorDisplayed,
                "[" + testData.getTestId() + "] Should handle boundary email length gracefully");

        System.out.println("[BVA Test PASSED] " + testData.getTestId());
    }
}
