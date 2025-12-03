package testcase;

import common.BaseTest;
import org.example.helpers.AppFlowManager;
import org.example.page.HomePage;

import org.example.page.RegisterPage;
import org.testng.Assert;
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

    @Test(priority = 2)
    public void registerSuccessfully() {
        registerPage = new HomePage().clickRegisterButton();

        // Register và nhận HomePage luôn (giống LoginPage)
        homePage = registerPage.registerExpectSuccess("Huynh Alice", "lily1234@gmail.com", "Kikiga18@");

        Assert.assertTrue(homePage.isLoggedIn());
        Assert.assertFalse(homePage.isRegisterButtonDisplayed());
    }

    @Test(priority = 6)
    public void registerFailedWithEmailAlreadyExisted() {
        registerPage = new HomePage().clickRegisterButton();

        // Register và vẫn ở RegisterPage
        registerPage.registerExpectFailure("Huynh Alice", "ace123@example.com", "Kikiga18@");

        // Verify error trên RegisterPage
        Assert.assertTrue(registerPage.isEmailExistedMessageDisplayed());
    }
}
