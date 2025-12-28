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

public class RegisterPage{

    private static final Logger logger = LogManager.getLogger(RegisterPage.class);

    public RegisterPage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
        logger.debug("RegisterPage initialized");
    }

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[1]")
    private WebElement fullName;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[2]")
    private WebElement email;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[3]")
    private WebElement password;

    @AndroidFindBy(xpath = "//android.widget.Button[@content-desc=\"Create account\"]")
    private WebElement createAccountButton;

    @AndroidFindBy(accessibility = "Sign in")
    private WebElement signIn;

    @AndroidFindBy(accessibility = "Please enter a full name")
    private WebElement fullNameInvalidMessage;

    @AndroidFindBy(accessibility = "Please enter your email address")
    private WebElement emailEmptyMessage;

    @AndroidFindBy(accessibility = "Please enter valid email address")
    private WebElement emailInvalidMessage;

    @AndroidFindBy(accessibility = "User already exists")
    private WebElement emailExistedMessage;

    @AndroidFindBy(accessibility = "Please enter your password")
    private WebElement passwordEmptyMessage;

    @AndroidFindBy(accessibility = "Password must have at least 8 characters that include at least 1 lowercase character, 1 uppercase character, 1 number, and 1 special character in (!@#$%^&*)")
    private WebElement passwordInvalidMessage;

    // ===== Action Methods =====

    @Step("Enter full name: {fullNameValue}")
    public void enterFullName(String fullNameValue) {
        logger.debug("Entering full name: {}", fullNameValue);
        MobileUI.setText(fullName, fullNameValue);
    }

    @Step("Enter email: {emailValue}")
    public void enterEmail(String emailValue) {
        logger.debug("Entering email: {}", emailValue);
        MobileUI.setText(email, emailValue);
    }

    @Step("Enter password")
    public void enterPassword(String passwordValue) {
        logger.debug("Entering password");
        MobileUI.setText(password, passwordValue);
    }

    @Step("Fill registration form with full name: {fullNameValue}, email: {emailValue}")
    public void fillRegistrationForm(String fullNameValue, String emailValue, String passwordValue) {
        logger.info("Filling registration form...");
        enterFullName(fullNameValue);
        MobileUI.sleep(0.1);
        enterEmail(emailValue);
        MobileUI.sleep(0.1);
        enterPassword(passwordValue);
        MobileUI.sleep(0.1);
        logger.info("Registration form filled completely");
    }

    @Step("Click Create Account button")
    public void clickCreateAccount() {
        logger.info("Clicking Create Account button");
        MobileUI.clickElement(createAccountButton);
    }

    @Step("Click Sign In link")
    public void clickSignIn() {
        logger.info("Clicking Sign In link");
        MobileUI.clickElement(signIn);
    }

    @Step("Clear all registration fields")
    public void clearAllFields() {
        logger.debug("Clearing all registration fields");
        MobileUI.clearText(fullName);
        MobileUI.clearText(email);
        MobileUI.clearText(password);
    }

    public boolean isCreateAccountButtonDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(createAccountButton);
        logger.debug("Create Account button displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isRegistrationScreenLoaded() {
        boolean isLoaded = MobileUI.isElementPresentAndDisplayed(fullName)
                && MobileUI.isElementPresentAndDisplayed(email)
                && MobileUI.isElementPresentAndDisplayed(password)
                && MobileUI.isElementPresentAndDisplayed(createAccountButton);
        logger.debug("Registration screen loaded: {}", isLoaded);
        return isLoaded;
    }

    public HomePage isHomePageDisplayed() {
        logger.debug("Returning HomePage instance");
        return new HomePage();
    }

    // Error checking methods
    public String getEmailEmptyMessage() {
        return MobileUI.getElementAttribute(emailEmptyMessage, "content-desc");
    }

    public boolean isEmailEmptyMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(emailEmptyMessage);
        logger.debug("Email empty error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public String getEmailInvalidMessage() {
        return MobileUI.getElementAttribute(emailInvalidMessage, "content-desc");
    }

    public boolean isEmailInvalidMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(emailInvalidMessage);
        logger.debug("Email invalid error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public String getPasswordEmptyMessage() {
        return MobileUI.getElementAttribute(passwordEmptyMessage, "content-desc");
    }

    public boolean isPasswordInvalidMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(passwordInvalidMessage);
        logger.debug("Password invalid error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isFullNameInvalidMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(fullNameInvalidMessage);
        logger.debug("Full name invalid error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public String getPasswordInvalidMessage() {
        return MobileUI.getElementAttribute(passwordInvalidMessage, "content-desc");
    }

    public boolean isPasswordEmptyMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(passwordEmptyMessage);
        logger.debug("Password empty error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isEmailExistedMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(emailExistedMessage);
        logger.debug("Email existed error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Register user expecting success - Email: {email}")
    public HomePage registerExpectSuccess(String fullName, String email, String password) {
        logger.info("Attempting registration with email: {}", email);
        fillRegistrationForm(fullName, email, password);
        logger.debug("Registration form filled");
        clickCreateAccount();
        logger.debug("Create account button clicked");

        HomePage homePage = new HomePage();
        homePage.waitForHomePageToLoad();
        logger.info("Registration successful - navigated to HomePage");
        return homePage;
    }

    @Step("Register user expecting failure - Email: {email}")
    public void registerExpectFailure(String fullName, String email, String password) {
        logger.info("Attempting registration expecting failure with email: {}", email);
        fillRegistrationForm(fullName, email, password);
        clickCreateAccount();
        MobileUI.sleep(0.2);
        logger.debug("Waiting for error message to appear...");
    }

    /*public void enterFullName(String fullNameValue) {
        fullName.click();
        fullName.clear();
        fullName.sendKeys(fullNameValue);
    }

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

    public void fillRegistrationForm(String fullNameValue, String emailValue, String passwordValue) {
        System.out.println("[RegisterPage] Filling registration form...");
        enterFullName(fullNameValue);
        MobileUI.sleep(0.1);
        enterEmail(emailValue);
        MobileUI.sleep(0.1);
        enterPassword(passwordValue);
        MobileUI.sleep(0.1);
        System.out.println("[RegisterPage] Form filled completely");
    }

    public void clickCreateAccount() {
        MobileUI.clickElement(createAccountButton);
    }

    public void clickSignIn() {
        MobileUI.clickElement(signIn);
    }

    public void clearAllFields() {
        fullName.clear();
        email.clear();
        password.clear();
    }

    public boolean isCreateAccountButtonDisplayed() {
        return isElementDisplayed(createAccountButton);
    }

    public boolean isRegistrationScreenLoaded() {
        return isElementDisplayed(fullName)
                && isElementDisplayed(email)
                && isElementDisplayed(password)
                && isElementDisplayed(createAccountButton);
    }

    public HomePage isHomePageDisplayed() {
        return new HomePage();
    }

    // Error checking methods
    public String getEmailEmptyMessage() {
        return getElementTextSafely(emailEmptyMessage);
    }

    public boolean isEmailEmptyMessageDisplayed() {
        return isElementDisplayed(emailEmptyMessage);
    }

    public String getEmailInvalidMessage() {
        return getElementTextSafely(emailInvalidMessage);
    }

    public boolean isEmailInvalidMessageDisplayed() {
        return isElementDisplayed(emailInvalidMessage);
    }

    public String getPasswordEmptyMessage() {
        return getElementTextSafely(passwordEmptyMessage);
    }

    public boolean isPasswordInvalidMessageDisplayed() {
        return isElementDisplayed(passwordInvalidMessage);
    }

    public boolean isFullNameInvalidMessageDisplayed() {
        return isElementDisplayed(fullNameInvalidMessage);
    }

    public String getPasswordInvalidMessage() {
        return getElementTextSafely(passwordInvalidMessage);
    }

    public boolean isPasswordEmptyMessageDisplayed() {
        return isElementDisplayed(passwordEmptyMessage);
    }

    public boolean isEmailExistedMessageDisplayed() {
        return isElementDisplayed(emailExistedMessage);
    }

    public HomePage registerExpectSuccess(String fullName, String email, String password) {
        fillRegistrationForm(fullName, email, password);
        System.out.println("[RegisterPage] Fill in form already");
        clickCreateAccount();
        System.out.println("[RegisterPage] Create account button has been clicked");

        HomePage homePage = new HomePage();
        homePage.waitForHomePageToLoad();
        return homePage;
    }

    public void registerExpectFailure(String fullName, String email, String password) {
        fillRegistrationForm(fullName, email, password);
        clickCreateAccount();
        MobileUI.sleep(0.2);
        System.out.println("[RegisterPage] Waiting for error message...");
    }

    private boolean isElementDisplayed(WebElement element) {
        return MobileUI.isElementPresentAndDisplayed(element);
    }

    private String getElementTextSafely(WebElement element) {
        return MobileUI.getElementAttribute(element, "content-desc");
    }*/
}
