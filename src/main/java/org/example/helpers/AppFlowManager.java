package org.example.helpers;

import org.example.keywords.MobileUI;
import org.example.page.HomePage;
import org.example.page.OnBoardingPage;

public class AppFlowManager {
    private OnBoardingPage onboardingPage;
    private HomePage homePage;

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

    /**
     * Handle app launch intelligently
     * Detects if onboarding is present and handles it
     * @return true if onboarding was handled, false if not present
     */
    public boolean handleAppLaunch() {
        System.out.println("\n[AppFlow] Handling app launch...");
        MobileUI.sleep(2);

        // Lazy init - chỉ tạo khi cần
        if (getOnboardingPage().isOnboardingDisplayed()) {
            System.out.println("[AppFlow] First launch detected - Onboarding shown");
            getOnboardingPage().completeOnboarding();
            getHomePage().waitForHomePageToLoad();
            System.out.println("[AppFlow] Onboarding completed → Home Page");
            return true;
        } else {
            System.out.println("[AppFlow] ✓ Subsequent launch - No onboarding");
            if (getHomePage().isHomePageDisplayed()) {
                System.out.println("[AppFlow] Already on Home Page - State: " + getHomePage().getCurrentState());
            } else {
                System.out.println("[AppFlow] Warning: Unknown screen state");
            }
            return false;
        }
    }

    /**
     * Navigate to Create Account page from Home Page
     * @return true if navigation successful
     */
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
            MobileUI.sleep(1);
            return true;
        } else if (currentState.equals("LOGGED_IN")) {
            System.out.println("[AppFlow] User already logged in");
            return false;
        } else {
            System.out.println("[AppFlow] Unknown state, attempting navigation...");
            try {
                homePage.clickRegisterButton();
                MobileUI.sleep(1);
                return true;
            } catch (Exception e) {
                System.out.println("[AppFlow] Navigation failed: " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * Get current app state
     * @return "ONBOARDING", "NOT_LOGGED_IN", "LOGGED_IN", or "UNKNOWN"
     */
    public String getCurrentAppState() {
        if (onboardingPage.isOnboardingDisplayed()) {
            return "ONBOARDING";
        }
        return homePage.getCurrentState();
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
