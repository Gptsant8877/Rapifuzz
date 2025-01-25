package com.RapifuzzPvt.Ltd.Assignment_Test_Case_Layer;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.RapifuzzPvt.Ltd.Assignment.Page_Layer.Rapifuzz;
import com.RapifuzzPvt.Ltd.Assignment.Util_Layer.WebUtil;


public class BaseTest {

        
protected WebUtil  gm;
	  
	
	
	@BeforeClass
	public void beforeClass() { 
		gm=WebUtil.getInstance();
		gm.createExtentReport();


	}

	@BeforeMethod
	public void setUp(Method method) {
		gm.createTestReport(method.getName());
		gm.launchbrowser("chrome");
		gm.openUrl("https://erail.in/");


	}

	@AfterMethod
	public void closeSetUp() {
	gm.tearDown_Quit();

	}

	@AfterClass
	public void afterClass() {
		gm.flushReport();
	}   
        
        
    
}
