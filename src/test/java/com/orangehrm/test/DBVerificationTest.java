package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class DBVerificationTest extends BaseClass {
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider = "emplVerification",dataProviderClass = DataProviders.class)
    public void verifyEmployeeNameVerificationFromDB(String emplID,String empName) {
        ExtentManager.logStep("Logging with Admin Credentials");
        loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

        ExtentManager.logStep("Click on PIM tab");
        homePage.clickOnPIMTab();

        ExtentManager.logStep("Search for Employee");
        homePage.employeeSearch(empName);

        ExtentManager.logStep("Get the Employee Name from DB");
        String employee_id = emplID;

        //Fetch the data into a map
        Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);

        String emplFirstName = employeeDetails.get("firstName");
        String emplMiddleName = employeeDetails.get("middleName");
        String emplLastName = employeeDetails.get("lastName");

        String emplFirstAndMiddleName = (emplFirstName + " " + emplMiddleName).trim();

        ExtentManager.logStep("Verify the employee first and middle name");
        Assert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName), "First and Middle name are not matching");

        ExtentManager.logStep("Verify the employee last name");
        Assert.assertTrue(homePage.verifyEmployeeLastName(emplLastName), "Last name are not matching");

        ExtentManager.logStep("DB Verification completed");
    }
}
