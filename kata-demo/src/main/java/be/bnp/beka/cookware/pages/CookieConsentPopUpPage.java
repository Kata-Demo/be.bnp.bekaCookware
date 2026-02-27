package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.v142.page.Page;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for cookie consent popup.
 */
public class CookieConsentPopUpPage extends Page{

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By root = By.id("pandectes-banner");
    //#pandectes-banner > div > div.cc-compliance.cc-highlight > button.cc-btn.cc-btn-decision.cc-allow
    private final By acceptBtn = By.cssSelector("#pandectes-banner > div > div.cc-compliance.cc-highlight > button.cc-btn.cc-btn-decision.cc-allow");
    private final By rejectBtn = By.cssSelector("#pandectes-banner > div > div.cc-compliance.cc-highlight > button.cc-btn.cc-btn-decision.cc-deny']");

    public CookieConsentPopUpPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    // Wait until popup is visible (returns true if shown within timeout)
    public boolean waitForVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(root));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Returns true if popup root is present and displayed
    public boolean isDisplayed() {
        return driver.findElements(root).stream().anyMatch(WebElement::isDisplayed);
    }

    // Click accept (consent)
    public void accept() {
        wait.until(ExpectedConditions.elementToBeClickable(acceptBtn)).click();
        // wait for disappearance
        wait.until(ExpectedConditions.invisibilityOfElementLocated(root));
    }

    // Click reject (decline)
    public void reject() {
        wait.until(ExpectedConditions.elementToBeClickable(rejectBtn)).click();
        // wait for disappearance
        wait.until(ExpectedConditions.invisibilityOfElementLocated(root));
    }


    // Convenience: accept if visible, otherwise no-op
    public void acceptIfVisible() {
        if (waitForVisible()) accept();
    }

    // Convenience: reject if visible, otherwise no-op
    public void rejectIfVisible() {
        if (waitForVisible()) reject();
    }
}
