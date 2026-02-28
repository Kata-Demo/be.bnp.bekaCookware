package pages.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
*  Page Object for component localization form (in banner Desktop).
*/
public class LocalizationFormComponent implements IComponent{

    private final WebDriver driver;
    private final WebDriverWait wait;

    // locators
    private final By form = By.id("localization_form");
    private final By disclosureToggle = By.cssSelector(".disclosure__toggle");
    private final By langList = By.id("lang-list");

    public LocalizationFormComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    /**
     * Click the language link inside the localization form that matches the provided lang code (supported "en" "nl" "de" "fr").
     */
    public void clickLanguage(String lang) {
        if (lang == null || lang.trim().isEmpty());
        String code = lang.trim().toLowerCase();
        // ensure form visible
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(form));
        } catch (TimeoutException e) {

        }

        WebElement root = driver.findElement(form);

        // open disclosure list if not visible
        boolean listVisible = !root.findElements(langList).isEmpty()
                && root.findElement(langList).isDisplayed();
        if (!listVisible) {
            // try clicking the toggle to reveal the list
            WebElement toggle = root.findElement(disclosureToggle);
            wait.until(ExpectedConditions.elementToBeClickable(toggle)).click();
            // short wait for list
            wait.until(ExpectedConditions.visibilityOfElementLocated(langList));
        }

        // find the link matching lang attribute inside the form
        By linkBy = By.cssSelector(String.format("#localization_form a.disclosure-list__option[lang='%s']", code));
        try {
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(linkBy));
            link.click();
        } catch (TimeoutException e) {

        }
    }

    @Override
    public boolean waitForVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(form));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public boolean isDisplayed() {
        return !driver.findElements(form).isEmpty() && driver.findElement(form).isDisplayed();
    }
}
