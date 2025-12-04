package testcase;

import common.BaseTest;
import common.AppFlowManager;
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
        appFlowManager.handleAppLaunch();  // ← Auto handle onboarding
    }

    @AfterMethod
    public void tearDown() {
        // CRITICAL: Logout after each test to ensure test isolation
        homePage = new HomePage();
        if (homePage.isLoggedIn()) {
            System.out.println("[RegisterTest] User is logged in, performing logout...");
            BasePage basePage = new BasePage();
            AccountPage accountPage = basePage.clickAccountMenuItem();
            accountPage.scrollAndLogout(); // This now handles modal properly
            MobileUI.sleep(1); // Wait for navigation to complete
            System.out.println("[RegisterTest] ✓ Logged out successfully");
        } else {
            System.out.println("[RegisterTest] User not logged in, skip logout");
        }
    }

    @Test(priority = 2, description = "RG.02 - Register successfully with valid information")
    public void registerSuccessfully() {
        registerPage = new HomePage().clickRegisterButton();

        // Register và nhận HomePage luôn (giống LoginPage)
        homePage = registerPage.registerExpectSuccess("Huynh Alice", "huynhphamdangkhoa@gmail.com", "Kikiga18@");
        Assert.assertTrue(homePage.isLoggedIn(),
                "User should be logged in after successful registration");
        Assert.assertFalse(homePage.isRegisterButtonDisplayed(),
                "Register button should not be displayed when logged in");
    }

    @Test(priority = 3)
    public void registerFailedWithEmailAlreadyExisted() {
        registerPage = new HomePage().clickRegisterButton();

        // Register và vẫn ở RegisterPage
        registerPage.registerExpectFailure("Huynh Alice", "lytuthat1234@gmail.com", "Kikiga18123@");

        // Verify error trên RegisterPage
        Assert.assertTrue(registerPage.isEmailExistedMessageDisplayed());
    }
}
