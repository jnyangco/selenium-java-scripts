package com.automation.base;

import com.automation.core.DriverFactory;
import com.automation.reporting.ExtentReportManager;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScreenshotUtils;
import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * Base Test class implementation
 * Contains common setup and teardown methods for all test classes
 *
 * This class should be inherited by all test classes
 * Do not create object instances of this class directly
 *
 * Example:
 *     public class LoginTest extends BaseTest {
 *         @Test
 *         public void testValidLogin() {
 *             // Test implementation
 *         }
 *     }
 */
public class BaseTest {

    protected WebDriver driver;
    protected Logger logger;
    protected ScreenshotUtils screenshotUtils;
    protected ExtentTest extentTest;

    @BeforeSuite
    public void beforeSuite() {
        logger = LoggerUtils.getLogger(this.getClass());
        logger.info("===== Starting Test Suite =====");
        ExtentReportManager.initializeReport();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        logger.info("===== Starting Test: {} =====", method.getName());

        // Create ExtentTest instance for each test method
        extentTest = ExtentReportManager.createTest(method.getName(),
                getTestDescription(method));

        // Create WebDriver instance
        driver = DriverFactory.createDriver();
        screenshotUtils = new ScreenshotUtils(driver);

        logger.info("Driver initialized for test: {}", method.getName());
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        if (result.getStatus() == ITestResult.FAILURE) {
            logger.error("Test failed: {}", testName);
            logger.error("Failure reason: {}", result.getThrowable().getMessage());

            // Take screenshot on failure
            String screenshotPath = screenshotUtils.takeScreenshot("test_failure_" + testName);

            // Add failure information to ExtentReport
            extentTest.fail("Test failed: " + result.getThrowable().getMessage());
            extentTest.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.info("Test passed: {}", testName);
            extentTest.pass("Test completed successfully");

        } else if (result.getStatus() == ITestResult.SKIP) {
            logger.info("Test skipped: {}", testName);
            extentTest.skip("Test was skipped: " + result.getThrowable().getMessage());
        }

        // Quit driver
        DriverFactory.quitDriver();
        logger.info("===== Completed Test: {} =====", testName);
    }

    @AfterSuite
    public void afterSuite() {
        logger.info("===== Test Suite Completed =====");
        ExtentReportManager.flushReport();
    }

    /**
     * Get test description from Test annotation or method name
     */
    private String getTestDescription(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        if (testAnnotation != null && !testAnnotation.description().isEmpty()) {
            return testAnnotation.description();
        }
        return "Test: " + method.getName();
    }

    /**
     * Log test step
     */
    protected void logStep(String stepDescription) {
        logger.info("Step: {}", stepDescription);
        extentTest.info("Step: " + stepDescription);
    }

    /**
     * Log test information
     */
    protected void logInfo(String message) {
        logger.info(message);
        extentTest.info(message);
    }

    /**
     * Log test warning
     */
    protected void logWarning(String message) {
        logger.warn(message);
        extentTest.warning(message);
    }

    /**
     * Log test error
     */
    protected void logError(String message) {
        logger.error(message);
        extentTest.fail(message);
    }
}