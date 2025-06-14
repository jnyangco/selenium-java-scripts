# selenium-java-tests

# Selenium Java Test Automation Framework

A comprehensive Page Object Model (POM) based test automation framework using Selenium WebDriver, TestNG, and ExtentReports for Java applications.

## 🚀 Features

- **Page Object Model (POM)** design pattern
- **TestNG** for test execution and parallel testing
- **ExtentReports** for beautiful HTML test reports
- **Log4j2** for comprehensive logging
- **WebDriverManager** for automatic driver management
- **Maven** for dependency management
- **Parallel execution** support
- **Cross-browser testing** (Chrome, Firefox, Edge)
- **Headless browser** support
- **Selenium Grid** integration
- **Screenshot capture** on failures
- **Test data management** via properties files
- **Faker library** for dynamic test data generation
- **Configuration management** for different environments

## 📁 Project Structure

```
selenium_java_tests/
├── src/
│   ├── main/java/
│   └── test/
│       ├── java/
│       │   └── com/automation/
│       │       ├── base/
│       │       │   ├── BasePage.java
│       │       │   ├── BaseTest.java
│       │       │   └── DriverFactory.java
│       │       ├── config/
│       │       │   └── ConfigReader.java
│       │       ├── pages/
│       │       │   ├── saucedemo/
│       │       │   │   ├── LoginPageSaucedemo.java
│       │       │   │   └── ProductPageSaucedemo.java
│       │       │   └── orangehrm/
│       │       │       ├── LoginPageOrangeHRM.java
│       │       │       └── DashboardPageOrangeHRM.java
│       │       ├── tests/
│       │       │   ├── saucedemo/
│       │       │   │   ├── LoginTestSaucedemo.java
│       │       │   │   └── ProductTestSaucedemo.java
│       │       │   └── orangehrm/
│       │       │       └── LoginTestOrangeHRM.java
│       │       ├── utils/
│       │       │   ├── ExtentReportManager.java
│       │       │   ├── LoggerUtils.java
│       │       │   ├── ScreenshotUtils.java
│       │       │   └── FakerUtils.java
│       │       └── listeners/
│       │           ├── TestListener.java
│       │           └── ExtentReportListener.java
│       └── resources/
│           ├── config/
│           │   ├── config.properties
│           │   └── data.properties
│           ├── testng-suites/
│           │   ├── smoke-tests.xml
│           │   ├── regression-tests.xml
│           │   └── parallel-tests.xml
│           └── log4j2.xml
├── reports/
│   ├── screenshots/
│   └── extent-reports/
├── pom.xml
└── README.md
```

## 🛠️ Prerequisites

- **Java 11** or higher
- **Maven 3.6** or higher
- **Chrome/Firefox/Edge** browser installed
- **Git** for version control

## ⚡ Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd selenium_java_tests
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Run Tests

#### Run Smoke Tests
```bash
mvn test -Dtest=TestNG -DsuiteXmlFile=src/test/resources/testng-suites/smoke-tests.xml
```

#### Run Regression Tests
```bash
mvn test -Dtest=TestNG -DsuiteXmlFile=src/test/resources/testng-suites/regression-tests.xml
```

#### Run with Different Browser
```bash
mvn test -Dbrowser=firefox -DsuiteXmlFile=src/test/resources/testng-suites/smoke-tests.xml
```

#### Run in Headless Mode
```bash
mvn test -Dheadless=true -DsuiteXmlFile=src/test/resources/testng-suites/smoke-tests.xml
```

#### Run with Selenium Grid
```bash
mvn test -Denvironment=grid -Dgrid.url=http://localhost:4444/wd/hub
```

## 🧪 Test Execution Options

### Maven Profiles

#### Smoke Tests
```bash
mvn test -Psmoke
```

#### Regression Tests
```bash
mvn test -Pregression
```

#### Parallel Execution
```bash
mvn test -Pparallel -Dthread.count=4
```

### System Properties

| Property | Default | Description |
|----------|---------|-------------|
| browser | chrome | Browser to use (chrome, firefox, edge) |
| headless | false | Run in headless mode |
| environment | local | Environment (local, grid, docker) |
| grid.url | http://localhost:4444/wd/hub | Selenium Grid URL |
| thread.count | 4 | Number of parallel threads |

### Test Categories (Groups)

- **smoke** - Critical functionality tests
- **regression** - Full test suite
- **login** - Login functionality tests
- **product** - Product-related tests
- **saucedemo** - SauceDemo application tests
- **orangehrm** - OrangeHRM application tests

## 📊 Reports

### ExtentReports
After test execution, HTML reports are generated in:
```
reports/ExtentReport_[timestamp].html
```

### Screenshots
Failure screenshots are captured in:
```
reports/screenshots/
```

### Logs
Detailed logs are available in:
```
reports/automation.log
reports/errors.log
```

## 🔧 Configuration

### Browser Configuration
Edit `src/test/resources/config/config.properties`:
```properties
browser=chrome
headless=false
implicit.wait=10
explicit.wait=15
```

### Test Data Configuration
Edit `src/test/resources/config/data.properties`:
```properties
saucedemo.base.url=https://www.saucedemo.com/
saucedemo.username=standard_user
saucedemo.password=secret_sauce
```

### Logging Configuration
Modify `src/test/resources/log4j2.xml` for custom logging settings.

## 🏗️ Framework Architecture

### Base Classes
- **BasePage** - Common page methods and WebDriver utilities
- **BaseTest** - Test setup, teardown, and reporting integration
- **DriverFactory** - WebDriver creation and management

