package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ChangePasswordPage {

    public ChangePasswordPage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
    }

    // ==================== ELEMENTS ====================

    @AndroidFindBy(accessibility = "Change password")
    private WebElement title;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[1]")
    private WebElement currentPassword;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[2]")
    private WebElement newPassword;

    @AndroidFindBy(accessibility = "Save my new password")
    private WebElement saveNewPasswordButton;

    @AndroidFindBy(accessibility = "Password must have at least 8 characters that include at least 1 lowercase character, 1 uppercase character, 1 number, and 1 special character in (!@#$%^&*)")
    private WebElement passwordInvalidMessage;

    @AndroidFindBy(accessibility = "Current password is invalid")
    private WebElement currentPasswordErrorMessage;

    @AndroidFindBy(accessibility = "Please enter your password")
    private WebElement passwordEmptyMessage;

    public void enterCurrentPassword(String passwordValue) {
        MobileUI.setText(currentPassword, passwordValue);
        System.out.println("[ChangePasswordPage] Entered current password");
    }

    public void enterNewPassword(String passwordValue) {
        MobileUI.setText(newPassword, passwordValue);
        System.out.println("[ChangePasswordPage] Entered new password");
    }

    public void fillChangePasswordForm(String currentPass, String newPass) {
        enterCurrentPassword(currentPass);
        enterNewPassword(newPass);
        System.out.println("[ChangePasswordPage] Filled change password form");
    }

    public void clickSaveButton() {
        MobileUI.clickElement(saveNewPasswordButton);
        System.out.println("[ChangePasswordPage] Clicked 'Save my new password' button");
    }

    public void clearAllFields() {
        MobileUI.clearText(currentPassword);
        MobileUI.clearText(newPassword);
        System.out.println("[ChangePasswordPage] Cleared all fields");
    }

    public boolean isChangePasswordTitleDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(title);
    }

    public boolean isCurrentPasswordErrorDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(currentPasswordErrorMessage);
    }

    public boolean isPasswordEmptyMessageDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(passwordEmptyMessage);
    }

    public boolean isPasswordInvalidMessageDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(passwordInvalidMessage);
    }

    public String getCurrentPasswordErrorMessage() {
        return MobileUI.getElementAttribute(currentPasswordErrorMessage, "content-desc");
    }

    public String getPasswordEmptyMessage() {
        return MobileUI.getElementAttribute(passwordEmptyMessage, "content-desc");
    }

    public String getPasswordInvalidMessage() {
        return MobileUI.getElementAttribute(passwordInvalidMessage, "content-desc");
    }

    public DetailsAndPasswordPage changePasswordExpectSuccess(String currentPass, String newPass) {
        fillChangePasswordForm(currentPass, newPass);
        clickSaveButton();
        MobileUI.sleep(0.3);
        System.out.println("[ChangePasswordPage] Change password successful - navigating to Details & Password");
        return new DetailsAndPasswordPage();
    }

    public void changePasswordExpectFailure(String currentPass, String newPass) {
        fillChangePasswordForm(currentPass, newPass);
        clickSaveButton();
        MobileUI.sleep(0.2);
        System.out.println("[ChangePasswordPage] Change password failed as expected");
    }
}
