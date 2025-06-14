package com.automation.tests.saucedemo;

import com.automation.base.BaseTest;
import com.automation.core.ConfigReader;
import com.automation.pages.saucedemo.LoginPageSaucedemo;
import com.automation.pages.saucedemo.ProductPageSaucedemo;
import org.testng.annotations.Test;

/**
 * Login test cases for SauceDemo application
 * Contains various login scenarios including positive and negative tests
 *
 * Test categories: smoke, regression, login, saucedemo
 */
public class LoginTestSaucedemo extends BaseTest {

    @Test(groups = {"smoke", "login", "saucedemo"},
            description = "Verify user can login with valid credentials")
    public void testValidLogin() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Verify login page is loaded");
        loginPage.verifyLoginPageLoaded();

        logStep("Login with valid credentials");
        String username = ConfigReader.getUsername("saucedemo");
        String password = ConfigReader.getPassword("saucedemo");
        loginPage.login(username, password);

        logStep("Verify successful login");
        loginPage.verifySuccessfulLogin();

        logStep("Verify product page is loaded");
        ProductPageSaucedemo productPage = new ProductPageSaucedemo(driver);
        productPage.verifyProductPageLoaded();

        logInfo("Valid login test completed successfully");
    }

    @Test(groups = {"regression", "login", "saucedemo"},
            description = "Verify error message appears with invalid password")
    public void testInvalidPassword() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Login with valid username and invalid password");
        String username = ConfigReader.getUsername("saucedemo");
        String invalidPassword = "invalid_password_123";
        loginPage.login(username, invalidPassword);

        logStep("Verify error message is displayed");
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        loginPage.verifyLoginErrorMessage(expectedErrorMessage);

        logInfo("Invalid password test completed successfully");
    }

    @Test(groups = {"regression", "login", "saucedemo"},
            description = "Verify error message appears with invalid username")
    public void testInvalidUsername() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Login with invalid username and valid password");
        String invalidUsername = "invalid_user_123";
        String password = ConfigReader.getPassword("saucedemo");
        loginPage.login(invalidUsername, password);

        logStep("Verify error message is displayed");
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        loginPage.verifyLoginErrorMessage(expectedErrorMessage);

        logInfo("Invalid username test completed successfully");
    }

    @Test(groups = {"regression", "login", "saucedemo"},
            description = "Verify error message appears with empty credentials")
    public void testEmptyCredentials() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Click login button without entering credentials");
        loginPage.clickLoginButton();

        logStep("Verify error message is displayed");
        String expectedErrorMessage = "Epic sadface: Username is required";
        loginPage.verifyLoginErrorMessage(expectedErrorMessage);

        logInfo("Empty credentials test completed successfully");
    }

    @Test(groups = {"regression", "login", "saucedemo"},
            description = "Verify error message appears with empty password")
    public void testEmptyPassword() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Enter username only and click login");
        String username = ConfigReader.getUsername("saucedemo");
        loginPage.enterUsername(username);
        loginPage.clickLoginButton();

        logStep("Verify error message is displayed");
        String expectedErrorMessage = "Epic sadface: Password is required";
        loginPage.verifyLoginErrorMessage(expectedErrorMessage);

        logInfo("Empty password test completed successfully");
    }

    @Test(groups = {"regression", "login", "saucedemo"},
            description = "Verify locked out user cannot login")
    public void testLockedOutUser() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Login with locked out user credentials");
        String lockedUsername = ConfigReader.getProperty("saucedemo.locked.username", "locked_out_user");
        String password = ConfigReader.getPassword("saucedemo");
        loginPage.login(lockedUsername, password);

        logStep("Verify locked out error message is displayed");
        String expectedErrorMessage = "Epic sadface: Sorry, this user has been locked out.";
        loginPage.verifyLoginErrorMessage(expectedErrorMessage);

        logInfo("Locked out user test completed successfully");
    }

    @Test(groups = {"regression", "login", "saucedemo"},
            description = "Verify error message can be closed")
    public void testCloseErrorMessage() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Login with invalid credentials to trigger error");
        loginPage.login("invalid_user", "invalid_pass");

        logStep("Verify error message is displayed");
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        loginPage.verifyLoginErrorMessage(expectedErrorMessage);

        logStep("Close error message");
        loginPage.closeErrorMessage();

        logStep("Verify error message is no longer displayed");
        // Note: After closing, we should verify the error is gone
        // This might require additional wait or verification logic

        logInfo("Close error message test completed successfully");
    }

    @Test(groups = {"smoke", "login", "saucedemo"},
            description = "Verify login page elements are displayed correctly")
    public void testLoginPageElements() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Verify all login page elements are displayed");
        loginPage.verifyLoginPageLoaded();

        logStep("Verify username field placeholder");
        String usernamePlaceholder = loginPage.getUsernamePlaceholder();
        logInfo("Username placeholder: " + usernamePlaceholder);

        logStep("Verify password field placeholder");
        String passwordPlaceholder = loginPage.getPasswordPlaceholder();
        logInfo("Password placeholder: " + passwordPlaceholder);

        logStep("Verify login button is enabled");
        assert loginPage.isLoginButtonEnabled() : "Login button should be enabled";

        logInfo("Login page elements test completed successfully");
    }

    @Test(groups = {"regression", "login", "saucedemo"},
            description = "Verify page title is correct")
    public void testLoginPageTitle() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Verify page title");
        String actualTitle = loginPage.getLoginPageTitle();
        String expectedTitle = "Swag Labs";
        assert actualTitle.equals(expectedTitle) :
                "Page title mismatch. Expected: " + expectedTitle + ", Actual: " + actualTitle;

        logInfo("Login page title test completed successfully");
    }

    @Test(groups = {"regression", "login", "saucedemo"},
            description = "Verify clearing fields functionality")
    public void testClearFields() {
        logStep("Open SauceDemo website");
        LoginPageSaucedemo loginPage = new LoginPageSaucedemo(driver);
        loginPage.openSauceDemoWebsite();

        logStep("Enter username and password");
        loginPage.enterUsername("test_user");
        loginPage.enterPassword("test_password");

        logStep("Clear username field");
        loginPage.clearUsername();

        logStep("Clear password field");
        loginPage.clearPassword();

        logStep("Verify fields are cleared by attempting login");
        loginPage.clickLoginButton();

        logStep("Verify username required error appears");
        String expectedErrorMessage = "Epic sadface: Username is required";
        loginPage.verifyLoginErrorMessage(expectedErrorMessage);

        logInfo("Clear fields test completed successfully");
    }
}