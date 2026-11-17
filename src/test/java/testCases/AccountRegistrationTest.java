package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;
import testBase.BaseClass;

public class AccountRegistrationTest extends BaseClass {

	@Test(priority = 1)
	public void verify_account_Register() {

		logger.info("****test Started*******");

		try {
			HomePage hp = new HomePage(driver);

			hp.clickMyAccount();

			logger.info("Clicked my account");
			hp.clickRegister();

			logger.info("Clicked register Link");

			AccountRegistrationPage regpage = new AccountRegistrationPage(driver);

			logger.info("Adding necessary details");
			regpage.setFname(randomString().toUpperCase());
			regpage.setLname(randomString().toUpperCase());
			regpage.setEmail(randomEmail());
			regpage.setTelephone(randomNumber());

			String Pwd = randomAlphaNumeric();
			regpage.setPassword(Pwd);
			regpage.setConfirmPassword(Pwd);
			regpage.setSubscriber();
			regpage.setPrivacyPolicy();
			regpage.clickContinue();

			logger.info("Account Created");
			String confirmationMsg = regpage.getConfirmationmsg();

			Assert.assertEquals(confirmationMsg, "Your Account Has Been Created!", "Account creation confirmation message mismatch!");

		} catch (Exception e) {
			logger.error("Test failed: " + e.getMessage());
			Assert.fail("Account registration failed: " + e.getMessage());
		}

	}

	@Test(priority = 2)
	public void verify_registration_duplicate_email() {

		logger.info("**** Starting Duplicate Email Registration Test *****");

		try {
			driver.manage().deleteAllCookies();
			driver.get(p.getProperty("appurl"));

			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickRegister();

			AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
			regpage.setFname(randomString().toUpperCase());
			regpage.setLname(randomString().toUpperCase());
			regpage.setEmail(p.getProperty("email"));
			regpage.setTelephone(randomNumber());

			String pwd = randomAlphaNumeric();
			regpage.setPassword(pwd);
			regpage.setConfirmPassword(pwd);
			regpage.setSubscriber();
			regpage.setPrivacyPolicy();
			regpage.clickContinue();

			String warningMsg = regpage.getWarningMessage();
			Assert.assertTrue(warningMsg.contains("Warning: E-Mail Address is already registered!"),
					"Expected warning message was not displayed for duplicate email registration!");
			logger.info("Duplicate email registration validation passed successfully");

		} catch (Exception e) {
			logger.error("Duplicate email registration test failed: " + e.getMessage());
			Assert.fail("Duplicate email registration test failed: " + e.getMessage());
		}
	}

	@Test(priority = 3)
	public void verify_registration_empty_mandatory_fields() {

		logger.info("**** Starting Empty Mandatory Fields Registration Test *****");

		try {
			driver.get(p.getProperty("appurl"));

			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickRegister();

			AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
			regpage.clickContinue();

			// 1. Verify top warning alert for privacy policy
			String warningMsg = regpage.getWarningMessage();
			Assert.assertTrue(warningMsg.contains("Warning: You must agree to the Privacy Policy!"),
					"Privacy policy warning was not displayed!");

			// 2. Verify all 5 field-level .text-danger errors
			Assert.assertEquals(regpage.getFirstNameError(), "First Name must be between 1 and 32 characters!",
					"First Name error mismatch!");
			Assert.assertEquals(regpage.getLastNameError(), "Last Name must be between 1 and 32 characters!",
					"Last Name error mismatch!");
			Assert.assertEquals(regpage.getEmailError(), "E-Mail Address does not appear to be valid!",
					"Email error mismatch!");
			Assert.assertEquals(regpage.getTelephoneError(), "Telephone must be between 3 and 32 characters!",
					"Telephone error mismatch!");
			Assert.assertEquals(regpage.getPasswordError(), "Password must be between 4 and 20 characters!",
					"Password error mismatch!");

			logger.info("All empty mandatory field validation messages verified successfully");

		} catch (Exception e) {
			logger.error("Empty mandatory fields registration test failed: " + e.getMessage());
			Assert.fail("Empty mandatory fields registration test failed: " + e.getMessage());
		}
	}
}
