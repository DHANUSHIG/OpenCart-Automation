package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AccountRegistrationPage extends BasePage {

	public AccountRegistrationPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(id = "input-firstname")
	private WebElement txtFirstname;

	@FindBy(id = "input-lastname")
	private WebElement txtLastname;

	@FindBy(id = "input-email")
	private WebElement txtEmail;

	@FindBy(id = "input-telephone")
	private WebElement txtTelephone;

	@FindBy(id = "input-password")
	private WebElement txtPassword;

	@FindBy(id = "input-confirm")
	private WebElement txtConfirmPassword;

	@FindBy(xpath = "//input[@name='newsletter' and @value='0'] | //input[@value='0']")
	private WebElement chkdSubscriber;

	@FindBy(xpath = "//input[@name='agree'] | //input[@type='checkbox']")
	private WebElement btnPrivacyPolicy;

	@FindBy(xpath = "//input[@value='Continue' or @type='submit']")
	private WebElement btnContinue;

	@FindBy(xpath = "//div//h1[normalize-space()='Your Account Has Been Created!']")
	private WebElement msgconfirmation;

	@FindBy(css = "div.alert-danger")
	private WebElement alertWarning;

	@FindBy(xpath = "//input[@id='input-firstname']/following-sibling::div[contains(@class,'text-danger')]")
	private WebElement msgFirstNameError;

	@FindBy(xpath = "//input[@id='input-lastname']/following-sibling::div[contains(@class,'text-danger')]")
	private WebElement msgLastNameError;

	@FindBy(xpath = "//input[@id='input-email']/following-sibling::div[contains(@class,'text-danger')]")
	private WebElement msgEmailError;

	@FindBy(xpath = "//input[@id='input-telephone']/following-sibling::div[contains(@class,'text-danger')]")
	private WebElement msgTelephoneError;

	@FindBy(xpath = "//input[@id='input-password']/following-sibling::div[contains(@class,'text-danger')]")
	private WebElement msgPasswordError;

	@FindBy(xpath = "//input[@id='input-confirm']/following-sibling::div[contains(@class,'text-danger')]")
	private WebElement msgConfirmPasswordError;

	public void setFname(String fname) {
		waitForVisibility(txtFirstname).sendKeys(fname);
	}

	public void setLname(String lname) {
		waitForVisibility(txtLastname).sendKeys(lname);
	}

	public void setEmail(String email) {
		waitForVisibility(txtEmail).sendKeys(email);
	}

	public void setTelephone(String phone) {
		waitForVisibility(txtTelephone).sendKeys(phone);
	}

	public void setPassword(String password) {
		waitForVisibility(txtPassword).sendKeys(password);
	}

	public void setConfirmPassword(String confirmPassword) {
		waitForVisibility(txtConfirmPassword).sendKeys(confirmPassword);
	}

	public void setSubscriber() {
		waitForClickability(chkdSubscriber).click();
	}

	public void setPrivacyPolicy() {
		waitForClickability(btnPrivacyPolicy).click();
	}

	public void clickContinue() {
		waitForClickability(btnContinue).click();
	}

	public String getConfirmationmsg() {
		try {
			return (waitForVisibility(msgconfirmation).getText());
		} catch (Exception e) {
			return "";
		}
	}

	public String getWarningMessage() {
		try {
			return waitForVisibility(alertWarning).getText();
		} catch (Exception e) {
			return "";
		}
	}

	public String getFirstNameError() {
		try {
			return waitForVisibility(msgFirstNameError).getText();
		} catch (Exception e) {
			return "";
		}
	}

	public String getLastNameError() {
		try {
			return waitForVisibility(msgLastNameError).getText();
		} catch (Exception e) {
			return "";
		}
	}

	public String getEmailError() {
		try {
			return waitForVisibility(msgEmailError).getText();
		} catch (Exception e) {
			return "";
		}
	}

	public String getTelephoneError() {
		try {
			return waitForVisibility(msgTelephoneError).getText();
		} catch (Exception e) {
			return "";
		}
	}

	public String getPasswordError() {
		try {
			return waitForVisibility(msgPasswordError).getText();
		} catch (Exception e) {
			return "";
		}
	}

	public String getConfirmPasswordError() {
		try {
			return waitForVisibility(msgConfirmPasswordError).getText();
		} catch (Exception e) {
			return "";
		}
	}

}
