package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	@FindBy(id = "input-email")
	private WebElement txtEmail;
	
	@FindBy(id = "input-password")
	private WebElement txtPassword;
	
	@FindBy(css = "input[value='Login'], input.btn.btn-primary")
	private WebElement btnLogin;

	@FindBy(css = "div.alert-danger")
	private WebElement alertWarning;
	
	public void setEmail(String email) {
		waitForVisibility(txtEmail).clear();
		txtEmail.sendKeys(email);
	}
	
	public void setPassword(String password) {
		waitForVisibility(txtPassword).clear();
		txtPassword.sendKeys(password);
	}

	public void clickLogin() {
		waitForClickability(btnLogin).click();
	}

	public String getWarningMessage() {
		try {
			return waitForVisibility(alertWarning).getText();
		} catch (Exception e) {
			return "";
		}
	}
}
