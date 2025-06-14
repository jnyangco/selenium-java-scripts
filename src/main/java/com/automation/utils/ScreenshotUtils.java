package com.automation.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.automation.reporting.ExtentReportManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screenshot utility class for capturing and managing screenshots
 * Used for test evidence and failure analysis
 *
 * Example:
 *     ScreenshotUtils screenshotUtils = new ScreenshotUtils(driver);
 *     String screenshotPath = screenshotUtils.takeScreenshot("login_page");
 */
public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private final WebDriver driver;
    private static final String SCREENSHOTS_DIR = "reports/screenshots";

    public ScreenshotUtils(WebDriver driver) {
        this.driver = driver;
        createScreenshotsDirectory();
    }

    /**
     * Create screenshots directory if it doesn't exist
     */
    private void createScreenshotsDirectory() {
        File screenshotsDir = new File(SCREENSHOTS_DIR);
        if (!screenshotsDir.exists()) {
            boolean created = screenshotsDir.mkdirs();
            if (created) {
                logger.info("Created screenshots directory: {}", SCREENSHOTS_DIR);
            } else {
                logger.warn("Failed to create screenshots directory: {}", SCREENSHOTS_DIR);
            }
        }
    }

    /**
     * Take screenshot with custom name
     * @param screenshotName Name for the screenshot file
     * @return Full path to the screenshot file
     */
    public String takeScreenshot(String screenshotName) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String fullPath = SCREENSHOTS_DIR + File.separator + fileName;

        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File(fullPath);

            FileUtils.copyFile(sourceFile, destFile);
            logger.info("Screenshot captured: {}", fullPath);

            return fullPath;

        } catch (IOException e) {
            logger.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Take screenshot with default naming
     * @return Full path to the screenshot file
     */
    public String takeScreenshot() {
        return takeScreenshot("screenshot");
    }

    /**
     * Take screenshot for test failure
     * @param testName Name of the failed test
     * @return Full path to the screenshot file
     */
    public String takeFailureScreenshot(String testName) {
        return takeScreenshot("FAILURE_" + testName);
    }

    /**
     * Take screenshot for test step
     * @param stepName Name of the test step
     * @return Full path to the screenshot file
     */
    public String takeStepScreenshot(String stepName) {
        return takeScreenshot("STEP_" + stepName);
    }

    /**
     * Take screenshot with thread information (for parallel execution)
     * @param screenshotName Name for the screenshot file
     * @return Full path to the screenshot file
     */
    public String takeScreenshotWithThread(String screenshotName) {
        String threadName = Thread.currentThread().getName();
        return takeScreenshot(screenshotName + "_" + threadName);
    }

    /**
     * Get screenshot as Base64 string (useful for embedding in reports)
     * @return Base64 encoded screenshot string
     */
    public String getScreenshotAsBase64() {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            String base64Screenshot = takesScreenshot.getScreenshotAs(OutputType.BASE64);
            logger.debug("Screenshot captured as Base64 string");
            return base64Screenshot;
        } catch (Exception e) {
            logger.error("Failed to capture screenshot as Base64: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Take screenshot and add to ExtentReports
     * @param screenshotName Name for the screenshot
     * @param description Description for the screenshot
     */
    public void takeScreenshotForExtentReport(String screenshotName, String description) {
        String screenshotPath = takeScreenshot(screenshotName);
        if (screenshotPath != null) {
            ExtentReportManager.addScreenshot(screenshotPath, description);
        }
    }

    /**
     * Take screenshot on test failure and add to ExtentReports
     * @param testName Name of the failed test
     */
    public void takeFailureScreenshotForExtentReport(String testName) {
        String screenshotPath = takeFailureScreenshot(testName);
        if (screenshotPath != null) {
            ExtentReportManager.addScreenshot(screenshotPath, "Test Failure Screenshot");
        }
    }

    /**
     * Clean up old screenshots (keep only recent ones)
     * @param daysToKeep Number of days to keep screenshots
     */
    public static void cleanupOldScreenshots(int daysToKeep) {
        File screenshotsDir = new File(SCREENSHOTS_DIR);
        if (!screenshotsDir.exists()) {
            return;
        }

        long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60L * 60L * 1000L);
        File[] files = screenshotsDir.listFiles();

        if (files != null) {
            int deletedCount = 0;
            for (File file : files) {
                if (file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        deletedCount++;
                    }
                }
            }
            LogManager.getLogger(ScreenshotUtils.class).info("Cleaned up {} old screenshots", deletedCount);
        }
    }

    /**
     * Get the screenshots directory path
     * @return Screenshots directory path
     */
    public static String getScreenshotsDirectory() {
        return SCREENSHOTS_DIR;
    }

    /**
     * Check if driver supports screenshot capability
     * @return true if driver can take screenshots, false otherwise
     */
    public boolean canTakeScreenshot() {
        return driver instanceof TakesScreenshot;
    }
}