package DriverSetup;

import ConfigReader.ConfigReader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class BaseTest {

    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
   
    static String USERNAME = ConfigReader.getKeyFromConfig("BROWSERSTACK_UNAME");
    static String ACCESS_KEY = ConfigReader.getKeyFromConfig("BROWSERSTACK_ACCESSKEY");
    public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";


    @BeforeClass
    @Parameters({"os", "os_version", "browser", "browser_version", "device", "isMobile","platformName"})
    public void setup(String os, String osVersion, String browser, String browserVersion, String device, boolean isMobile,
                      String platformName) throws MalformedURLException {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", browser);

        Map<String, Object> bstackOptions = new HashMap<>();

        bstackOptions.put("projectName", "ElPais");
        bstackOptions.put("buildName", "ElPais Test Final");
        bstackOptions.put("seleniumVersion", "4.33.0");

        if(isMobile){
            bstackOptions.put("deviceName", device);
            bstackOptions.put("realMobile", true);
            bstackOptions.put("platformName", platformName);
        }
        else {
            bstackOptions.put("os", os);
            bstackOptions.put("osVersion", osVersion);
            bstackOptions.put("browserVersion", browserVersion);
        }

        caps.setCapability("bstack:options", bstackOptions);

        driver.set(new RemoteWebDriver(new URL(URL), caps));
        if(!browser.equalsIgnoreCase("safari"))
            driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    @AfterClass
    public void tearDownAfterClass() throws Exception {
        getDriver().quit();
        driver.remove();
    }

}
