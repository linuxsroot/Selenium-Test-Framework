package com.orangehrm.actiondriver;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;
    public static final Logger logger = BaseClass.logger;

    public ActionDriver(WebDriver driver) {
        this.driver =driver;
        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        logger.info("WebDriver instance is created.");
    }


    //Method to Click an element
    public void click(By by){
        String elementDescription = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logStep("Clicked an element : "+elementDescription);
            logger.info("Clicked an element--> {}", elementDescription);
        } catch (Exception e) {
            System.out.println("Unable to click element: " + e.getMessage());
            ExtentManager.logFailure(BaseClass.getDriver(),"Unable to click element: ",elementDescription+"_unable to click");
            logger.error("Unable to click element");
        }
    }


    //Method to enter the text into an input field
    public void enterText(By by, String value){
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
            logger.info("Entered text on: {}-->{}", getElementDescription(by), value);
        } catch (Exception e) {
            logger.error("Unable to enter value: {}", e.getMessage());
        }
    }

    //Method to get text from an input field
    public String getText(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            logger.error("Unable to get text: {}", e.getMessage());
            return "";
        }
    }

    //Method to compare two text --changed the return type
    public boolean compareText(By by, String expectedText){
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if(expectedText.equals(actualText)){
                logger.info("Texts are Matching: {} equals {}", actualText, expectedText);
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Compare Text","Text Verified successfully! "+actualText+" equals "+expectedText);
                return true;
            }
            else {
                logger.error("Texts are not Matching: {} not equals {}", actualText, expectedText);
                ExtentManager.logFailure(BaseClass.getDriver(),"Compare Text","Text Comparison Failed! "+actualText+" not equals "+expectedText);
                return false;
            }
        } catch (Exception e) {
            logger.error("Unable to compare texts: {}", e.getMessage());
        }
        return false;
    }


    //Method to check if an element is displayed
    public boolean isDisplayed(By by){
        try {
            waitForElementToBeVisible(by);
            logger.info("Element is displayed: {}", getElementDescription(by));
            ExtentManager.logStep("Element is displayed: "+getElementDescription(by));
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Element is displayed: ","Element is displayed: "+getElementDescription(by));
            return driver.findElement(by).isDisplayed();
        }
        catch (Exception e) {
            logger.error("Element is not displayed: {}", e.getMessage());
            ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed: ","Element is not displayed"+getElementDescription(by));
            return false;
        }
    }


    //Wait for the page to load
    public void waitForPageLoad(int  timeOutInSec){
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully.");
        } catch (Exception e) {
            logger.error("Page did not load within {} seconds. Exception: {}", timeOutInSec, e.getMessage());
        }
    }

    //Scroll to an element
    public void scrollToElement(By by){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0].scrollIntoView(true);",element);
        } catch (Exception e) {
            logger.error("Unable to scroll element: {}", e.getMessage());
        }
    }


    //Wait for element to be clickable
    private void waitForElementToBeClickable(By by){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.error("Element is not clickable: {}", e.getMessage());
        }
    }


    //Element to be visible
    private void waitForElementToBeVisible(By by){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            logger.error("Element is not visible: {}", e.getMessage());
        }
    }

    //Method to get the description of an element using By  locator
    public String getElementDescription(By locator){
        //Check for null driver or locator to avoid NullPointer Exception
        if(driver==null)
            return "Driver is null";
        if(locator==null)
            return "Locator is null";

        try {
            //Find the element using the locator
            WebElement element = driver.findElement(locator);

            //Get element Attributes
            String name = element.getDomAttribute("name");
            String id = element.getDomAttribute("id");
            String text = element.getText();
            String className = element.getDomAttribute("class");
            String placeHolder = element.getAttribute("placeholder");

            //Return the Description based on element attributes
            if(isNotEmpty(name)){
                return "Element with name: " + name;
            } else if (isNotEmpty(id)) {
                return "Element with id: " + id;
            } else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text,50);
            } else if (isNotEmpty(className)) {
                return "Element with class name: " + className;
            } else if (isNotEmpty(placeHolder)) {
                return "Element with placeholder: " + placeHolder;
            }
        } catch (Exception e) {
            logger.error("Unable to describe the element: {}", e.getMessage());
        }
        return "Unable to describe the element";
    }

    //Utility method to check a String is not NULL or empty
    private boolean isNotEmpty(String value){
        return value!=null && !value.isEmpty();
    }

    //Utility Method to truncate long String
    private String truncate(String value, int maxLength){
        if(value==null || value.length() <= maxLength){
            return value;
        }
        return value.substring(0, maxLength)+"...";
    }




}
