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

public class DetailsAndPasswordPage {

    private static final Logger logger = LogManager.getLogger(DetailsAndPasswordPage.class);

    public DetailsAndPasswordPage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
        logger.debug("DetailsAndPasswordPage initialized");
    }

    @AndroidFindBy(accessibility = "Change my password")
    private WebElement changePasswordButton;

    @AndroidFindBy(accessibility = "Details & password")
    private WebElement title;

    // ===== Action Methods =====

    @Step("Click Change Password button")
    public ChangePasswordPage clickChangePasswordButton() {
        logger.info("Clicking 'Change my password' button");
        MobileUI.clickElement(changePasswordButton);
        logger.debug("Navigated to Change Password page");
        MobileUI.sleep(0.3);
        return new ChangePasswordPage();
    }

    public boolean isDetailsAndPasswordPageDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(title);
        logger.debug("Details & Password page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    public String getPageTitle() {
        String titleText = MobileUI.getElementAttribute(title, "content-desc");
        logger.debug("Page title: {}", titleText);
        return titleText;
    }

    /*public ChangePasswordPage clickChangePasswordButton() {
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
    }*/
}
