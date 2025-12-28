package testcase_datadriven;

import common.CommonTest;
import dataprovider.TestDataProviders;
import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.example.model.ChangePasswordTestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import page.*;

@Epic("User Management")
@Feature("Change Password Functionality - Data Driven")
public class ChangePasswordTestDataDriven extends CommonTest {
    private static final Logger logger = LogManager.getLogger(ChangePasswordTestDataDriven.class);

    // CREDENTIALS - MUST MATCH test_data.json
    private static final String TEST_EMAIL = "khoadang1510@gmail.com";
    private static final String ORIGINAL_PASSWORD = "Kikiga18123@";

    private ChangePasswordPage changePasswordPage;
    private DetailsAndPasswordPage detailsPage;
    private AccountPage accountPage;

    @Override
    protected String getTestName() {
        return "Change Password Test - Data Driven";
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
        return false; // Keep logged in for performance
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
        logger.debug("Successfully navigated to Change Password page");
    }

    /**
     * Navigate back to HomePage manually using back button
     */
    @Step("Navigate back to HomePage")
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
                logger.debug("Navigate attempt {} exception: {}", (i + 1), e.getMessage());
            }
        }

        HomePage hp = new HomePage();
        if (!hp.isHomePageDisplayed()) {
            logger.warn("WARNING: Not on HomePage after manual navigation");
        }
    }

    @Step("Restore password to original: {originalPassword}")
    private void restorePasswordToOriginal(String newPassword, String originalPassword) {
        logger.info("⚠ CRITICAL: Restoring password from '{}' back to original...", newPassword);

        // Navigate back to HomePage first
        navigateBackToHomePageManually();

        // Navigate to change password page again
        navigateToChangePassword();

        // Change password from NEW back to ORIGINAL
        detailsPage = changePasswordPage.changePasswordExpectSuccess(newPassword, originalPassword);

        Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                "Should return to Details & Password page after restoring password");

        logger.info("✓ Password successfully restored to original");
    }

    @Test(dataProvider = "changePasswordValidData",
            dataProviderClass = TestDataProviders.class,
            description = "Change password with valid data")
    @Story("Change Password - Valid Scenarios")
    @Severity(SeverityLevel.CRITICAL)
    public void testChangePasswordValid(ChangePasswordTestData testData) {
        logger.info("\n========================================");
        logger.info("[{}] {}", testData.getTestId(), testData.getDescription());
        logger.info("========================================");

        navigateToChangePassword();

        if ("success".equals(testData.getExpectedResult())) {
            logger.info("Executing: Change password from '{}' to '{}'",
                    testData.getCurrentPassword(), testData.getNewPassword());

            // Step 1: Change password
            detailsPage = changePasswordPage.changePasswordExpectSuccess(
                    testData.getCurrentPassword(),
                    testData.getNewPassword()
            );

            Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                    "Should return to Details & Password page after successful password change");
            logger.info("✓ Password changed successfully");

            // Step 2: CRITICAL - Restore password to original
            restorePasswordToOriginal(testData.getNewPassword(), ORIGINAL_PASSWORD);

        } else {
            Assert.fail("Invalid test data: expectedResult should be 'success' for valid test cases");
        }

        logger.info("[{}] ✓ TEST PASSED\n", testData.getTestId());
    }

    @Test(dataProvider = "changePasswordInvalidCurrent",
            dataProviderClass = TestDataProviders.class,
            description = "Change password with invalid current password")
    @Story("Change Password - Invalid Current Password")
    @Severity(SeverityLevel.CRITICAL)
    public void testChangePasswordInvalidCurrent(ChangePasswordTestData testData) {
        logger.info("\n========================================");
        logger.info("[{}] {}", testData.getTestId(), testData.getDescription());
        logger.info("========================================");

        navigateToChangePassword();

        logger.debug("Testing invalid current password scenario");
        changePasswordPage.changePasswordExpectFailure(
                testData.getCurrentPassword(),
                testData.getNewPassword()
        );

        if ("current_password_invalid".equals(testData.getExpectedResult())) {
            Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                    "Current password error should be displayed");

            String actualError = changePasswordPage.getCurrentPasswordErrorMessage();
            logger.debug("Error message displayed: {}", actualError);

        } else {
            Assert.fail("Unknown expected result: " + testData.getExpectedResult());
        }

        logger.info("[{}] ✓ TEST PASSED\n", testData.getTestId());
    }

    @Test(dataProvider = "changePasswordInvalidFormat",
            dataProviderClass = TestDataProviders.class,
            description = "Change password with invalid password format")
    @Story("Change Password - Password Format Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testChangePasswordInvalidFormat(ChangePasswordTestData testData) {
        logger.info("\n========================================");
        logger.info("[{}] {}", testData.getTestId(), testData.getDescription());
        logger.info("========================================");

        navigateToChangePassword();

        logger.debug("Testing invalid password format: {}", testData.getNewPassword());
        changePasswordPage.changePasswordExpectFailure(
                testData.getCurrentPassword(),
                testData.getNewPassword()
        );

        if ("password_invalid".equals(testData.getExpectedResult())) {
            Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                    "Password format error should be displayed");

            String actualError = changePasswordPage.getPasswordInvalidMessage();
            logger.debug("Error message: {}", actualError);

            String expectedError = "Password must have at least 8 characters that include at least 1 lowercase character, " +
                    "1 uppercase character, 1 number, and 1 special character in (!@#$%^&*)";
            Assert.assertEquals(actualError, expectedError,
                    "Error message should match password requirements");

        } else {
            Assert.fail("Unknown expected result: " + testData.getExpectedResult());
        }

        logger.info("[{}] ✓ TEST PASSED\n", testData.getTestId());
    }

    @Test(dataProvider = "changePasswordEmptyFields",
            dataProviderClass = TestDataProviders.class,
            description = "Change password with empty fields")
    @Story("Change Password - Empty Fields Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testChangePasswordEmptyFields(ChangePasswordTestData testData) {
        logger.info("\n========================================");
        logger.info("[{}] {}", testData.getTestId(), testData.getDescription());
        logger.info("========================================");

        navigateToChangePassword();

        logger.debug("Testing empty fields scenario");
        changePasswordPage.changePasswordExpectFailure(
                testData.getCurrentPassword(),
                testData.getNewPassword()
        );

        switch (testData.getExpectedResult()) {
            case "password_empty":
                Assert.assertTrue(changePasswordPage.isPasswordEmptyMessageDisplayed(),
                        "Empty password error should be displayed");
                logger.debug("Empty password error displayed correctly");
                break;

            case "current_password_invalid":
                Assert.assertTrue(changePasswordPage.isCurrentPasswordErrorDisplayed(),
                        "Current password error should be displayed");
                logger.debug("Current password error displayed correctly");
                break;

            default:
                Assert.fail("Unknown expected result: " + testData.getExpectedResult());
        }

        logger.info("[{}] ✓ TEST PASSED\n", testData.getTestId());
    }

    @Test(dataProvider = "changePasswordBoundary",
            dataProviderClass = TestDataProviders.class,
            description = "Change password boundary value tests")
    @Story("Change Password - Boundary Value Analysis")
    @Severity(SeverityLevel.NORMAL)
    public void testChangePasswordBoundary(ChangePasswordTestData testData) {
        logger.info("\n========================================");
        logger.info("[{}] {}", testData.getTestId(), testData.getDescription());
        logger.info("Password length: {}", testData.getNewPassword().length());
        logger.info("========================================");

        navigateToChangePassword();

        if ("success".equals(testData.getExpectedResult())) {
            // Should succeed
            logger.debug("Expecting success for boundary value");
            detailsPage = changePasswordPage.changePasswordExpectSuccess(
                    testData.getCurrentPassword(),
                    testData.getNewPassword()
            );

            Assert.assertTrue(detailsPage.isDetailsAndPasswordPageDisplayed(),
                    "Should accept password at boundary value");
            logger.info("✓ Boundary value accepted");

            // CRITICAL: Restore password
            restorePasswordToOriginal(testData.getNewPassword(), ORIGINAL_PASSWORD);

        } else if ("password_invalid".equals(testData.getExpectedResult())) {
            // Should fail
            logger.debug("Expecting failure for boundary value");
            changePasswordPage.changePasswordExpectFailure(
                    testData.getCurrentPassword(),
                    testData.getNewPassword()
            );

            Assert.assertTrue(changePasswordPage.isPasswordInvalidMessageDisplayed(),
                    "Should reject password at invalid boundary");
            logger.info("✓ Invalid boundary rejected");
        }

        logger.info("[{}] ✓ TEST PASSED\n", testData.getTestId());
    }
}
