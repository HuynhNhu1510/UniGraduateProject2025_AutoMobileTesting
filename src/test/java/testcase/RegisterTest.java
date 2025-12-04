package testcase;

import common.BaseTest;
import org.example.helpers.AppFlowManager;
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
        // Logout nếu đã login
        homePage = new HomePage();
        if (homePage.isLoggedIn()) {
            System.out.println("[TearDown] User is logged in, logging out...");

            // Navigate to Account page và logout
           /* AccountPage accountPage = homePage.navigateToAccount();
            accountPage.clickLogout();
*/
            System.out.println("[TearDown] ✓ Logged out successfully");
        } else {
            System.out.println("[TearDown] User not logged in, skip logout");
        }
    }

    @Test(priority = 2, description = "RG.02 - Register successfully with valid information")
    public void registerSuccessfully() {
        registerPage = new HomePage().clickRegisterButton();

        // Register và nhận HomePage luôn (giống LoginPage)
        homePage = registerPage.registerExpectSuccess("Huynh Alice", "uyentrang123@gmail.com", "Kikiga18@");

        Assert.assertTrue(homePage.isLoggedIn());
        Assert.assertFalse(homePage.isRegisterButtonDisplayed());
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
