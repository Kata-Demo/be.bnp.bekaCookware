package be.bnp.beka.cookware.pages;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import be.bnp.beka.cookware.pages.component.*;
import be.bnp.beka.cookware.utils.typedEnum.*;

public class HomePage implements IPage{

    private WebDriver driver;
    private final WebDriverWait wait;

    // Used component
    private final CookieConsentPopUpComponent cookieConsentComponent;
    private final FlyoutModalComponent flyoutModalComponent;
    private final GeolocationModalComponent geolocationModalComponent;
    private final LocalizationFormComponent localizationFormComponent;
    private final BannerComponent bannerComponent;
    private final FooterComponent footerComponent;

    // locators
    private final By homeTitle =  By.cssSelector("h1.slideshow__title");

    public HomePage(WebDriver driver) {
        //this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        this.cookieConsentComponent = new CookieConsentPopUpComponent(driver);
        this.flyoutModalComponent = new FlyoutModalComponent(driver);
        this.geolocationModalComponent = new GeolocationModalComponent(driver);
        this.localizationFormComponent = new LocalizationFormComponent(driver);
        this.bannerComponent = new BannerComponent(driver);
        this.footerComponent = new FooterComponent(driver);
    }

    public void acceptCookie(){
        cookieConsentComponent.acceptIfVisible();
    }

    public void allowGeoloc(){
        geolocationModalComponent.allowIfVisible();
    }

    public void selectLanguage(Lang lang){
        localizationFormComponent.clickLanguage(lang);
    }

    public void closeMarketingAdd(){
        flyoutModalComponent.closeIfVisible();
    }

    public String getHomeTitleText(){
        WebElement homeH1 = wait.until(ExpectedConditions.elementToBeClickable(homeTitle));
        // find direct child elements of h1 and join their visible text
        List<WebElement> children = homeH1.findElements(By.xpath("./*"));
        return children.stream()
                .map(WebElement::getText)
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.joining(" "));

    }


    @Override
    public String getUrlPath() {
        return "";
    }

    @Override
    public boolean isLoaded(WebDriver driver) {
        return !driver.findElements(homeTitle).isEmpty() && driver.findElement(homeTitle).isDisplayed();
    }
    
}
