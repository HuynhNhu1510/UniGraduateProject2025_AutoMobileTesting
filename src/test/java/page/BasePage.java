package page;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;


public class BasePage {

        //Constructor bắt buộc để init elements
        public BasePage() {
            PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
        }

        @AndroidFindBy(accessibility = "ic_home")
        WebElement homeMenuItem;

        @AndroidFindBy(accessibility = "ic_search")
        WebElement searchMenuItem;

        @AndroidFindBy(accessibility = "ic_wishlist")
        WebElement wishListMenuItem;

        @AndroidFindBy(accessibility = "ic_shopping_bag_add")
        WebElement shoppingCartMenuItem;

        @AndroidFindBy(accessibility = "ic_designers")
        WebElement shopDesignerMenuItem;

        @AndroidFindBy(accessibility = "ic_person")
        WebElement accountMenuItem;

        @AndroidFindBy(accessibility = "ic_chevron_left")
        WebElement backButtonBelow;

        @AndroidFindBy(accessibility = "ic_close")
        WebElement closeButton;

        public void clickBackButtonBelow() {
            backButtonBelow.click();
        }

        public HomePage clickHomeMenuItem() {
            homeMenuItem.click();
            return new HomePage();
        }
        public void clickSearchMenuItem() {
            searchMenuItem.click();
        }

        public void clickWishListMenuItem() {
            wishListMenuItem.click();
        }

        public void clickShoppingCartMenuItem() {
            shoppingCartMenuItem.click();
        }

        public void clickShopDesignerMenuItem() {
            shopDesignerMenuItem.click();
        }

        public AccountPage clickAccountMenuItem() {
            accountMenuItem.click();
            return new AccountPage();
        }

        public void clickCloseButton() {
            closeButton.click();
        }


}

