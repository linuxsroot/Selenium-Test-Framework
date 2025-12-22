package com.orangehrm.base;

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
    protected WebDriver driver;

    @BeforeSuite
    public void loadConfig() throws IOException {
        //Load the configuration file
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
    }


    @BeforeMethod
    public void setup() throws IOException {
        System.out.println("Setting up Browser"+this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);
    }

    private void launchBrowser(){

        //Initialize the WebDriver based on browser defined in config.properties file
        String browser = prop.getProperty("browser");

        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        }
        else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }else if (browser.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        }else{
            throw new IllegalArgumentException("Browser Not Supported "+browser);
        }
    }

    //Browser settings
    private void configureBrowser(){

        //ImplicitWait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        //Maximize the browser
        driver.manage().window().maximize();

        //Navigate to URL
        try {
            driver.get(prop.getProperty("url"));
        } catch (Exception e) {
            System.out.println("Failed to navigate to the URL"+e.getMessage());
        }

    }

    @AfterMethod
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception e) {
            System.out.println("Failed to quit Browser"+e.getMessage());
        }
    }


    //Getter Method for prop
    public static Properties getProp() {
        return prop;
    }

    //Driver getter method
    public WebDriver getDriver() {
        return driver;
    }

    //Driver setter method
    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    //Static wait for pause
    public void staticWait(int seconds){

        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));

    }
}
