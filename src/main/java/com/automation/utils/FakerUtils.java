package com.automation.utils;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Faker utility class for generating random test data
 * Uses JavaFaker library to generate realistic test data
 *
 * Example:
 *     String name = FakerUtils.generateRandomFullName();
 *     String email = FakerUtils.generateRandomEmail();
 *     Map<String, String> employee = FakerUtils.generateEmployeeData();
 */
public class FakerUtils {

    private static final Logger logger = LogManager.getLogger(FakerUtils.class);
    private static final Faker faker = new Faker(new Locale("en-US"));
    private static final Random random = new Random();

    /**
     * Generate random full name
     * @return Random full name (first + last name)
     */
    public static String generateRandomFullName() {
        String fullName = faker.name().fullName();
        logger.debug("Generated random full name: {}", fullName);
        return fullName;
    }

    /**
     * Generate random first name
     * @return Random first name
     */
    public static String generateRandomFirstName() {
        String firstName = faker.name().firstName();
        logger.debug("Generated random first name: {}", firstName);
        return firstName;
    }

    /**
     * Generate random last name
     * @return Random last name
     */
    public static String generateRandomLastName() {
        String lastName = faker.name().lastName();
        logger.debug("Generated random last name: {}", lastName);
        return lastName;
    }

    /**
     * Generate random email address
     * @return Random email address
     */
    public static String generateRandomEmail() {
        String email = faker.internet().emailAddress();
        logger.debug("Generated random email: {}", email);
        return email;
    }

    /**
     * Generate random email with custom domain
     * @param domain Custom domain name
     * @return Email with custom domain
     */
    public static String generateRandomEmail(String domain) {
        String username = faker.name().username();
        String email = username + "@" + domain;
        logger.debug("Generated random email with custom domain: {}", email);
        return email;
    }

    /**
     * Generate random phone number
     * @return Random phone number
     */
    public static String generateRandomPhoneNumber() {
        String phoneNumber = faker.phoneNumber().phoneNumber();
        logger.debug("Generated random phone number: {}", phoneNumber);
        return phoneNumber;
    }

    /**
     * Generate random address
     * @return Map containing address components
     */
    public static Map<String, String> generateRandomAddress() {
        Map<String, String> address = new HashMap<>();
        address.put("street", faker.address().streetAddress());
        address.put("city", faker.address().city());
        address.put("state", faker.address().stateAbbr());
        address.put("zipCode", faker.address().zipCode());
        address.put("country", faker.address().country());
        address.put("fullAddress", faker.address().fullAddress());

        logger.debug("Generated random address: {}", address.get("fullAddress"));
        return address;
    }

    /**
     * Generate random company name
     * @return Random company name
     */
    public static String generateRandomCompanyName() {
        String company = faker.company().name();
        logger.debug("Generated random company name: {}", company);
        return company;
    }

    /**
     * Generate random username
     * @return Random username
     */
    public static String generateRandomUsername() {
        String username = faker.name().username();
        logger.debug("Generated random username: {}", username);
        return username;
    }

    /**
     * Generate random password
     * @param minLength Minimum password length
     * @param maxLength Maximum password length
     * @param includeSpecialChars Whether to include special characters
     * @return Random password
     */
    public static String generateRandomPassword(int minLength, int maxLength, boolean includeSpecialChars) {
        String password = faker.internet().password(minLength, maxLength, includeSpecialChars);
        logger.debug("Generated random password of length: {}", password.length());
        return password;
    }

    /**
     * Generate random password with default settings
     * @return Random password (8-16 characters with special chars)
     */
    public static String generateRandomPassword() {
        return generateRandomPassword(8, 16, true);
    }

    /**
     * Generate random date of birth (adult)
     * @return Random date of birth string
     */
    public static String generateRandomDateOfBirth() {
        String dateOfBirth = faker.date().birthday(18, 80).toString();
        logger.debug("Generated random date of birth: {}", dateOfBirth);
        return dateOfBirth;
    }

    /**
     * Generate random credit card number
     * @return Random credit card number
     */
    public static String generateRandomCreditCardNumber() {
        String creditCard = faker.finance().creditCard();
        logger.debug("Generated random credit card number");
        return creditCard;
    }

    /**
     * Generate random bank account number
     * @return Random bank account number
     */
    public static String generateRandomBankAccount() {
        String bankAccount = faker.finance().bic();
        logger.debug("Generated random bank account: {}", bankAccount);
        return bankAccount;
    }

    /**
     * Generate random job title
     * @return Random job title
     */
    public static String generateRandomJobTitle() {
        String jobTitle = faker.job().title();
        logger.debug("Generated random job title: {}", jobTitle);
        return jobTitle;
    }

