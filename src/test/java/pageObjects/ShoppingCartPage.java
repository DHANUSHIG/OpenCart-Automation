package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object representing the OpenCart Shopping Cart page (index.php?route=checkout/cart).
 */
public class ShoppingCartPage extends BasePage {

	public ShoppingCartPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//input[contains(@name,'quantity')]")
	WebElement txtQuantity;

	@FindBy(xpath = "//div[@id='content']//form//button[@data-original-title='Update' or contains(@onclick,'cart.update')]")
	WebElement btnUpdate;

	@FindBy(xpath = "//div[@id='content']//form//button[@data-original-title='Remove' or contains(@class,'btn-danger')]")
	WebElement btnRemove;

	@FindBy(xpath = "//div[@id='content']//p[contains(text(),'Your shopping cart is empty!')]")
	WebElement msgEmptyCart;

	@FindBy(css = "a[href='#collapse-shipping']")
	WebElement lnkEstimateShipping;

	@FindBy(id = "input-country")
	WebElement drpCountry;

	@FindBy(id = "input-zone")
	WebElement drpState;

	@FindBy(id = "input-postcode")
	WebElement txtPostcode;

	@FindBy(id = "button-quote")
	WebElement btnGetQuotes;

	@FindBy(xpath = "//a[contains(@class,'btn-primary') and contains(@href,'checkout/checkout')]")
	WebElement btnCheckout;

	@FindBy(css = "div.alert.alert-success")
	WebElement alertSuccessMsg;

	@FindBy(xpath = "//div[@class='table-responsive']//tbody/tr[1]/td[5]")
	WebElement txtProductUnitPrice;

	@FindBy(xpath = "//div[@class='table-responsive']//tbody/tr[1]/td[6]")
	WebElement txtProductLineTotal;

	@FindBy(xpath = "//div[@id='content']//div[contains(@class,'row')]//table[contains(@class,'table-bordered')]//tr[td[contains(.,'Sub-Total')]]/td[last()]")
	WebElement txtSubTotal;

	@FindBy(xpath = "//div[@id='content']//div[contains(@class,'row')]//table[contains(@class,'table-bordered')]//tr[last()]/td[last()]")
	WebElement txtGrandTotal;

	@FindBy(xpath = "//div[@class='table-responsive']//tbody/tr[1]/td[2]/a")
	WebElement txtProductName;

	public void setQuantity(String qty) {
		WebElement qtyField = waitForVisibility(txtQuantity);
		qtyField.clear();
		qtyField.sendKeys(qty);
	}

	public void clickUpdate() {
		waitForClickability(btnUpdate).click();
	}

	public void clickRemove() {
		org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
		waitForVisibility(btnRemove);
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btnRemove);
		waitForClickability(btnRemove).click();
	}

	public String getEmptyCartMessage() {
		return waitForVisibility(msgEmptyCart).getText();
	}

	public void clickEstimateShipping() {
		waitForClickability(lnkEstimateShipping).click();
	}

	public void selectCountry(String country) {
		Select select = new Select(waitForVisibility(drpCountry));
		select.selectByVisibleText(country);
	}

	public void selectState(String state) {
		Select select = new Select(waitForVisibility(drpState));
		select.selectByVisibleText(state);
	}

	public void clickGetQuotes() {
		waitForClickability(btnGetQuotes).click();
	}

	public void clickCheckout() {
		waitForClickability(btnCheckout).click();
	}

	public String getSuccessAlertText() {
		return waitForVisibility(alertSuccessMsg).getText();
	}

	public String getProductUnitPrice() {
		return waitForVisibility(txtProductUnitPrice).getText();
	}

	public double parsePrice(String priceStr) {
		return Double.parseDouble(priceStr.replace("$", "").replace(",", "").trim());
	}

	public double getProductUnitPriceAsDouble() {
		return parsePrice(getProductUnitPrice());
	}

	public String getProductLineTotal() {
		return waitForVisibility(txtProductLineTotal).getText();
	}

	public double getProductLineTotalAsDouble() {
		return parsePrice(getProductLineTotal());
	}

	public String getSubTotal() {
		return waitForVisibility(txtSubTotal).getText();
	}

	public double getSubTotalAsDouble() {
		return parsePrice(getSubTotal());
	}

	public double getGrandTotalAsDouble() {
		return parsePrice(getGrandTotal());
	}

	// ================= Multi-Product Row-Indexed Operations =================

	public int getCartItemCount() {
		return driver.findElements(By.xpath("//div[@class='table-responsive']//tbody/tr")).size();
	}

	public int waitForCartItemCount(int expectedCount) {
		new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
				.until(org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBe(
						By.xpath("//div[@class='table-responsive']//tbody/tr"), expectedCount));
		return getCartItemCount();
	}

	public String getProductName(int rowIndex) {
		By locator = By.xpath("//div[@class='table-responsive']//tbody/tr[" + rowIndex + "]/td[2]/a");
		return waitForVisibility(locator).getText();
	}

	public String getProductQuantity(int rowIndex) {
		By locator = By.xpath("//div[@class='table-responsive']//tbody/tr[" + rowIndex + "]//input[contains(@name,'quantity')]");
		return waitForVisibility(locator).getAttribute("value");
	}

	public String getProductUnitPrice(int rowIndex) {
		By locator = By.xpath("//div[@class='table-responsive']//tbody/tr[" + rowIndex + "]/td[5]");
		return waitForVisibility(locator).getText();
	}

	public double getProductUnitPriceAsDouble(int rowIndex) {
		return parsePrice(getProductUnitPrice(rowIndex));
	}

	public String getProductLineTotal(int rowIndex) {
		By locator = By.xpath("//div[@class='table-responsive']//tbody/tr[" + rowIndex + "]/td[6]");
		return waitForVisibility(locator).getText();
	}

	public double getProductLineTotalAsDouble(int rowIndex) {
		return parsePrice(getProductLineTotal(rowIndex));
	}

	public void setQuantity(int rowIndex, String qty) {
		By locator = By.xpath("//div[@class='table-responsive']//tbody/tr[" + rowIndex + "]//input[contains(@name,'quantity')]");
		WebElement field = waitForVisibility(locator);
		field.clear();
		field.sendKeys(qty);
	}

	public void clickUpdate(int rowIndex) {
		By locator = By.xpath("//div[@class='table-responsive']//tbody/tr[" + rowIndex + "]//button[@data-original-title='Update' or @type='submit']");
		waitForClickability(waitForVisibility(locator)).click();
	}

	public void clickRemove(int rowIndex) {
		By locator = By.xpath("//div[@class='table-responsive']//tbody/tr[" + rowIndex + "]//button[contains(@class,'btn-danger')]");
		WebElement btn = waitForVisibility(locator);
		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
		waitForClickability(btn).click();
	}

	public String getGrandTotal() {
		return waitForVisibility(txtGrandTotal).getText();
	}

	public String getProductName() {
		return waitForVisibility(txtProductName).getText();
	}

	public boolean isCartEmpty() {
		try {
			return waitForVisibility(msgEmptyCart).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
}
