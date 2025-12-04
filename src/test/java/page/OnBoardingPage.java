package page;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.example.drivers.DriverManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class OnBoardingPage {

    // Constructor
    public OnBoardingPage() {
        PageFactory.initElements(
                new AppiumFieldDecorator(DriverManager.getDriver()),
                this
        );
    }

    @AndroidFindBy(accessibility = "Next")
    private WebElement nextButton;

    @AndroidFindBy(accessibility = "Women")
    WebElement firstOption;

    @AndroidFindBy(accessibility = "Not now, maybe later")
    private WebElement skipButton;

    public void clickNext() {
        nextButton.click();
    }

    public void clickFirstOption() {
        firstOption.click();
    }

    public void clickSkip() {
        skipButton.click();
    }

    public void completeOnboarding() {
        System.out.println("[Onboarding] Starting onboarding flow (4 screens)...");

        // Click Next 3 times (screens 1-3)
        for (int i = 1; i <= 3; i++) {
            clickNext();
            System.out.println("[Onboarding] Completed screen " + i + "/3");
        }
        // Click "Not now, maybe later" on screen 4
        clickSkip();
        System.out.println("[Onboarding] Completed screen 4/4 (notifications)");
        System.out.println("[Onboarding] ✓ Onboarding completed successfully");
    }

    public void skipOnboardingQuick() {
        System.out.println("[Onboarding] Skipping onboarding quickly...");

        clickNext();
        clickFirstOption();
        if(nextButton.isEnabled()) {
            clickNext();
        }
        clickNext();
        clickSkip();

        System.out.println("[Onboarding] ✓ Onboarding skipped");
    }

    public boolean isOnboardingDisplayed() {
        return isElementDisplayed(nextButton);
    }

    public boolean isSkipButtonAvailable() {
        return isElementDisplayed(skipButton);
    }

    public boolean isNextButtonAvailable() {
        return isElementDisplayed(nextButton);
    }

    private boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}
