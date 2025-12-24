package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage{

    public RegisterPage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
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

    public void enterFullName(String fullNameValue) {
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
        homePage.waitForHomePageToLoad(1);
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
    }
}
