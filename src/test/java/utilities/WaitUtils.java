package utilities;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * WaitUtils provides reusable explicit wait utility methods for the OpenCart automation framework.
 * It encapsulates Selenium's WebDriverWait and ExpectedConditions to handle synchronization,
 * dynamic elements, and asynchronous AJAX operations safely.
 */
public class WaitUtils {

    private WebDriver driver;
    private WebDriverWait wait;
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    public WaitUtils(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    /**
     * Waits until the specified WebElement becomes visible on the page.
     */
    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the specified WebElement becomes visible within custom timeout.
     */
    public WebElement waitForVisibility(WebElement element, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the specified locator becomes visible on the page.
     */
    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits until the specified WebElement is clickable (visible and enabled).
     */
    public WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the specified WebElement is clickable within custom timeout.
     */
    public WebElement waitForClickability(WebElement element, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the specified WebElement disappears or becomes invisible.
     */
    public boolean waitForInvisibility(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Waits until the specified locator becomes invisible.
     */
    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits until the specified text is present inside the given WebElement.
     */
    public boolean waitForTextToBePresent(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    /**
     * Waits until a JavaScript alert popup is present.
     */
    public Alert waitForAlertPresent() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Waits until the current URL contains the expected fraction.
     */
    public boolean waitForUrlContains(String fraction) {
        return wait.until(ExpectedConditions.urlContains(fraction));
    }
}
