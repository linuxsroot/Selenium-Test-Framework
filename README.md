# OrangeHRMProject - Selenium Test Automation Framework (UI + Dummy API + Dummy DB)

A Hybrid / Data-Driven **Automation Framework** built using **Selenium Java + TestNG** with **Page Object Model (POM)**.  
This framework supports **GUI Testing**, **Dummy API Testing**, and **Dummy Database Testing**, and is integrated with **Jenkins** and **Docker** for CI/CD execution.

---

## ✅ Tech Stack
- Java
- Selenium WebDriver
- TestNG
- Maven
- Extent Reports
- Log4j2
- Apache POI (Excel Data Driven)
- JDBC (Dummy DB Testing)
- Jenkins Pipeline (Jenkinsfile)
- Docker / Docker Compose

---

## ✅ Framework Highlights
- **Page Object Model (POM)** for UI pages
    - `LoginPage`, `HomePage`
- **Data Driven Testing** using Excel + TestNG DataProviders
    - `ExcelReaderUtility`, `DataProviders`
- **Reusable Base / Driver setup**
    - `BaseClass`, `ActionDriver`
- **Listeners + Retry**
    - `TestListener`, `RetryAnalyzer`
- **Reporting + Screenshots**
    - Extent report stored under: `src/test/resources/ExtentReport`
    - Screenshots stored under: `src/test/resources/screenshots`
- **Logging**
    - `log4j2.xml` configured inside `resources`
- **Multiple test types**
    - UI Tests: `LoginPageTest`, `HomePageTest`
    - Dummy API Tests: `ApiTest` *(sample/dummy API validations)*
    - Dummy DB Tests: `DBVerificationTest` *(sample/dummy DB validations)*

---

## 📁 Project Structure
```text
OrangeHRMProject
 ├── docker/
 │   └── docker-compose.yml
 ├── logs/
 ├── src/
 │   ├── main/
 │   │   ├── java/com.orangehrm/
 │   │   │   ├── actiondriver/ActionDriver.java
 │   │   │   ├── base/BaseClass.java
 │   │   │   ├── listeners/TestListener.java
 │   │   │   ├── pages/LoginPage.java
 │   │   │   ├── pages/HomePage.java
 │   │   │   └── utilities/
 │   │   │       ├── ApiUtility.java
 │   │   │       ├── DataProviders.java
 │   │   │       ├── DBConnection.java
 │   │   │       ├── ExcelReaderUtility.java
 │   │   │       ├── ExtentManager.java
 │   │   │       ├── LoggerManager.java
 │   │   │       └── RetryAnalyzer.java
 │   │   └── resources/
 │   │       ├── config.properties
 │   │       └── log4j2.xml
 │   └── test/
 │       ├── java/com.orangehrm.test/
 │       │   ├── ApiTest.java               # Dummy API tests
 │       │   ├── DBVerificationTest.java    # Dummy DB tests
 │       │   ├── HomePageTest.java
 │       │   └── LoginPageTest.java
 │       └── resources/
 │           ├── ExtentReport/
 │           ├── screenshots/
 │           ├── testdata/
 │           └── testng.xml
 ├── Jenkinsfile
 ├── pom.xml
 └── README.md
