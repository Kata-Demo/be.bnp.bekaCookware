package pages.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for Survey modal.
 * to auto-close survey modal (see example below where wd is your active WebDriver).
 *      SurveyModalComponent SM = new SurveyModalComponent(wd);
 *      SM.closeIfVisible();
 */
public class SurveyModalComponent implements IComponent{
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By root = By.xpath("//div[@data-testid='survey-root']");
    private final By closeButton = By.id("hj-survey-toggle-1");

    public SurveyModalComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    // Wait until modal is visible; returns true if visible within timeout
    @Override
    public boolean waitForVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(root));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Check if modal present & displayed
    @Override
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
