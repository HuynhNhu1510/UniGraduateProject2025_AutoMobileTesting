package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.keywords.MobileUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class ChangePasswordPage {

    private static final Logger logger = LogManager.getLogger(ChangePasswordPage.class);

    public ChangePasswordPage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
        logger.debug("ChangePasswordPage initialized");
    }

    @AndroidFindBy(accessibility = "Change password")
    private WebElement title;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[1]")
    private WebElement currentPasswordField;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.widget.EditText[2]")
    private WebElement newPasswordField;

    @AndroidFindBy(accessibility = "Save my new password")
    private WebElement saveButton;

    @AndroidFindBy(accessibility = "Password must have at least 8 characters that include at least 1 lowercase character, 1 uppercase character, 1 number, and 1 special character in (!@#$%^&*)")
    private WebElement passwordInvalidMessage;

    @AndroidFindBy(accessibility = "Current password is invalid")
    private WebElement currentPasswordErrorMessage;

    @AndroidFindBy(accessibility = "Please enter your password")
    private WebElement passwordEmptyMessage;

    // ===== Action Methods =====

    @Step("Enter current password")
    public void enterCurrentPassword(String password) {
        logger.debug("Entering current password");
        MobileUI.setText(currentPasswordField, password);
        logger.debug("Current password entered successfully");
    }

    @Step("Enter new password")
    public void enterNewPassword(String password) {
        logger.debug("Entering new password");
        MobileUI.setText(newPasswordField, password);
        logger.debug("New password entered successfully");
    }

    @Step("Click Save button")
    public void clickSaveButton() {
        logger.info("Clicking Save button");
        MobileUI.clickElement(saveButton);
    }

    @Step("Fill change password form")
    public void fillChangePasswordForm(String currentPassword, String newPassword) {
        logger.info("Filling change password form");
        enterCurrentPassword(currentPassword);
        MobileUI.sleep(0.1);
        enterNewPassword(newPassword);
        MobileUI.sleep(0.1);
        logger.debug("Change password form filled successfully");
    }

    @Step("Change password expecting success")
    public DetailsAndPasswordPage changePasswordExpectSuccess(String currentPassword, String newPassword) {
        logger.info("Attempting password change expecting success");
        fillChangePasswordForm(currentPassword, newPassword);
        clickSaveButton();
        logger.debug("Submitted password change");
        MobileUI.sleep(1.85);

        DetailsAndPasswordPage detailsPage = new DetailsAndPasswordPage();
        logger.info("Password changed successfully - returned to Details & Password page");
        return detailsPage;
    }

    @Step("Change password expecting failure")
    public void changePasswordExpectFailure(String currentPassword, String newPassword) {
        logger.info("Attempting password change expecting failure");
        fillChangePasswordForm(currentPassword, newPassword);
        clickSaveButton();
        MobileUI.sleep(1.75);
        logger.debug("Waiting for error message...");
    }

    public boolean isChangePasswordPageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(title);
        logger.debug("Change Password page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isPasswordInvalidMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(passwordInvalidMessage);
        logger.debug("Password invalid error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isCurrentPasswordErrorDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(currentPasswordErrorMessage);
        logger.debug("Current password error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public boolean isPasswordEmptyMessageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(passwordEmptyMessage);
        logger.debug("Password empty error displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public String getPasswordInvalidMessage() {
        return MobileUI.getElementAttribute(passwordInvalidMessage, "content-desc");
    }

    public String getCurrentPasswordErrorMessage() {
        return MobileUI.getElementAttribute(currentPasswordErrorMessage, "content-desc");
    }

    public String getPasswordEmptyMessage() {
        return MobileUI.getElementAttribute(passwordEmptyMessage, "content-desc");
    }

    @Step("Clear all password fields")
    public void clearAllFields() {
        logger.debug("Clearing all password fields");
        MobileUI.clearText(currentPasswordField);
        MobileUI.clearText(newPasswordField);
    }

    /*public void enterCurrentPassword(String password) {
        MobileUI.setText(currentPasswordField, password);
        System.out.println("[ChangePasswordPage] Entered current password");
    }

    public void enterNewPassword(String password) {
        MobileUI.setText(newPasswordField, password);
        System.out.println("[ChangePasswordPage] Entered new password");
    }

    public void clickSaveButton() {
        MobileUI.clickElement(saveButton);
        System.out.println("[ChangePasswordPage] Clicked Save button");
    }

    public void fillChangePasswordForm(String currentPassword, String newPassword) {
        enterCurrentPassword(currentPassword);
        MobileUI.sleep(0.1);
        enterNewPassword(newPassword);
        MobileUI.sleep(0.1);
        System.out.println("[ChangePasswordPage] Filled change password form");
    }

    public DetailsAndPasswordPage changePasswordExpectSuccess(String currentPassword, String newPassword) {
        fillChangePasswordForm(currentPassword, newPassword);
        clickSaveButton();
        System.out.println("[ChangePasswordPage] Submitted password change");
        MobileUI.sleep(1.85);

        DetailsAndPasswordPage detailsPage = new DetailsAndPasswordPage();
        System.out.println("[ChangePasswordPage] Returned to Details & Password page");
        return detailsPage;
    }

    public void changePasswordExpectFailure(String currentPassword, String newPassword) {
        fillChangePasswordForm(currentPassword, newPassword);
        clickSaveButton();
        MobileUI.sleep(1.75);
        System.out.println("[ChangePasswordPage] Waiting for error message...");
    }

    public boolean isChangePasswordPageDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(title);
    }

    public boolean isPasswordInvalidMessageDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(passwordInvalidMessage);
    }

    public boolean isCurrentPasswordErrorDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(currentPasswordErrorMessage);
    }

    public boolean isPasswordEmptyMessageDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(passwordEmptyMessage);
    }

    public String getPasswordInvalidMessage() {
        return MobileUI.getElementAttribute(passwordInvalidMessage, "content-desc");
    }

    public String getCurrentPasswordErrorMessage() {
        return MobileUI.getElementAttribute(currentPasswordErrorMessage, "content-desc");
    }

    public String getPasswordEmptyMessage() {
        return MobileUI.getElementAttribute(passwordEmptyMessage, "content-desc");
    }

    public void clearAllFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
    }*/
}
