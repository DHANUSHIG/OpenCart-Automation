package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object representing the OpenCart 6-Step Checkout process (index.php?route=checkout/checkout).
 */
public class CheckoutPage extends BasePage {

	public CheckoutPage(WebDriver driver) {
		super(driver);
	}

	// ================= Step 1: Checkout Options =================
	@FindBy(xpath = "//input[@value='guest']")
	WebElement rdoGuestCheckout;

	@FindBy(xpath = "//input[@value='register']")
	WebElement rdoRegisterAccount;

	@FindBy(id = "button-account")
	WebElement btnStep1Continue;

	// ================= Step 2: Billing / Guest Details =================
	@FindBy(id = "input-payment-firstname")
	WebElement txtBillingFirstName;

	@FindBy(id = "input-payment-lastname")
	WebElement txtBillingLastName;

	@FindBy(id = "input-payment-email")
	WebElement txtBillingEmail;

	@FindBy(id = "input-payment-telephone")
	WebElement txtBillingTelephone;

	@FindBy(id = "input-payment-address-1")
	WebElement txtBillingAddress1;

	@FindBy(id = "input-payment-city")
	WebElement txtBillingCity;

	@FindBy(id = "input-payment-postcode")
	WebElement txtBillingPostcode;

	@FindBy(id = "input-payment-country")
	WebElement drpBillingCountry;

	@FindBy(id = "input-payment-zone")
	WebElement drpBillingZone;

	@FindBy(id = "button-guest")
	WebElement btnStep2GuestContinue;

	@FindBy(id = "button-payment-address")
	WebElement btnStep2RegisteredContinue;

	// ================= Step 4: Delivery Method =================
	@FindBy(id = "button-shipping-method")
	WebElement btnStep4ShippingMethodContinue;

	// ================= Step 5: Payment Method =================
	@FindBy(xpath = "//input[@name='agree']")
	WebElement chkTermsAndConditions;

	@FindBy(id = "button-payment-method")
	WebElement btnStep5PaymentMethodContinue;

	// ================= Step 6: Confirm Order =================
	@FindBy(id = "button-confirm")
	WebElement btnConfirmOrder;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tbody//tr[1]/td[1]//a")
	WebElement txtConfirmProductName;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tbody//tr[1]/td[3]")
	WebElement txtConfirmProductQuantity;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tbody//tr[1]/td[5]")
	WebElement txtConfirmProductTotal;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tfoot//tr[td[contains(.,'Flat Shipping')]]/td[last()]")
	WebElement txtConfirmShippingRate;

	@FindBy(xpath = "//div[@id='collapse-checkout-confirm']//tfoot//tr[last()]/td[last()]")
	WebElement txtConfirmGrandTotal;

	// ================= Order Placed Confirmation =================
	@FindBy(xpath = "//div[@id='content']//h1[text()='Your order has been placed!']")
	WebElement msgOrderPlaced;

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
		new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
				.until(org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement(drpBillingZone, state));
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
		waitForClickability(btnStep4ShippingMethodContinue).click();
	}

	public void checkTermsAndConditions() {
		waitForClickability(chkTermsAndConditions).click();
	}

	public void clickStep5PaymentMethodContinue() {
		waitForClickability(btnStep5PaymentMethodContinue).click();
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
		org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
		WebElement btn = waitForClickability(btnConfirmOrder);
		js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
		js.executeScript("arguments[0].click();", btn);
	}

	public boolean isOrderPlaced() {
		try {
			return waitForVisibility(msgOrderPlaced, java.time.Duration.ofSeconds(15)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public String getOrderConfirmationText() {
		return waitForVisibility(msgOrderPlaced).getText();
	}
}
