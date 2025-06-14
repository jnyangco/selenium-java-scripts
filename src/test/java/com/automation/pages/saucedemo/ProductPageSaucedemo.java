package com.automation.pages.saucedemo;

import com.automation.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Product Page for SauceDemo application
 * Contains locators and methods for product listing and interactions
 *
 * Example:
 *     ProductPageSaucedemo productPage = new ProductPageSaucedemo(driver);
 *     productPage.addProductToCart("Sauce Labs Backpack");
 *     productPage.verifyCartItemCount(1);
 */
public class ProductPageSaucedemo extends BasePage {

    // Locators
    private final By productCards = By.className("inventory_item_name");
    private final By productPrices = By.className("inventory_item_price");
    private final By addToCartButtons = By.xpath("//button[contains(@id,'add-to-cart')]");
    private final By removeButtons = By.xpath("//button[contains(@id,'remove')]");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By sortDropdown = By.className("product_sort_container");
    private final By hamburgerMenu = By.id("react-burger-menu-btn");
    private final By menuItems = By.xpath("//nav[@class='bm-item-list']/a");
    private final By productContainer = By.id("inventory_container");

    // Dynamic locators
    private final String addToCartButtonByName = "//div[text()='%s']/ancestor::div[@class='inventory_item']//button[contains(@id,'add-to-cart')]";
    private final String removeButtonByName = "//div[text()='%s']/ancestor::div[@class='inventory_item']//button[contains(@id,'remove')]";
    private final String productPriceByName = "//div[text()='%s']/ancestor::div[@class='inventory_item']//div[@class='inventory_item_price']";

    public ProductPageSaucedemo(WebDriver driver) {
        super(driver);
    }

    /**
     * Verify product page is loaded
     */
    public void verifyProductPageLoaded() {
        logger.info("Verifying product page is loaded");

        Assert.assertTrue(isElementDisplayed(productContainer), "Product container should be displayed");
        Assert.assertTrue(isElementDisplayed(cartIcon), "Cart icon should be displayed");
        Assert.assertTrue(isElementDisplayed(sortDropdown), "Sort dropdown should be displayed");

        logger.info("Product page verification successful");
    }

    /**
     * Get all product names
     *
     * @return List of product names
     */
    public List<String> getAllProductNames() {
        logger.info("Getting all product names");

        List<WebElement> products = findElements(productCards);
        List<String> productNames = new ArrayList<>();

        for (WebElement product : products) {
            productNames.add(product.getText().trim());
        }

        logger.info("Found {} products: {}", productNames.size(), productNames);
        return productNames;
    }

    /**
     * Get all product prices
     *
     * @return List of product prices as strings
     */
    public List<String> getAllProductPrices() {
        logger.info("Getting all product prices");

        List<WebElement> prices = findElements(productPrices);
        List<String> productPrices = new ArrayList<>();

        for (WebElement price : prices) {
            productPrices.add(price.getText().replace("$", "").trim());
        }

        logger.info("Found {} prices: {}", productPrices.size(), productPrices);
        return productPrices;
    }

    /**
     * Add product to cart by name
     *
     * @param productName Name of the product to add
     */
    public void addProductToCart(String productName) {
        logger.info("Adding product to cart: {}", productName);

        By addToCartButton = By.xpath(String.format(addToCartButtonByName, productName));
        clickElement(addToCartButton);

        logger.info("Product added to cart successfully: {}", productName);
    }

    /**
     * Remove product from cart by name
     *
     * @param productName Name of the product to remove
     */
    public void removeProductFromCart(String productName) {
        logger.info("Removing product from cart: {}", productName);

        By removeButton = By.xpath(String.format(removeButtonByName, productName));
        clickElement(removeButton);

        logger.info("Product removed from cart successfully: {}", productName);
    }

    /**
     * Get product price by name
     *
     * @param productName Name of the product
     * @return Product price as string (without $ symbol)
     */
    public String getProductPrice(String productName) {
        logger.info("Getting price for product: {}", productName);

        By priceLocator = By.xpath(String.format(productPriceByName, productName));
        String price = getText(priceLocator).replace("$", "").trim();

        logger.info("Price for {} is: {}", productName, price);
        return price;
    }

    /**
     * Check if product's Add to Cart button is displayed
     *
     * @param productName Name of the product
     * @return true if Add to Cart button is displayed, false otherwise
     */
    public boolean isAddToCartButtonDisplayed(String productName) {
        By addToCartButton = By.xpath(String.format(addToCartButtonByName, productName));
        boolean displayed = isElementDisplayed(addToCartButton);
        logger.info("Add to Cart button for {} is {}", productName, displayed ? "displayed" : "not displayed");
        return displayed;
    }

    /**
     * Check if product's Remove button is displayed
     *
     * @param productName Name of the product
     * @return true if Remove button is displayed, false otherwise
     */
    public boolean isRemoveButtonDisplayed(String productName) {
        By removeButton = By.xpath(String.format(removeButtonByName, productName));
        boolean displayed = isElementDisplayed(removeButton);
        logger.info("Remove button for {} is {}", productName, displayed ? "displayed" : "not displayed");
        return displayed;
    }

    /**
     * Verify product is added to cart (Remove button should be displayed)
     *
     * @param productName Name of the product
     */
    public void verifyProductAddedToCart(String productName) {
        logger.info("Verifying product is added to cart: {}", productName);

        Assert.assertTrue(isRemoveButtonDisplayed(productName),
                "Remove button should be displayed after adding product to cart");
        Assert.assertFalse(isAddToCartButtonDisplayed(productName),
                "Add to Cart button should not be displayed after adding product");

        logger.info("Product successfully verified as added to cart: {}", productName);
    }

    /**
     * Get cart item count from badge
     *
     * @return Cart item count, 0 if badge not displayed
     */
    public int getCartItemCount() {
        if (isElementDisplayed(cartBadge)) {
            String count = getText(cartBadge);
            int itemCount = Integer.parseInt(count);
            logger.info("Cart item count: {}", itemCount);
            return itemCount;
        }
        logger.info("Cart badge not displayed, assuming 0 items");
        return 0;
    }

    /**
     * Verify cart item count
     *
     * @param expectedCount Expected number of items in cart
     */
    public void verifyCartItemCount(int expectedCount) {
        logger.info("Verifying cart item count: {}", expectedCount);

        int actualCount = getCartItemCount();
        Assert.assertEquals(actualCount, expectedCount,
                "Cart item count mismatch");

        logger.info("Cart item count verification successful: {}", expectedCount);
    }

    /**
     * Click on cart icon to navigate to cart page
     */
    public void clickCartIcon() {
        logger.info("Clicking cart icon");
        clickElement(cartIcon);
    }

    /**
     * Select sort option from dropdown
     *
     * @param sortOption Sort option text (e.g., "Name (A to Z)", "Price (low to high)")
     */
    public void selectSortOption(String sortOption) {
        logger.info("Selecting sort option: {}", sortOption);

        WebElement dropdown = findElement(sortDropdown);
        Select select = new Select(dropdown);
        select.selectByVisibleText(sortOption);

        logger.info("Sort option selected successfully: {}", sortOption);
    }
}
