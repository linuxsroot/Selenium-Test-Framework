package com.orangehrm.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class BaseClass {

    protected Properties prop;
    protected WebDriver driver;

    @BeforeMethod
    public void setup() throws IOException {

        //Load the configuration file
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

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
            throw new IllegalArgumentException("Browser Not Supported"+browser);
        }

        //ImplicitWait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        //Maximize the browser
        driver.manage().window().maximize();

        //Navigate to URL
        driver.get(prop.getProperty("url"));

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
