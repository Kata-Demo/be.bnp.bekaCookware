
import org.openqa.selenium.WebDriver;

import utils.ConfigLoader;
import utils.WebDriverFactory;
import pages.CookieConsentPopUpPage;

public class Main {
    public static void main(String[] args) {

        ConfigLoader cfg = ConfigLoader.get();

        String env = cfg.getEnvironment();                          // e.g., "test"
        String baseUrl = cfg.get("base.url");                  // resolved with precedence

        System.out.println("Hello Tester!");
        System.out.println("You are configured to use: " + env );
        System.out.println("Your base url is : " + baseUrl );
        WebDriver wd = WebDriverFactory.create("PHONE", "CHROME", "EN");
        //WebDriver wd = WebDriverFactory.create("TABLET", "FIREFOX", "NL");
        //WebDriver wd = WebDriverFactory.create("DESKTOP", "FIREFOX", "DE");
        //nav to base url

        wd.get(baseUrl);
        CookieConsentPopUpPage CC = new CookieConsentPopUpPage(wd);
        System.out.println("is displayed : " + CC.isDisplayed() );
        CC.acceptIfVisible();
        System.out.println("is displayed after consent : " + CC.waitForVisible() );

    }
}