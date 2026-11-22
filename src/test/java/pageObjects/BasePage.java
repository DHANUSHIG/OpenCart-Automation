package pageObjects;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import utilities.WaitUtils;

/**
 * BasePage is the abstract parent class for all Page Objects in the framework.
 * It encapsulates the WebDriver instance, initializes PageFactory WebElements,
 * and delegates explicit wait operations to WaitUtils.
 */
public abstract class BasePage {

	protected WebDriver driver;
	protected WaitUtils waitUtils;

	// Constructor initializing the driver, wait utility, and page factory elements
	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.waitUtils = new WaitUtils(driver);
		PageFactory.initElements(driver, this);
	}

	// Reusable wait helper methods delegated to WaitUtils
	public WebElement waitForVisibility(WebElement element) {
		return waitUtils.waitForVisibility(element);
	}

	public WebElement waitForVisibility(WebElement element, Duration timeout) {
		return waitUtils.waitForVisibility(element, timeout);
	}

	public WebElement waitForVisibility(By locator) {
		return waitUtils.waitForVisibility(locator);
	}

	public WebElement waitForClickability(WebElement element) {
		return waitUtils.waitForClickability(element);
	}

	public WebElement waitForClickability(WebElement element, Duration timeout) {
		return waitUtils.waitForClickability(element, timeout);
	}

	public boolean waitForInvisibility(WebElement element) {
		return waitUtils.waitForInvisibility(element);
	}

	public boolean waitForTextToBePresent(WebElement element, String text) {
		return waitUtils.waitForTextToBePresent(element, text);
	}

}
