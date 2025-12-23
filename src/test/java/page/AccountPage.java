package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.example.keywords.MobileUI;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AccountPage {

    public AccountPage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
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

    public void clickOnOrderAndReturns(){
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


}
