package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import utils.typedEnum.Device;
import utils.typedEnum.Browser;

/**
 * Simple WebDriver factory that returns WebDriver based on device, browser, language.
 * Supports local Chrome/Firefox and RemoteWebDriver if remote.url is provided in config.
 */
public final class WebDriverFactory {


    private WebDriverFactory() {}

    public static WebDriver create(String deviceParam, String browserParam, String langParam) {
        ConfigLoader cfg = ConfigLoader.get();

        String browserStr = Optional.ofNullable(browserParam).orElse(cfg.getDefaultBrowser());
        String deviceStr  = Optional.ofNullable(deviceParam).orElse(cfg.getDefaultDevice());
        String lang       = Optional.ofNullable(langParam).orElse(cfg.getDefaultLang());
        String remoteUrl  = cfg.get("remote.url"); // may be null/empty

        Browser browser = Browser.valueOf(browserStr.trim().toUpperCase());
        Device device = Device.valueOf(deviceStr.trim().toUpperCase());

        boolean useRemote = remoteUrl != null && !remoteUrl.trim().isEmpty();
        WebDriver wd = null;
        switch (browser) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                // language
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("intl.accept_languages", lang);
                chromeOptions.setExperimentalOption("prefs", prefs);
                //full screen size for webdriver launch
                chromeOptions.addArguments("--start-maximized");
                if (useRemote) {
                    wd = new RemoteWebDriver(toURL(remoteUrl), chromeOptions);
                } else {
                    // if using WebDriverManager, ensure driver binary is available
                    wd = new ChromeDriver(chromeOptions);
                }
                DevToolsEmulation.applyEmulation(wd, device);
                return wd;

            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                // language
                firefoxOptions.addPreference("intl.accept_languages", lang);
                //full screen size for webdriver launch
                firefoxOptions.addArguments("--kiosk");
                if (device == Device.PHONE) {
                    // approximate mobile by setting user agent and window size
                    firefoxOptions.addPreference("general.useragent.override",
                            "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Mobile Safari/537.36");
                    firefoxOptions.addArguments("--width=390");
                    firefoxOptions.addArguments("--height=844");
                    } else if (device == Device.TABLET) {
                    firefoxOptions.addPreference("general.useragent.override",
                            "Mozilla/5.0 (Linux; Android 11; Tablet) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
                    firefoxOptions.addArguments("--width=820");
                    firefoxOptions.addArguments("--height=1180");
                } // desktop uses defaults
                if (useRemote) {
                    wd = new RemoteWebDriver(toURL(remoteUrl), firefoxOptions);
                } else {
                    wd = new FirefoxDriver(firefoxOptions);
                }
                return wd;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserParam);
        }
    }

    private static URL toURL(String s) {
        //use of new URL(s) is deprecated since Java 20 
        try { return new URI(s).toURL(); }
        catch (Exception e) { throw new RuntimeException("Invalid remote.url: " + s, e); }
    }
}
