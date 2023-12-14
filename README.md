# Zetta Technical Interview Task - QA Engineer

## Selenium Java Project: Christmas Shopping Test

This project is a Selenium WebDriver test script written in Java, designed to automate the process of adding laptops to a shopping cart on Amazon. It demonstrates the use of Selenium for web automation, along with TestNG for organizing tests.

### Prerequisites

To run this project, you need the following:

- Java JDK (Java 20 is used in this project)
- Maven (for managing dependencies and project build)
- Chrome web browser (for running the test)

### Project Structure

- `pom.xml`: Maven Project Object Model file, which contains project configuration including dependencies such as Selenium WebDriver, WebDriverManager, and TestNG.
- `src`: Source folder containing the test script.

### Dependencies

- Selenium WebDriver `4.16.1` for browser automation.
- WebDriverManager `5.6.2` automatically manages WebDriver binaries.
- TestNG `7.8.0` for organizing and running tests.

### Setting Up the Project

1. **Install Java**: Ensure Java JDK 20 is installed on your system. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase/jdk20-archive-downloads.html).

2. **Install Maven**: If Maven is not installed, download and install it from [Maven's official website](https://maven.apache.org/download.cgi).

4. **Install Chrome**: If needed, you can download it from [Chrome's website](www.google.com/chrome).

3. **Clone the Repository**: Clone this project to your local machine.

### Running the Test

To run the test:

1. Navigate to the project directory `selenium-java` in your terminal or command prompt.

2. Run the command `mvn test`. This command will compile the project and execute the test in headed mode.

### Test Steps for `ChristmasShoppingTest.java`

- Goes to amazon.com
- Search for keyword "laptop"
- Add all non-discounted items to cart from the first page of results
- Check on product page if item is in stock and verify item is not discounted
- Count the amount of items added to cart and items skipped
- Check the added amount of items to cart, matches the items in the cart
- Check the items added and items skipped match the total items that should be on the first page of results
- Check the items in cart are still in stock

### Notes

Because the items on the page change with each search, the page is not refreshed until the last item is added. New tabs are used for each item being added to the cart.

The total number of valid items is calculated to be 22, based on the selector used for the purpose of this test.