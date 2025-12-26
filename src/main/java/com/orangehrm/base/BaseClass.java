package com.orangehrm.base;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.LoggerManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties prop;
    //protected static WebDriver driver;
    //private static ActionDriver actionDriver;

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

    public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeSuite
    public void loadConfig() throws IOException {
        //Load the configuration file
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        logger.info("config.properties loaded");
    }


    @BeforeMethod
    public synchronized void setup() throws IOException {
        System.out.println("Setting up Browser"+this.getClass().getSimpleName());
        launchBrowser();
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

    private synchronized void launchBrowser(){

        //Initialize the WebDriver based on browser defined in config.properties file
        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            //driver = new ChromeDriver();
            driver.set(new ChromeDriver()); //New Changes as per Thread
            logger.info("ChromeDriver Instance is created");
        }
        else if (browser.equalsIgnoreCase("firefox")) {
            //driver = new FirefoxDriver();
            driver.set(new FirefoxDriver()); //New Changes as per Thread
            logger.info("FirefoxDriver Instance is created");
        }else if (browser.equalsIgnoreCase("edge")) {
            //driver = new EdgeDriver();
            driver.set(new EdgeDriver()); //New Changes as per Thread
            logger.info("EdgeDriver Instance is created");
        }else{
            throw new IllegalArgumentException("Browser Not Supported "+browser);
        }
    }

    //Browser settings
    private void configureBrowser(){

        //ImplicitWait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        //Maximize the browser
        getDriver().manage().window().maximize();

        //Navigate to URL
        try {
            getDriver().get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to navigate to the URL"+e.getMessage());
        }

    }

    @AfterMethod
    public synchronized void tearDown() {
        if (getDriver() != null) {
            try {
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("Failed to quit Browser"+e.getMessage());
            }
        }
        logger.info("WebDriver instance is closed");
        driver.remove();
        actionDriver.remove();

    }


//    //Getter Method for prop
    public static Properties getProp() {
        return prop;
    }

    //Driver getter method
//    public WebDriver getDriver() {
//        return driver;
//    }

    //Getter Method for WebDriver
    public static WebDriver getDriver() {

        if (driver == null) {
            System.out.println("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver.get();
    }

    //Getter Method for ActionDriver
    public static ActionDriver getActionDriver() {

        if (actionDriver == null) {
            System.out.println("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();
    }

    //Driver setter method
    public void setDriver(ThreadLocal<WebDriver> driver) {
        BaseClass.driver = driver;
    }

    //Static wait for pause
    public void staticWait(int seconds){

        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));

    }
}
