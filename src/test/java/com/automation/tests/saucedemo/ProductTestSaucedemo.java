package com.automation.tests.saucedemo;

import com.automation.base.BaseTest;
import com.automation.pages.saucedemo.LoginPageSaucedemo;
import com.automation.pages.saucedemo.ProductPageSaucedemo;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Product test cases for SauceDemo application
 * Contains tests for product listing, sorting, filtering, and cart functionality
 *
 * Test categories: smoke, regression, product, saucedemo, filter, ui
 */
public class ProductTestSaucedemo extends BaseTest {

    private LoginPageSaucedemo loginPage;
    private ProductPageSaucedemo productPage;

    @BeforeMethod(groups = {"smoke", "regression", "product", "saucedemo"})
    public void loginToApplication() {
        logStep("Setup: Login to SauceDemo application");
        loginPage = new LoginPageSaucedemo(driver);
        productPage = new ProductPageSaucedemo(driver);

        loginPage.openSauceDemoWebsite();
        loginPage.loginWithValidCredentials();
        loginPage.verifySuccessfulLogin();
        productPage.verifyProductPageLoaded();

        logInfo("Setup completed - User logged in successfully");
    }

    @Test(groups = {"smoke", "product", "saucedemo"},
            description = "Verify all products are displayed on the product page")
    public void testProductsDisplayed() {
        logStep("Verify total number of products displayed");
        productPage.verifyTotalProductCount(6);

        logStep("Get all product names");
        List<String> productNames = productPage.getAllProductNames();
        logInfo("Products displayed: " + productNames);

        logStep("Verify expected products are present");
        List<String> expectedProducts = Arrays.asList(
                "Sauce Labs Backpack",
                "Sauce Labs Bike Light",
                "Sauce Labs Bolt T-Shirt",
                "Sauce Labs Fleece Jacket",
                "Sauce Labs Onesie",
                "Test.allTheThings() T-Shirt (Red)"
        );

        for (String expectedProduct : expectedProducts) {
            assert productNames.contains(expectedProduct) :
                    "Expected product not found: " + expectedProduct;
        }

        logInfo("Product display test completed successfully");
    }

    @Test(groups = {"regression", "product", "saucedemo"},
            description = "Verify product prices are displayed correctly")
    public void testProductPrices() {
        logStep("Get all product prices");
        List<String> productPrices = productPage.getAllProductPrices();
        logInfo("Product prices: " + productPrices);

        logStep("Verify all prices are numeric and greater than 0");
        for (String price : productPrices) {
            try {
                double priceValue = Double.parseDouble(price);
                assert priceValue > 0 : "Price should be greater than 0: " + price;
            } catch (NumberFormatException e) {
                assert false : "Invalid price format: " + price;
            }
        }

        logStep("Verify specific product price");
        String backpackPrice = productPage.getProductPrice("Sauce Labs Backpack");
        assert backpackPrice.equals("29.99") :
                "Backpack price mismatch. Expected: 29.99, Actual: " + backpackPrice;

        logInfo("Product prices test completed successfully");
    }

    @Test(groups = {"smoke", "product", "saucedemo"},
            description = "Verify adding single product to cart")
    public void testAddSingleProductToCart() {
        String productName = "Sauce Labs Backpack";

        logStep("Verify product is available for purchase");
        productPage.verifyProductAvailable(productName);

        logStep("Add product to cart: " + productName);
        productPage.addProductToCart(productName);

        logStep("Verify product is added to cart");
        productPage.verifyProductAddedToCart(productName);

        logStep("Verify cart item count");
        productPage.verifyCartItemCount(1);

        logInfo("Single product to cart test completed successfully");
    }

    @Test(groups = {"regression", "product", "saucedemo"},
            description = "Verify adding multiple products to cart")
    public void testAddMultipleProductsToCart() {
        List<String> productsToAdd = Arrays.asList(
                "Sauce Labs Backpack",
                "Sauce Labs Bike Light",
                "Sauce Labs Bolt T-Shirt"
        );

        logStep("Add multiple products to cart");
        productPage.addMultipleProductsToCart(productsToAdd);

        logStep("Verify all products are added to cart");
        for (String product : productsToAdd) {
            productPage.verifyProductAddedToCart(product);
        }

        logStep("Verify cart item count");
        productPage.verifyCartItemCount(productsToAdd.size());

        logInfo("Multiple products to cart test completed successfully");
    }

    @Test(groups = {"regression", "product", "saucedemo"},
            description = "Verify removing product from cart")
    public void testRemoveProductFromCart() {
        String productName = "Sauce Labs Backpack";

        logStep("Add product to cart first");
        productPage.addProductToCart(productName);
        productPage.verifyProductAddedToCart(productName);

        logStep("Remove product from cart");
        productPage.removeProductFromCart(productName);

        logStep("Verify product is removed from cart");
        productPage.verifyProductAvailable(productName);

        logStep("Verify cart item count is 0");
        productPage.verifyCartItemCount(0);

        logInfo("Remove product from cart test completed successfully");
    }

