package be.bnp.beka.cookware.tests;

import org.openqa.selenium.WebDriver;

import be.bnp.beka.cookware.utils.WebDriverFactory;
import be.bnp.beka.cookware.utils.typedEnum.Device;
import be.bnp.beka.cookware.utils.typedEnum.Lang;
import be.bnp.beka.cookware.utils.typedEnum.Browser;

public class BaseTest {
    
    //protected WebDriver driver;
    
    protected WebDriver initWebdriver(){
        return WebDriverFactory.create(Device.DESKTOP, Browser.CHROME, Lang.EN);
    }

}
