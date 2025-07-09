# Browserstack-Tech-Assignment

This project is a Selenium-based automation framework that tests the Spanish news website **El País** across various browsers and mobile devices using **BrowserStack**.

##  Features

-  Verifies the language of the page is Spanish.
-  Fetches and prints titles and content from articles under the "Opinión" section.
-  Translates titles using a custom Google Translate API class.
-  Downloads article images.
-  Runs cross-browser tests (Chrome, Safari, Mobile).
-  Handles stale element exceptions and mobile-specific behaviors.

##  Tech Stack

- **Java 17+**
- **Selenium WebDriver**
- **TestNG**
- **BrowserStack Cloud Grid**
- **Google Translate API (custom implementation)**
- **Maven**

## 🗂️ Project Structure

├── pom.xml
├── testng.xml
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ ├── ConfigReader/
│ │ │ │ └── ConfigReader.java
│ │ │ ├── DriverSetup/
│ │ │ │ └── BaseTest.java
│ │ │ └── GoogleTranslate/
│ │ │ └── GoogleTranslator.java
│ │ └── resources/
│ │ └── config.properties
│ └── test/
│ └── java/
│ └── com/
│ └── Elpais/
│ └── ElPaisTest.java


## ⚙️ How to Run

1. Clone the repo:

   git clone https://github.com/your-username/Browserstack-Tech-Assignment.git
   cd Browserstack-Tech-Assignment
Set up your config.properties with BrowserStack credentials.

Run tests with Maven:

  mvn clean test
  
🔒 .gitignore
Sensitive files like config.properties are excluded from version control.

📸 Screenshots & Logs
Screenshots of articles and images are saved in target/images/.

🧑 Author
Sri Hari
Lead Automation Developer 
📧 harikrrish1235@gmail.com
