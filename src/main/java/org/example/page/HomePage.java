package org.example.page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    public HomePage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
    }

    @AndroidFindBy(accessibility = "Register")
    private WebElement registerButton;

    @AndroidFindBy(accessibility = "Sign in")
    private WebElement signInButton;

    @AndroidFindBy(accessibility = "Sign up for an improved experience and insider pricing. " +
            "If you already have an account, sign in instead.")
    private WebElement contentDescHomePage;

    @AndroidFindBy(accessibility = "Shop Recently Added")
    private WebElement shopRecentlyButton;

    // ===== Action Methods =====

    public RegisterPage clickRegisterButton() {
        registerButton.click();
        System.out.println("[HomePage] Clicked Register button");
        return new RegisterPage();
    }

    public void clickSignInButton() {
        signInButton.click();
        System.out.println("[HomePage] Clicked Sign In button");
    }

    public void navigateToCreateAccount() {
        clickRegisterButton();
    }

    public void navigateToSignIn() {
        clickSignInButton();
    }

    public boolean isHomePageDisplayed() {
        return isElementDisplayed(registerButton)
                && isElementDisplayed(signInButton);
    }

    public boolean isNotLoggedIn() {
        return isElementDisplayed(registerButton)
                && isElementDisplayed(signInButton);
    }

    public boolean isLoggedIn() {
        return isElementDisplayed(shopRecentlyButton);
    }

    public String getCurrentState() {
        if (isNotLoggedIn()) {
            return "NOT_LOGGED_IN";
        } else if (isLoggedIn()) {
            return "LOGGED_IN";
        } else {
            return "UNKNOWN";
        }
    }

    public boolean isRegisterButtonDisplayed() {
        return isElementDisplayed(registerButton);
    }

    public boolean isSignInButtonDisplayed() {
        return isElementDisplayed(signInButton);
    }

    public boolean waitForHomePageToLoad() {
        return waitForHomePageToLoad(10);
    }

    public boolean waitForHomePageToLoad(int timeoutSeconds) {
        int attempts = 0;
        int maxAttempts = timeoutSeconds;

        while (attempts < maxAttempts) {
            if (isHomePageDisplayed()) {
                System.out.println("[HomePage] Home page loaded after " + attempts + " seconds");
                return true;
            }
            try {
                Thread.sleep(10);
                attempts++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[HomePage] Home page did not load within " + timeoutSeconds + " seconds");
        return false;
    }

    private boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
