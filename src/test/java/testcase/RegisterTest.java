package testcase;

import common.BaseTest;
import org.example.helpers.AppFlowManager;
import org.example.page.HomePage;
import org.example.page.OnBoardingPage;
import org.example.page.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class RegisterTest extends BaseTest {
    RegisterPage registerPage;
    HomePage homePage;
    OnBoardingPage onBoardingPage;
    AppFlowManager appFlowManager;

    @BeforeMethod
    public void setUp() {
        appFlowManager = new AppFlowManager();
        appFlowManager.handleAppLaunch();  // ← Auto handle onboarding
    }

    @Test
    public void registerSuccessfully() {
        homePage = new HomePage();
        homePage.clickRegisterButton();

        registerPage = new RegisterPage();
        registerPage.fillRegistrationForm("Huynh Alice", "alice1010@gmail.com", "Kikiga18123@");
        registerPage.clickCreateAccount();

        Assert.assertTrue(registerPage.isHomePageDisplayed(), "Shop recently button is not displayed");
    }
}
