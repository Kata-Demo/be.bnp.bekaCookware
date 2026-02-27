package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for Marketing modal.
 * to auto-close Marketing modal (see example below where wd is your active WebDriver).
 *      FlyoutModalPage FM = new FlyoutModalPage(wd);
 *      FM.closeIfVisible();
 */
public class FlyoutModalPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // locators
    private final By root = By.xpath("//div[@data-testid='FLYOUT']");
    private final By closeButton = By.cssSelector("div[data-testid='FLYOUT'] > div > div > div > button");

    public FlyoutModalPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    // Wait until modal is visible; returns true if visible within timeout
    public boolean waitForVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(root));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Check if modal present & displayed
    public boolean isDisplayed() {
        return !driver.findElements(root).isEmpty() && driver.findElement(root).isDisplayed();
    }

    // Close via close button and wait for modal to disappear
    public void close() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(root));
    }

    // Convenience: close if visible
    public void closeIfVisible() {
        if (waitForVisible()) close();
    }
}
