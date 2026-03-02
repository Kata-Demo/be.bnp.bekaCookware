package be.bnp.beka.cookware.pages.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
*  Page Object for component Footer.
*/
public class FooterComponent implements IComponent{

    private final WebDriver driver;
    private final WebDriverWait wait;

    // locators
    private final By root = By.id("shopify-section-footer");

    public FooterComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    // Wait until is visible (returns true if shown within timeout)
    @Override
    public boolean waitForVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(root));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Check if present & displayed
    @Override
    public boolean isDisplayed() {
        return !driver.findElements(root).isEmpty() && driver.findElement(root).isDisplayed();
    }
    
}
