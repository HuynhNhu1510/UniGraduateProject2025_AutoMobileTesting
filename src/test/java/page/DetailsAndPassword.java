package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class DetailsAndPassword {
    public DetailsAndPassword() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
    }

    @AndroidFindBy(accessibility = "Change my password")
    WebElement changePasswordButton;

}
