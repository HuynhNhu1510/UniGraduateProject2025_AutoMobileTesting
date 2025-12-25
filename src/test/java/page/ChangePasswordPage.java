package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;

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


}
