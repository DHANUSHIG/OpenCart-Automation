package pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object representing OpenCart Product Display / Details page (index.php?route=product/product&product_id=XX).
 */
public class ProductDisplayPage extends BasePage {

	public ProductDisplayPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//div[@id='content']//h1")
	private WebElement txtProductTitle;

	@FindBy(id = "input-quantity")
	private WebElement txtQuantity;

	@FindBy(id = "button-cart")
	private WebElement btnAddToCart;

	@FindBy(css = "div.alert.alert-success")
	private WebElement alertSuccessMessage;

	@FindBy(xpath = "//button[@data-original-title='Add to Wish List' or contains(@onclick,'wishlist.add')]")
	private WebElement btnAddToWishList;

	@FindBy(xpath = "//button[@data-original-title='Compare this Product' or contains(@onclick,'compare.add')]")
	private WebElement btnCompareProduct;

	public String getProductTitle() {
		return waitForVisibility(txtProductTitle).getText();
	}

	public void setQuantity(String qty) {
		WebElement qtyField = waitForVisibility(txtQuantity);
		qtyField.clear();
		qtyField.sendKeys(qty);
	}

	public void clickAddToCart() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		waitForVisibility(btnAddToCart);
		js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btnAddToCart);
		waitForClickability(btnAddToCart).click();
	}

	public String getSuccessAlertText() {
		return waitForVisibility(alertSuccessMessage).getText();
	}

	public void clickAddToWishList() {
		waitForClickability(btnAddToWishList).click();
	}

	public void clickCompareProduct() {
		waitForClickability(btnCompareProduct).click();
	}
}
