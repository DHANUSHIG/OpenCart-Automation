package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.ProductDisplayPage;
import pageObjects.SearchPage;
import pageObjects.ShoppingCartPage;
import testBase.BaseClass;

/**
 * Test cases verifying shopping cart operations:
 * TC_CART_01: Quantity update & dynamic total recalculation
 * TC_CART_02: Product removal & empty cart verification
 */
public class ShoppingCartTest extends BaseClass {

	@Test(priority = 1)
	public void verify_update_cart_quantity() {

		logger.info("***** Starting Shopping Cart Quantity Update Test *****");

		try {
			// Step 1: Search and navigate to in-stock product
			SearchPage sp = new SearchPage(driver);
			sp.search("HP LP3065");
			sp.clickSearchBtn();
			sp.selectProduct("HP LP3065");

			// Step 2: Add to cart
			ProductDisplayPage pdp = new ProductDisplayPage(driver);
			pdp.clickAddToCart();
			String addAlert = pdp.getSuccessAlertText();
			Assert.assertTrue(addAlert.contains("Success: You have added"), "Add to cart alert not displayed!");

			// Step 3: Navigate to cart
			driver.get(p.getProperty("appurl") + "index.php?route=checkout/cart");
			ShoppingCartPage cartPage = new ShoppingCartPage(driver);

			// Assert initial state dynamically
			Assert.assertEquals(cartPage.getProductName(), "HP LP3065", "Cart product name mismatch!");
			double unitPrice = cartPage.getProductUnitPriceAsDouble();
			double initialLineTotal = cartPage.getProductLineTotalAsDouble();
			double initialSubTotal = cartPage.getSubTotalAsDouble();
			double initialGrandTotal = cartPage.getGrandTotalAsDouble();

			double expectedInitialTotal = unitPrice * 1;
			Assert.assertEquals(initialLineTotal, expectedInitialTotal, 0.01, "Initial line total mismatch!");
			Assert.assertEquals(initialGrandTotal, expectedInitialTotal, 0.01, "Initial grand total mismatch!");

			// Step 4: Update quantity to 2
			cartPage.setQuantity("2");
			cartPage.clickUpdate();

			// Step 5: Assert success message and recalculated totals dynamically
			String modAlert = cartPage.getSuccessAlertText();
			Assert.assertTrue(modAlert.contains("Success: You have modified your shopping cart!"),
					"Cart modification alert mismatch!");

			double expectedUpdatedLineTotal = unitPrice * 2;
			double expectedUpdatedSubTotal = initialSubTotal * 2;
			double expectedUpdatedGrandTotal = initialGrandTotal * 2;

			Assert.assertEquals(cartPage.getProductLineTotalAsDouble(), expectedUpdatedLineTotal, 0.01, "Updated line total mismatch after modifying quantity!");
			Assert.assertEquals(cartPage.getSubTotalAsDouble(), expectedUpdatedSubTotal, 0.01, "Updated subtotal mismatch after modifying quantity!");
			Assert.assertEquals(cartPage.getGrandTotalAsDouble(), expectedUpdatedGrandTotal, 0.01, "Updated grand total mismatch after modifying quantity!");

			logger.info("***** Shopping Cart Quantity Update Test Passed Successfully *****");

		} catch (Exception e) {
			logger.error("Shopping cart quantity update test failed: " + e.getMessage());
			Assert.fail("Shopping cart quantity update test failed: " + e.getMessage());
		}
	}

