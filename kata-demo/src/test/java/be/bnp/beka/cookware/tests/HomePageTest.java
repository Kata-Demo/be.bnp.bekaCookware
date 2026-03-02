package be.bnp.beka.cookware.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import be.bnp.beka.cookware.utils.ConfigLoader;
import be.bnp.beka.cookware.utils.I18nLoader;
import be.bnp.beka.cookware.utils.typedEnum.Lang;
import be.bnp.beka.cookware.pages.HomePage;

public class HomePageTest extends BaseTest{

    HomePage homePage;
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ConfigLoader cfg = ConfigLoader.get();
        String baseurl = cfg.getBaseUrl();
        //System.out.println(" Hello Tester your baseurl is : " + baseurl);
        driver = initWebdriver();
        driver.get(baseurl);
        homePage = new HomePage(driver);
        homePage.acceptCookie();
        homePage.allowGeoloc();
        homePage.closeMarketingAdd();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try { driver.quit(); } catch (Exception ignored) {}
            driver = null;
        }
    }

    //Validate language switching functionality
    //Validate page title in English and Dutch
    @Test(description = "Change site language and verify title")
    public void changeLanguageAndVerifyTitle() {
        verifyChangeLanguage(Lang.FR);
        verifyChangeLanguage(Lang.NL);
        verifyChangeLanguage(Lang.DE);
        verifyChangeLanguage(Lang.EN);
    }

    @Test(description = "verify logo")
    public void VerifyLogo() {
            // todo next
    }

    private void verifyChangeLanguage(Lang lang){

        I18nLoader loader = I18nLoader.forLang(lang.toString());
        homePage.selectLanguage(lang);
        String expectedTitle = loader.get("HomePage.titlePage");
        String title = homePage.getHomeTitleText();
        //System.out.println("title from page : " + title );
        //System.out.println("title from oracle : " + expectedTitle );

        // Assert that title match oracle
        Assert.assertEquals(title,expectedTitle);
    }
}
