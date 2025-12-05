package testcase;

import common.BaseTest;
import common.AppFlowManager;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import page.AccountPage;
import page.BasePage;
import page.HomePage;

import page.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class RegisterTest extends BaseTest {
    RegisterPage registerPage;
    HomePage homePage;
    AppFlowManager appFlowManager;

    // Xử lý trạng thái onboarding
    @BeforeMethod
    public void setUp() {
        appFlowManager = new AppFlowManager();
        appFlowManager.handleAppLaunch();  // Auto handle onboarding
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("\n[RegisterTest] ========== CLEANUP STARTED ==========");
        try {
            homePage = new HomePage();

            if (homePage.isLoggedIn()) {
                System.out.println("[RegisterTest] User is logged in, performing logout...");
                BasePage basePage = new BasePage();
                AccountPage accountPage = basePage.clickAccountMenuItem();
                accountPage.scrollAndLogout();
                MobileUI.sleep(0.5);
                System.out.println("[RegisterTest] ✓ Logged out successfully");
            }

            // Case 2: User NOT logged in → Press back to HomePage
            else {
                System.out.println("[RegisterTest] User not logged in, checking current screen...");
                if (homePage.isHomePageDisplayed()) {
                    System.out.println("[RegisterTest] Already on HomePage, no action needed");
                } else {
                    // Not on HomePage → Press back button
                    System.out.println("[RegisterTest] Not on HomePage, pressing back button...");
                    DriverManager.getDriver().navigate().back();
                    MobileUI.sleep(0.05);

                    // Verify we reached HomePage
                    homePage = new HomePage();
                    if (homePage.isHomePageDisplayed()) {
                        System.out.println("[RegisterTest] Successfully navigated to HomePage");
                    } else {
                        System.out.println("[RegisterTest] Still not on HomePage, pressing back again...");
                        DriverManager.getDriver().navigate().back();
                        MobileUI.sleep(0.05);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("[RegisterTest] Error during cleanup: " + e.getMessage());
        }
        System.out.println("[RegisterTest] ========== CLEANUP COMPLETED ==========\n");
    }

    @Test(priority = 2, description = "RG.02 - Register successfully with valid information")
    public void registerSuccessfullyWithValidData() {
        registerPage = new HomePage().clickRegisterButton();
        homePage = registerPage.registerExpectSuccess("Huynh Alice", "tancang123@gmail.com", "Kikiga18@");
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
}
