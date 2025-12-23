package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class DetailsAndPasswordPage {

    public DetailsAndPasswordPage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
    }

    @AndroidFindBy(accessibility = "Change my password")
    WebElement changePasswordButton;

    @AndroidFindBy(accessibility = "Details & password")
    WebElement title;

    public ChangePasswordPage clickChangePasswordButton() {
        MobileUI.clickElement(changePasswordButton);
        System.out.println("[DetailsAndPasswordPage] Clicked 'Change my password' button");
        MobileUI.sleep(0.3);
        return new ChangePasswordPage();
    }

    public boolean isDetailsAndPasswordPageDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(title);
    }

    public String getPageTitle() {
        return MobileUI.getElementAttribute(title, "content-desc");
    }
}
