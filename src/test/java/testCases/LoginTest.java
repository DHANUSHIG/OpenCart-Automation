package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class LoginTest extends BaseClass {
	
	@Test(priority = 1)
	public void verify_Login() {
		
		logger.info("****Test Started*****");
		
		try {
			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickLogin();

			LoginPage lp = new LoginPage(driver);
			lp.setEmail(p.getProperty("email"));
			lp.setPassword(p.getProperty("password"));
			lp.clickLogin();

			MyAccountPage mp = new MyAccountPage(driver);
			Assert.assertTrue(mp.isMyaccountDisplayed(), "My Account is not displayed after login!");

			// Clean logout so subsequent tests run in clean session
			hp.clickLogout();
		} catch (Exception e) {
			logger.error("Login test failed: " + e.getMessage());
			Assert.fail("Login test failed: " + e.getMessage());
		}
		
		logger.info("Finshed test case");
	}

	@Test(priority = 2)
	public void verify_Login_Invalid_Credentials() {

		logger.info("**** Starting Invalid Login Test *****");

		try {
			driver.get(p.getProperty("appurl"));

			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickLogin();

			LoginPage lp = new LoginPage(driver);
			lp.setEmail("invalid_" + randomString() + "@test.com");
			lp.setPassword("WrongPassword@123");
			lp.clickLogin();

			String warningMsg = lp.getWarningMessage();
			Assert.assertTrue(warningMsg.contains("Warning: No match for E-Mail Address and/or Password."),
					"Expected warning message was not displayed for invalid login!");
			logger.info("Invalid login validation passed successfully");

		} catch (Exception e) {
			logger.error("Invalid login test failed: " + e.getMessage());
			Assert.fail("Invalid login test failed: " + e.getMessage());
		}
	}

}
