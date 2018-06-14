package com.tests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.Assertion;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.utilities.ExtentManager;
import com.utilities.Util;

public class Validate extends Util {
	static double value=10.26;
	WebDriver driver;
	
	
	private static ExtentReports extent;
    private static ThreadLocal parentTest = new ThreadLocal();
    private static ThreadLocal test = new ThreadLocal();

	@BeforeSuite
	public void beforeSuite() {
		extent = ExtentManager.createInstance("extent.html");
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("extent.html");
		extent.attachReporter(htmlReporter);
	}
	
   /* @BeforeClass
    public synchronized void beforeClass() {
        ExtentTest parent = extent.createTest(getClass().getName());
        parentTest.set(parent);
    }*/

   
	
	@Parameters({ "browser" })
	@BeforeTest
	public void setUp(String browser){
		ExtentTest parent = extent.createTest(browser);
	     parentTest.set(parent);
		driver=launch(browser, driver);
	}


	@Test(dataProvider="dataSet", dataProviderClass=Util.class)
	public void verifyParallel(String sit, String uat) {
		
		ExtentTest child = ((ExtentTest) parentTest.get()).createNode(sit);
		
        test.set(child);
		SoftAssert assert1=new SoftAssert();
		driver.get(sit);
		
		if (Thread.currentThread().getName().equals("Chrome")) {
			System.out.println("asdlkasdasjldjasld");
			
			if(!sit.equals("test")) {
				assert1.fail("Value is 200");
			}else {
				assert1.assertSame("11.6", "11.6", "Value is 11.6");
			}
		}
		
		driver.get(uat);
		assert1.assertAll();
		
	}
	
	 @AfterMethod
	    public synchronized void afterMethod(ITestResult result) {
	        if (result.getStatus() == ITestResult.FAILURE)
	            ((ExtentTest) test.get()).fail(result.getThrowable());
	        else if (result.getStatus() == ITestResult.SKIP)
	            ((ExtentTest) test.get()).skip(result.getThrowable());
	        else
	            ((ExtentTest) test.get()).pass("Test passed - "+value);

	        extent.flush();
	    }
	
	@AfterTest
	public void tearDown() {
		driver.quit();
	}
}
