package com.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logger utility class for centralized logging configuration
 * Uses Log4j2 for logging functionality
 *
 * Example:
 *     Logger logger = LoggerUtils.getLogger(LoginPage.class);
 *     logger.info("Login successful");
 */
public class LoggerUtils {

    /**
     * Get logger instance for a specific class
     * @param clazz Class for which logger is needed
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }

    /**
     * Get logger instance with custom name
     * @param name Logger name
     * @return Logger instance
     */
    public static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }

    /**
     * Log test step information
     * @param logger Logger instance
     * @param stepDescription Step description
     */
    public static void logStep(Logger logger, String stepDescription) {
        logger.info("STEP: {}", stepDescription);
    }

    /**
     * Log test verification
     * @param logger Logger instance
     * @param verificationDescription Verification description
     */
    public static void logVerification(Logger logger, String verificationDescription) {
        logger.info("VERIFICATION: {}", verificationDescription);
    }

    /**
     * Log test action
     * @param logger Logger instance
     * @param actionDescription Action description
     */
    public static void logAction(Logger logger, String actionDescription) {
        logger.info("ACTION: {}", actionDescription);
    }

    /**
     * Log error with exception
     * @param logger Logger instance
     * @param message Error message
     * @param throwable Exception
     */
    public static void logError(Logger logger, String message, Throwable throwable) {
        logger.error("ERROR: {} - Exception: {}", message, throwable.getMessage());
        logger.debug("Stack trace:", throwable);
    }

    /**
     * Log warning message
     * @param logger Logger instance
     * @param message Warning message
     */
    public static void logWarning(Logger logger, String message) {
        logger.warn("WARNING: {}", message);
    }

    /**
     * Log debug information
     * @param logger Logger instance
     * @param message Debug message
     */
    public static void logDebug(Logger logger, String message) {
        logger.debug("DEBUG: {}", message);
    }

    /**
     * Log test start
     * @param logger Logger instance
     * @param testName Test name
     */
    public static void logTestStart(Logger logger, String testName) {
        logger.info("========== STARTING TEST: {} ==========", testName);
    }

    /**
     * Log test end
     * @param logger Logger instance
     * @param testName Test name
     * @param result Test result (PASS/FAIL/SKIP)
     */
    public static void logTestEnd(Logger logger, String testName, String result) {
        logger.info("========== COMPLETED TEST: {} - RESULT: {} ==========", testName, result);
    }

    /**
     * Log page navigation
     * @param logger Logger instance
     * @param pageName Page name
     * @param url URL navigated to
     */
    public static void logPageNavigation(Logger logger, String pageName, String url) {
        logger.info("NAVIGATION: Navigated to {} page - URL: {}", pageName, url);
    }

    /**
     * Log element interaction
     * @param logger Logger instance
     * @param action Action performed
     * @param element Element description
     */
    public static void logElementInteraction(Logger logger, String action, String element) {
        logger.info("INTERACTION: {} on element: {}", action, element);
    }

    /**
     * Log assertion result
     * @param logger Logger instance
     * @param assertion Assertion description
     * @param result Result (true/false)
     */
    public static void logAssertion(Logger logger, String assertion, boolean result) {
        if (result) {
            logger.info("ASSERTION PASSED: {}", assertion);
        } else {
            logger.error("ASSERTION FAILED: {}", assertion);
        }
    }

    /**
     * Log data generation
     * @param logger Logger instance
     * @param dataType Type of data generated
     * @param value Generated value
     */
    public static void logDataGeneration(Logger logger, String dataType, String value) {
        logger.info("DATA GENERATED: {} = {}", dataType, value);
    }

    /**
     * Log API call information
     * @param logger Logger instance
     * @param method HTTP method
     * @param endpoint API endpoint
     * @param statusCode Response status code
     */
    public static void logApiCall(Logger logger, String method, String endpoint, int statusCode) {
        logger.info("API CALL: {} {} - Status: {}", method, endpoint, statusCode);
    }

    /**
     * Log database operation
     * @param logger Logger instance
     * @param operation Database operation
     * @param table Table name
     * @param result Operation result
     */
    public static void logDatabaseOperation(Logger logger, String operation, String table, String result) {
        logger.info("DATABASE: {} on table '{}' - Result: {}", operation, table, result);
    }
}