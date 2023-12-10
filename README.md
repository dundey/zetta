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

1. Navigate to the project directory in your terminal or command prompt.

2. Run the command `mvn test`. This command will compile the project and execute the test in headed mode.

### Test Steps for `ChristmasShoppingTest.java`

- Goes to amazon.com
- Search for keyword "laptop"
- Add all non-discounted items to cart from the first page of results
- Count the amount of items added and items skipped
- Check the added amount of items matches the items in cart
- Check the items added and items skipped match the total items on page
- Check the items in cart are in stock

### Notes

Because the items on the page change with each search, the page is not refreshed until the last item is added. New tabs are used for each item being added to the cart.

The total number of valid items is calculated to be 22, based on the selector used for the purpose of this test.

Other forms of validation can be used, such as verifying that a specific item added to the cart matches its name or ID, rather than just counting the numbers. However, I've decided not to overcomplicate the task for now.

I've added a validation step in the cart to look for out-of-stock items. This validation might not be entirely accurate, as I can't fully test it, becase an item typically can't be added if it's already out of stock. A more effective approach will be to add a validation when attempting to click the 'Add to Cart' button and skip the item if it's out of stock, which will result in a more valuable test.

I didn't include a step at the end to check each cart item individually for discounts, as this adds excessive complexity. Moreover, if such a check is necessary, it might indicate an issue with the code. You can't reliably check if an item is discounted without opening its product page.

