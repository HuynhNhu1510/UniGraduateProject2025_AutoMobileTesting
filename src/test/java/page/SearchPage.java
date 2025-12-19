package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SearchPage {

    public SearchPage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
    }

    @AndroidFindBy(accessibility = "Find your favorite designer")
    WebElement searchField;

    @AndroidFindBy(accessibility = "Women\\nTab 1 of 3")
    WebElement tabWomen;

    @AndroidFindBy(accessibility = "Men\\nTab 2 of 3")
    WebElement tabMen;

    @AndroidFindBy(accessibility = "Kids\\nTab 3 of 3")
    WebElement tabKids;

    /* Category : Women & Men */
    @AndroidFindBy(accessibility = "Sale")
    private WebElement menuSale;

    @AndroidFindBy(accessibility = "New in")
    private WebElement menuNewIn;

    @AndroidFindBy(accessibility = "Shop By")
    private WebElement menuShopBy;

    @AndroidFindBy(accessibility = "Designers")
    private WebElement menuDesigners;

    @AndroidFindBy(accessibility = "Clothing")
    private WebElement menuClothing;

    @AndroidFindBy(accessibility = "Shoes")
    private WebElement menuShoes;

    @AndroidFindBy(accessibility = "Bags")
    private WebElement menuBags;

    @AndroidFindBy(accessibility = "Accessibility")
    private WebElement menuAccessories;

    @AndroidFindBy(accessibility = "Jewelry")
    private WebElement menuJewelry;

    @AndroidFindBy(accessibility = "Home")
    private WebElement menuHome;

    /* Category : Kids */
    @AndroidFindBy(accessibility = "Baby Girls (0-36 M)")
    WebElement menuBabyGirls;

    @AndroidFindBy(accessibility = "Baby Boys (0-36 M)")
    WebElement menuBabyBoys;

    @AndroidFindBy(accessibility = "Girls (2-16 YRS)")
    WebElement menuGirls;

    @AndroidFindBy(accessibility = "Boys (2-16 YRS)")
    WebElement menuBoys;

    public void  clickOnSearchButton(){
        searchField.click();
    }

    public void clickTabWomen(){
        tabWomen.click();
    }

    public void clickTabMen(){
        tabMen.click();
    }

    public void clickTabKids () {
        tabKids.click();
    }

    public void  clickMenuSale(){
        menuSale.click();
    }

    public void clickMenuNewIn(){
        menuNewIn.click();
    }

    public void clickMenuShopBy(){
        menuShopBy.click();
    }

    public void clickMenuDesigners(){
        menuDesigners.click();
    }

    public void clickMenuClothing(){
        menuClothing.click();
    }

    public void clickMenuShoes(){
        menuShoes.click();
    }

    public void clickMenuBags(){
        menuBags.click();
    }

    public void clickMenuAccessories(){
        menuAccessories.click();
    }

    public void clickMenuJewelry(){
        menuJewelry.click();
    }

    public void clickMenuHome(){
        menuHome.click();
    }

    public void clickMenuBabyGirl() {
        menuBabyGirls.click();
    }

    public void clickMenuBabyBoys(){
        menuBabyBoys.click();
    }

    public void clickMenuGirl() {
        menuGirls.click();
    }

    public void clickMenuBoys(){
        menuBoys.click();
    }

}
