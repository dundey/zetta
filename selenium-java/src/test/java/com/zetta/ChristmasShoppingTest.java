import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class ChristmasShoppingTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5)); // Global wait time set to 5 seconds to speed up the test
        driver.get("https://www.amazon.com");
    }

    @Test
    public void addLaptopsToCart() {
        // Find the search box, type "laptop", and press the Enter key to search
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("laptop" + Keys.ENTER);

        // To keep track of how many items are added to cart and how many are skipped
        int strikeThroughPriceCount = 0;
        int itemsAddedToCart = 0;

        // Loop this a few times to add items to cart
        for (int i = 3; i <= 31; i++) {
            try {
                WebElement searchResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-component-type='s-search-result'][@data-index='" + i + "']")));
                
                // Check if the item has a strike-through price and skip if it does
                List<WebElement> priceSpans = searchResult.findElements(By.xpath(".//span[@class='a-price a-text-price'][@data-a-strike='true']"));
                if (!priceSpans.isEmpty()) {
                    strikeThroughPriceCount++;
                    continue;
                }

                // Click on the product image to open the product page in a new tab
                WebElement productImageSpan = searchResult.findElement(By.xpath(".//span[@data-component-type='s-product-image']/a"));
                ((JavascriptExecutor)driver).executeScript("window.open('" + productImageSpan.getAttribute("href") + "','_blank');");

                // Switch to the new tab
                ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                driver.switchTo().window(tabs.get(1));

                // Add the item to the cart
                WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit.add-to-cart")));
                addToCartButton.click();
                itemsAddedToCart++;

                // Close the current tab and return to the search results
                driver.close();
                driver.switchTo().window(tabs.get(0));
            } catch (Exception e) {
                // Log any exceptions encountered during the process
                System.out.println("Item at index " + i + " not processed: " + e.getMessage());
            }
        }

        // Refresh the page to update the cart count
        driver.navigate().refresh();

        // Get the current cart count
        WebElement cartCountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-cart-count")));
        int cartCount = Integer.parseInt(cartCountElement.getText());

        // Verify if the cart count and skipped items match the expected number of 22 total items on page (exluding 2 sponsored items that sit next to a banner)
        if (cartCount + strikeThroughPriceCount != 22) {
            throw new RuntimeException("The total count of items does not match the expected number. " +
                                       "Discounted items counted: " + strikeThroughPriceCount + ", " +
                                       "Items added to cart: " + cartCount);
        }

        // Navigate to the shopping cart
        WebElement cartIcon = driver.findElement(By.id("nav-cart-count-container"));
        cartIcon.click();

        // Check if any items in the cart are out of stock (assuming data-outofstock=1 is for out of stock items)
        List<WebElement> cartItems = driver.findElements(By.xpath("//div[@data-bundleitem='absent']"));
        for (WebElement item : cartItems) {
            String outOfStock = item.getAttribute("data-outofstock");
            if ("1".equals(outOfStock)) {       
                throw new RuntimeException("One or more items are out of stock.");
            }
        }

        // Print confirmation that all items in the cart are in stock
        System.out.println("All items in the cart are in stock.");
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
