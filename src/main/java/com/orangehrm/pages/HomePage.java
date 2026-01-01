package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private ActionDriver actionDriver;

    //Define locators using By class
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIdButton = By.className("oxd-userdropdown-name");
    private By logOutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMlogo = By.xpath("//div[@class='oxd-brand-banner']//img");


    private By pimTab = By.xpath("//span[text()='PIM']");
    private By employeeSearch = By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
    private By searchButton = By.xpath("//button[text()=' Search ']");
    private By emplFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
    private By emplLastName = By.xpath("//div[@class='oxd-table-card']/div/div[4]");


    //Initialize the ActionDriver object by passing WebDriver instance
    /*public HomePage(WebDriver driver){
        this.actionDriver = new ActionDriver(driver);
    }*/


    public HomePage(WebDriver driver) {
        this.actionDriver = BaseClass.getActionDriver();
    }

    //Method to verify if Admin tab is visible
    public boolean isAdminTabVisible(){
        return actionDriver.isDisplayed(adminTab);
    }

    //Method to verify orangeHRM logo
    public boolean isOrangeHRMLogo(){
        return actionDriver.isDisplayed(orangeHRMlogo);
    }

    //Method to Navigate to PIM tab
    public void clickOnPIMTab(){
        actionDriver.click(pimTab);
    }

    //Employee Search
    public void employeeSearch(String value){
        actionDriver.enterText(employeeSearch, value);
        actionDriver.click(searchButton);
        actionDriver.scrollToElement(emplFirstAndMiddleName);
    }

    //Verify employee first and middle name
    public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDb){
        return actionDriver.compareText(emplFirstAndMiddleName,emplFirstAndMiddleNameFromDb);

    }

    //verify employee last name
    public boolean verifyEmployeeLastName(String emplLastNameFromDb){
        return actionDriver.compareText(emplLastName,emplLastNameFromDb);
    }

    //Method to perform logout operation
    public void logout(){
        actionDriver.click(userIdButton);
        actionDriver.click(logOutButton);
    }
}
