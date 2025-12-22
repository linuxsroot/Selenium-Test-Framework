package com.orangehrm.pages;

import com.orangehrm.actiondriver.ActionDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private ActionDriver actionDriver;

    //Define locators using By class
    private By userNameField = By.name("username");
    private By passwordField = By.cssSelector("input[type='password']");
    private By loginButton = By.xpath("//button[text()=' Login ']");
    private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    //Initialize the ActionDriver object by passing WebDriver instance
    public LoginPage(WebDriver driver){
        this.actionDriver = new ActionDriver(driver);
    }

    //Method to perform login
    public void login(String username, String password){
        actionDriver.enterText(userNameField, username);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
    }

    //Method to check if error message is displayed
    public boolean isErrorMessageDisplayed(){
        return actionDriver.isDisplayed(errorMessage);
    }

    //Method to get the text from error message
    public String getErrorMessageText(){
        return actionDriver.getText(errorMessage);
    }

    //Verify if error is correct or not
    public void verifyErrorMessage(String expectedError){
        actionDriver.enterText(errorMessage, expectedError);
    }


}
