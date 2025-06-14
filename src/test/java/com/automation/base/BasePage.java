package com.automation.base;

import com.automation.utils.LoggerUtils;
import com.automation.utils.ScreenshotUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

/**
 * Base Page class implementation
 * Contains common methods used across all page classes
 *
 * This class should be inherited by all page classes
 * Do not create object instances of this class directly
 *
 * Example:
 *     public class LoginPage extends BasePage {
 *         public LoginPage(WebDriver driver) {
 *             super(driver);
 *         }
 *     }
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected Logger logger;
    protected ScreenshotUtils screenshotUtils;

    private static final int DEFAULT_TIMEOUT = 10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.actions = new Actions(driver);
        this.logger = LoggerUtils.getLogger(this.getClass());
        this.screenshotUtils = new ScreenshotUtils(driver);
    }

    /**
     * Navigate to specified URL
     * @param url URL to navigate to
     */
    public void openUrl(String url) {
        logger.info("Opening URL: {}", url);
        driver.get(url);
    }

    /**
     * Get page title
     * @return Current page title
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.info("Page title: {}", title);
        return title;
    }

    /**
     * Get current URL
     * @return Current page URL
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.info("Current URL: {}", url);
        return url;
    }

    /**
     * Find web element with explicit wait
     * @param locator By locator
     * @param timeout Timeout in seconds
     * @return WebElement if found
     */
    public WebElement findElement(By locator, int timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement element = customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Found element: {}", locator);
            return element;
        } catch (TimeoutException e) {
            screenshotUtils.takeScreenshot("element_not_found");
            logger.error("Element not found: {}", locator);
            Assert.fail("Element not found: " + locator);
            return null;
        }
    }

    /**
     * Find web element with default timeout
     * @param locator By locator
     * @return WebElement if found
     */
    public WebElement findElement(By locator) {
        return findElement(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Find multiple web elements
     * @param locator By locator
     * @param timeout Timeout in seconds
     * @return List of WebElements
     */
    public List<WebElement> findElements(By locator, int timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            List<WebElement> elements = customWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            logger.info("Found {} elements: {}", elements.size(), locator);
            return elements;
        } catch (TimeoutException e) {
            logger.error("Elements not found: {}", locator);
            screenshotUtils.takeScreenshot("elements_not_found");
            return List.of(); // Return empty list instead of null
        }
    }

    /**
     * Find multiple web elements with default timeout
     * @param locator By locator
     * @return List of WebElements
     */
    public List<WebElement> findElements(By locator) {
        return findElements(locator, DEFAULT_TIMEOUT);
    }

    /**
     * Click an element
     * @param locator By locator
     */
    public void clickElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Clicking element: {}", locator);
            element.click();
        } catch (TimeoutException e) {
            screenshotUtils.takeScreenshot("element_not_clickable");
            logger.error("Element not clickable: {}", locator);
            Assert.fail("Element not clickable: " + locator);
        }
    }

    /**
     * Click element using JavaScript
     * @param locator By locator
     */
    public void clickElementWithJS(By locator) {
        try {
            WebElement element = findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logger.info("Clicked element using JavaScript: {}", locator);
        } catch (Exception e) {
            screenshotUtils.takeScreenshot("js_click_failed");
            logger.error("JavaScript click failed: {}", locator);
            Assert.fail("JavaScript click failed: " + locator);
        }
    }

    /**
     * Enter text into an element after clearing it
     * @param locator By locator
     * @param text Text to enter
     */
    public void enterText(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
        logger.info("Entered text '{}' into element: {}", text, locator);
    }

    /**
     * Get text from an element
     * @param locator By locator
     * @return Element text
     */
    public String getText(By locator) {
        WebElement element = findElement(locator);
        String text = element.getText().trim();
        logger.info("Got text '{}' from element: {}", text, locator);
        return text;
    }

    /**
     * Check if element is displayed
     * @param locator By locator
     * @return true if element is displayed, false otherwise
     */
    public boolean isElementDisplayed(By locator) {
        try {
            WebElement element = findElement(locator, 5); // Shorter timeout for display check
            boolean isDisplayed = element.isDisplayed();
            logger.info("Element {} is {}", locator, isDisplayed ? "displayed" : "not displayed");
            return isDisplayed;
        } catch (Exception e) {
            logger.info("Element {} is not displayed", locator);
            return false;
        }
    }

    /**
     * Check if element is enabled
     * @param locator By locator
     * @return true if element is enabled, false otherwise
     */
    public boolean isElementEnabled(By locator) {
        try {
            WebElement element = findElement(locator);
            boolean isEnabled = element.isEnabled();
            logger.info("Element {} is {}", locator, isEnabled ? "enabled" : "disabled");
            return isEnabled;
        } catch (Exception e) {
            logger.info("Element {} is not enabled", locator);
            return false;
        }
    }

    /**
     * Wait for element to be visible
     * @param locator By locator
     * @param timeout Timeout in seconds
     * @return WebElement when visible
     */
    public WebElement waitForElementVisible(By locator, int timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement element = customWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Element not visible: {}", locator);
            screenshotUtils.takeScreenshot("element_not_visible");
            Assert.fail("Element not visible within " + timeout + " seconds: " + locator);
            return null;
        }
    }

    /**
     * Wait for element to be clickable
     * @param locator By locator
     * @param timeout Timeout in seconds
     * @return WebElement when clickable
     */
    public WebElement waitForElementClickable(By locator, int timeout) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            WebElement element = customWait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element is clickable: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Element not clickable: {}", locator);
            screenshotUtils.takeScreenshot("element_not_clickable");
            Assert.fail("Element not clickable within " + timeout + " seconds: " + locator);
            return null;
        }
    }

    /**
     * Hover over an element
     * @param locator By locator
     */
    public void hoverOverElement(By locator) {
        WebElement element = findElement(locator);
        actions.moveToElement(element).perform();
        logger.info("Hovered over element: {}", locator);
    }

    /**
     * Scroll to element
     * @param locator By locator
     */
    public void scrollToElement(By locator) {
        WebElement element = findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        logger.info("Scrolled to element: {}", locator);
    }

    /**
     * Get element attribute value
     * @param locator By locator
     * @param attributeName Attribute name
     * @return Attribute value
     */
    public String getElementAttribute(By locator, String attributeName) {
        WebElement element = findElement(locator);
        String attributeValue = element.getAttribute(attributeName);
        logger.info("Got attribute '{}' value '{}' from element: {}", attributeName, attributeValue, locator);
        return attributeValue;
    }

    /**
     * Assert element text matches expected text
     * @param locator By locator
     * @param expectedText Expected text
     * @param assertMessage Custom assertion message
     */
    public void assertElementTextMatches(By locator, String expectedText, String assertMessage) {
        String actualText = getText(locator);
        if (!actualText.equals(expectedText)) {
            screenshotUtils.takeScreenshot("text_assertion_failed");
            logger.error("{}: Expected = '{}', Actual = '{}'", assertMessage, expectedText, actualText);
        }
        Assert.assertEquals(actualText, expectedText, assertMessage + ": Expected = '" + expectedText + "', Actual = '" + actualText + "'");
    }

    /**
     * Assert element text contains expected text
     * @param locator By locator
     * @param expectedText Expected text to be contained
     * @param assertMessage Custom assertion message
     */
    public void assertElementTextContains(By locator, String expectedText, String assertMessage) {
        String actualText = getText(locator);
        if (!actualText.contains(expectedText)) {
            screenshotUtils.takeScreenshot("text_contains_assertion_failed");
            logger.error("{}: Expected text '{}' not found in actual text '{}'", assertMessage, expectedText, actualText);
        }
        Assert.assertTrue(actualText.contains(expectedText),
                assertMessage + ": Expected text '" + expectedText + "' not found in actual text '" + actualText + "'");
    }

    /**
     * Wait for specified seconds
     * @param seconds Seconds to wait
     */
    public void waitForSeconds(int seconds) {
        try {
            logger.info("Waiting for {} seconds...", seconds);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Wait interrupted: {}", e.getMessage());
        }
    }

    /**
     * Refresh the current page
     */
    public void refreshPage() {
        logger.info("Refreshing the page");
        driver.navigate().refresh();
    }

    /**
     * Navigate back
     */
    public void navigateBack() {
        logger.info("Navigating back");
        driver.navigate().back();
    }

    /**
     * Navigate forward
     */
    public void navigateForward() {
        logger.info("Navigating forward");
        driver.navigate().forward();
    }
}