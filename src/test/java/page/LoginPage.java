package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import javax.xml.xpath.XPath;

public class LoginPage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);

    // constructor
    public LoginPage () {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
        logger.debug("LoginPage initialized");
    }

    @AndroidFindBy(accessibility = "We've had a problem, please try again")
    private WebElement emailNotExistedErrorMessage;

    @AndroidFindBy(accessibility = "Please enter valid email address")
    private WebElement emailInvalidMessage;

    @AndroidFindBy(accessibility = "Please enter your email address")
    private WebElement emailEmptyField;

    @AndroidFindBy(accessibility = "Please enter your password")
    private WebElement passwordEmptyField;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[1]")
    private WebElement email;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[2]")
    private WebElement password;

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc=\"Sign in\"]")
    private WebElement signInButton;

    @AndroidFindBy(accessibility = "Create account")
    private WebElement createAccount;

    @Step("Enter email: {emailValue}")
    public void enterEmail(String emailValue) {
        logger.debug("Entering email: {}", emailValue);
        MobileUI.setText(email, emailValue);
        logger.debug("Email entered successfully");
    }

    @Step("Enter password")
    public void enterPassword(String passwordValue) {
        logger.debug("Entering password");
        MobileUI.setText(password, passwordValue);
        logger.debug("Password entered successfully");
    }

    @Step("Fill login form with email: {emailValue}")
    public void fillLoginForm(String emailValue, String passwordValue) {
        logger.info("Filling login form for email: {}", emailValue);
        enterEmail(emailValue);
        enterPassword(passwordValue);
        logger.debug("Login form filled successfully");
    }

    @Step("Click Sign In button")
    public void clickSignInButton() {
        logger.info("Clicking Sign In button");
        MobileUI.clickElement(signInButton);
    }

    @Step("Clear all login fields")
    public void clearAllFields() {
        logger.debug("Clearing all login fields");
        MobileUI.clearText(email);
        MobileUI.clearText(password);
    }

    public boolean isLoginScreenLoaded() {
        boolean isLoaded = MobileUI.isElementPresentAndDisplayed(email)
                && MobileUI.isElementPresentAndDisplayed(password)
                && MobileUI.isElementPresentAndDisplayed(signInButton);
        logger.debug("Login screen loaded: {}", isLoaded);
        return isLoaded;
    }

    public boolean isSignInButtonDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(signInButton);
        logger.debug("Sign In button displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isEmailNotExistedErrorDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(emailNotExistedErrorMessage);
        logger.debug("Email not existed error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isEmailInvalidMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(emailInvalidMessage);
        logger.debug("Email invalid error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isEmailEmptyFieldDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(emailEmptyField);
        logger.debug("Email empty error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isPasswordEmptyFieldDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(passwordEmptyField);
        logger.debug("Password empty error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public String getEmailNotExistedErrorMessage() {
        return MobileUI.getElementAttribute(emailNotExistedErrorMessage, "content-desc");
    }

    public String getEmailInvalidMessage() {
        return MobileUI.getElementAttribute(emailInvalidMessage, "content-desc");
    }

    public String getEmailEmptyMessage() {
        return MobileUI.getElementAttribute(emailEmptyField, "content-desc");
    }

    public String getPasswordEmptyMessage() {
        return MobileUI.getElementAttribute(passwordEmptyField, "content-desc");
    }

    // ===== Action Methods =====

    @Step("Login expecting success - Email: {email}")
    public HomePage loginExpectSuccess(String email, String password) {
        logger.info("Attempting login with email: {}", email);
        fillLoginForm(email, password);
        logger.debug("Login form filled");
        clickSignInButton();
        logger.debug("Sign In button clicked");
        MobileUI.sleep(1.5);

        HomePage homePage = new HomePage();
        homePage.waitForHomePageToLoad();
        logger.info("Login successful - HomePage loaded");
        return homePage;
    }

    @Step("Login expecting failure - Email: {email}")
    public void loginExpectFailure(String email, String password) {
        logger.info("Attempting login expecting failure with email: {}", email);
        fillLoginForm(email, password);
        clickSignInButton();
        logger.debug("Waiting for error message...");
    }

/*    public void enterEmail(String emailValue) {
        MobileUI.setText(email, emailValue);
        System.out.println("[LoginPage] Entered email");
    }

    public void enterPassword(String passwordValue) {
        MobileUI.setText(password, passwordValue);
        System.out.println("[LoginPage] Entered password");
    }

    public void fillLoginForm(String emailValue, String passwordValue) {
        enterEmail(emailValue);
        enterPassword(passwordValue);
        System.out.println("[LoginPage] Filled login form");
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
        MobileUI.sleep(1.5);

        HomePage homePage = new HomePage();
        homePage.waitForHomePageToLoad(2);
        System.out.println("[LoginPage] Login completed, HomePage loaded");
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
    }*/
}