    @Test(groups = {"smoke", "filter", "saucedemo"},
            description = "Verify sorting products by name (A to Z)")
    public void testSortProductsByNameAscending() {
        logStep("Select sort option: Name (A to Z)");
        productPage.selectSortOption("Name (A to Z)");

        logStep("Verify products are sorted by name in ascending order");
        productPage.verifyProductsSortedByNameAscending();

        logStep("Verify current sort option is selected");
        String currentSort = productPage.getCurrentSortOption();
        assert currentSort.equals("Name (A to Z)") :
                "Sort option mismatch. Expected: Name (A to Z), Actual: " + currentSort;

        logInfo("Sort by name ascending test completed successfully");
    }

    @Test(groups = {"regression", "filter", "saucedemo"},
            description = "Verify sorting products by name (Z to A)")
    public void testSortProductsByNameDescending() {
        logStep("Select sort option: Name (Z to A)");
        productPage.selectSortOption("Name (Z to A)");

        logStep("Verify products are sorted by name in descending order");
        productPage.verifyProductsSortedByNameDescending();

        logInfo("Sort by name descending test completed successfully");
    }

    @Test(groups = {"regression", "filter", "saucedemo"},
            description = "Verify sorting products by price (low to high)")
    public void testSortProductsByPriceLowToHigh() {
        logStep("Select sort option: Price (low to high)");
        productPage.selectSortOption("Price (low to high)");

        logStep("Verify products are sorted by price (low to high)");
        productPage.verifyProductsSortedByPriceLowToHigh();

        logInfo("Sort by price low to high test completed successfully");
    }

    @Test(groups = {"regression", "filter", "saucedemo"},
            description = "Verify sorting products by price (high to low)")
    public void testSortProductsByPriceHighToLow() {
        logStep("Select sort option: Price (high to low)");
        productPage.selectSortOption("Price (high to low)");

        logStep("Verify products are sorted by price (high to low)");
        productPage.verifyProductsSortedByPriceHighToLow();

        logInfo("Sort by price high to low test completed successfully");
    }

    @Test(groups = {"smoke", "ui", "saucedemo"},
            description = "Verify hamburger menu functionality")
    public void testHamburgerMenu() {
        logStep("Click hamburger menu");
        productPage.clickHamburgerMenu();

        logStep("Verify hamburger menu items");
        List<String> expectedMenuItems = Arrays.asList(
                "All Items",
                "About",
                "Logout",
                "Reset App State"
        );
        productPage.verifyHamburgerMenuItems(expectedMenuItems);

        logInfo("Hamburger menu test completed successfully");
    }

    @Test(groups = {"regression", "filter", "saucedemo"},
            description = "Verify all sort options are available")
    public void testAllSortOptions() {
        logStep("Get all available sort options");
        List<String> actualSortOptions = productPage.getAllSortOptions();

        logStep("Verify expected sort options are available");
        List<String> expectedSortOptions = Arrays.asList(
                "Name (A to Z)",
                "Name (Z to A)",
                "Price (low to high)",
                "Price (high to low)"
        );

        assert actualSortOptions.containsAll(expectedSortOptions) :
                "Missing sort options. Expected: " + expectedSortOptions + ", Actual: " + actualSortOptions;

        logInfo("All sort options test completed successfully");
    }

    @Test(groups = {"regression", "product", "saucedemo"},
            description = "Verify cart badge visibility")
    public void testCartBadgeVisibility() {
        logStep("Verify cart badge is not visible initially");
        assert !productPage.isCartBadgeDisplayed() :
                "Cart badge should not be visible when cart is empty";

        logStep("Add product to cart");
        productPage.addProductToCart("Sauce Labs Backpack");

        logStep("Verify cart badge is now visible");
        assert productPage.isCartBadgeDisplayed() :
                "Cart badge should be visible when cart has items";

        logStep("Verify cart badge shows correct count");
        productPage.verifyCartItemCount(1);

        logInfo("Cart badge visibility test completed successfully");
    }

    @Test(groups = {"smoke", "product", "saucedemo"},
            description = "Verify navigation to cart page")
    public void testNavigateToCart() {
        logStep("Add product to cart first");
        productPage.addProductToCart("Sauce Labs Backpack");
        productPage.verifyCartItemCount(1);

        logStep("Click cart icon to navigate to cart page");
        productPage.clickCartIcon();

        logStep("Verify navigation to cart page");
        String currentUrl = productPage.getCurrentUrl();
        assert currentUrl.contains("/cart.html") :
                "Should navigate to cart page. Current URL: " + currentUrl;

        logInfo("Navigate to cart test completed successfully");
    }
}