### Page Objects
- Organized by application (saucedemo, orangehrm)
- Encapsulate page elements and behaviors
- Inherit from BasePage for common functionality

### Test Classes
- Organized by application and feature
- Use TestNG annotations and groups
- Inherit from BaseTest for setup/teardown

### Utilities
- **ExtentReportManager** - Report generation and management
- **ScreenshotUtils** - Screenshot capture and management
- **LoggerUtils** - Centralized logging utilities
- **FakerUtils** - Dynamic test data generation

## 🔄 Parallel Execution

### TestNG Configuration
```xml
<suite name="ParallelSuite" parallel="methods" thread-count="4">
```

### Maven Surefire Configuration
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
    </configuration>
</plugin>
```

## 🐳 Docker & Selenium Grid

### Start Selenium Grid with Docker
```bash
# Start Selenium Hub
docker run -d -p 4444:4444 --name selenium-hub selenium/hub:4.15.0

# Start Chrome Node
docker run -d --link selenium-hub:hub selenium/node-chrome:4.15.0

# Start Firefox Node
docker run -d --link selenium-hub:hub selenium/node-firefox:4.15.0
```

### Docker Compose (Optional)
Create `docker-compose.yml`:
```yaml
version: '3'
services:
  selenium-hub:
    image: selenium/hub:4.15.0
    ports:
      - "4444:4444"
  
  chrome:
    image: selenium/node-chrome:4.15.0
    depends_on:
      - selenium-hub
    environment:
      - HUB_HOST=selenium-hub
```

Run with:
```bash
docker-compose up -d
mvn test -Denvironment=grid
```

## 📈 Test Data Management

### Static Test Data
Use properties files for configuration:
```properties
# data.properties
user.email=test@example.com
user.password=TestPassword123!
```

### Dynamic Test Data
Use Faker for random data generation:
```java
String randomEmail = FakerUtils.generateRandomEmail();
String randomName = FakerUtils.generateRandomFullName();
Map<String, String> customerData = FakerUtils.generateCustomerData();
```

### Environment-Specific Data
```properties
# config.properties
environment=qa
qa.base.url=https://qa.example.com
prod.base.url=https://prod.example.com
```

## 🔍 Debugging & Troubleshooting

### Common Issues

#### WebDriver Issues
```bash
# Clear WebDriver cache
rm -rf ~/.cache/selenium
mvn clean install
```

#### Port Conflicts
```bash
# Check running processes
lsof -i :4444
# Kill process if needed
kill -9 <PID>
```

#### Browser Version Mismatch
WebDriverManager automatically handles driver versions, but you can specify:
```properties
webdriver.chrome.driver=/path/to/chromedriver
```

### Debug Mode
Enable debug logging:
```properties
log.level=DEBUG
```

Run single test for debugging:
```bash
mvn test -Dtest=LoginTestSaucedemo#testValidLogin -Dbrowser=chrome
```

## 🏅 Best Practices

### Page Object Model
- Keep page classes focused on a single page/component
- Use meaningful method names that describe business actions
- Encapsulate element locators as private fields
- Return page objects from navigation methods

### Test Design
- Follow AAA pattern (Arrange, Act, Assert)
- Use descriptive test method names
- Keep tests independent and isolated
- Use TestNG groups for test categorization

### Code Quality
- Follow Java naming conventions
- Add meaningful comments and documentation
- Use consistent formatting and indentation
- Implement proper exception handling

### Maintenance
- Regularly update dependencies
- Review and refactor test code
- Monitor test execution times
- Keep test data up to date

## 🔧 Customization

### Adding New Applications
1. Create new package under `pages` (e.g., `com.automation.pages.newapp`)
2. Create page classes extending `BasePage`
3. Create test classes under `tests` extending `BaseTest`
4. Add application data to `data.properties`
5. Create TestNG suite XML file

### Adding New Utilities
1. Create utility class under `utils` package
2. Follow static method pattern for reusability
3. Add proper logging and error handling
4. Document usage with examples

### Custom Listeners
Implement TestNG listeners for custom behavior:
```java
public class CustomListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        // Custom failure handling
    }
}
```

## 📚 Dependencies

### Core Dependencies
- **Selenium WebDriver 4.25.0** - Browser automation
- **TestNG 7.8.0** - Test framework
- **ExtentReports 5.1.2** - HTML reporting
- **Log4j2 2.21.1** - Logging framework
- **WebDriverManager 5.6.2** - Driver management

### Utility Dependencies
- **JavaFaker 1.0.2** - Test data generation
- **Jackson 2.16.0** - JSON processing
- **Commons IO 2.15.0** - File operations

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push to branch (`git push origin feature/new-feature`)
5. Create Pull Request

### Code Standards
- Follow existing code style
- Add unit tests for new utilities
- Update documentation
- Ensure all tests pass

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For questions and support:
- Create an issue in the repository
- Check existing documentation
- Review test execution logs
- Consult Selenium and TestNG documentation

## 🔄 Continuous Integration

### GitHub Actions Example
```yaml
name: Test Automation
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Run tests
        run: mvn test -Dheadless=true
```

### Jenkins Pipeline Example
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test -Dheadless=true'
            }
        }
        stage('Reports') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'reports',
                    reportFiles: '*.html',
                    reportName: 'Test Report'
                ])
            }
        }
    }
}
```

## 📊 Performance Monitoring

### Test Execution Metrics
- Track test execution times
- Monitor browser resource usage
- Identify slow-running tests
- Optimize wait strategies

### Reporting Metrics
- Test pass/fail rates
- Execution trends over time
- Browser compatibility results
- Environment-specific results

---

**Happy Testing! 🚀**