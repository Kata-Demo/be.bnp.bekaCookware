package be.bnp.beka.cookware;
import org.openqa.selenium.WebDriver;

import be.bnp.beka.cookware.utils.ConfigLoader;
import be.bnp.beka.cookware.utils.WebDriverFactory;
import be.bnp.beka.cookware.utils.typedEnum.Browser;
import be.bnp.beka.cookware.utils.typedEnum.Device;
import be.bnp.beka.cookware.utils.typedEnum.Lang;
import be.bnp.beka.cookware.pages.HomePage;

public class Main {
    public static void main(String[] args) {

        ConfigLoader cfg = ConfigLoader.get();

        String env = cfg.getEnvironment();                          // e.g., "test"
        String baseUrl = cfg.get("base.url");                  // resolved with precedence

        WebDriver wd = WebDriverFactory.create(Device.PHONE, Browser.CHROME, Lang.EN);
        //WebDriver wd = WebDriverFactory.create("TABLET", "FIREFOX", "NL");
        //WebDriver wd = WebDriverFactory.create("DESKTOP", "FIREFOX", "DE");
        //nav to base url
        wd.get(baseUrl);
        HomePage Hp = new HomePage(wd);
        Hp.acceptCookie();
        Hp.allowGeoloc();
        Hp.closeMarketingAdd();

    }
}