package org.example.page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.sql.Driver;

public class AccountPage {

    public AccountPage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
    }

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

    public void clickOnOrderAndReturns(){
        orderAndReturns.click();
    }

    public void clickOnAccountInformation(){
        accountInformation.click();
    }

    public void clickOnAddressBook(){
        addressBook.click();
    }

    public void clickOnSettings(){
        settings.click();
    }

    public void clickOnLogoutButton(){
        logoutButton.click();
    }
}
