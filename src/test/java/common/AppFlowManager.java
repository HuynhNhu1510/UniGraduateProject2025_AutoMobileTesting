package common;

import io.appium.java_client.android.AndroidDriver;
import org.example.drivers.DriverManager;
import org.example.helpers.PropertiesHelpers;
import org.example.keywords.MobileUI;
import page.HomePage;
import org.example.constants.ConfigData;
import page.OnBoardingPage;


public class AppFlowManager {
    private OnBoardingPage onboardingPage;
    private HomePage homePage;

    public static final String APP_PACKAGE = PropertiesHelpers.getValue("APP_PACKAGE");
    public static final String APP_ACTIVITY = PropertiesHelpers.getValue("APP_ACTIVITY");

    private static boolean isOnboardingCompleted = false;
    private static boolean isFirstCheck = true;

    private OnBoardingPage getOnboardingPage() {
        if (onboardingPage == null) {
            onboardingPage = new OnBoardingPage();
        }
        return onboardingPage;
    }

    private HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage();
        }
        return homePage;
    }

    private void activateApp() {
        try {
            AndroidDriver driver = (AndroidDriver) DriverManager.getDriver();

            String currentPackage = driver.getCurrentPackage();

            if (currentPackage == null || !currentPackage.equals(ConfigData.APP_PACKAGE)) {
                System.out.println("[AppFlow] App not active, activating...");
                driver.activateApp(ConfigData.APP_PACKAGE);
                MobileUI.sleep(0.5);
                System.out.println("[AppFlow] App activated successfully");
            } else {
                System.out.println("[AppFlow] App already active");
            }

        } catch (Exception e) {
            System.out.println("[AppFlow] Warning: Could not verify app state - " + e.getMessage());
            try {
                AndroidDriver driver = (AndroidDriver) DriverManager.getDriver();
                driver.activateApp(ConfigData.APP_PACKAGE);
                MobileUI.sleep(0.5);
                System.out.println("[AppFlow] App activation attempted");
            } catch (Exception ex) {
                System.out.println("[AppFlow] ERROR: Failed to activate app - " + ex.getMessage());
            }
        }
    }


    public boolean handleAppLaunch() {
        System.out.println("\n[AppFlow] Handling app launch...");
        activateApp();

        if (isFirstCheck) {
            MobileUI.sleep(0.3);

            if (getOnboardingPage().isOnboardingDisplayed()) {
                System.out.println("[AppFlow] First launch detected - Onboarding shown");
                getOnboardingPage().completeOnboarding();
                getHomePage().waitForHomePageToLoad();
                System.out.println("[AppFlow] Onboarding completed -> Home Page");
                isOnboardingCompleted = true;
                isFirstCheck = false;
                return true;
            } else {
                System.out.println("[AppFlow] Onboarding already completed or skipped");
                isOnboardingCompleted = true;
                isFirstCheck = false;
            }
        } else {
            System.out.println("[AppFlow] Using cached state - Onboarding already handled");
        }

        // Quick validation - chỉ khi cần thiết
        if (getHomePage().isHomePageDisplayed()) {
            System.out.println("[AppFlow] On Home Page - State: " + getHomePage().getCurrentState());
        }
        return isOnboardingCompleted;
    }

    // Thêm method để reset cache nếu cần
    public static void resetAppFlowCache() {
        isOnboardingCompleted = false;
        isFirstCheck = true;
        System.out.println("[AppFlow] Cache reset");
    }

    public boolean navigateToCreateAccount() {
        System.out.println("\n[AppFlow] Navigating to Create Account...");

        // Ensure we're on home page
        if (!homePage.isHomePageDisplayed()) {
            System.out.println("[AppFlow] Not on Home Page, cannot navigate");
            return false;
        }

        // Check current state
        String currentState = homePage.getCurrentState();
        System.out.println("[AppFlow] Current state: " + currentState);

        if (currentState.equals("NOT_LOGGED_IN")) {
            homePage.clickRegisterButton();
            System.out.println("[AppFlow] Clicked Register → Create Account Page");
            MobileUI.sleep(0.05);
            return true;
        } else if (currentState.equals("LOGGED_IN")) {
            System.out.println("[AppFlow] User already logged in");
            return false;
        } else {
            System.out.println("[AppFlow] Unknown state, attempting navigation...");
            try {
                homePage.clickRegisterButton();
                MobileUI.sleep(0.05);
                return true;
            } catch (Exception e) {
                System.out.println("[AppFlow] Navigation failed: " + e.getMessage());
                return false;
            }
        }
    }

    public String getCurrentAppState() {
        if (getOnboardingPage().isOnboardingDisplayed()) {
            return "ONBOARDING";
        }
        return getHomePage().getCurrentState();
    }

    // Reset app to fresh state
    public void resetAppToFreshState() {
        try {
            handleAppLaunch();
            System.out.println("[AppFlow] App reset completed");
        } catch (Exception e) {
            System.out.println("[AppFlow] Reset failed: " + e.getMessage());
        }
    }

    public void printCurrentStatus() {
        System.out.println("\n========== APP FLOW STATUS ==========");
        System.out.println("Current State: " + getCurrentAppState());
        System.out.println("=====================================\n");
    }
}
