package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class DummyClass extends BaseClass {

    @Test
    public void dummyTest(){
        //ExtentManager.startTest("DummyTest1 Test"); --This has been implemented in TestListener
        String title = getDriver().getTitle();
        ExtentManager.logStep("Verifying the title");
        assert title.equals("OrangeHRM"):"Test Failed - Title is not Matching";

        System.out.println("Test Passed - Title is Matching");
        //ExtentManager.logStep("This case is skipped");
        //throw new SkipException("Skipping the test as part of Testing");
    }


}
