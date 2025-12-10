package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
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
        MobileUI.clickElement(registerButton);
        System.out.println("[HomePage] Clicked Register button");
        return new RegisterPage();
    }

    public void clickSignInButton() {
        MobileUI.clickElement(signInButton);
        System.out.println("[HomePage] Clicked Sign In button");
    }

    public void navigateToCreateAccount() {
        clickRegisterButton();
    }

    public void navigateToSignIn() {
        clickSignInButton();
    }

    public boolean isHomePageDisplayed() {
        return isElementDisplayed(contentDescHomePage);
    }

    public boolean isNotLoggedIn() {
        return isElementDisplayed(contentDescHomePage);
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
        return waitForHomePageToLoad(1);
    }

    public boolean waitForHomePageToLoad(int timeoutSeconds) {
        int attempts = 0;
        int maxAttempts = timeoutSeconds;

        while (attempts < maxAttempts) {
            if (isHomePageDisplayed()) {
                System.out.println("[HomePage] Home page loaded after " + attempts + " seconds");
                return true;
            }
            MobileUI.sleep(0.3);
            attempts++;
        }
        System.out.println("[HomePage] Home page did not load within " + timeoutSeconds + " seconds");
        return false;
    }

    private boolean isElementDisplayed(WebElement element) {
        return MobileUI.isElementPresentAndDisplayed(element);
    }
}
