package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import org.testng.annotations.Test;

public class DummyClass extends BaseClass {

    @Test
    public void dummyTest(){
        String title = driver.getTitle();
        assert title.equals("OrangeHRM"):"Test Failed - Title is not Matching";

        System.out.println("Test Passed - Title is Matching");
    }


}
