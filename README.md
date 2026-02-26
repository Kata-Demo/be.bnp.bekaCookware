# be.bnp.bekaCookware
## 1 Description

Kata - BEKA Cookware Repository is an advanced level repo to provide Functional Test Scenarios respecting Kata Technical Requirements and demonstrate Automation keys concept and project structure.

## 2 Prerequirements

- Java 25 (download here: https://www.oracle.com/java/technologies/downloads/#jdk25-windows)
- Set JAVA_HOME env variable to point out java installed root folder
- Add %JAVA_HOME%\bin to the path system variable
- Maven (download Binary tar.gz archive here: https://maven.apache.org/download.cgi)
- Set M3_HOME env variable to point out maven installed root folder
- Add %M3_HOME%\bin to the path system variable
- Google Chrome browser (Version in use : 145.0.7632.117 (Official Build) (64-bit))
- Selenium Chrome Driver (download here: http://chromedriver.chromium.org/downloads)
- (Optional) Selenium Gecko Driver for Firefox (download here: https://github.com/mozilla/geckodriver/releases)

## 3 Installation

- Checkout/download the project from the git repository into a root folder

## 4 Keys concept

### Page Object Model

Page Object Model (POM) is a design pattern in Selenium that creates an object repository to store all web elements of an application. It reduces code duplication and simplifies test case maintenance by organizing elements in separate classes.

### Mobile first

Mobile-first testing prioritizes testing on mobile devices before desktop, ensuring optimal performance, usability, and responsiveness for the majority of users. It focuses on touch interactions, smaller screen layouts, and faster load times, crucial for SEO and user experience. 

- Responsive Design Validation: Ensure layout adjusts seamlessly across screen sizes, with proper content hierarchy, "hamburger" menus, and accessible buttons.

### Triple A Patern

Over the years, many approaches to unit testing have emerged, with the Arrange-Act-Assert (AAA, or 3A) pattern standing out as the most popular. This pattern involves structuring your unit tests into three steps:

- Arrange: Set up the test environment.
- Act: Execute the code to test.
- Assert: Verify the results.
