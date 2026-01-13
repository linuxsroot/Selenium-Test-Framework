package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

    protected static Properties prop;
    //protected static WebDriver driver;
    //private static ActionDriver actionDriver;

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

    //Getter method for soft assert
    public SoftAssert getSoftAssert() {
        return softAssert.get();
    }

    @BeforeSuite
    public void loadConfig() throws IOException {
        //Load the configuration file
        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties loaded");

        //Start the Extent Report
        //ExtentManager.getReporter();  --This has been implemented in TestListener
    }

    @BeforeMethod
    @Parameters("browser")
    public void setup(String browser) throws IOException {
        System.out.println("Setting up Browser"+this.getClass().getSimpleName());
        launchBrowser(browser);
        configureBrowser();
        staticWait(2);
        logger.info("WebDriver Initialized and Browser Maximized");
        logger.trace("This is a Trace message");
        logger.error("This is a Error message");
        logger.debug("This is a Debug message");
        logger.fatal("This is a Fatal message");
        logger.warn("This is a Warn message");

        /*//Initialize the actionDriver only once
        if(actionDriver == null){
            actionDriver = new ActionDriver(driver);
            logger.info("Action Driver instance created. "+Thread.currentThread().getId());
        }*/

        //Initialize ActionDriver fo the current Thread
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver Initialized for Thread: {}", Thread.currentThread().getId());
    }

    private void launchBrowser(String browser) {

        //Initialize the WebDriver based on browser defined in config.properties file
        //String browser = prop.getProperty("browser");
        boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
        String gridURL = prop.getProperty("gridURL");

        if (seleniumGrid) {
            try {
                if (browser.equalsIgnoreCase("chrome")) {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
                    driver.set(new RemoteWebDriver(new URL(gridURL), options));
                } else if (browser.equalsIgnoreCase("firefox")) {
                    FirefoxOptions options = new FirefoxOptions();
                    options.addArguments("-headless");
                    driver.set(new RemoteWebDriver(new URL(gridURL), options));
                } else if (browser.equalsIgnoreCase("edge")) {
                    EdgeOptions options = new EdgeOptions();
                    options.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
                    driver.set(new RemoteWebDriver(new URL(gridURL), options));
                } else {
                    throw new IllegalArgumentException("Browser Not Supported: " + browser);
                }
                ExtentManager.registerDriver(getDriver());
                logger.info("RemoteWebDriver instance created for Grid in headless mode");
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Grid URL", e);
            }
        } else {

            if (browser.equalsIgnoreCase("chrome")) {
                //Create ChromeOptions
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new"); //Run Chrome in headless mode
                options.addArguments("--disable-gpu"); //Disable GPU for headless mode
                options.addArguments("--window-size=1920,1080"); //Set Window size
                options.addArguments("--disable-notifications"); //Disable browser notifications
                options.addArguments("--no-sandbox"); //Required for some CI environments
                //options.addArguments("--start-maximized");
                options.addArguments("--remote-allow-origins=*");
                options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources shared usages

                //driver = new ChromeDriver();
                driver.set(new ChromeDriver(options)); //New Changes as per Thread
                ExtentManager.registerDriver(getDriver());
                logger.info("ChromeDriver Instance is created");
            } else if (browser.equalsIgnoreCase("firefox")) {

                //Create FirefoxOptions
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--headless"); //Run Firefox in headless mode
                options.addArguments("--disable-gpu"); //Disable GPU for headless mode
                options.addArguments("--width=1920"); //Set browser width
                options.addArguments("--height=1080"); //Set browser height
                options.addArguments("--disable-notifications"); //Disable browser notifications
                options.addArguments("--no-sandbox"); //Required for some CI/CD environments
                options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources shared usages

                //driver = new FirefoxDriver();
                driver.set(new FirefoxDriver(options)); //New Changes as per Thread
                ExtentManager.registerDriver(getDriver());
                logger.info("FirefoxDriver Instance is created");
            } else if (browser.equalsIgnoreCase("edge")) {

                //Create EdgeOptions
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--headless"); //Run Edge in headless mode
                options.addArguments("--disable-gpu"); //Disable GPU for headless mode
                options.addArguments("--window-size=1920,1080"); //Set Window size
                options.addArguments("--disable-notifications"); //Disable browser notifications
                options.addArguments("--no-sandbox"); //Required for some CI environments
                options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resources shared usages

                //driver = new EdgeDriver();
                driver.set(new EdgeDriver(options)); //New Changes as per Thread
                ExtentManager.registerDriver(getDriver());
                logger.info("EdgeDriver Instance is created");
            } else {
                throw new IllegalArgumentException("Browser Not Supported " + browser);
            }
        }
    }

    //Browser settings
    private void configureBrowser(){

        // Implicit Wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        // maximize the browser
        getDriver().manage().window().maximize();

        // Navigate to URL
		/*try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to Navigate to the URL:" + e.getMessage());
		} */

        boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
        if (seleniumGrid) {
            getDriver().get(prop.getProperty("url_grid"));
        } else {
            getDriver().get(prop.getProperty("url"));
        }

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver.get() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("Failed to quit Browser"+e.getMessage());
            }
        }
        logger.info("WebDriver instance is closed");
        driver.remove();
        actionDriver.remove();
        softAssert.remove();
        //ExtentManager.endTest();  --This has been implemented in TestListener

    }

    public static Properties getProp() {
        return prop;
    }

    public static WebDriver getDriver() {

        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            System.out.println("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return webDriver;
    }

    public static ActionDriver getActionDriver() {

        ActionDriver ad = actionDriver.get();
        if (ad == null) {
            System.out.println("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return ad;
    }

    public void setDriver(ThreadLocal<WebDriver> driver) {
        BaseClass.driver = driver;
    }

    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }
}
