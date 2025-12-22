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

    @AndroidFindBy(accessibility = "Change password")
    WebElement title;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[1]")
    WebElement currentPassword;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[2]")
    WebElement newPassword;

    @AndroidFindBy(accessibility = "Save my new password")
    WebElement saveNewPasswordButton;

    @AndroidFindBy(accessibility = "Password must have at least 8 characters that include at least 1 lowercase character, 1 uppercase character, 1 number, and 1 special character in (!@#$%^&*)")
    WebElement passwordInvalidMessage;

    @AndroidFindBy(accessibility = "Current password is invalid")
    WebElement currentPasswordErrorMessage;

    @AndroidFindBy(accessibility = "Please enter your password")
    WebElement passwordEmptyMessage;

    public void enterCurrentPassword(String password) {
        MobileUI.setText(currentPassword, password);
        System.out.println("[ChangePasswordPage] Entered current password");
    }

    public void enterNewPassword(String password) {
        MobileUI.setText(newPassword, password);
        System.out.println("[ChangePasswordPage] Entered new password");
    }

    public void clickSaveNewPasswordButton() {
        MobileUI.clickElement(saveNewPasswordButton);
        System.out.println("[ChangePasswordPage] Clicked Save my new password button");
        MobileUI.sleep(1.0); // Wait for navigation or error message
    }

    public DetailsAndPasswordPage changePasswordExpectSuccess(String currentPass, String newPass) {
        if (!currentPass.isEmpty()) {
            enterCurrentPassword(currentPass);
        }
        if (!newPass.isEmpty()) {
            enterNewPassword(newPass);
        }
        clickSaveNewPasswordButton();
        System.out.println("[ChangePasswordPage] Change password successful - navigating to Details & Password");
        return new DetailsAndPasswordPage();
    }

    public void changePasswordExpectFailure(String currentPass, String newPass) {
        if (!currentPass.isEmpty()) {
            enterCurrentPassword(currentPass);
        }
        if (!newPass.isEmpty()) {
            enterNewPassword(newPass);
        }
        clickSaveNewPasswordButton();
        System.out.println("[ChangePasswordPage] Change password failed - error expected");
    }

    public boolean isCurrentPasswordErrorDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(currentPasswordErrorMessage);
        System.out.println("[ChangePasswordPage] Current password error displayed: " + isDisplayed);
        return isDisplayed;
    }

    public boolean isPasswordEmptyMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(passwordEmptyMessage);
        System.out.println("[ChangePasswordPage] Password empty message displayed: " + isDisplayed);
        return isDisplayed;
    }

    public boolean isPasswordFormatErrorDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(passwordInvalidMessage);
        System.out.println("[ChangePasswordPage] Password format error displayed: " + isDisplayed);
        return isDisplayed;
    }

    public boolean isChangePasswordPageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(title);
        System.out.println("[ChangePasswordPage] Change Password page displayed: " + isDisplayed);
        return isDisplayed;
    }

    public String getCurrentPasswordErrorMessage() {
        return MobileUI.getElementText(currentPasswordErrorMessage);
    }

    public String getPasswordEmptyMessage() {
        return MobileUI.getElementText(passwordEmptyMessage);
    }

    public String getPasswordFormatErrorMessage() {
        return MobileUI.getElementText(passwordInvalidMessage);
    }
}
