package common;

import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import page.AccountPage;
import page.BasePage;
import page.HomePage;

public abstract class CommonTest extends BaseTest{
    protected HomePage homePage;
    protected AppFlowManager appFlowManager;
    protected abstract String getTestName();

    @BeforeClass
    public void setUpClass() {
        appFlowManager = new AppFlowManager();
        appFlowManager.handleAppLaunch();
        System.out.println("[" + getTestName() + "] ========== TEST SUITE STARTED ==========");
    }

    @BeforeMethod
    public void setUp() {
        System.out.println("\n[" + getTestName() + "] ========== TEST STARTED ==========");
        homePage = new HomePage();

        // Check on HomePage before each test
        if (!homePage.isHomePageDisplayed()) {
            System.out.println("[" + getTestName() + "] Not on HomePage, navigating back...");
            DriverManager.getDriver().navigate().back();
            MobileUI.sleep(0.1);
        }
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("\n[" + getTestName() + "] ========== CLEANUP STARTED ==========");
        try {
            homePage = new HomePage();
            if (homePage.isLoggedIn()) {
                performLogout();
            } else if (!homePage.isHomePageDisplayed()) {
                navigateBackToHomePage();
            } else {
                System.out.println("[" + getTestName() + "] Already on HomePage - No cleanup needed");
            }

        } catch (Exception e) {
            System.out.println("[" + getTestName() + "] Cleanup error: " + e.getMessage());
            try {
                DriverManager.getDriver().navigate().back();
            } catch (Exception ex) {
                System.out.println("[" + getTestName() + "] Fallback navigation failed");
            }
        }
        System.out.println("[" + getTestName() + "] ========== CLEANUP COMPLETED ==========\n");
    }

    protected void performLogout() {
        System.out.println("[" + getTestName() + "] Logging out...");
        BasePage basePage = new BasePage();
        AccountPage accountPage = basePage.clickAccountMenuItem();
        accountPage.scrollAndLogout();
        MobileUI.sleep(0.1);
        System.out.println("[" + getTestName() + "] Logged out");
    }

    protected void navigateBackToHomePage() {
        System.out.println("[" + getTestName() + "] Navigating back to HomePage...");
        DriverManager.getDriver().navigate().back();
    }
}

