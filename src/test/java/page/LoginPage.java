package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import javax.xml.xpath.XPath;

public class LoginPage {

    // constructor
    public LoginPage () {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
    }

    @AndroidFindBy(accessibility = "We've had a problem, please try again")
    WebElement emailNotExistedErrorMessage;

    @AndroidFindBy(accessibility = "Please enter valid email address")
    WebElement emailInvalidMessage;

    @AndroidFindBy(accessibility = "Please enter your email address")
    WebElement emailEmptyField;

    @AndroidFindBy(accessibility = "Please enter your password")
    WebElement passwordEmptyField;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[1]")
    WebElement email;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[2]")
    WebElement password;

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc=\"Sign in\"]")
    WebElement signInButton;

    @AndroidFindBy(accessibility = "Create account")
    WebElement createAccount;


    public void enterEmail(String emailValue) {
        email.click();
        email.clear();
        email.sendKeys(emailValue);
    }

    public void enterPassword(String passwordValue) {
        password.click();
        password.clear();
        password.sendKeys(passwordValue);
    }

    public void fillLoginForm(String emailValue, String passwordValue) {
        enterEmail(emailValue);
        enterPassword(passwordValue);
    }

    public void clickSignInButton() {
        MobileUI.clickElement(signInButton);
    }

    public void clearAllFields() {
        email.clear();
        password.clear();
    }

    public boolean isLoginScreenLoaded() {
        return isElementDisplayed(email)
                && isElementDisplayed(password)
                && isElementDisplayed(signInButton);
    }

    public boolean isSignInButtonDisplayed() {
        return isElementDisplayed(signInButton);
    }

    public boolean isEmailNotExistedErrorDisplayed() {
        return isElementDisplayed(emailNotExistedErrorMessage);
    }

    public boolean isEmailInvalidMessageDisplayed() {
        return isElementDisplayed(emailInvalidMessage);
    }

    public boolean isEmailEmptyFieldDisplayed() {
        return isElementDisplayed(emailEmptyField);
    }

    public boolean isPasswordEmptyFieldDisplayed() {
        return isElementDisplayed(passwordEmptyField);
    }

    public String getEmailNotExistedErrorMessage() {
        return getElementTextSafely(emailNotExistedErrorMessage);
    }

    public String getEmailInvalidMessage() {
        return getElementTextSafely(emailInvalidMessage);
    }

    public String getEmailEmptyMessage() {
        return getElementTextSafely(emailEmptyField);
    }

    public String getPasswordEmptyMessage() {
        return getElementTextSafely(passwordEmptyField);
    }

    public HomePage loginExpectSuccess(String email, String password) {
        fillLoginForm(email, password);
        System.out.println("[LoginPage] Filled login form");
        clickSignInButton();
        System.out.println("[LoginPage] Clicked Sign In button");

        HomePage homePage = new HomePage();
        homePage.waitForHomePageToLoad(1);
        return homePage;
    }

    public void loginExpectFailure(String email, String password) {
        fillLoginForm(email, password);
        clickSignInButton();
    }

    private boolean isElementDisplayed(WebElement element) {
        return MobileUI.isElementPresentAndDisplayed(element);
    }

    private String getElementTextSafely(WebElement element) {
        return MobileUI.getElementAttribute(element, "content-desc");
    }

}
