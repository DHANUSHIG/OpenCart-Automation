package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object representing the OpenCart 6-Step Checkout process (index.php?route=checkout/checkout).
 */
public class CheckoutPage extends BasePage {

	public CheckoutPage(WebDriver driver) {
		super(driver);
	}

	// ================= Step 1: Checkout Options =================
	@FindBy(xpath = "//input[@value='guest']")
	private WebElement rdoGuestCheckout;

	@FindBy(xpath = "//input[@value='register']")
	private WebElement rdoRegisterAccount;

	@FindBy(id = "button-account")
	private WebElement btnStep1Continue;

	// ================= Step 2: Billing / Guest Details =================
	@FindBy(id = "input-payment-firstname")
	private WebElement txtBillingFirstName;

	@FindBy(id = "input-payment-lastname")
	private WebElement txtBillingLastName;

	@FindBy(id = "input-payment-email")
	private WebElement txtBillingEmail;

	@FindBy(id = "input-payment-telephone")
	private WebElement txtBillingTelephone;

	@FindBy(id = "input-payment-address-1")
	private WebElement txtBillingAddress1;

	@FindBy(id = "input-payment-city")
	private WebElement txtBillingCity;

	@FindBy(id = "input-payment-postcode")
	private WebElement txtBillingPostcode;

	@FindBy(id = "input-payment-country")
	private WebElement drpBillingCountry;

	@FindBy(id = "input-payment-zone")
	private WebElement drpBillingZone;

	@FindBy(id = "button-guest")
	private WebElement btnStep2GuestContinue;

	@FindBy(id = "button-payment-address")
	private WebElement btnStep2RegisteredContinue;

	// ================= Step 4: Delivery Method =================
	@FindBy(id = "button-shipping-method")
	private WebElement btnStep4ShippingMethodContinue;

	// ================= Step 5: Payment Method =================
	@FindBy(xpath = "//input[@name='agree']")
	private WebElement chkTermsAndConditions;

	@FindBy(id = "button-payment-method")
	private WebElement btnStep5PaymentMethodContinue;

	// ================= Step 6: Confirm Order =================
	@FindBy(id = "button-confirm")
	private WebElement btnConfirmOrder;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tbody//tr[1]/td[1]//a")
	private WebElement txtConfirmProductName;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tbody//tr[1]/td[3]")
	private WebElement txtConfirmProductQuantity;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tbody//tr[1]/td[5]")
	private WebElement txtConfirmProductTotal;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tfoot//tr[td[contains(.,'Flat Shipping')]]/td[last()]")
	private WebElement txtConfirmShippingRate;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tfoot//tr[last()]/td[last()]")
	private WebElement txtConfirmGrandTotal;

	// ================= Order Placed Confirmation =================
	@FindBy(xpath = "//div[@id='content']//h1[text()='Your order has been placed!']")
	private WebElement msgOrderPlaced;

	// ================= Action Methods =================

	public void selectGuestCheckout() {
		waitForClickability(rdoGuestCheckout).click();
	}

	public void clickStep1Continue() {
		waitForClickability(btnStep1Continue).click();
	}

	public void setBillingDetails(String fname, String lname, String email, String phone,
			String address, String city, String postcode, String country, String state) {
		waitForVisibility(txtBillingFirstName).sendKeys(fname);
		txtBillingLastName.sendKeys(lname);
		txtBillingEmail.sendKeys(email);
		txtBillingTelephone.sendKeys(phone);
		txtBillingAddress1.sendKeys(address);
		txtBillingCity.sendKeys(city);
		txtBillingPostcode.sendKeys(postcode);

		Select countrySelect = new Select(waitForVisibility(drpBillingCountry));
		countrySelect.selectByVisibleText(country);

		// Wait for AJAX dynamic state options to populate
		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.textToBePresentInElement(drpBillingZone, state));
		Select zoneSelect = new Select(waitForVisibility(drpBillingZone));
		zoneSelect.selectByVisibleText(state);
	}

	public void clickStep2GuestContinue() {
		waitForClickability(btnStep2GuestContinue).click();
	}

	public void clickStep2RegisteredContinue() {
		waitForClickability(btnStep2RegisteredContinue).click();
	}

	public void clickStep4ShippingMethodContinue() {
		waitForVisibility(By.id("collapse-shipping-method"));
		waitForClickability(btnStep4ShippingMethodContinue).click();
	}

	public void checkTermsAndConditions() {
		waitForVisibility(By.id("collapse-payment-method"));
		waitForClickability(chkTermsAndConditions).click();
	}

	public void clickStep5PaymentMethodContinue() {
		waitForClickability(btnStep5PaymentMethodContinue).click();
		waitForVisibility(By.id("collapse-checkout-confirm"));
	}

	public String getConfirmProductName() {
		return waitForVisibility(txtConfirmProductName).getText();
	}

	public String getConfirmProductQuantity() {
		return waitForVisibility(txtConfirmProductQuantity).getText();
	}

	public String getConfirmProductTotal() {
		return waitForVisibility(txtConfirmProductTotal).getText();
	}

	public String getConfirmShippingRate() {
		return waitForVisibility(txtConfirmShippingRate).getText();
	}

	public String getConfirmGrandTotal() {
		return waitForVisibility(txtConfirmGrandTotal).getText();
	}

	public double parsePrice(String priceStr) {
		return Double.parseDouble(priceStr.replace("$", "").replace(",", "").trim());
	}

	public double getConfirmProductTotalAsDouble() {
		return parsePrice(getConfirmProductTotal());
	}

	public double getConfirmShippingRateAsDouble() {
		return parsePrice(getConfirmShippingRate());
	}

	public double getConfirmGrandTotalAsDouble() {
		return parsePrice(getConfirmGrandTotal());
	}

	public void clickConfirmOrder() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement btn = waitForClickability(btnConfirmOrder);
		js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
		js.executeScript("arguments[0].click();", btn);
	}

	public boolean isOrderPlaced() {
		try {
			return waitForVisibility(msgOrderPlaced, Duration.ofSeconds(15)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public String getOrderConfirmationText() {
		return waitForVisibility(msgOrderPlaced).getText();
	}
}