    /**
     * Generate random text
     * @param wordCount Number of words
     * @return Random text
     */
    public static String generateRandomText(int wordCount) {
        String text = faker.lorem().words(wordCount).toString();
        logger.debug("Generated random text with {} words", wordCount);
        return text;
    }

    /**
     * Generate random number within range
     * @param min Minimum value
     * @param max Maximum value
     * @return Random number
     */
    public static int generateRandomNumber(int min, int max) {
        int number = faker.number().numberBetween(min, max);
        logger.debug("Generated random number: {}", number);
        return number;
    }

    /**
     * Generate random boolean value
     * @return Random boolean
     */
    public static boolean generateRandomBoolean() {
        boolean value = faker.bool().bool();
        logger.debug("Generated random boolean: {}", value);
        return value;
    }

    /**
     * Generate complete employee data
     * @return Map containing all employee information
     */
    public static Map<String, String> generateEmployeeData() {
        Map<String, String> employee = new HashMap<>();

        String firstName = generateRandomFirstName();
        String lastName = generateRandomLastName();

        employee.put("firstName", firstName);
        employee.put("lastName", lastName);
        employee.put("fullName", firstName + " " + lastName);
        employee.put("email", firstName.toLowerCase() + "." + lastName.toLowerCase() + "@company.com");
        employee.put("phone", generateRandomPhoneNumber());
        employee.put("username", firstName.toLowerCase() + lastName.toLowerCase() + generateRandomNumber(100, 999));
        employee.put("password", generateRandomPassword(10, 12, false) + "!");
        employee.put("employeeId", "EMP" + generateRandomNumber(100000, 999999));
        employee.put("jobTitle", generateRandomJobTitle());
        employee.put("department", faker.commerce().department());

        logger.info("Generated employee data for: {} {}", firstName, lastName);
        return employee;
    }

    /**
     * Generate complete customer data
     * @return Map containing all customer information
     */
    public static Map<String, String> generateCustomerData() {
        Map<String, String> customer = new HashMap<>();
        Map<String, String> address = generateRandomAddress();

        customer.put("firstName", generateRandomFirstName());
        customer.put("lastName", generateRandomLastName());
        customer.put("fullName", generateRandomFullName());
        customer.put("email", generateRandomEmail());
        customer.put("phone", generateRandomPhoneNumber());
        customer.put("company", generateRandomCompanyName());
        customer.put("jobTitle", generateRandomJobTitle());
        customer.putAll(address);

        logger.info("Generated customer data for: {}", customer.get("fullName"));
        return customer;
    }

    /**
     * Generate test user credentials
     * @return Map containing username and password
     */
    public static Map<String, String> generateUserCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", generateRandomUsername());
        credentials.put("password", generateRandomPassword());

        logger.info("Generated user credentials for username: {}", credentials.get("username"));
        return credentials;
    }

    /**
     * Generate random product data
     * @return Map containing product information
     */
    public static Map<String, String> generateProductData() {
        Map<String, String> product = new HashMap<>();

        product.put("name", faker.commerce().productName());
        product.put("description", faker.lorem().sentence(10));
        product.put("price", faker.commerce().price(10, 1000));
        product.put("category", faker.commerce().department());
        product.put("brand", faker.company().name());
        product.put("sku", "SKU" + generateRandomNumber(10000, 99999));
        product.put("barcode", faker.code().ean13());

        logger.info("Generated product data: {}", product.get("name"));
        return product;
    }

    /**
     * Generate random order data
     * @return Map containing order information
     */
    public static Map<String, String> generateOrderData() {
        Map<String, String> order = new HashMap<>();

        order.put("orderId", "ORD" + generateRandomNumber(100000, 999999));
        order.put("orderDate", faker.date().birthday().toString());
        order.put("status", faker.options().option("Pending", "Processing", "Shipped", "Delivered"));
        order.put("total", faker.commerce().price(50, 500));
        order.put("currency", "USD");
        order.put("paymentMethod", faker.options().option("Credit Card", "PayPal", "Bank Transfer"));

        logger.info("Generated order data with ID: {}", order.get("orderId"));
        return order;
    }

    /**
     * Generate random file name
     * @param extension File extension (e.g., "pdf", "jpg")
     * @return Random file name with extension
     */
    public static String generateRandomFileName(String extension) {
        String fileName = faker.file().fileName().replace(".", "_") + "." + extension;
        logger.debug("Generated random file name: {}", fileName);
        return fileName;
    }

    /**
     * Generate random URL
     * @return Random URL
     */
    public static String generateRandomUrl() {
        String url = faker.internet().url();
        logger.debug("Generated random URL: {}", url);
        return url;
    }
}