	@Test(priority = 2)
	public void verify_remove_product_from_cart() {

		logger.info("***** Starting Shopping Cart Product Removal Test *****");

		try {
			// Step 1: Ensure product is added to cart
			SearchPage sp = new SearchPage(driver);
			sp.search("HP LP3065");
			sp.clickSearchBtn();
			sp.selectProduct("HP LP3065");

			ProductDisplayPage pdp = new ProductDisplayPage(driver);
			pdp.clickAddToCart();
			String addAlert = pdp.getSuccessAlertText();
			Assert.assertTrue(addAlert.contains("Success: You have added"), "Add to cart alert not displayed!");

			// Step 2: Navigate to cart
			driver.get(p.getProperty("appurl") + "index.php?route=checkout/cart");
			ShoppingCartPage cartPage = new ShoppingCartPage(driver);

			// Step 3: Click remove
			cartPage.clickRemove();

			// Step 4: Assert empty cart message
			Assert.assertTrue(cartPage.isCartEmpty(), "Shopping cart is not empty after clicking remove!");
			Assert.assertTrue(cartPage.getEmptyCartMessage().contains("Your shopping cart is empty!"),
					"Empty cart message text mismatch!");

			logger.info("***** Shopping Cart Product Removal Test Passed Successfully *****");

		} catch (Exception e) {
			logger.error("Shopping cart product removal test failed: " + e.getMessage());
			Assert.fail("Shopping cart product removal test failed: " + e.getMessage());
		}
	}

