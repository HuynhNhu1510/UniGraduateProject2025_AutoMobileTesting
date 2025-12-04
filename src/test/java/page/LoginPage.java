package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class LoginPage {

    @AndroidFindBy(accessibility = "We've had a problem, please try again")
    WebElement errorMessage;
}
