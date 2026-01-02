# Mobile Automation Testing Framework

A production-ready mobile test automation framework built with Appium, Java, and TestNG, implementing Page Object Model and data-driven testing approaches.

## Overview

This framework provides automated testing capabilities for Android mobile applications, specifically designed for e-commerce platforms. It covers authentication flows including Login, Registration, and Change Password functionality.

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17+ |
| Mobile Automation | Appium 2.x (UiAutomator2) |
| Test Framework | TestNG 7.x |
| Build Tool | Maven |
| Reporting | Allure Report |
| Logging | Log4j2 |
| Data Parser | Jackson |
| Platform | Android |

## Project Structure
```
src/
├── main/java/org/example/
│   ├── constants/          # ConfigData - centralized configuration
│   ├── drivers/            # DriverManager with ThreadLocal
│   ├── helpers/            # JsonDataReader, PropertiesHelpers, SystemHelpers
│   ├── keywords/           # MobileUI - reusable mobile actions
│   ├── model/              # POJOs for test data (Login, Register, ChangePassword)
│   └── utils/              # ScreenshotUtils
│
├── main/resources/
│   ├── config/             # config.properties, log4j2.xml
│   └── test_data/          # test_data.json
│
└── test/java/
    ├── common/             # BaseTest, CommonTest, AppFlowManager
    ├── dataprovider/       # TestDataProviders
    ├── listeners/          # TestListener (Allure integration)
    ├── page/               # Page Objects (HomePage, LoginPage, RegisterPage, etc.)
    ├── testcase/           # Standard test classes
    └── testcase_datadriven/# Data-driven test classes
```

## Key Features

### 1. Page Object Model Implementation
- Clean separation between test logic and page interactions
- Each page class encapsulates element locators and actions
- Allure @Step annotations for detailed reporting

### 2. Data-Driven Testing
- JSON-based test data with structured hierarchy
- Support for multiple test design techniques:
  - Equivalence Partitioning
  - Boundary Value Analysis
  - Decision Table Testing
- TestNG DataProviders for parameterized execution

### 3. Reusable Keyword Library (MobileUI)
```java
// Touch gestures
MobileUI.swipe(), tap(), zoom(), scroll()

// Element interactions
MobileUI.clickElement(), setText(), clearText(), getElementText()

// Wait conditions
MobileUI.waitForElementVisible(), waitForElementClickable(), waitForTextToBePresent()

// Verifications
MobileUI.verifyElementPresentAndDisplayed(), verifyElementText()
```

### 4. Smart Test Management
- Automatic onboarding flow handling
- Precondition support (login before test)
- Conditional cleanup based on test results
- Retry mechanism for flaky scenarios

### 5. Reporting and Logging
- Allure Report with screenshots, error details, and stack traces
- Log4j2 with separate console and file appenders
- Configurable screenshot capture (pass/fail/all)

## Test Coverage

| Module | Test Cases | Techniques |
|--------|------------|------------|
| Login | 60+ | EP, BVA, Special Cases, Combination |
| Registration | 35+ | EP, BVA, Decision Table |
| Change Password | 20+ | EP, BVA, Security |

### Test Data Categories

**Login Module:**
- Valid credentials
- Invalid email formats (10 scenarios)
- Invalid password formats (8 scenarios)
- Invalid special characters (22 scenarios)
- Empty fields validation
- Password length boundaries
- Email length boundaries
- Security tests (SQL Injection, XSS)

**Registration Module:**
- Full name validation
- Email validation
- Password requirements
- Decision table combinations (8 scenarios)

**Change Password Module:**
- Current password validation
- New password format validation
- Empty fields handling
- Boundary values

## Configuration

Update `src/main/resources/config/config.properties`:
```properties
# Appium Server
APPIUM_HOST=127.0.0.1
APPIUM_PORT=4723

# Device Configuration
PLATFORM_NAME=Android
PLATFORM_VERSION=16
DEVICE_NAME=pixel-7-api36
AUTOMATION_NAME=UiAutomator2

# App Configuration
APP_PACKAGE=com.italist.mobile
APP_ACTIVITY=com.italist.mobile.MainActivity

# Timeouts
TIMEOUT_EXPLICIT_DEFAULT=2
TIMEOUT_PAGE_LOAD=30

# Screenshot
SCREENSHOT_FAIL=true
SCREENSHOT_PASS=true
```

## Running Tests
```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=LoginTest

# Run data-driven tests only
mvn test -Dtest=LoginTestDataDriven

# Generate Allure report
mvn allure:serve
```

## Test Data Structure
```json
{
  "login_test_data": {
    "equivalence_partitioning": {
      "invalid_email_format": [
        {
          "testId": "LG.05_EP1",
          "category": "Invalid Email Format - Missing @",
          "email": "invalidemail",
          "password": "ValidPass123@",
          "expectedResult": "email_invalid",
          "expectedMessage": "Please enter valid email address"
        }
      ]
    },
    "boundary_value_analysis": {...},
    "special_cases": [...]
  }
}
```

## Architecture Highlights

1. **ThreadLocal Driver**: Thread-safe driver management for parallel execution
2. **Centralized Configuration**: Single source of truth for all settings
3. **Flexible Wait Strategy**: Explicit waits with configurable timeouts
4. **Error Handling**: Graceful handling of WebDriver exceptions with retry logic
5. **Test Independence**: Each test manages its own preconditions and cleanup

## Prerequisites

- Java 17+
- Maven 3.8+
- Android SDK
- Appium Server 2.x
- Android Emulator or Real Device

## Author

https://www.linkedin.com/in/huynhnhu1510/

## License

MIT License
```

---

## 3. LinkedIn Skills Section
```
Technical Skills:
- Mobile Test Automation (Appium)
- Java Programming
- TestNG Framework
- Page Object Model (POM)
- Data-Driven Testing
- Test Design Techniques (EP, BVA, Decision Table)
- Allure Reporting
- Maven
- Git/GitHub
- Android Testing (UiAutomator2)
- JSON Data Management
- Log4j2
