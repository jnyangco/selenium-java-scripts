package com.automation.pages.saucedemo;

import com.automation.base.BasePage;
import com.automation.core.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * Login Page for SauceDemo application
 * Contains locators and methods specific to the login functionality
 *
 * Example:
 *     LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
 *     loginPage.openSauceDemoWebsite();
 *     loginPage.login("standard_user", "secret_sauce");
 */
public class LoginPageSaucedemo extends BasePage {

    // Locators
    private final By usernameTextbox = By.id("user-name");
    private final By passwordTextbox = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By swagLabsLogo = By.className("app_logo");
    private final By errorMessage = By.xpath("//h3[@data-test='error']");
    private final By errorButton = By.className("error-button");

    public LoginPageSaucedemo(WebDriver driver) {
        super(driver);
    }

    /**
     * Open SauceDemo website
     */
    public void openSauceDemoWebsite() {
        String baseUrl = ConfigReader.getBaseUrl("saucedemo");
        logger.info("Opening SauceDemo website: {}", baseUrl);
        openUrl(baseUrl);
    }

    /**
     * Perform login with username and password
     * @param username Username
     * @param password Password
     */
    public void login(String username, String password) {
        logger.info("Performing login with username: {}", username);

        enterText(usernameTextbox, username);
        enterText(passwordTextbox, password);
        clickElement(loginButton);

        logger.info("Login attempt completed");
    }

    /**
     * Perform login with valid credentials from config
     */
    public void loginWithValidCredentials() {
        String username = ConfigReader.getUsername("saucedemo");
        String password = ConfigReader.getPassword("saucedemo");

        logger.info("Performing login with valid credentials");
        login(username, password);
    }

    /**
     * Enter username only
     * @param username Username to enter
     */
    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        enterText(usernameTextbox, username);
    }

    /**
     * Enter password only
     * @param password Password to enter
     */
    public void enterPassword(String password) {
        logger.info("Entering password");
        enterText(passwordTextbox, password);
    }

    /**
     * Click login button
     */
    public void clickLoginButton() {
        logger.info("Clicking login button");
        clickElement(loginButton);
    }

    /**
     * Clear username field
     */
    public void clearUsername() {
        logger.info("Clearing username field");
        findElement(usernameTextbox).clear();
    }

    /**
     * Clear password field
     */
    public void clearPassword() {
        logger.info("Clearing password field");
        findElement(passwordTextbox).clear();
    }

    /**
     * Verify user is successfully logged in by checking Swag Labs logo
     */
    public void verifySuccessfulLogin() {
        logger.info("Verifying successful login");

        try {
            boolean logoDisplayed = isElementDisplayed(swagLabsLogo);
            Assert.assertTrue(logoDisplayed, "Swag Labs logo should be displayed after successful login");
            logger.info("Login verification successful - Swag Labs logo is displayed");
        } catch (AssertionError e) {
            screenshotUtils.takeFailureScreenshot("login_verification_failed");
            logger.error("Login verification failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Verify login error message is displayed
     * @param expectedErrorMessage Expected error message text
     */
    public void verifyLoginErrorMessage(String expectedErrorMessage) {
        logger.info("Verifying login error message");

        try {
            String actualErrorMessage = getText(errorMessage);
            Assert.assertEquals(actualErrorMessage, expectedErrorMessage,
                    "Error message mismatch");
            logger.info("Error message verification successful: {}", actualErrorMessage);
        } catch (AssertionError e) {
            screenshotUtils.takeFailureScreenshot("error_message_verification_failed");
            logger.error("Error message verification failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Check if error message is displayed
     * @return true if error message is displayed, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        boolean displayed = isElementDisplayed(errorMessage);
        logger.info("Error message displayed: {}", displayed);
        return displayed;
    }

    /**
     * Get error message text
     * @return Error message text if displayed, null otherwise
     */
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            String message = getText(errorMessage);
            logger.info("Retrieved error message: {}", message);
            return message;
        }
        logger.info("No error message displayed");
        return null;
    }

    /**
     * Close error message by clicking X button
     */
    public void closeErrorMessage() {
        if (isElementDisplayed(errorButton)) {
            logger.info("Closing error message");
            clickElement(errorButton);
        }
    }

    /**
     * Check if login button is enabled
     * @return true if login button is enabled, false otherwise
     */
    public boolean isLoginButtonEnabled() {
        boolean enabled = isElementEnabled(loginButton);
        logger.info("Login button enabled: {}", enabled);
        return enabled;
    }

    /**
     * Check if username field is displayed
     * @return true if username field is displayed, false otherwise
     */
    public boolean isUsernameFieldDisplayed() {
        boolean displayed = isElementDisplayed(usernameTextbox);
        logger.info("Username field displayed: {}", displayed);
        return displayed;
    }

    /**
     * Check if password field is displayed
     * @return true if password field is displayed, false otherwise
     */
    public boolean isPasswordFieldDisplayed() {
        boolean displayed = isElementDisplayed(passwordTextbox);
        logger.info("Password field displayed: {}", displayed);
        return displayed;
    }

    /**
     * Get username field placeholder text
     * @return Placeholder text
     */
    public String getUsernamePlaceholder() {
        String placeholder = getElementAttribute(usernameTextbox, "placeholder");
        logger.info("Username placeholder: {}", placeholder);
        return placeholder;
    }

    /**
     * Get password field placeholder text
     * @return Placeholder text
     */
    public String getPasswordPlaceholder() {
        String placeholder = getElementAttribute(passwordTextbox, "placeholder");
        logger.info("Password placeholder: {}", placeholder);
        return placeholder;
    }

    /**
     * Verify login page is loaded
     */
    public void verifyLoginPageLoaded() {
        logger.info("Verifying login page is loaded");

        Assert.assertTrue(isUsernameFieldDisplayed(), "Username field should be displayed");
        Assert.assertTrue(isPasswordFieldDisplayed(), "Password field should be displayed");
        Assert.assertTrue(isLoginButtonEnabled(), "Login button should be enabled");

        logger.info("Login page verification successful");
    }

    /**
     * Get current page title
     * @return Page title
     */
    public String getLoginPageTitle() {
        String title = getPageTitle();
        logger.info("Login page title: {}", title);
        return title;
    }
}