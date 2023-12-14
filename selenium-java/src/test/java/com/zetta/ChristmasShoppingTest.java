import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class ChristmasShoppingTest {

    private static final String BASE_URL = "https://www.amazon.com";
    private static final String NOT_IN_STOCK_MESSAGE = "Item at index %d is not in stock within 'availabilityInsideBuyBox_feature_div'.";
    private WebDriver driver;
    private WebDriverWait wait;

    // Sets up the WebDriver and navigate to the homepage.
    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Global wait time reduced to 5 seconds to speed up the tests
        driver.get(BASE_URL);
    }

    // The test script for adding laptops to the cart on Amazon.
    @Test
    public void addLaptopsToCart() {
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox")); // Locating the search box
        searchBox.sendKeys("laptop" + Keys.ENTER); // Typing "laptop" and hitting ENTER

        int strikeThroughPriceCount = 0; // Counter for found items that are discounted
        int itemsAddedToCart = 0; // Counter for items added to cart

        for (int i = 3; i <= 31; i++) { // Looping through search results
            try {
                // Check if the item is discount and skip, or continue if not.
                WebElement searchResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-component-type='s-search-result'][@data-index='" + i + "']")));
                List<WebElement> priceSpans = searchResult.findElements(By.xpath(".//span[@class='a-price a-text-price'][@data-a-strike='true']"));
                if (!priceSpans.isEmpty()) {
                    strikeThroughPriceCount++;
                    continue;
                }

                // Click on the product image and open a new browser tab if the item was not skipped as discounted
                WebElement productImageSpan = searchResult.findElement(By.xpath(".//span[@data-component-type='s-product-image']/a"));
                ((JavascriptExecutor)driver).executeScript("window.open('" + productImageSpan.getAttribute("href") + "','_blank');");
                switchToNewTab();

                // Check again on the product page if the item has a discount there, to be sure that the previous check is working correctly
                List<WebElement> strikeThroughPriceElements = driver.findElements(By.xpath("//*[@id='apex_desktop']//span[@class='a-price a-text-price'][@data-a-strike='true']"));
                if (!strikeThroughPriceElements.isEmpty()) {
                    continue;
                }

                // On the product page check if the item is In Stock
                WebElement availabilityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("availabilityInsideBuyBox_feature_div")));
                String availabilityText = availabilityElement.getText().toLowerCase();
                if (!availabilityText.contains("in stock")) {
                    System.out.println(String.format(NOT_IN_STOCK_MESSAGE, i));
                    driver.close();
                    driver.switchTo().window(driver.getWindowHandles().iterator().next());
                    continue;
                }

                // If the item passed so far, add it to cart
                WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit.add-to-cart")));
                addToCartButton.click();
                itemsAddedToCart++;

                // Close the browser tab and continue with the next item on page, or return an error.
                driver.close();
                driver.switchTo().window(driver.getWindowHandles().iterator().next());
            } catch (NoSuchElementException e) {
                System.out.println("Element not found for item at index " + i + ": " + e.getMessage());
            } catch (TimeoutException e) {
                System.out.println("Timed out waiting for element for item at index " + i + ": " + e.getMessage());
            } catch (WebDriverException e) {
                System.out.println("WebDriver exception for item at index " + i + ": " + e.getMessage());
            }
        }

        // Once all items are checked, refresh the page to update the cart count
        driver.navigate().refresh();

        WebElement cartCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-cart-count")));
        int cartCount = Integer.parseInt(cartCountElement.getText());

        // Total number of items on page based on the used selector is 22. Check the sum of skipped and added items is 22.
        if (cartCount + strikeThroughPriceCount != 22) {
            Assert.fail("The total count of items does not match the expected number. " +
                        "Discounted items counted: " + strikeThroughPriceCount + ", " +
                        "Items added to cart: " + cartCount);
        }

        // Go to the shopping cart
        WebElement cartIcon = driver.findElement(By.id("nav-cart-count-container"));
        cartIcon.click();

        // Verify that no items in the cart have become out of stock, or return an error
        List<WebElement> cartItems = driver.findElements(By.xpath("//div[@data-bundleitem='absent']"));
        for (WebElement item : cartItems) {
            String outOfStock = item.getAttribute("data-outofstock");
            if ("1".equals(outOfStock)) {
                Assert.fail("One or more items are out of stock.");
            }
        }

        System.out.println("All items in the cart are in stock.");
    }

    // Switch tabs function
    private void switchToNewTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
    }

    // Quit the web driver
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
