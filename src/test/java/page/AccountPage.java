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

public class AccountPage {

    private static final Logger logger = LogManager.getLogger(AccountPage.class);

    public AccountPage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
        logger.debug("AccountPage initialized");
    }

    // Tọa độ cho scroll down: từ dưới lên trên
    private static final int SCROLL_START_X = 485;
    private static final int SCROLL_START_Y = 1896;
    private static final int SCROLL_END_X = 494;
    private static final int SCROLL_END_Y = 379;
    private static final int SCROLL_DURATION_MS = 1000;

    @AndroidFindBy(accessibility = "Orders & returns")
    WebElement orderAndReturns;

    @AndroidFindBy(accessibility = "Details & password")
    WebElement accountInformation;

    @AndroidFindBy(accessibility = "Address book")
    WebElement addressBook;

    @AndroidFindBy(accessibility = "Notifications & emails")
    WebElement settings;

    @AndroidFindBy(accessibility = "Sign out")
    WebElement logoutButton;

    @AndroidFindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[2]")
    WebElement signOutModal;

    @AndroidFindBy(accessibility = "Confirm sign out?")
    WebElement signOutModalTitle;

    @AndroidFindBy(accessibility = "Are you sure you want to sign out?")
    WebElement signOutModalMessage;

    @AndroidFindBy(accessibility = "Cancel")
    WebElement cancelButton;

    @AndroidFindBy(accessibility = "Yes, sign out")
    WebElement confirmSignOutButton;

    // ===== Action Methods =====

    @Step("Click Orders & Returns")
    public void clickOnOrderAndReturns() {
        logger.info("Clicking Orders & Returns");
        MobileUI.clickElement(orderAndReturns);
    }

    @Step("Click Details & Password")
    public DetailsAndPasswordPage clickOnAccountInformation() {
        logger.info("Clicking 'Details & password'");
        MobileUI.clickElement(accountInformation);
        logger.debug("Navigated to Details & Password page");
        MobileUI.sleep(0.2);
        return new DetailsAndPasswordPage();
    }

    @Step("Click Address Book")
    public void clickOnAddressBook() {
        logger.info("Clicking Address Book");
        MobileUI.clickElement(addressBook);
    }

    @Step("Click Notifications & Emails")
    public void clickOnSettings() {
        logger.info("Clicking Notifications & Emails");
        MobileUI.clickElement(settings);
    }

    @Step("Click Sign Out button")
    public void clickOnLogoutButton() {
        logger.info("Clicking Sign Out button");
        MobileUI.clickElement(logoutButton);
    }

    public boolean waitForSignOutModal() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(signOutModal);
        logger.debug("Sign out modal displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Confirm sign out")
    public void confirmSignOut() {
        logger.info("Confirming sign out");
        MobileUI.clickElement(confirmSignOutButton);
        logger.debug("Clicked 'Yes, sign out' button");
    }

    @Step("Cancel sign out")
    public void cancelSignOut() {
        logger.info("Canceling sign out");
        MobileUI.clickElement(cancelButton);
        logger.debug("Clicked 'Cancel' button");
    }

    public boolean isLogoutButtonVisible() {
        boolean isVisible = MobileUI.isElementPresentAndDisplayed(logoutButton);
        logger.debug("Logout button visible: {}", isVisible);
        return isVisible;
    }

    public boolean isSignOutModalDisplayed() {
        boolean isDisplayed = MobileUI.isElementPresentAndDisplayed(signOutModal);
        logger.debug("Sign out modal displayed: {}", isDisplayed);
        return isDisplayed;
    }

    @Step("Scroll and perform logout")
    public void scrollAndLogout() {
        logger.info("Scrolling to Sign Out button and performing logout");

        // Step 1: Scroll to Sign Out button
        MobileUI.scroll(SCROLL_START_X, SCROLL_START_Y, SCROLL_END_X, SCROLL_END_Y, SCROLL_DURATION_MS);
        clickOnLogoutButton();
        logger.debug("Sign Out button clicked");
        MobileUI.sleep(0.5);

        confirmSignOut();
        logger.info("Sign out confirmed - modal closed");
    }

    /*public void clickOnOrderAndReturns(){
        MobileUI.clickElement(orderAndReturns);
    }

    public DetailsAndPasswordPage clickOnAccountInformation(){
        MobileUI.clickElement(accountInformation);
        System.out.println("[AccountPage] Clicked 'Details & password'");
        MobileUI.sleep(0.2);
        return new DetailsAndPasswordPage();
    }
    public void clickOnAddressBook(){
        MobileUI.clickElement(addressBook);
    }

    public void clickOnSettings(){
        MobileUI.clickElement(settings);
    }

    public void clickOnLogoutButton(){
        logoutButton.click();
    }

    public boolean waitForSignOutModal() {
        return MobileUI.isElementPresentAndDisplayed(signOutModal);
    }

    public void confirmSignOut() {
        MobileUI.clickElement(confirmSignOutButton);
        System.out.println("[AccountPage] Clicked 'Yes, sign out' button");
    }

    public void cancelSignOut() {
        MobileUI.clickElement(cancelButton);
        System.out.println("[AccountPage] Clicked 'Cancel' button");
    }

    public boolean isLogoutButtonVisible() {
        return MobileUI.isElementPresentAndDisplayed(logoutButton);
    }

    public boolean isSignOutModalDisplayed() {
        return MobileUI.isElementPresentAndDisplayed(signOutModal);
    }

    public void scrollAndLogout() {
        // Step 1: Scroll to Sign Out button
        MobileUI.scroll(SCROLL_START_X, SCROLL_START_Y, SCROLL_END_X, SCROLL_END_Y, SCROLL_DURATION_MS);
        clickOnLogoutButton();
        System.out.println("[AccountPage] Clicked Sign Out button");
        MobileUI.sleep(0.5);

        confirmSignOut();
        System.out.println("[AccountPage] Confirmed sign out - modal closed");
    }
*/
}
