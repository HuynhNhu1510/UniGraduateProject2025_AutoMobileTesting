package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
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


}
