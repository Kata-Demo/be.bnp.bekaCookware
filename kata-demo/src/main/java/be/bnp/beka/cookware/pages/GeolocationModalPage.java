package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for an in-page geolocation permission modal (not the browser native permission prompt).
 * If your app uses the browser's permission prompt, prefer configuring the browser (ChromeOptions prefs)
 * to auto-allow/block geolocation (see example below).
 *
 */
public class GeolocationModalPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // locators
    private final By root = By.cssSelector("#geolocation-container");
    private final By allowButton = By.cssSelector("div.geolocation__ctas > form > button");
    private final By closeButton = By.cssSelector("#geolocation-container > button");

    public GeolocationModalPage(WebDriver driver) {
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

    // Click allow and wait for modal to disappear
    public void allow() {
        wait.until(ExpectedConditions.elementToBeClickable(allowButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(root));
    }

    // Close via close button and wait for modal to disappear
    public void close() {
        wait.until(ExpectedConditions.elementToBeClickable(closeButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(root));
    }

    // Convenience: accept if visible
    public void allowIfVisible() {
        if (waitForVisible()) allow();
    }

    // Convenience: close if visible
    public void closeIfVisible() {
        if (waitForVisible()) close();
    }
}
