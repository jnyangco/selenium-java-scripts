package com.automation.listeners;

import com.automation.core.DriverFactory;
import com.automation.reporting.ExtentReportManager;
import com.automation.utils.LoggerUtils;
import com.automation.utils.ScreenshotUtils;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG Listener for handling test events
 * Integrates with ExtentReports and logging
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LoggerUtils.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();

        logger.info("========== STARTING TEST: {} in {} ==========", testName, className);

        // Create ExtentTest for the current test
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = "Test: " + testName;
        }

        ExtentReportManager.createTest(testName, description);

        // Add test categories
        String[] groups = result.getMethod().getGroups();
        for (String group : groups) {
            ExtentReportManager.addCategory(group);
        }

        ExtentReportManager.logInfo("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        long duration = result.getEndMillis() - result.getStartMillis();

        logger.info("TEST PASSED: {} (Duration: {}ms)", testName, duration);

        ExtentReportManager.logPass("Test completed successfully");
        ExtentReportManager.logInfo("Execution time: " + duration + "ms");

        // Clean up ExtentTest from ThreadLocal
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable().getMessage();
        long duration = result.getEndMillis() - result.getStartMillis();

        logger.error("TEST FAILED: {} (Duration: {}ms)", testName, duration);
        logger.error("Failure reason: {}", errorMessage);

        // Take screenshot on failure
        try {
            if (DriverFactory.getDriver() != null) {
                ScreenshotUtils screenshotUtils = new ScreenshotUtils(DriverFactory.getDriver());
                String screenshotPath = screenshotUtils.takeFailureScreenshot(testName);

                if (screenshotPath != null) {
                    ExtentReportManager.addScreenshot(screenshotPath, "Failure Screenshot");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
        }

        ExtentReportManager.logFail("Test failed: " + errorMessage);
        ExtentReportManager.logInfo("Execution time: " + duration + "ms");

        // Log stack trace
        logger.debug("Stack trace:", result.getThrowable());

        // Clean up ExtentTest from ThreadLocal
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ?
                result.getThrowable().getMessage() : "Unknown reason";

        logger.warn("TEST SKIPPED: {} - Reason: {}", testName, skipReason);

        ExtentReportManager.logSkip("Test skipped: " + skipReason);

        // Clean up ExtentTest from ThreadLocal
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.warn("TEST FAILED BUT WITHIN SUCCESS PERCENTAGE: {}", testName);

        ExtentReportManager.logWarning("Test failed but within success percentage");

        // Clean up ExtentTest from ThreadLocal
        ExtentReportManager.removeTest();
    }
}