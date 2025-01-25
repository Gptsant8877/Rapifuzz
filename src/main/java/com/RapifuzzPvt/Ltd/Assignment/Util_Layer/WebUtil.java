package com.RapifuzzPvt.Ltd.Assignment.Util_Layer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.collect.Table.Cell;
import com.google.common.io.Files;
import com.itextpdf.text.log.SysoCounter;

public class WebUtil {

	public static WebDriver driver;
	 private WebDriverWait wait;
	private static WebUtil gm;
	private static ExtentReports extreport;

	private static ExtentTest extTest;/// null

	public static WebUtil getInstance() { 
		if (gm == null) {
			gm = new WebUtil();
		}
		return gm;
	}

	public void createExtentReport() {
		String dt = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date());
		  

		File folder = new File("Reports");
		if (!folder.exists()) {
			folder.mkdir();
		}
		ExtentSparkReporter extSpark = new ExtentSparkReporter(System.getProperty("user.dir") + "/Reports/" + "Rapifuzz_Reports__" + dt + ".html");
		extreport = new ExtentReports();
		extreport.attachReporter(extSpark);
	}

	public void createTestReport(String testcaseName) {
		if (extreport == null) {
			createExtentReport();

		}
		extTest = extreport.createTest(testcaseName);


	}

	public void flushReport() {
		extreport.flush();
	}

	// ***********************Browser Launch Generic method*******************
	/*
	 * This method will take screenshot of page where will it find exception
	 *
	 * @Param -String imagetName
	 *
	 * @Return- not return 
	 */

	public  String captureScreenshot( String imageName, ITestResult result) {
		DateFormat datef = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss_a");
		String dateTime = datef.format(new Date());
      System.out.print(result.getStatus()+"status to the testcase ");
		// Only capture screenshot if test failed
		if (result.getStatus() == ITestResult.FAILURE) {
			TakesScreenshot tss = (TakesScreenshot) driver;
			File source = tss.getScreenshotAs(OutputType.FILE);

			// Create folder for screenshots if it doesn't exist
			File folder = new File(System.getProperty("user.dir") + "/SnapShots");
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Define the destination file for the screenshot
			File finalDestination = new File(folder + "/" + imageName + "_" + dateTime + ".png");
			String destination = finalDestination.getAbsolutePath();

			try {
				FileUtils.copyFile(source, finalDestination); // Use FileUtils for copying files
				extTest.log(Status.INFO, "Screenshot saved at: " + destination);

			} catch (IOException e) {
				e.printStackTrace();
				extTest.log(Status.INFO, "Error while saving screenshot: " + e.getMessage());
			}

			return destination;
		}

		return null; // Return null if no screenshot was captured
	}

	/*
	 * This method will Launch Browser
	 *
	 * @Param - String browserName
	 *
	 * @Return- no return
	 */

	public void launchbrowser(String browser) {

		// driver.manage().addCookie(null);
		try {
			switch (browser.toLowerCase()) {

			case "chrome":
				driver = new ChromeDriver();
				extTest.log(Status.INFO, "chrome : launched Successfully ");

				break;
			case "firefox":
				driver = new FirefoxDriver();
				extTest.log(Status.INFO, "firefox : launched Successfully ");
				break;
			case "edge":
				driver = new EdgeDriver();
				extTest.log(Status.INFO, "edge : launched Successfully ");
				break;
			default:
				extTest.log(Status.FAIL, "Browser : name did not match ");
			}
		} catch (Exception e) {

			extTest.log(Status.FAIL, "Browser : did not launch  ");
			throw e;
		}
		// it is implicit wait WebElement default run with driver for all element //

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

	}

	// ****************driver generic method******************

	/*
	 * This method will force for sleep and stop execution of code specified
	 * duration
	 *
	 * @Param= int durationForWaitInSecond
	 *
	 *
	 * @return -no return
	 */
	public void threadSleep(int duration,String sleep) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method will wait for specific element until element not be enabled
	 *
	 * @Param= int durationForWaitInSecond
	 *
	 * @Param=WebElement objectForLocating
	 *
	 * @Param=String
	 *
	 * @return -no return
	 */ 
	public void exWaitElementEnabled(int durationOfSecond, WebElement we,String TestCaseName) {
		try {
			WebDriverWait exwaitObj = new WebDriverWait(driver, Duration.ofSeconds(durationOfSecond));
			exwaitObj.until(ExpectedConditions.elementToBeClickable(we));
			extTest.log(Status.PASS, "Successfully match ");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "UNsuccessfully match ");
			throw e;
		}
	}
	/*
	 * This method will wait for specific element until page not be loaded
	 *
	 * @Param= int durationForWaitInSecond
	 *
	 * @Param=WebElement objectForLocating
	 *
	 * @Param=String
	 *
	 * @return -no return
	 */

	public void exWaitElementPresence(int durationOfSecond, By we,String TestCasesName) {
		try {
			WebDriverWait exwaitObj = new WebDriverWait(driver, Duration.ofSeconds(durationOfSecond));
			exwaitObj.until(ExpectedConditions.presenceOfElementLocated(we));
			extTest.log(Status.PASS, "Successfully match ");
		} catch (Exception e) {

			extTest.log(Status.FAIL, "unSuccessfully match ");
			throw e;
		}
	}

	/*
	 * This method will wait for specific element until Element not be visible
	 *
	 * @Param= int durationForWaitInSecond
	 *
	 * @Param=WebElement objectForLocating
	 *
	 * @Param=String
	 *
	 * @return -no return
	 */

	public void exWaitElementVisibility(int durationOfSecond, WebElement we,String TestCasesName) {
		try {
			WebDriverWait exwaitObj = new WebDriverWait(driver, Duration.ofSeconds(durationOfSecond));
			exwaitObj.until(ExpectedConditions.visibilityOf(we));
			extTest.log(Status.PASS, "  Element is visible on page ");
		} catch (Exception e) {

			extTest.log(Status.FAIL, "  Element is not Visible ");
			throw e;
		}
	}
	/*
	 * This method will wait for specific element until Element text not be changed
	 *
	 * @Param= int durationForWaitInSecond
	 *
	 * @Param=WebElement objectForLocating
	 *
	 * @Param=String
	 *
	 * @Param=String textForMatch
	 *
	 * @return -no return
	 */

	public void exWaitElementTextChange(int durationOfSecond, WebElement we, String textForMactch,String TestCasesName) {
		try {
			WebDriverWait exwaitObj = new WebDriverWait(driver, Duration.ofSeconds(durationOfSecond));
			exwaitObj.until(ExpectedConditions.textToBePresentInElementValue(we, textForMactch));
			extTest.log(Status.PASS, "  Element is present on html ");
		} catch (Exception e) {

			extTest.log(Status.FAIL, "  Text is not present  ");
			throw e;




		}
	}
	/* This method will refresh page */

	public void refresh(String testCaseName) {
		try {
			driver.navigate().refresh();
			extTest.log(Status.INFO, "Page refreshed");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to refresh the page");
			throw e;
		}
	}
	/*
	 * This method will Back to previous page
	 *
	 * @return no return
	 */
	public void back(String testCaseName) {
		try {
			driver.navigate().back();
			extTest.log(Status.INFO, "Navigated back to the previous page successfully");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to navigate back");
			throw e;
		}
	}

	public void forward(String testCaseName) {
		try {
			driver.navigate().forward();
			extTest.log(Status.INFO, "Navigated forward to the next page successfully");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to navigate forward");
			throw e;
		}
	}


	/*
	 * This method retrieve the size of Element
	 *
	 * @Param - WebElement object
	 *
	 * @Param - String
	 *
	 * @Return - Array int
	 *
	 */
	public int[] getElementSize(WebElement we) {
		try {
			Dimension dimension = we.getSize();
			int height = dimension.getHeight();
			int width = dimension.getWidth();
			extTest.log(Status.INFO, "Retrieved element size: height = " + height + ", width = " + width);
			return new int[] { height, width };
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve element size");
			throw e;
		}
	}

	public int[] getElementLocation(WebElement we) {
		try {
			Point point = we.getLocation();
			int x = point.getX();
			int y = point.getY();
			extTest.log(Status.INFO, "Retrieved element location: X = " + x + ", Y = " + y);
			return new int[] { x, y };
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve element location");
			throw e;
		}
	}
	public void maximize() {
		try {
			driver.manage().window().maximize();
			extTest.log(Status.INFO, "Window maximized successfully");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to maximize the window");
			throw e;
		}
	}
	public void openUrl(String url) {
		try {
			driver.get(url);
			extTest.log(Status.INFO, url + " opened successfully");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to open URL: " + url);
			throw e;
		}
	}

	public void switchToFrameWebElement(WebElement we) {
		try {
			driver.switchTo().frame(we);
			extTest.log(Status.INFO, "Switched to the specified iframe");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to switch to the specified iframe");
			throw e;
		}
	}

	public void switchToDefaultContent(String testCaseName) {
		try {
			driver.switchTo().defaultContent();
			extTest.log(Status.INFO, "Switched back to the main content from iframe");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to switch back to the main content");
			throw e;
		}
	}

	public void alertAccept(String testCaseName) {
		try {
			driver.switchTo().alert().accept();
			extTest.log(Status.INFO, "Alert accepted successfully");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to accept the alert");
			throw e;
		}
	}

	public void alertDismissed(String testCaseName) {
		try {
			driver.switchTo().alert().dismiss();
			extTest.log(Status.INFO, "Alert dismissed successfully");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to dismiss the alert");
			throw e;
		}
	}

	public void switchToWindowByTitle(String title) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(title)) {
					extTest.log(Status.INFO, "Switched to the window with title: " + title);
					return;
				}
			}
			extTest.log(Status.FAIL, "No window with title: " + title + " found");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to switch to the window with title: " + title);
			throw e;
		}
	}

	public String getCurrentURL(String pageName) {
		try {
			String url = driver.getCurrentUrl();
			extTest.log(Status.INFO, pageName + " - Retrieved URL: " + url);
			return url;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve the current URL for " + pageName);
			throw e;
		}
	}

	public String getCurrentTitle(String testCaseName) {
		try {
			String title = driver.getTitle();
			extTest.log(Status.INFO, "Retrieved page title: " + title);
			return title;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve the page title");
			throw e;
		}
	}

	public String getTitle(String pageName) {
		try {
			String title = driver.getTitle();
			extTest.log(Status.INFO, "Successfully returned the title of the page: " + pageName);
			return title;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve the title of the page: " + pageName);
			throw e;
		}
	}

	public void checkedAllCheckBoxes(List<WebElement> listCheckedBoxes, String elementCollectionName) {
		try {
			for (WebElement weCheckBox : listCheckedBoxes) {
				if (!weCheckBox.isSelected()) {
					weCheckBox.click();
				}
			}
			extTest.log(Status.INFO, "Successfully checked all checkboxes in: " + elementCollectionName);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to check all checkboxes in: " + elementCollectionName);
			throw e;
		}
	}

	public void uncheckedAllCheckBoxes(List<WebElement> listCheckedBoxes, String elementCollectionName) {
		try {
			for (WebElement weCheckBox : listCheckedBoxes) {
				if (weCheckBox.isSelected()) {
					weCheckBox.click();
				}
			}
			extTest.log(Status.INFO, "Successfully unchecked all checkboxes in: " + elementCollectionName);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to uncheck all checkboxes in: " + elementCollectionName);
			throw e;
		}
	}

	public void tearDown_Quit() {
		try {
			threadSleep(2000, "");
			driver.quit();
			extTest.log(Status.INFO, "Successfully closed all browser instances");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to quit browser instances");
			throw e;
		}
	}

	public void closeWindow() {
		try {
			driver.close();
			extTest.log(Status.INFO, "Successfully closed the current browser window");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to close the current browser window");
			throw e;
		}
	}

	public void actionClick(WebElement we,String elementname ) {
		Actions act = new Actions(driver);
		try {
			act.click(we).build().perform();
			extTest.log(Status.INFO, "Successfully performed mouse over and clicked on the element");
		} catch (ElementNotInteractableException e) {
			jsMouseOver(we);
			jsClickMethod(we,"");
			extTest.log(Status.INFO, "Performed fallback JavaScript click");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to perform action click");
			throw e;
		}
	}

	public void actionInputValue(WebElement we, String value) {
		Actions act = new Actions(driver);
		try {
			act.sendKeys(we, value).build().perform();
			extTest.log(Status.INFO, "Successfully entered value into the input field");
		} catch (ElementNotInteractableException e) {
			jsMouseOver(we);
			jsInputValueMethod(we, value);
			extTest.log(Status.INFO, "Performed fallback JavaScript input");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to enter value into the input field");
			throw e;
		}
	}

	public void mouseOver(WebElement we) {
		Actions act = new Actions(driver);
		try {
			act.moveToElement(we).build().perform();
			extTest.log(Status.INFO, "Successfully performed mouse over on the element");
		} catch (ElementNotInteractableException e) {
			jsMouseOver(we);
			extTest.log(Status.INFO, "Performed fallback JavaScript mouse over");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to perform mouse over on the element");
			throw e;
		}
	}

	public void actionDragAndDrop(WebElement we, WebElement target) throws Exception {
		Actions act = new Actions(driver);
		try {
			act.dragAndDrop(we, target).build().perform();
			extTest.log(Status.INFO, "Successfully performed drag and drop");
		} catch (ElementNotInteractableException e) {
			jsDragAndDrop(we, target);
			extTest.log(Status.INFO, "Performed fallback JavaScript drag and drop");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to perform drag and drop");
			throw e;
		}
	}
	public void jsDropdown(WebElement we, String value) {
		try {
			((JavascriptExecutor) driver).executeScript(
					"var select = arguments[0]; for(var i = 0; i < select.options.length; i++){" +
							"if(select.options[i].text == arguments[1]){ select.options[i].selected = true; } }",
							we, value);
			extTest.log(Status.INFO, "Successfully selected text in dropdown through visible text using JavaScript.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to select dropdown option using JavaScript.");
			throw e;
		}
	}

	public void jsInputValueMethod(WebElement we, String value) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value='" + value + "';", we);
			extTest.log(Status.INFO, "Successfully entered value using JavaScript: " + value);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to enter value using JavaScript.");
			throw e;
		}
	}

	public void jsClickMethod(WebElement we, String elementname ) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click();", we);
			extTest.log(Status.INFO, "Successfully clicked on the element using JavaScript.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to click on the element using JavaScript.");
			throw e;
		}
	}

	public void jsMouseOver(WebElement we) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript(
					"if(document.createEvent) { var evObj = document.createEvent('MouseEvents'); evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj); }" +
							"else if(document.createEventObject) { arguments[0].fireEvent('onmouseover'); }",
							we);
			extTest.log(Status.INFO, "Successfully performed mouse over on the element using JavaScript.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to perform mouse over using JavaScript.");
			throw e;
		}
	}

	public void jsDragAndDrop(WebElement source, WebElement destination) throws Exception {
		try {
			Actions act = new Actions(driver);
			act.moveToElement(source)
			.pause(Duration.ofSeconds(1))
			.clickAndHold(source)
			.pause(Duration.ofSeconds(1))
			.moveByOffset(1, 0)
			.moveToElement(destination)
			.moveByOffset(1, 0)
			.pause(Duration.ofSeconds(1))
			.release()
			.perform();
			Thread.sleep(3000);
			extTest.log(Status.INFO, "Successfully performed drag and drop using JavaScript.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to perform drag and drop using JavaScript.");
			throw e;
		}
	}

	public void jsScroll(WebElement we) throws Exception {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", we);
			Thread.sleep(2000);
			extTest.log(Status.INFO, "Successfully scrolled to the element using JavaScript.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to scroll to the element using JavaScript.");
			throw e;
		}
	}



	public void jsDoubleClick(WebElement we) {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].dispatchEvent(new MouseEvent('dblclick', { bubbles: true }));", we);
			extTest.log(Status.INFO, "Successfully performed double click on the element using JavaScript.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to perform double click using JavaScript.");
			throw e;
		}
	}



	public void selectByText(WebElement we, String text) {
		Select selectDD = new Select(we);
		try {
			selectDD.selectByVisibleText(text);
			extTest.log(Status.INFO, "Successfully selected text in the dropdown");
		} catch (ElementNotInteractableException e) {
			jsDropdown(we, text);
			extTest.log(Status.INFO, "Performed fallback JavaScript dropdown selection");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to select dropdown option by visible text");
			throw e;
		}
	}

	public List<String> getAllSelected_Options(WebElement we) {
		Select selectDD = new Select(we);
		List<String> selectedOptions = new ArrayList<>();
		try {
			for (WebElement option : selectDD.getAllSelectedOptions()) {
				selectedOptions.add(option.getText());
			}
			extTest.log(Status.INFO, "Successfully retrieved all selected options from the dropdown");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve all selected options");
			throw e;
		}
		return selectedOptions;
	}



	/*
	 * This method will mouseOver on the element After that Double click on element
	 *
	 * @Param - WebElement object
	 *
	 * @Return- Not return AnyThings
	 *


	/*
	 * this method will select option from dropdown WebElement Partial text
	 *
	 * @Param - WebElement
	 *
	 * @Param - String partialvisibletext
	 *
	 * @Param - Element Name
	 *
	 * @return - no return
	 */
	public void actionDoubleClick(WebElement we) {
		Actions act = new Actions(driver);
		try {
			act.doubleClick(we).build().perform();
			extTest.log(Status.INFO, "Successfully performed double-click on the element.");
		} catch (ElementNotInteractableException e) {
			jsMouseOver(we);
			jsDoubleClick(we);
			extTest.log(Status.INFO, "Performed fallback JavaScript double-click on the element.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while performing double-click on the element.");
			throw e;
		}
	}

	public void selectBy_Text(WebElement we, String textToSelect) {
		Select selectDD = new Select(we);
		try {
			selectDD.selectByVisibleText(textToSelect);
			extTest.log(Status.INFO, "Successfully selected text in dropdown using visible text: " + textToSelect);
		} catch (ElementNotInteractableException e) {
			jsDropdown(we, textToSelect );
			extTest.log(Status.INFO, "Performed fallback JavaScript dropdown selection with visible text: " + textToSelect);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while selecting dropdown option by visible text: " + textToSelect);
			throw e;
		}
	}

	public void selectByIndex(WebElement we, int optionIndex) {
		Select selectDD = new Select(we);
		try {
			selectDD.selectByIndex(optionIndex);
			extTest.log(Status.INFO, "Successfully selected dropdown option by index: " + optionIndex);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while selecting dropdown option by index: " + optionIndex);
			throw e;
		}
	}

	public void selectByValue(WebElement we, String optionValueAttribute) {
		Select selectDD = new Select(we);
		try {
			selectDD.selectByValue(optionValueAttribute);
			extTest.log(Status.INFO, "Successfully selected text in dropdown using attribute value: " + optionValueAttribute);
		} catch (ElementNotInteractableException e) {
			jsDropdown(we, optionValueAttribute );
			extTest.log(Status.INFO, "Performed fallback JavaScript dropdown selection using value: " + optionValueAttribute);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while selecting dropdown option by value: " + optionValueAttribute);
			throw e;
		}
	}


	public void selectByTextContains(WebElement we, String selectText) {
		int indexNumber = -1;
		Select slt = new Select(we);
		List<WebElement> weListOption = null;
		try {
			weListOption = slt.getOptions();
			for (int i = 0; i < weListOption.size(); i++) {
				WebElement weOption = weListOption.get(i);
				String optionText = weOption.getText();
				if (optionText.contains(selectText)) {
					indexNumber = i;
				}
			}
			slt.selectByIndex(indexNumber);
			extTest.log(Status.INFO, "Successfully selected text in dropdown through partial visible text.");
		} catch (StaleElementReferenceException e) {
			extTest.log(Status.INFO, "Encountered stale element, retrying...");
			try {
				weListOption = slt.getOptions();
				for (int i = 0; i < weListOption.size(); i++) {
					WebElement weOption = weListOption.get(i);
					String optionText = weOption.getText();
					if (optionText.contains(selectText)) {
						indexNumber = i;
					}
				}
				slt.selectByIndex(indexNumber);
				extTest.log(Status.INFO, "Successfully selected text in dropdown after retrying.");
			} catch (Exception innerException) {
				extTest.log(Status.FAIL, "Failed to select text in dropdown through partial visible text.");
				throw innerException;
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to select text in dropdown through partial visible text.");
			throw e;
		}
	}

	public void selectAllCheckBoxes(List<WebElement> checkBoxLocator) {
	    try {
	        // Loop through all checkboxes
	        for (int i = 0; i < checkBoxLocator.size(); i++) {	        	
	            WebElement checkbox = checkBoxLocator.get(i);

	            // Check if checkbox is already selected
	            if (!checkbox.isSelected()) {  
	                checkbox.click();
	                if (checkbox.isSelected()) {
	                    extTest.log(Status.PASS, "Checkbox at index " + i + " is successfully selected.");
	                } else {
	                    extTest.log(Status.FAIL, "Checkbox at index " + i + " could not be selected.");
	                }
	            } else {
	                extTest.log(Status.INFO, "Checkbox at index " + i + " is already selected.");
	            }
	        }
	    } catch (Exception e) {
	        String message = e.getMessage();
	        extTest.log(Status.FAIL, "Exception occurred: " + message);
	    }
	}


	public void selectRandomCheckBox(List<WebElement> checkBoxLocator) {
	    try {
	        if (checkBoxLocator.isEmpty()) {
	            extTest.log(Status.FAIL, "No checkboxes found to select.");
	            return;
	        }

	        // Generate a random index within the size of the list
	        Random random = new Random();
	        int randomIndex = random.nextInt(checkBoxLocator.size());

	        // Get the random checkbox element
	        WebElement randomCheckBox = checkBoxLocator.get(randomIndex);

	        // Check if the checkbox is already selected, if not, click it
	        if (!randomCheckBox.isSelected()) {
	            randomCheckBox.click();
	            extTest.log(Status.PASS, "Random checkbox at index " + randomIndex + " is selected.");
	        } else {
	            extTest.log(Status.INFO, "Random checkbox at index " + randomIndex + " was already selected.");
	        }
	    } catch (Exception e) {
	        String message = e.getMessage();
	        extTest.log(Status.FAIL, "Error occurred while selecting a random checkbox: " + message);
	    }
	}
	
	public void selectRandomMultipleCheckBoxes(List<WebElement> checkBoxLocator, int maxSelections) {
	    try {
	        if (checkBoxLocator.isEmpty()) {
	            extTest.log(Status.FAIL, "No checkboxes found to select.");
	            return;
	        }

	        // Limit the number of checkboxes to select based on the list size
	        int totalCheckBoxes = checkBoxLocator.size();
	        int selectionCount = Math.min(maxSelections, totalCheckBoxes);
	        
	        // Use a Random object to randomly select checkboxes
	        Random random = new Random();
	        boolean[] selectedIndices = new boolean[totalCheckBoxes]; // To track already clicked indices

	        for (int i = 0; i < selectionCount; i++) {
	            int randomIndex;

	            // Ensure we select a unique checkbox each time
	            do {
	                randomIndex = random.nextInt(totalCheckBoxes);
	            } while (selectedIndices[randomIndex]);

	            // Mark the index as selected
	            selectedIndices[randomIndex] = true;

	            // Get the random checkbox
	            WebElement randomCheckBox = checkBoxLocator.get(randomIndex);

	            // Click the checkbox if not already selected
	            if (!randomCheckBox.isSelected()) {
	                randomCheckBox.click();
	                extTest.log(Status.PASS, "Checkbox at index " + randomIndex + " is selected.");
	            } else {
	                extTest.log(Status.INFO, "Checkbox at index " + randomIndex + " was already selected.");
	            }
	        }
	    } catch (Exception e) {
	        String message = e.getMessage();
	        extTest.log(Status.FAIL, "Error occurred while selecting multiple random checkboxes: " + message);
	    }
	}

	
	
	
	public List<String> getAllSelectedOptions(WebElement we) {
		List<String> list = new ArrayList<>();
		try {
			Select slt = new Select(we);
			List<WebElement> selectedOptions = slt.getAllSelectedOptions();
			for (WebElement option : selectedOptions) {
				list.add(option.getText());
			}
			extTest.log(Status.INFO, "Successfully retrieved all selected options from dropdown.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve selected options from dropdown.");
			throw e;
		}
		return list;
	}

	public String getDropdownSelectedText(WebElement we) {
		try {
			Select slt = new Select(we);
			String selectedOption = slt.getFirstSelectedOption().getText();
			extTest.log(Status.INFO, "Successfully retrieved the selected text from dropdown: " + selectedOption);
			return selectedOption;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve selected text from dropdown.");
			throw e;
		}
	}

	public List<String> getTextAllOptionsDropdown(WebElement we) {
		List<String> allOptions = new ArrayList<>();
		try {
			Select slt = new Select(we);
			List<WebElement> options = slt.getOptions();
			for (WebElement option : options) {
				allOptions.add(option.getText());
			}
			extTest.log(Status.INFO, "Successfully retrieved all dropdown options.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve dropdown options.");
			throw e;
		}
		return allOptions;
	}

	public int getAllOptionCount(WebElement we) {
		try {
			Select slt = new Select(we);
			int count = slt.getOptions().size();
			extTest.log(Status.INFO, "Successfully retrieved the count of dropdown options: " + count);
			return count;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve dropdown options count.");
			throw e;
		}
	}

	public void jsInputValue_Method(WebElement we, String value) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].value='" + value + "';", we);
			extTest.log(Status.INFO, "Successfully entered value using JavaScript: " + value);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to enter value using JavaScript.");
			throw e;
		}
	}

	public void click(WebElement we, String elementName) {
		try {
			we.click();
			extTest.log(Status.INFO, "Successfully clicked on element: " + elementName);
		} catch (ElementNotInteractableException e) {
			extTest.log(Status.INFO, "Element not interactable, trying JavaScript click...");
			jsClickMethod(we,"");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to click on element: " + elementName);
			throw e;
		}
	}
	public void clickWithEnter(WebElement we, String elementName) {
		try {
			we.click();
			extTest.log(Status.INFO, "Successfully clicked on element: " + elementName);
		} catch (ElementNotInteractableException e) {
			extTest.log(Status.INFO, "Element not interactable, trying ENTER key action...");
			try {
				we.sendKeys(Keys.ENTER);
				extTest.log(Status.INFO, "Successfully triggered ENTER key action on element: " + elementName);
			} catch (Exception innerException) {
				extTest.log(Status.FAIL, "Failed to perform ENTER key action on element: " + elementName);
				throw innerException;
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to interact with element: " + elementName);
			throw e;
		}
	}


	public void inputValue(WebElement we, String value) {
		try {
			we.sendKeys(value);
			if(we.getAttribute("value").equals("admin")) {
				 System.out.println("passed");
				
			}
			else {
				System.out.println();
			}
			extTest.log(Status.INFO, "Successfully entered value: " + value);
		} catch (ElementNotInteractableException e) {
			extTest.log(Status.INFO, "Element not interactable, trying JavaScript input...");
			jsInputValueMethod(we, value);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to enter value in the text box.");
			throw e;
		}
	}

	public void clear(WebElement we) {
		try {
			we.clear();
			extTest.log(Status.INFO, "Successfully cleared the text box.");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to clear the text box.");
			throw e;
		}
	}

	public String getText(WebElement we) {

		String innertext = null;
		try {
			innertext = we.getText();
			extTest.log(Status.INFO, "Successfully Retrieve inner Text - ");

		} catch (StaleElementReferenceException e) {
			innertext = we.getText();
			extTest.log(Status.INFO, "Successfully Value enterd in the  ");

		} catch (Exception e) {
			extTest.log(Status.FAIL, "Due To Exception Inner text not found");
			e.printStackTrace();

		}
		return innertext;

	}

	public String getAttribute(WebElement we, String attributeName) {

		String attributeValue = null;
		try {
			attributeValue = we.getAttribute(attributeName);
			extTest.log(Status.INFO, "Successfully got  Attribute Value - ");

		} catch (Exception e) {
			extTest.log(Status.FAIL, "Due To Exception Attribute Value not found");
			e.printStackTrace();

		}
		return attributeValue;

	}
	/* Verification method */

	public void verifyTextContains(String actualValue, String expectedTextContains) {
		if (actualValue.contains(expectedTextContains)) {
			extTest.log(Status.PASS, actualValue + " is contains value :" + expectedTextContains);
		} else {
			extTest.log(Status.FAIL, actualValue + " is not contains value :" + expectedTextContains);
		}
	}

	/*
	 * this method will verify Enability of element
	 *
	 * @Param - WebElement we
	 *
	 *
	 * @return - not return anything
	 */
	public void verifyEnabled(WebElement we) {
		try {
			boolean status = we.isEnabled();
			if (status) {
				extTest.log(Status.PASS, " is enable for action performing ");
			} else {
				extTest.log(Status.FAIL, "unSuccessfully match ");
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Element is not enable for action performing and Exception occur");
			e.printStackTrace();
			extTest.addScreenCaptureFromPath("");
		}
	}

	public void verifySelected(WebElement we) {
		try {
			boolean status = we.isSelected();
			if (status) {
				extTest.log(Status.PASS, "Check box is selected ");
			} else {
				extTest.log(Status.FAIL, "Check box is not selected   ");
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Check box is not selectand Exception occur");
			e.printStackTrace();
		}
	}

	/*
	 * this method will verify that element is displayed on UI
	 *
	 * @Param - WebElement we
	 *
	 *
	 * @return - not return anything
	 */
	public void verifyDisplayed(WebElement we) {
		try {
			boolean status = we.isDisplayed();
			if (status) {
				extTest.log(Status.PASS, "Element is Displayed on UI");
			} else {
				extTest.log(Status.FAIL, "Element is not  Displayed on UI ");
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Element is not  Displayed on UI and exception occur ");
			e.printStackTrace();
		}
	}

	public void verifyAttributeValue(WebElement we, String attributeName, String expectedValue) {
		try {
			String actualvalue = we.getAttribute(attributeName);
			if (expectedValue.equals(actualvalue)) {
				extTest.log(Status.PASS, "Successfully match ");
			} else {
				extTest.log(Status.FAIL, "Did not match");
			}

		} catch (Exception e) {
			extTest.log(Status.FAIL, "Did not match attribute value and Exception occur");
			e.printStackTrace();
		}
	}

	/*
	 * this method will verify two string
	 *
	 * @Param - String actual
	 *
	 * @Param - String expected
	 *
	 * @return - not return anything
	 */

	public void verifyText(String actual, String expected) {
		try {
			if (actual.equals(expected)) {
				extTest.log(Status.PASS, "Actual value [ " + actual + " ] and Expected value matched [ " + expected + " ]");
			} else {
				extTest.log(Status.FAIL, "Actual value [ " + actual + " ] and Expected value did not match [ " + expected + " ]");
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while verifying text.");
			throw e;
		}
	}

	public void verifyInnerText(WebElement we, String expected) {
		try {
			String actual = getText(we);
			if (actual.equals(expected)) {
				extTest.log(Status.PASS, "Actual InnerText [ " + actual + " ] and Expected InnerText matched [ " + expected + " ]");
			} else {
				extTest.log(Status.FAIL, "Actual InnerText [ " + actual + " ] and Expected InnerText did not match [ " + expected + " ]");
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while verifying inner text.");
			throw e;
		}
	}

	public void verifyTitle(String expectedTitle, String pageName) {
		try {
			String actualTitle = driver.getTitle();
			if (actualTitle.equals(expectedTitle)) {
				extTest.log(Status.PASS, "Actual title [ " + actualTitle + " ] and Expected title matched [ " + expectedTitle + " ]");
			} else {
				extTest.log(Status.FAIL, "Actual title [ " + actualTitle + " ] and Expected title did not match [ " + expectedTitle + " ]");
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while verifying the title.");
			throw e;
		}
	}

	public void verifyURL(String expectedURL, String pageName) {
		try {
			String actualURL = driver.getCurrentUrl();
			if (actualURL.equals(expectedURL)) {
				extTest.log(Status.PASS, "Actual URL [ " + actualURL + " ] and Expected URL matched [ " + expectedURL + " ]");
			} else {
				extTest.log(Status.FAIL, "Actual URL [ " + actualURL + " ] and Expected URL did not match [ " + expectedURL + " ]");
			}
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while verifying the URL.");
			throw e;
		}
	}

	public int getColumnNumberWebElementColumnName(String xpath, String columnName) {
		try {
			int columnNumber = -1;
			List<WebElement> listColumnName = driver.findElements(By.xpath(xpath));
			for (int i = 0; i < listColumnName.size(); i++) {
				String colName = listColumnName.get(i).getText();
				if (colName.equalsIgnoreCase(columnName)) {
					columnNumber = i;
					break;
				}
			}
			extTest.log(Status.INFO, "Successfully retrieved column number for column: " + columnName);
			return columnNumber;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while retrieving column number for column: " + columnName);
			throw e;
		}
	}

	public String getTableDataWebElementUniqueData(String tableXpath, String uniqueData, String uniqueDataColumnName, 
			String requiredDataColumnName) {
		try {
			int rowNumber = 0;
			List<WebElement> listRows = driver.findElements(By.xpath(tableXpath + "//tr"));
			int cn1 = getColumnNumberWebElementColumnName(tableXpath, uniqueDataColumnName);
			int cn2 = getColumnNumberWebElementColumnName(tableXpath, requiredDataColumnName);

			for (int i = 0; i < listRows.size(); i++) {
				String text = driver.findElement(By.xpath(tableXpath + "//tr[" + (i + 1) + "]//td[" + cn1 + "]")).getText();
				if (text.equalsIgnoreCase(uniqueData)) {
					rowNumber = i;
					break;
				}
			}
			String requiredText = driver.findElement(By.xpath(tableXpath + "//tr[" + rowNumber + "]//td[" + cn2 + "]")).getText();
			extTest.log(Status.INFO, "Successfully retrieved data from table.");
			return requiredText;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception occurred while retrieving data from table.");
			throw e;
		}
	}

	public void InputValue_PressEnterKeys(WebElement we, String value) {
		try {
			clear(we);
			we.sendKeys(value);
			we.sendKeys(Keys.ENTER);
			extTest.log(Status.INFO, "Successfully entered value and pressed Enter.");
		} catch (ElementNotInteractableException e) {
			jsInputValueMethod(we, value);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to enter value and press Enter.");
			throw e;
		}
	}
	public void jsClick_Method(WebElement we) {
		try {
			// JavaScript Executor for clicking
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", we);
			extTest.pass("Successfully clicked on the element using JavaScript for test case: " );
		} catch (Exception e) {
			extTest.fail("Exception occurred while clicking the element for test case: " );
			try {
				// Capture screenshot and log it to ExtentReports
			} catch (Exception screenshotException) {
				extTest.fail("Failed to capture screenshot for test case: " );
			}
		}
	}

	public void pressTab_click(WebElement we,String elementName) {
		try {
			we.sendKeys(Keys.TAB);
			we.click();
			extTest.log(Status.INFO, "Successfully pressed Tab and clicked on the element.");
		} catch (ElementNotInteractableException e) {
			jsClickMethod(we,elementName);
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to press Tab and click on the element.");
			throw e;
		}
	}

	public int getTableRowCount(List<WebElement> listCheckedBoxes, String elementCollectionName) {
		try {
			int rowCount = listCheckedBoxes.size();
			extTest.log(Status.INFO, "Successfully retrieved row count for: " + elementCollectionName);
			return rowCount;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve row count for: " + elementCollectionName);
			throw e;
		}
	}

	public List<String> getColumnDataWebElementColumnNumber(String tableName, String xpath, int columnNumber) {
		try {
			List<WebElement> columnList = driver.findElements(By.xpath(xpath));
			List<String> columnNameList = new ArrayList<>();
			for (WebElement column : columnList) {
				columnNameList.add(column.getText());
			}
			extTest.log(Status.INFO, "Successfully retrieved column data for column number: " + columnNumber);
			return columnNameList;
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Failed to retrieve column data for column number: " + columnNumber);
			throw e;
		}
	}

	////gmail.code
	public void loginToGmail(WebElement we,String email, String password) {
		try {
			extTest.log(Status.INFO, "Starting Gmail login test case: " );

			// Navigate to Gmail   
			driver.get("https://mail.google.com/");
			extTest.log(Status.INFO, "Navigated to Gmail.");

			// Enter Email

			FluentWait<WebDriver> wait = null;
			WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(we));
			InputValue_PressEnterKeys(emailField, email);

			// Wait for Password Field and Enter Password
			WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(we));
			InputValue_PressEnterKeys(passwordField, password);

			// Check if the inbox is displayed
			wait.until(ExpectedConditions.titleContains("Inbox"));
			extTest.log(Status.PASS, "Successfully logged into Gmail.");
		} catch (ElementNotInteractableException e) {
			extTest.log(Status.FAIL, "Element not interactable during Gmail login: " + e.getMessage());
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Exception during Gmail login: " + e.getMessage());
			throw e; // Re-throwing the exception for debugging or reporting
		}
	}

	/**
	 * Reads data from an Excel file and returns it as a 2D List of Strings.
	 *
	 * @param filePath the path to the Excel file
	 * @param sheetName the name of the sheet to read
	 * @return a List of rows, where each row is a List of cell values
	 */



	public List<List<String>> readExcelData(String filePath, String sheetName,String rowdata) {
		List<List<String>> data = new ArrayList<>();

		try (FileInputStream fileInputStream = new FileInputStream(new File(filePath));
				Workbook workbook = new XSSFWorkbook(fileInputStream)) {

			// Get the sheet by name
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Sheet with name '" + sheetName + "' not found in the Excel file.");
			}

			// Iterate through rows
			for (Row row : sheet) {
				List<String> rowData = new ArrayList<>();
				for (org.apache.poi.ss.usermodel.Cell cell : row) {
					rowData.add(rowdata); // Handle cell values
				}
				data.add(rowData);
			}

			// Log success to Extent Reports
			extTest.log(Status.PASS, "Successfully read data from sheet: " + sheetName);

		} catch (ElementNotInteractableException e) {
			extTest.log(Status.FAIL, "Element not interactable: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			extTest.log(Status.FAIL, "Sheet not found: " + e.getMessage());
		} catch (IOException e) {
			extTest.log(Status.FAIL, "Error reading Excel file: " + e.getMessage());
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Unexpected exception: " + e.getMessage());
			throw e; // Re-thr owing for further debugging
		}

		return data;
	}
	public void switchToWindowHandle(String expectedTitle) {
	    try {
	        // Get the current window handle
	        String currentWindow = driver.getWindowHandle();
	        
	        // Get all window handles
	        Set<String> allWindows = driver.getWindowHandles();
	        
	        // Iterate through the window handles
	        for (String window : allWindows) {
	            driver.switchTo().window(window);
	            
	            // Check if the window title matches the expected title
	            if (driver.getTitle().equalsIgnoreCase(expectedTitle)) {
	                extTest.log(Status.INFO, "Successfully switched to window with title: " + expectedTitle);
	                return;
	            }
	        }
	        
	        // Switch back to the original window if no match is found
	        driver.switchTo().window(currentWindow);
	        extTest.log(Status.FAIL, "Window with title '" + expectedTitle + "' not found. Switched back to original window.");
	    } catch (Exception e) {
	        extTest.log(Status.FAIL, "Exception occurred while switching to window with title: " + expectedTitle);
	        throw e;
	    }
	}

	public void switchToWindowHandles(String parentWindowHandle) {
	    try {
	        // Get all window handles
	        Set<String> allWindows = driver.getWindowHandles();
	        
	        // Iterate through the window handles
	        for (String window : allWindows) {
	            if (!window.equals(parentWindowHandle)) {
	                driver.switchTo().window(window);
	                extTest.log(Status.INFO, "Successfully switched to new window: " + driver.getTitle());
	                return;
	            }
	        }
	        
	        extTest.log(Status.FAIL, "No new window found to switch to.");
	    } catch (Exception e) {
	        extTest.log(Status.FAIL, "Exception occurred while switching to a new window.");
	        throw e;
	    }
	}


	public void clickAndReturn(List<WebElement> elements){
	    try {
	        for (WebElement element : elements) {
	            try {
	                // Log the element being clicked
	                String elementText = element.getText();
	                extTest.log(Status.INFO, "Clicking on: " + elementText);
	                System.out.println("Clicking on: " + elementText);

	                // Perform the click
	                click(element,"");

	                // Optional delay for navigation to complete

	                // Navigate back
	               driver.navigate().back();;

	                // Optional delay after navigating back
	               threadSleep(2000, elementText);

	                // Log success
	                extTest.log(Status.PASS, "Successfully clicked and navigated back for element: " + elementText);
	            } catch (Exception e) {
	                // Log failure for a specific element
	                extTest.log(Status.FAIL, "Failed to click or navigate for element: " + element.getText() + ". Exception: " + e.getMessage());
	            }
	        }
	    } catch (Exception e) {
	        // Log failure for the entire process
	        extTest.log(Status.FAIL, "An error occurred during the clickAndReturn process: " + e.getMessage());
	        throw e;
	    }
	    
	    
	}
	
	
	public String insertDataAndSelectFourthStation(WebElement dropdownElement, int selectPosition) {
	    try {
	        // Wait for the dropdown to populate and fetch the list of options
	        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElements(dropdownElement.findElements(By.linkText("hi Azadpur"))));

	        // Validate if the dropdown has enough elements
	        if (options.size() > selectPosition) {
	            WebElement selectedOption = options.get(selectPosition); // Select the element by the provided position
	            String stationName = selectedOption.getText();
	            selectedOption.click(); // Click the selected option
	            extTest.log(Status.PASS, "Selected Station: " + stationName);
	            return stationName; // Return the station name
	        } else {
	            throw new RuntimeException("Insufficient stations available in the dropdown.");
	        }
	    } catch (Exception e) {
	        // Log the exception and return null to indicate failure
	        extTest.log(Status.FAIL, "Error occurred while selecting the station at position " + selectPosition + ": " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
	}

	public static List<String> readExcel(String filePath, String sheetName) {
        List<String> data = new ArrayList<>();
        FileInputStream fis = null;
        Workbook workbook = null;
        try {
            fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            for (Row row : sheet) {
                data.add(row.getCell(0).getStringCellValue());
            }
            extTest.log(Status.PASS, "Successfully read data from Excel file: " + filePath);
        } catch (Exception e) {
            extTest.log(Status.FAIL, "Failed to read Excel file: " + filePath + ". Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) workbook.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                extTest.log(Status.FAIL, "Failed to close resources after reading Excel file. Error: " + e.getMessage());
            }
        }
        return data; 
    }

    public static void writeExcel(String filePath, String sheetName, List<String> data, ExtentTest extTest) {
        FileOutputStream fos = null;
        Workbook workbook = null;
        try {
            fos = new FileOutputStream(filePath);
            workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(sheetName);

            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(data.get(i));
            }
            workbook.write(fos);
            extTest.log(Status.PASS, "Successfully wrote data to Excel file: " + filePath);
        } catch (Exception e) {
            extTest.log(Status.FAIL, "Failed to write to Excel file: " + filePath + ". Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) workbook.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                extTest.log(Status.FAIL, "Failed to close resources after writing Excel file. Error: " + e.getMessage());
            }
        }
    }
    
    public void getdata(String pathoffile,String SheetName123,String cellname) throws IOException {
		FileInputStream f=null;
		
		///map  has created to keep data in key and value pair
		
		HashMap<String,String> datamap=   new  HashMap<String , String>();
		
		
		      ////it is using fileInputStream to read data from this file 
			f=  new FileInputStream(pathoffile);
			
			
			 // it has used to reach on work book of excel sheet
			XSSFWorkbook workbook=    new XSSFWorkbook(f);
			
			 /// with help of sheet we will reach on the any specific Sheet
			XSSFSheet sobj=        workbook.getSheet(SheetName123);///pass the sheetName
			
			int datarowNumber=0;//it will declare for the keep value of i so that we can use out this block
			
			for(int i=0;i<=sobj.getLastRowNum();i++){// it will be execute from 0 to lastRow
				Row row= sobj.getRow(i);/// it will give the row depend on i 's value 
				
				// we want to access any specific   data id  that why we will create this to compare your Expected data id 
				//who will mentioned o excel sheet 
				// we are using this method due to blank cell  if we  will not use this  then we will get null pointer exception
				Cell cell= (Cell) row.getCell(0,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				String data=    ((org.apache.poi.ss.usermodel.Cell) cell).getStringCellValue();
				if(data.equalsIgnoreCase("Station 1")) {
					datarowNumber=i; 
				}
			}
			/// it will execute based  on the what is row number of that data id 
			Row datarow=    sobj.getRow(datarowNumber);
			 // will be used for make the key to keep on map  so we are using 0 mean to say that will be first row
			Row firstrow=    sobj.getRow(0);
                ///we have  started this loop form 1 because we want  to skip data id cell
			for(int i=1;i<=firstrow.getLastCellNum()-1;i++) {
				  /// Depend  on iteration of I it will be return cell (key) from first row
				Cell cellKey=	(Cell) firstrow.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				 
				/// Depend  on iteration of I it will be return cell (data) from first row
				Cell   cellData = (Cell) datarow.getCell(i,MissingCellPolicy.CREATE_NULL_AS_BLANK);
				//  from  the key cell we will get the keyName in Sting type 
				String  columnName=      ((org.apache.poi.ss.usermodel.Cell) cellKey) .getStringCellValue();
				
				//  from  the data cell we will get the DataName in Sting type 
				String dataValue=     ((org.apache.poi.ss.usermodel.Cell) cellData).getStringCellValue();
				
				//hear we will put the data in map key and value pair 
				datamap.put(columnName, dataValue);
				 System.out.println(datamap.get(cellname));
			}


	}
    
    
    
    public  void getDropdownData( String dropdownId, String outputFilePath) throws IOException {
        // Locate the drop-down
        WebElement dropdown = driver.findElement(By.id(dropdownId));
        Select select = new Select(dropdown);

        // Get all options
        List<WebElement> options = select.getOptions();
        List<String> dropdownData = new ArrayList<>();
        for (WebElement option : options) {
            dropdownData.add(option.getText());
        }

        // Write to Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Actual Stations");
        int rowIndex = 0;
        for (String data : dropdownData) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(data);
        }

        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            workbook.write(fos);
        }
        System.out.println("Dropdown data written to: " + outputFilePath);
    }
    
    

    public  boolean compareExcelFiles1(String file1Path, String file2Path) throws IOException {
        List<String> file1Data = readExcelFile1(file1Path);
        List<String> file2Data = readExcelFile1(file2Path);
        extTest.log(Status.PASS, "pass  resources after finding Excel file. : " +file1Path  +  file2Path);
        // Compare lists
        return file1Data.equals(file2Data);
    }

    public  List<String> readExcelFile1(String filePath) throws IOException {
        List<String> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                org.apache.poi.ss.usermodel.Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                data.add(cell.toString());
            }
        }
        return data;
    }
    
    public  void readExcelFile(String filePath) {
        FileInputStream fileInputStream = null;

        try {
            File file = new File(filePath);
            
            // Check if the file exists and is readable
            if (!file.exists()) {
                throw new IOException("File not found: " + filePath);
            }
            if (!file.canRead()) {
                throw new IOException("File is not readable: " + filePath);
            }

            // Open the file for reading
            fileInputStream = new FileInputStream(file);
            extTest.log(Status.PASS, "Excel file loaded successfully.");


            // Add logic to read the Excel file using Apache POI or similar libraries

        } catch (IOException e) {
            extTest.log(Status.FAIL, "Error reading Excel file: " + e.getMessage());

            e.printStackTrace(); // Log the stack trace for debugging
        } finally {
            // Ensure file input stream is closed
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    extTest.log(Status.FAIL, "Error closing file: " + e.getMessage());

                }
            }
        }
    }
    

	public void getRondomDate(List<WebElement> elements, int calendarNumber,List<WebElement> elements2,String expectedDate) {
		          for(int  i=0; i<=elements.size()-1;i++) {
		        	        WebElement  calendar =  elements.get(calendarNumber);
		        	        if(i==calendarNumber) {
		        	             break;
		        	        }
		        	  
		          }
		           
		          for(int i=0;i<=elements2.size()-1;i++) {
		        	             WebElement e=    elements2.get(i);
		        	    String   date=        e.getText();
		               if(date.equalsIgnoreCase(expectedDate)) {
		            	      e.click();
		            	   
		               }
		          
		             
		          
		          }
		

	}}











