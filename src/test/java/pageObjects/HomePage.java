package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

	public HomePage(WebDriver driver) {
		super(driver);
		// constructor invoking the driver and pagefactory form the base page
	}

	@FindBy(xpath = "//a[@title=\"My Account\"]")
	WebElement lnkMyaccount;

	@FindBy(xpath = "//a[normalize-space()='Register']")
	WebElement lnkRegister;

	@FindBy(xpath = "//li//a[normalize-space()='Login']")
	WebElement lnkLogin;
	
	@FindBy(xpath = "//div[@class='list-group']//a[text()='Logout']")
	WebElement lnkLogout;

	public void clickMyAccount() {
		waitForClickability(lnkMyaccount).click();
	}

	public void clickRegister() {
		waitForClickability(lnkRegister).click();
	}

	public void clickLogin() {
		waitForClickability(lnkLogin).click();
	}
	
	public void clickLogout() {
		waitForClickability(lnkLogout).click();
	}

	// Backward-compatible alias
	public void clcikLogout() {
		clickLogout();
	}
}
