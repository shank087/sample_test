package com.tests;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.utilities.ExtentManager;
import com.utilities.Util;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;


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
		driver.get("https://www.wellsfargo.com/");
		
		//Actions actions=new Actions(driver);
		
		List<WebElement> elements=driver.findElements(By.cssSelector("img.deferred"));
		for (WebElement e : elements) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e);
		}
		
		
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
		
		
		if(Thread.currentThread().getName().contains("Chrome")) {
			Shutterbug.shootPage(driver, ScrollStrategy.BOTH_DIRECTIONS).withName("test").save("C:\\temp\\");
			/*Screenshot screenshot = new AShot()
					  .shootingStrategy(ShootingStrategies.viewportPasting(100))
					  .takeScreenshot(driver);
			try {
				ImageIO.write(screenshot.getImage(), "PNG", new   File("c:\\temp\\details.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
		else {
			Shutterbug.shootPage(driver, ScrollStrategy.BOTH_DIRECTIONS).withName("test").save("C:\\temp\\temp2\\");
			/*Screenshot screenshot = new AShot()
					  .shootingStrategy(ShootingStrategies.viewportPasting(100))
					  .takeScreenshot(driver);*/
			/*try {
				ImageIO.write(screenshot.getImage(), "PNG", new   File("C:\\temp\\temp2\\details.png"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
		
		
		//actions.moveByOffset(0, 160).build().perform();
		
		/*if (Thread.currentThread().getName().equals("Chrome")) {
			System.out.println("asdlkasdasjldjasld");
			
			if(!sit.equals("test")) {
				assert1.fail("Value is 200");
			}else {
				assert1.assertSame("11.6", "11.6", "Value is 11.6");
			}
		}*/
		
		//driver.get(uat);
		//assert1.assertAll();
		
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
		//driver.quit();
	}
}
