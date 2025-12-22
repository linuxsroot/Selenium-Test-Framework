package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private ActionDriver actionDriver;

    //Define locators using By class
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIdButton = By.className("oxd-userdropdown-name");
    private By logOutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMlogo = By.xpath("//div[@class='oxd-brand-banner']//img");

    //Initialize the ActionDriver object by passing WebDriver instance
    public HomePage(WebDriver driver){
        this.actionDriver = new ActionDriver(driver);
    }

    //Method to verify if Admin tab is visible
    public boolean isAdminTabVisible(){
        return actionDriver.isDisplayed(adminTab);
    }

    //Method to verify orangeHRM logo
    public boolean isOrangeHRMLogo(){
        return actionDriver.isDisplayed(orangeHRMlogo);
    }

    //Method to perform logout operation
    public void logout(){
        actionDriver.click(userIdButton);
        actionDriver.click(logOutButton);
    }
}
