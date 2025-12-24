package com.orangehrm.test;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
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
    public void verifyValidateLoginTest(){
        loginPage.login("Admin","admin123");
        Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successful login");
        homePage.logout();
        staticWait(2);
    }

    @Test
    public void invalidLoginTest(){
        loginPage.login("Admin","admin");
        String expectedErrorMessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: Invalid error message");
    }
}
