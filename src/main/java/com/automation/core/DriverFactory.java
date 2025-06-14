package com.automation.core;

import com.automation.core.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * WebDriver Factory class for creating WebDriver instances
 * Supports local and remote (Selenium Grid) execution
 *
 * Example:
 *     WebDriver driver = DriverFactory.createDriver("chrome", false);
 *     WebDriver headlessDriver = DriverFactory.createDriver("chrome", true);
 */
public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Create WebDriver instance based on browser configuration
     * @param browserName Browser name (chrome, firefox, edge)
     * @param isHeadless Whether to run in headless mode
     * @return WebDriver instance
     */
    public static WebDriver createDriver(String browserName, boolean isHeadless) {
        WebDriver driver;
        String environment = ConfigReader.getProperty("environment", "local");

        logger.info("Creating {} driver in {} mode", browserName, isHeadless ? "headless" : "normal");

        if ("docker".equalsIgnoreCase(environment) || "grid".equalsIgnoreCase(environment)) {
            driver = createRemoteDriver(browserName, isHeadless);
        } else {
            driver = createLocalDriver(browserName, isHeadless);
        }

        // Configure driver
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.getProperty("implicit.wait", "10"))
        ));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        setDriver(driver);
        logger.info("{} driver created successfully", browserName);
        return driver;
    }

    /**
     * Create local WebDriver instance
     */
    private static WebDriver createLocalDriver(String browserName, boolean isHeadless) {
        WebDriver driver;

        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                if (isHeadless) {
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }

                // Additional Chrome options for stability
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--start-maximized");

                // Disable password manager popup
                chromeOptions.addArguments("--disable-save-password-bubble");
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (isHeadless) {
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--width=1920");
                    firefoxOptions.addArguments("--height=1080");
                }

                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();

                if (isHeadless) {
                    edgeOptions.addArguments("--headless");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }

                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Browser " + browserName + " is not supported");
        }

        return driver;
    }

    /**
     * Create remote WebDriver instance for Selenium Grid
     */
    private static WebDriver createRemoteDriver(String browserName, boolean isHeadless) {
        String gridUrl = ConfigReader.getProperty("grid.url", "http://localhost:4444/wd/hub");

        try {
            WebDriver driver;

            switch (browserName.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("browserName", "chrome");

                    if (isHeadless) {
                        chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("--window-size=1920,1080");
                    }

                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");

                    driver = new RemoteWebDriver(new URL(gridUrl), chromeOptions);
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setCapability("browserName", "firefox");

                    if (isHeadless) {
                        firefoxOptions.addArguments("--headless");
                    }

                    driver = new RemoteWebDriver(new URL(gridUrl), firefoxOptions);
                    break;

                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.setCapability("browserName", "MicrosoftEdge");

                    if (isHeadless) {
                        edgeOptions.addArguments("--headless");
                    }

                    driver = new RemoteWebDriver(new URL(gridUrl), edgeOptions);
                    break;

                default:
                    throw new IllegalArgumentException("Browser " + browserName + " is not supported for remote execution");
            }

            logger.info("Remote driver created for {} on grid: {}", browserName, gridUrl);
            return driver;

        } catch (MalformedURLException e) {
            logger.error("Invalid Selenium Grid URL: {}", gridUrl);
            throw new RuntimeException("Failed to create remote driver", e);
        }
    }

    /**
     * Set driver to ThreadLocal for parallel execution
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    /**
     * Get driver from ThreadLocal
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quit driver and remove from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Quitting driver");
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    /**
     * Get driver based on system property or default
     */
    public static WebDriver createDriver() {
        String browser = System.getProperty("browser", ConfigReader.getProperty("browser", "chrome"));
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", ConfigReader.getProperty("headless", "false")));

        return createDriver(browser, headless);
    }
}