package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

public class DDTTestcase extends BaseClass {

	@Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class)
	public void verify_loginDDT(String Email, String Password, String Result) {

		try {
			logger.info("****Test Started***");

			// Ensure fresh home page state for each iteration
			driver.get(p.getProperty("appurl"));

			HomePage hp = new HomePage(driver);
			hp.clickMyAccount();
			hp.clickLogin();

			LoginPage lp = new LoginPage(driver);
			lp.setEmail(Email);
			lp.setPassword(Password);
			lp.clickLogin();

			MyAccountPage mp = new MyAccountPage(driver);
			boolean targetpage = mp.isMyaccountDisplayed();

			if (Result.equalsIgnoreCase("Valid")) {
				if (targetpage) {
					hp.clickLogout();
					Assert.assertTrue(true);
				} else {
					Assert.fail("Login failed with valid credentials");
				}
			}

			if (Result.equalsIgnoreCase("Invalid")) {
				if (targetpage) {
					hp.clickLogout();
					Assert.fail("Login succeeded with invalid credentials");
				} else {
					Assert.assertTrue(true);
				}
			}

			logger.info("test Finished");

		} catch (Exception e) {
			Assert.fail("An exception occurred: " + e.getMessage());
		}
	}
}
