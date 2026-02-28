package pages.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BannerComponent implements IComponent{
    
    private final WebDriver driver;
    private final WebDriverWait wait;

    // locators
    private final By bannerNavRoot = By.cssSelector("#shopify-section-sections--24709882118520__announcement-bar-menu");
    //private final By bannerNavList = By.cssSelector("ul.top-nav__list");
    private final By bannerOverBeka = By.cssSelector("li.top-nav__list-item:nth-child(1)");
    private final By bannerFaq = By.cssSelector("li.top-nav__list-item:nth-child(2)");
    private final By bannerFindStore = By.cssSelector("li.top-nav__list-item:nth-child(3)");
    private final By bannerInfoPFA = By.cssSelector("li.top-nav__list-item:nth-child(4)");
    private final By bannerBestSellers = By.cssSelector("li.top-nav__list-item:nth-child(5)");

    public BannerComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    public void clickBannerOverBeka(){
       wait.until(ExpectedConditions.elementToBeClickable(bannerOverBeka)).click();
    }

    public void clickBannerFaq(){
       wait.until(ExpectedConditions.elementToBeClickable(bannerFaq)).click();
    }
    
    public void clickBannerFindStore(){
       wait.until(ExpectedConditions.elementToBeClickable(bannerFindStore)).click();
    }

    public void clickBannerInfoPFA(){
       wait.until(ExpectedConditions.elementToBeClickable(bannerInfoPFA)).click();
    }

    public void clickBannerBestSellers(){
       wait.until(ExpectedConditions.elementToBeClickable(bannerBestSellers)).click();
    }

    // Wait until is visible; returns true if visible within timeout
    @Override
    public boolean waitForVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(bannerNavRoot));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Check if present & displayed
    @Override
    public boolean isDisplayed() {
        return !driver.findElements(bannerNavRoot).isEmpty() && driver.findElement(bannerNavRoot).isDisplayed();
    }
    
}
