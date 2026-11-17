package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;

/*
 * BaseClass is the parent class for all test classes.
 * It handles:
 * 1) Browser setup
 * 2) Reading configuration file
 * 3) Common utility methods
 * 4) Screenshot capturing
 * 5) Closing browser
 */

public class BaseClass {

	// ThreadLocal WebDriver object for parallel thread-safety
	private static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();

	// Instance WebDriver object (accessible directly by test subclasses)
	public WebDriver driver;

	// Logger object (used to print logs in console/report)
	public Logger logger;

	// Properties object (used to read config.properties file)
	public static Properties p;

	/*
	 * Returns thread-safe WebDriver instance
	 */
	public static WebDriver getDriver() {
		return tdriver.get();
	}

	// =================== BROWSER SETUP ===================

	/*
	 * @BeforeClass runs before any test method in that class. Parameters come from
	 * testng.xml file.
	 */
	@BeforeClass
	@Parameters({ "os", "browser" })
	public void setup(@org.testng.annotations.Optional("Windows") String os, @org.testng.annotations.Optional("chrome") String br) throws IOException {

		// Load config.properties file
		FileReader file = new FileReader("./src//test//resources//config.properties");
		p = new Properties();
		p.load(file);

		// Check for headless mode flag (from system property e.g. -Dheadless=true, or config.properties)
		boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", p.getProperty("headless", "false")));

		WebDriver localDriver = null;

		if (p.getProperty("Execution_env").equalsIgnoreCase("remote")) {
			String gridUrl = p.getProperty("gridURL", "http://192.168.0.100:4444/wd/hub");
			Platform platform;
			if (os.equalsIgnoreCase("Windows")) {
				platform = Platform.WIN11;
			} else if (os.equalsIgnoreCase("mac")) {
				platform = Platform.MAC;
			} else {
				platform = Platform.LINUX;
			}

			switch (br.toLowerCase()) {
			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.setPlatformName(platform.name());
				if (isHeadless) {
					chromeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
				}
				localDriver = new RemoteWebDriver(new URL(gridUrl), chromeOptions);
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				edgeOptions.setPlatformName(platform.name());
				if (isHeadless) {
					edgeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
				}
				localDriver = new RemoteWebDriver(new URL(gridUrl), edgeOptions);
				break;
			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setPlatformName(platform.name());
				if (isHeadless) {
					firefoxOptions.addArguments("--headless", "--width=1920", "--height=1080");
				}
				localDriver = new RemoteWebDriver(new URL(gridUrl), firefoxOptions);
				break;
			default:
				System.out.println("No matching browser for remote execution: " + br);
				return;
			}

		} else if (p.getProperty("Execution_env").equalsIgnoreCase("local")) {
			// Launch browser based on parameter passed from testng.xml
			switch (br.toLowerCase()) {

			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions();
				if (isHeadless) {
					chromeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
				}
				localDriver = new ChromeDriver(chromeOptions);
				break;

			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				if (isHeadless) {
					edgeOptions.addArguments("--headless=new", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
				}
				localDriver = new EdgeDriver(edgeOptions);
				break;

			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				if (isHeadless) {
					firefoxOptions.addArguments("--headless", "--width=1920", "--height=1080");
				}
				localDriver = new FirefoxDriver(firefoxOptions);
				break;

			default:
				System.out.println("Invalid Browser: " + br);
				return; // Stops execution if browser is wrong
			}

		}

		tdriver.set(localDriver);
		this.driver = tdriver.get();

		// Initialize logger for current class
		logger = LogManager.getLogger(this.getClass());

		if (getDriver() != null) {
			// Browser basic configurations
			getDriver().manage().window().maximize(); // Maximize browser
			getDriver().manage().deleteAllCookies(); // Clear cookies
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait

			// Load application URL from config file
			getDriver().get(p.getProperty("appurl"));
		}
	}

	// =================== RANDOM DATA METHODS ===================

	/*
	 * Generates random alphabet string (used for email, name etc.)
	 */
	public String randomString() {
		return RandomStringUtils.randomAlphabetic(6);
	}

	/*
	 * Generates a unique collision-free email address with timestamp
	 */
	public String randomEmail() {
		return "user_" + RandomStringUtils.randomAlphabetic(5).toLowerCase() + System.currentTimeMillis() + "@test.com";
	}

	/*
	 * Generates random numeric string
	 */
	public String randomNumber() {
		return RandomStringUtils.randomNumeric(10);
	}

	/*
	 * Generates random alphanumeric string
	 */
	public String randomAlphaNumeric() {
		String letters = RandomStringUtils.randomAlphabetic(5);
		String numbers = RandomStringUtils.randomNumeric(3);
		return numbers + letters;
	}

	// Helper method to perform login for dependent tests
	public void performLogin() {

		logger.info("**** Performing Login Setup *****");

		try {
			WebDriver activeDriver = getDriver() != null ? getDriver() : this.driver;
			activeDriver.get(p.getProperty("appurl"));
			HomePage hp = new HomePage(activeDriver);
			hp.clickMyAccount();
			hp.clickLogin();

			LoginPage lp = new LoginPage(activeDriver);
			lp.setEmail(p.getProperty("email"));
			lp.setPassword(p.getProperty("password"));
			lp.clickLogin();

			MyAccountPage mp = new MyAccountPage(activeDriver);
			Assert.assertTrue(mp.isMyaccountDisplayed(), "Login failed: My Account is not displayed");
		} catch (Exception e) {
			logger.error("Login setup failed: " + e.getMessage());
			Assert.fail("Login setup failed: " + e.getMessage());
		}

		logger.info("Login setup finished successfully");
	}

	// =================== SESSION TEARDOWN ===================

	/*
	 * @AfterMethod ensures each test method starts with a clean session by clearing all browser cookies.
	 */
	@org.testng.annotations.AfterMethod
	public void cleanSession() {
		try {
			WebDriver activeDriver = getDriver() != null ? getDriver() : this.driver;
			if (activeDriver != null) {
				activeDriver.manage().deleteAllCookies();
			}
		} catch (Exception e) {
			if (logger != null) {
				logger.warn("Could not clear session cookies: " + e.getMessage());
			}
		}
	}

	// =================== BROWSER CLOSE ===================

	/*
	 * @AfterClass runs after all test methods complete. It closes the browser
	 * completely and removes thread-local instance.
	 */
	@AfterClass
	public void teardown() {
		if (getDriver() != null) {
			getDriver().quit();
			tdriver.remove();
		}
	}

	// =================== SCREENSHOT METHOD ===================

	/*
	 * This method captures screenshot when test fails. It is called from
	 * ExtentReportManager.
	 *
	 * Steps: 1) Generate unique name using timestamp 2) Take screenshot 3) Store
	 * inside "screenshots" folder 4) Return relative path (used in Extent report)
	 */

	public String captureScreen(String tname) throws IOException {

		// Create timestamp (to avoid file name duplication)
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		// Convert driver to TakesScreenshot interface
		WebDriver currentDriver = getDriver() != null ? getDriver() : this.driver;
		TakesScreenshot ts = (TakesScreenshot) currentDriver;

		// Capture screenshot and store in temporary file
		File sourceFile = ts.getScreenshotAs(OutputType.FILE);

		// Create unique file name
		String fileName = tname + "_" + timeStamp + ".png";

		// Define screenshot folder path
		String screenshotFolder = System.getProperty("user.dir") + "/screenshots";

		// Create screenshots folder if not exists
		File folder = new File(screenshotFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		// Final file location
		File targetFile = new File(screenshotFolder + "/" + fileName);

		// Copy screenshot from temp location to project folder
		FileUtils.copyFile(sourceFile, targetFile);

		// Return relative path (used inside Extent Report)
		return "../screenshots/" + fileName;
	}
}