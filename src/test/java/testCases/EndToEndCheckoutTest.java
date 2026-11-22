package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.CheckoutPage;
import pageObjects.HomePage;
import pageObjects.ProductDisplayPage;
import pageObjects.SearchPage;
import pageObjects.ShoppingCartPage;
import testBase.BaseClass;

/**
 * End-to-End Business Flow Test:
 * Product Search -> Add In-Stock Product to Cart -> View Cart -> Guest Checkout -> Place Order -> Order Confirmation
 */
public class EndToEndCheckoutTest extends BaseClass {

	@Test(priority = 1)
	public void verify_EndToEnd_Guest_Checkout() {

		logger.info("***** Starting EndToEnd Guest Checkout Test *****");

		try {
			// Step 1: Search for an in-stock product
			SearchPage sp = new SearchPage(driver);
			sp.search("HP LP3065");
			sp.clickSearchBtn();

			Assert.assertTrue(sp.isProductDisplayed("HP LP3065"), "Product HP LP3065 not found in search results!");

			// Step 2: Navigate to Product Details and Add In-Stock Product to Cart
			sp.selectProduct("null");
			ProductDisplayPage pdp = new ProductDisplayPage(driver);
			pdp.clickAddToCart();
			String successMsg = pdp.getSuccessAlertText();
			Assert.assertTrue(successMsg.contains("Success: You have added"), "Add to cart success message mismatch!");
			logger.info("Product added to cart successfully from Product Display Page");

			// Step 3: Navigate to Shopping Cart and Validate Product Details
			driver.get(p.getProperty("appurl") + "index.php?route=checkout/cart");
			ShoppingCartPage cartPage = new ShoppingCartPage(driver);
			Assert.assertEquals(cartPage.getProductName(), "HP LP3065", "Cart product name mismatch!");
			double unitPrice = cartPage.getProductUnitPriceAsDouble();
			double expectedLineTotal = unitPrice * 1;
			Assert.assertEquals(cartPage.getProductLineTotalAsDouble(), expectedLineTotal, 0.01, "Cart line total mismatch!");
			cartPage.clickCheckout();
			logger.info("Validated Shopping Cart details and dynamically calculated line total: " + expectedLineTotal);

			// Step 4: Checkout Step 1 - Choose Guest Checkout
			CheckoutPage checkoutPage = new CheckoutPage(driver);
			checkoutPage.selectGuestCheckout();
			checkoutPage.clickStep1Continue();
			logger.info("Selected Guest Checkout");

			// Step 5: Checkout Step 2 - Enter Billing Details
			String email = randomEmail();
			checkoutPage.setBillingDetails(
					randomString().toUpperCase(),
					randomString().toUpperCase(),
					email,
					randomNumber(),
					"123 Quality Street",
					"San Jose",
					"95110",
					"United States",
					"California"
			);
			checkoutPage.clickStep2GuestContinue();
			logger.info("Entered Billing Details");

			// Step 6: Checkout Step 4 - Delivery Method
			checkoutPage.clickStep4ShippingMethodContinue();
			logger.info("Confirmed Delivery Method");

			// Step 7: Checkout Step 5 - Payment Method & Terms
			checkoutPage.checkTermsAndConditions();
			checkoutPage.clickStep5PaymentMethodContinue();
			logger.info("Agreed to terms and selected Payment Method");

			// Step 8: Checkout Step 6 - Verify Financial Totals & Confirm Order
			Assert.assertEquals(checkoutPage.getConfirmProductName(), "HP LP3065", "Confirm order product name mismatch!");
			Assert.assertEquals(checkoutPage.getConfirmProductQuantity(), "1", "Confirm order quantity mismatch!");
			double productTotal = checkoutPage.getConfirmProductTotalAsDouble();
			double shippingRate = checkoutPage.getConfirmShippingRateAsDouble();
			double expectedGrandTotal = productTotal + shippingRate;
			Assert.assertEquals(checkoutPage.getConfirmGrandTotalAsDouble(), expectedGrandTotal, 0.01, "Confirm order grand total mismatch!");
			logger.info("Dynamically verified Step 6 financial totals: Net " + productTotal + " + Shipping " + shippingRate + " = Grand Total " + expectedGrandTotal);

			checkoutPage.clickConfirmOrder();
			logger.info("Clicked Confirm Order");

			// Step 9: Verify Order Confirmation
			boolean isPlaced = checkoutPage.isOrderPlaced();
			Assert.assertTrue(isPlaced, "Order confirmation heading 'Your order has been placed!' was not displayed!");
			logger.info("***** EndToEnd Guest Checkout Test Completed Successfully! *****");

		} catch (Exception e) {
			logger.error("EndToEnd Guest Checkout Test failed: " + e.getMessage());
			Assert.fail("EndToEnd Guest Checkout failed: " + e.getMessage());
		}
	}
}