	@Test(priority = 3)
	public void verify_multi_product_cart_operations() {

		logger.info("***** Starting Multi-Product Shopping Cart Operations Test *****");

		try {
			// Step 1: Search and add Product 1 ("HP LP3065")
			SearchPage sp = new SearchPage(driver);
			sp.search("HP LP3065");
			sp.clickSearchBtn();
			sp.selectProduct("HP LP3065");

			ProductDisplayPage pdp = new ProductDisplayPage(driver);
			pdp.clickAddToCart();
			String addAlert1 = pdp.getSuccessAlertText();
			Assert.assertTrue(addAlert1.contains("Success: You have added"), "Add to cart alert for HP LP3065 not displayed!");
			logger.info("Added first product (HP LP3065) to cart");

			// Step 2: Search and add Product 2 ("HTC Touch HD")
			sp.search("HTC Touch HD");
			sp.clickSearchBtn();
			sp.selectProduct("HTC Touch HD");

			pdp.clickAddToCart();
			String addAlert2 = pdp.getSuccessAlertText();
			Assert.assertTrue(addAlert2.contains("Success: You have added"), "Add to cart alert for HTC Touch HD not displayed!");
			logger.info("Added second product (HTC Touch HD) to cart");

			// Step 3: Navigate to cart and verify both products are present
			driver.get(p.getProperty("appurl") + "index.php?route=checkout/cart");
			ShoppingCartPage cartPage = new ShoppingCartPage(driver);

			int itemCount = cartPage.waitForCartItemCount(2);
			Assert.assertEquals(itemCount, 2, "Expected 2 products in shopping cart!");

			String nameRow1 = cartPage.getProductName(1);
			String nameRow2 = cartPage.getProductName(2);
			Assert.assertTrue((nameRow1.contains("HP LP3065") || nameRow1.contains("HTC Touch HD")), "Row 1 product name unexpected: " + nameRow1);
			Assert.assertTrue((nameRow2.contains("HP LP3065") || nameRow2.contains("HTC Touch HD")), "Row 2 product name unexpected: " + nameRow2);
			Assert.assertNotEquals(nameRow1, nameRow2, "Cart rows should contain distinct products!");

			Assert.assertEquals(cartPage.getProductQuantity(1), "1", "Row 1 initial quantity should be 1");
			Assert.assertEquals(cartPage.getProductQuantity(2), "1", "Row 2 initial quantity should be 1");

			// Step 4: Dynamically verify initial line totals, subtotal and grand total
			double unitPrice1 = cartPage.getProductUnitPriceAsDouble(1);
			double unitPrice2 = cartPage.getProductUnitPriceAsDouble(2);
			double lineTotal1 = cartPage.getProductLineTotalAsDouble(1);
			double lineTotal2 = cartPage.getProductLineTotalAsDouble(2);
			Assert.assertEquals(lineTotal1, unitPrice1 * 1, 0.01, "Row 1 initial line total mismatch!");
			Assert.assertEquals(lineTotal2, unitPrice2 * 1, 0.01, "Row 2 initial line total mismatch!");

			double initialSubTotal = cartPage.getSubTotalAsDouble();
			double initialGrandTotal = cartPage.getGrandTotalAsDouble();
			double expectedInitialGrandTotal = lineTotal1 + lineTotal2;
			Assert.assertEquals(initialGrandTotal, expectedInitialGrandTotal, 0.01, "Initial cart grand total mismatch!");
			Assert.assertTrue(initialSubTotal > 0, "Initial subtotal should be greater than zero");
			logger.info("Verified 2 distinct products in cart. Dynamic Grand Total: " + expectedInitialGrandTotal + ", Subtotal: " + initialSubTotal);

			// Step 5: Update quantity of Row 1 to 2
			cartPage.setQuantity(1, "2");
			cartPage.clickUpdate(1);

			String modAlert = cartPage.getSuccessAlertText();
			Assert.assertTrue(modAlert.contains("Success: You have modified your shopping cart!"),
					"Cart modification alert mismatch after quantity update!");
			Assert.assertEquals(cartPage.getProductQuantity(1), "2", "Row 1 quantity mismatch after update!");

			// Step 6: Dynamically verify recalculated line totals, subtotal and grand total
			double updatedLineTotal1 = cartPage.getProductLineTotalAsDouble(1);
			double updatedLineTotal2 = cartPage.getProductLineTotalAsDouble(2);
			Assert.assertEquals(updatedLineTotal1, unitPrice1 * 2, 0.01, "Row 1 line total mismatch after quantity update!");
			Assert.assertEquals(updatedLineTotal2, unitPrice2 * 1, 0.01, "Row 2 line total mismatch after Row 1 update!");

			double expectedUpdatedGrandTotal = (unitPrice1 * 2) + (unitPrice2 * 1);
			Assert.assertEquals(cartPage.getGrandTotalAsDouble(), expectedUpdatedGrandTotal, 0.01, "Recalculated grand total mismatch after updating quantity!");

			double unitNetPrice = initialSubTotal / 2.0;
			double expectedUpdatedSubTotal = initialSubTotal + unitNetPrice;
			Assert.assertEquals(cartPage.getSubTotalAsDouble(), expectedUpdatedSubTotal, 0.01, "Recalculated subtotal mismatch after updating quantity!");
			logger.info("Updated Row 1 quantity to 2 and verified dynamic Grand Total: " + expectedUpdatedGrandTotal + ", Subtotal: " + expectedUpdatedSubTotal);

			// Record remaining product details from Row 2 before removing Row 1
			String remainingExpectedName = cartPage.getProductName(2);
			double remainingUnitPrice = cartPage.getProductUnitPriceAsDouble(2);

			// Step 7: Remove Row 1 product
			cartPage.clickRemove(1);

			// Step 8: Verify remaining product and recalculated totals
			int remainingCount = cartPage.waitForCartItemCount(1);
			Assert.assertEquals(remainingCount, 1, "Expected exactly 1 product in cart after removal!");

			String actualRemainingName = cartPage.getProductName(1);
			Assert.assertEquals(actualRemainingName, remainingExpectedName, "Remaining product name mismatch!");

			double expectedRemainingLineTotal = remainingUnitPrice * 1;
			Assert.assertEquals(cartPage.getProductLineTotalAsDouble(1), expectedRemainingLineTotal, 0.01, "Remaining product line total mismatch!");
			Assert.assertEquals(cartPage.getGrandTotalAsDouble(), expectedRemainingLineTotal, 0.01, "Remaining cart grand total mismatch!");
			Assert.assertEquals(cartPage.getSubTotalAsDouble(), unitNetPrice, 0.01, "Remaining cart subtotal mismatch!");
			logger.info("Removed one product and verified remaining product (" + actualRemainingName + "), Grand Total: " + expectedRemainingLineTotal + ", Subtotal: " + unitNetPrice);

			logger.info("***** Multi-Product Shopping Cart Operations Test Passed Successfully *****");

		} catch (Exception e) {
			logger.error("Multi-product cart operations test failed: " + e.getMessage());
			Assert.fail("Multi-product cart operations test failed: " + e.getMessage());
		}
	}
}
