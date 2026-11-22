package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class SearchPage extends BasePage {

	public SearchPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//input[@name='search']")
	private WebElement txtSearchBox;
	
	@FindBy(xpath = "//span[@class='input-group-btn']/button")
	private WebElement btnSearch;
	
	@FindBy(xpath = "//ul[@class='nav navbar-nav']//a[contains(text(),'Phones & PDAs')]")
	private WebElement navPhonesAndPdas;
	
	@FindBy(xpath = "//select[@id='input-sort']")
	private WebElement drpSortBy;
	
	@FindBy(xpath = "//button[contains(@onclick,\"cart.add('28'\")] | //div[contains(@class,'product-layout')][1]//button[contains(@onclick,'cart.add')]")
	private WebElement btnAddCart;
	
	@FindBy(css = "div.alert.alert-success")
	private WebElement alertSuccessMsg;

	@FindBy(xpath = "//div[@id='content']//p[contains(text(),'There is no product that matches the search criteria.')]")
	private WebElement msgNoProductFound;
	
	public void search(String productName) {
		waitForVisibility(txtSearchBox).clear();
		txtSearchBox.sendKeys(productName);	
	}
	
	public void clickSearchBtn() {
		waitForClickability(btnSearch).click();
	}
	
	public void selectNavbar() {
		waitForClickability(navPhonesAndPdas).click();
	}
	
	public void selectSortBy(String optionText) {
		Select sortSelect = new Select(waitForVisibility(drpSortBy));
		sortSelect.selectByContainsVisibleText(optionText);
	}
	
	public void clickAddToCart() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    waitForVisibility(btnAddCart);
	    js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btnAddCart);
	    waitForClickability(btnAddCart).click();
	}

	public void addProductToCart(String productName) {
		By dynamicCartBtn = By.xpath("//div[contains(@class,'product-layout')]//h4/a[text()='" + productName + "']/ancestor::div[contains(@class,'product-thumb')]//button[contains(@onclick,'cart.add')]");
		WebElement btn = waitForVisibility(dynamicCartBtn);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", btn);
		waitForClickability(btn).click();
	}

	public void selectProduct(String productName) {
		By productLink = By.xpath("//div[contains(@class,'product-layout')]//h4/a[text()='" + productName + "']");
		WebElement element = waitForVisibility(productLink);
		waitForClickability(element).click();
	}

	public boolean isProductDisplayed(String productName) {
		try {
			By productLink = By.xpath("//div[contains(@class,'product-layout')]//h4/a[text()='" + productName + "']");
			return waitForVisibility(productLink).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getAddToCartSuccessMsg() {
		return waitForVisibility(alertSuccessMsg).getText();
	}

	public boolean isNoProductFoundMessageDisplayed() {
		try {
			return waitForVisibility(msgNoProductFound).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public String getNoProductFoundText() {
		try {
			return waitForVisibility(msgNoProductFound).getText();
		} catch (Exception e) {
			return "";
		}
	}

}
