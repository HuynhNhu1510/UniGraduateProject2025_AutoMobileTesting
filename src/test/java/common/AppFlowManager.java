package common;

import org.example.constants.ConfigData;
import org.example.keywords.MobileUI;
import page.HomePage;
import page.OnBoardingPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AppFlowManager {
    private OnBoardingPage onboardingPage;
    private HomePage homePage;

    private static boolean isOnboardingCompleted = false;
    private static boolean isFirstCheck = true;

    private static final Logger logger = LogManager.getLogger(AppFlowManager.class);

    /*private OnBoardingPage getOnboardingPage() {
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

    public boolean handleAppLaunch() {
        System.out.println("\n[AppFlow] Handling app launch...");

        activateApp();

        if (isFirstCheck) {
            // THÊM SLEEP DÀI HƠN để app có thời gian khởi động hoàn toàn
            System.out.println("[AppFlow] Waiting for app to fully initialize...");
            MobileUI.sleep(1);  // Tăng từ 0.3s lên 1.5s

            // THÊM RETRY LOGIC khi check onboarding
            int maxRetries = 3;
            boolean onboardingCheckSuccess = false;

            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                try {
                    System.out.println("[AppFlow] Checking onboarding status (Attempt " + attempt + "/" + maxRetries + ")");

                    if (getOnboardingPage().isOnboardingDisplayed()) {
                        System.out.println("[AppFlow] First launch detected - Onboarding shown");
                        getOnboardingPage().completeOnboarding();
                        getHomePage().waitForHomePageToLoad();
                        System.out.println("[AppFlow] Onboarding completed → Home Page");
                        isOnboardingCompleted = true;
                        isFirstCheck = false;
                        onboardingCheckSuccess = true;
                        return true;
                    } else {
                        System.out.println("[AppFlow] Onboarding already completed or skipped");
                        isOnboardingCompleted = true;
                        isFirstCheck = false;
                        onboardingCheckSuccess = true;
                        break;
                    }
                } catch (org.openqa.selenium.WebDriverException e) {
                    System.out.println("[AppFlow] WARNING: WebDriver error on attempt " + attempt + " - " + e.getMessage());

                    if (attempt < maxRetries) {
                        System.out.println("[AppFlow] Retrying after 2 seconds...");
                        MobileUI.sleep(2.0);
                    } else {
                        System.out.println("[AppFlow] ERROR: All retry attempts failed!");
                        throw new RuntimeException("App launch failed after " + maxRetries + " attempts", e);
                    }
                }
            }
        } else {
            System.out.println("[AppFlow] Using cached state - Onboarding already handled");
        }

        // Quick validation - chỉ khi cần thiết
        try {
            String currentState = getHomePage().getCurrentState();
            if (!currentState.equals("UNKNOWN")) {
                System.out.println("[AppFlow] On Home Page - State: " + currentState);
            }
        } catch (org.openqa.selenium.WebDriverException e) {
            System.out.println("[AppFlow] WARNING: Could not validate home page state - " + e.getMessage());
        }

        return isOnboardingCompleted;
    }


    private void activateApp() {
        try {
            System.out.println("[AppFlow] Activating app via ADB...");

            String command = "adb shell am start -n " + ConfigData.APP_PACKAGE + "/" + ConfigData.APP_ACTIVITY;
            Process process = Runtime.getRuntime().exec(command);

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                MobileUI.sleep(0.3);
                System.out.println("[AppFlow] App activated successfully via ADB");
            } else {
                System.out.println("[AppFlow] Warning: ADB command returned exit code: " + exitCode);
            }

        } catch (Exception e) {
            System.out.println("[AppFlow] ERROR: Failed to activate app - " + e.getMessage());
        }
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
    }*/

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

    public boolean handleAppLaunch() {
        logger.info("\n[AppFlow] Handling app launch...");

        activateApp();

        if (isFirstCheck) {
            logger.info("[AppFlow] Waiting for app to fully initialize...");
            MobileUI.sleep(1);

            int maxRetries = 3;
            boolean onboardingCheckSuccess = false;

            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                try {
                    logger.debug("[AppFlow] Checking onboarding status (Attempt {}/{})", attempt, maxRetries);

                    if (getOnboardingPage().isOnboardingDisplayed()) {
                        logger.info("[AppFlow] First launch detected - Onboarding shown");
                        getOnboardingPage().completeOnboarding();
                        getHomePage().waitForHomePageToLoad();
                        logger.info("[AppFlow] Onboarding completed → Home Page");
                        isOnboardingCompleted = true;
                        isFirstCheck = false;
                        onboardingCheckSuccess = true;
                        return true;
                    } else {
                        logger.info("[AppFlow] Onboarding already completed or skipped");
                        isOnboardingCompleted = true;
                        isFirstCheck = false;
                        onboardingCheckSuccess = true;
                        break;
                    }
                } catch (org.openqa.selenium.WebDriverException e) {
                    logger.warn("[AppFlow] WARNING: WebDriver error on attempt {} - {}", attempt, e.getMessage());

                    if (attempt < maxRetries) {
                        logger.debug("[AppFlow] Retrying after 2 seconds...");
                        MobileUI.sleep(2.0);
                    } else {
                        logger.error("[AppFlow] ERROR: All retry attempts failed!");
                        throw new RuntimeException("App launch failed after " + maxRetries + " attempts", e);
                    }
                }
            }
        } else {
            logger.info("[AppFlow] Using cached state - Onboarding already handled");
        }

        // Quick validation
        try {
            String currentState = getHomePage().getCurrentState();
            if (!currentState.equals("UNKNOWN")) {
                logger.info("[AppFlow] On Home Page - State: {}", currentState);
            }
        } catch (org.openqa.selenium.WebDriverException e) {
            logger.warn("[AppFlow] WARNING: Could not validate home page state - {}", e.getMessage());
        }

        return isOnboardingCompleted;
    }

    private void activateApp() {
        try {
            logger.debug("[AppFlow] Activating app via ADB...");

            String command = "adb shell am start -n " + ConfigData.APP_PACKAGE + "/" + ConfigData.APP_ACTIVITY;
            Process process = Runtime.getRuntime().exec(command);

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                MobileUI.sleep(0.3);
                logger.info("[AppFlow] App activated successfully via ADB");
            } else {
                logger.warn("[AppFlow] Warning: ADB command returned exit code: {}", exitCode);
            }

        } catch (Exception e) {
            logger.error("[AppFlow] ERROR: Failed to activate app - {}", e.getMessage());
        }
    }

    public static void resetAppFlowCache() {
        isOnboardingCompleted = false;
        isFirstCheck = true;
        logger.info("[AppFlow] Cache reset");
    }

    public boolean navigateToCreateAccount() {
        logger.info("\n[AppFlow] Navigating to Create Account...");

        if (!homePage.isHomePageDisplayed()) {
            logger.warn("[AppFlow] Not on Home Page, cannot navigate");
            return false;
        }

        String currentState = homePage.getCurrentState();
        logger.debug("[AppFlow] Current state: {}", currentState);

        if (currentState.equals("NOT_LOGGED_IN")) {
            homePage.clickRegisterButton();
            logger.info("[AppFlow] Clicked Register → Create Account Page");
            MobileUI.sleep(0.05);
            return true;
        } else if (currentState.equals("LOGGED_IN")) {
            logger.warn("[AppFlow] User already logged in");
            return false;
        } else {
            logger.warn("[AppFlow] Unknown state, attempting navigation...");
            try {
                homePage.clickRegisterButton();
                MobileUI.sleep(0.05);
                return true;
            } catch (Exception e) {
                logger.error("[AppFlow] Navigation failed: {}", e.getMessage());
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

    public void resetAppToFreshState() {
        try {
            handleAppLaunch();
            logger.info("[AppFlow] App reset completed");
        } catch (Exception e) {
            logger.error("[AppFlow] Reset failed: {}", e.getMessage());
        }
    }

    public void printCurrentStatus() {
        logger.info("\n========== APP FLOW STATUS ==========");
        logger.info("Current State: {}", getCurrentAppState());
        logger.info("=====================================\n");
    }
}
