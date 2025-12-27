package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.annotations.Test;

public class DummyClass2 extends BaseClass {

    @Test
    public void dummyTest(){
        ExtentManager.startTest("DummyTest2 Test");
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verifying the title");
        assert title.equals("OrangeHRM"):"Test Failed - Title is not Matching";

        System.out.println("Test Passed - Title is Matching");
        ExtentManager.logStep("Validation Successful");
    }


}
