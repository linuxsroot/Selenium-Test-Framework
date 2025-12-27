package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void verifyValidLoginTest(){
        ExtentManager.startTest("Valid Login Test");
        System.out.println("Running testMethod1 on thread: "+Thread.currentThread().getId());
        ExtentManager.logStep("Navigating to Login Page entering username and Password");
        loginPage.login("Admin","admin123");
        ExtentManager.logStep("Verifying Admin tab is visible or not");
        Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successful login");
        ExtentManager.logStep("Validation Successful");
        homePage.logout();
        ExtentManager.logStep("Logged out Successfully");
        staticWait(2);
    }

    @Test
    public void invalidLoginTest(){
        ExtentManager.startTest("Invalid Login Test");
        System.out.println("Running testMethod2 on thread: "+Thread.currentThread().getId());
        ExtentManager.logStep("Navigating to Login Page entering username and Password");
        loginPage.login("Admin","admin");
        String expectedErrorMessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid error message");
        ExtentManager.logStep("Validation Successful");
    }
}
