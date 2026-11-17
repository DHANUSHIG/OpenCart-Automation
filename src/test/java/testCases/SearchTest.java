package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.SearchPage;
import testBase.BaseClass;

public class SearchTest extends BaseClass {

	@Test(priority = 1)
	public void verify_Search() {

		logger.info("** Search test method has started (Guest session) **");

		try {
			driver.get(p.getProperty("appurl"));

			SearchPage SP = new SearchPage(driver);
			SP.search(p.getProperty("searchproductname"));
			SP.clickSearchBtn();
			SP.selectNavbar();
			SP.selectSortBy("Name (Z - A)");

			SP.clickAddToCart();
			String addCartSuccessMSG = SP.getAddToCartSuccessMsg();
			Assert.assertTrue(addCartSuccessMSG.contains("Success: You have added") || addCartSuccessMSG.contains("added"), "Success message not displayed");

			logger.info("** Search test method completed successfully **");
		} catch (Exception e) {
			logger.error("Search test failed: " + e.getMessage());
			Assert.fail("Search test failed: " + e.getMessage());
		}
	}

	@Test(priority = 2)
	public void verify_Search_Non_Existing_Product() {

		logger.info("** Starting Search Non Existing Product Test (Guest session) **");

		try {
			driver.get(p.getProperty("appurl"));

			SearchPage sp = new SearchPage(driver);
			sp.search("xyzabc123nonexistent");
			sp.clickSearchBtn();

			boolean isMsgDisplayed = sp.isNoProductFoundMessageDisplayed();
			Assert.assertTrue(isMsgDisplayed, "'No product matches' message is not displayed!");
			Assert.assertEquals(sp.getNoProductFoundText(), "There is no product that matches the search criteria.",
					"No product found message mismatch!");

			logger.info("** Search Non Existing Product Test completed successfully **");
		} catch (Exception e) {
			logger.error("Search non existing product test failed: " + e.getMessage());
			Assert.fail("Search non existing product test failed: " + e.getMessage());
		}
	}
}
