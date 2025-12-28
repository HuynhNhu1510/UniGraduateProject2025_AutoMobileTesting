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


public class BasePage {

        //Constructor bắt buộc để init elements
        public BasePage() {
            PageFactory.initElements(new AppiumFieldDecorator(DriverManager.getDriver()), this);
            logger.debug("BasePage initialized");
        }

        private static final Logger logger = LogManager.getLogger(BasePage.class);

        @AndroidFindBy(accessibility = "ic_home")
        private WebElement homeMenuItem;

        @AndroidFindBy(accessibility = "ic_search")
        private WebElement searchMenuItem;

        @AndroidFindBy(accessibility = "ic_wishlist")
        private WebElement wishListMenuItem;

        @AndroidFindBy(accessibility = "ic_shopping_bag_add")
        private WebElement shoppingCartMenuItem;

        @AndroidFindBy(accessibility = "ic_designers")
        private WebElement shopDesignerMenuItem;

        @AndroidFindBy(accessibility = "ic_person")
        private WebElement accountMenuItem;

        @AndroidFindBy(accessibility = "ic_chevron_left")
        private WebElement backButtonBelow;

        @AndroidFindBy(accessibility = "ic_close")
        private WebElement closeButton;

    // ===== Action Methods =====

    @Step("Click back button below")
    public void clickBackButtonBelow() {
        logger.info("Clicking back button below");
        MobileUI.clickElement(backButtonBelow);  // ← SỬA: Dùng MobileUI
    }

    @Step("Click Home menu item")
    public HomePage clickHomeMenuItem() {
        logger.info("Clicking Home menu item");
        MobileUI.clickElement(homeMenuItem);
        return new HomePage();
    }

    @Step("Click Search menu item")
    public void clickSearchMenuItem() {
        logger.info("Clicking Search menu item");
        MobileUI.clickElement(searchMenuItem);
    }

    @Step("Click WishList menu item")
    public void clickWishListMenuItem() {
        logger.info("Clicking WishList menu item");
        MobileUI.clickElement(wishListMenuItem);
    }

    @Step("Click Shopping Cart menu item")
    public void clickShoppingCartMenuItem() {
        logger.info("Clicking Shopping Cart menu item");
        MobileUI.clickElement(shoppingCartMenuItem);  // ← SỬA: Dùng MobileUI
    }

    @Step("Click Shop Designer menu item")
    public void clickShopDesignerMenuItem() {
        logger.info("Clicking Shop Designer menu item");
        MobileUI.clickElement(shopDesignerMenuItem);  // ← SỬA: Dùng MobileUI
    }

    @Step("Click Account menu item")
    public AccountPage clickAccountMenuItem() {
        logger.info("Clicking Account menu item");
        MobileUI.clickElement(accountMenuItem);
        return new AccountPage();
    }

    @Step("Click Close button")
    public void clickCloseButton() {
        logger.info("Clicking Close button");
        MobileUI.clickElement(closeButton);
    }

        /*public void clickBackButtonBelow() {
            backButtonBelow.click();
        }

        public HomePage clickHomeMenuItem() {
            MobileUI.clickElement(homeMenuItem);
            return new HomePage();
        }
        public void clickSearchMenuItem() {
            MobileUI.clickElement(searchMenuItem);
        }

        public void clickWishListMenuItem() {
            MobileUI.clickElement(wishListMenuItem);
        }

        public void clickShoppingCartMenuItem() {
            shoppingCartMenuItem.click();
        }

        public void clickShopDesignerMenuItem() {
            shopDesignerMenuItem.click();
        }

        public AccountPage clickAccountMenuItem() {
        MobileUI.clickElement(accountMenuItem);
        return new AccountPage();
    }

        public void clickCloseButton() {
            MobileUI.clickElement(closeButton);
        }
*/

}

