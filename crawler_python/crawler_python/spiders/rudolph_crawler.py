import scrapy
from scrapy.selector import Selector
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
from selenium.common.exceptions import NoSuchElementException
from datetime import datetime

class RudolphCrawlerSpider(scrapy.Spider):
    name = "rudolph_crawler"

    def __init__(self):
        self.start_urls = ["https://www.amazon.com/"]
        self.driver = webdriver.Chrome()
        self.visited_links = []

    def parse(self, response):
        self.driver.get(response.url)

        # Click on the hamburger menu
        WebDriverWait(self.driver, 10).until(EC.element_to_be_clickable((By.ID, "nav-hamburger-menu"))).click()

        # Click 'See All' for 'Shop by Department'
        WebDriverWait(self.driver, 10).until(EC.element_to_be_clickable((By.XPATH, "//div[contains(text(), 'shop by department')]/following::a[contains(@class, 'hmenu-compressed-btn')][1]"))).click()

        # Click on 'Electronics'
        WebDriverWait(self.driver, 10).until(EC.element_to_be_clickable((By.XPATH, "//div[contains(text(), 'Electronics')]"))).click()

        # Locate the 'Accessories & Supplies' element
        accessories_supplies_element = WebDriverWait(self.driver, 10).until(EC.element_to_be_clickable((By.XPATH, "//li/a[contains(text(), 'Accessories & Supplies')]")))

        # Create an ActionChains object and move the cursor to the 'Accessories & Supplies' element
        action = ActionChains(self.driver)
        action.move_to_element(accessories_supplies_element).perform()

        # Click on the back to Main Menu button
        WebDriverWait(self.driver, 10).until(EC.element_to_be_clickable((By.XPATH, '//*[@id="hmenu-content"]/ul[5]/li[1]/a/div'))).click()

            # Process each ul
        for ul_number in range(5, 27):
            ul_xpath = f'/html/body/div[3]/div[2]/div/ul[{ul_number}]/li[1]'
            
            try:
                ul = WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.XPATH, ul_xpath)))
                self.process_ul(ul)
            except NoSuchElementException:
                self.logger.info(f"No ul found at index {ul_number}")

        # Writing results to file
        self.write_results()

    def process_ul(self, ul_element):
        try:
            ul_html = ul_element.get_attribute('outerHTML')
            response = Selector(text=ul_html)

            for li in response.xpath("//li[contains(@class, 'hmenu-item')]"):
                link = li.xpath("./a/@href").get()
                if link:
                    full_link = response.urljoin(link)
                    self.visited_links.append((full_link, self.get_title(full_link), "OK"))
        except Exception as e:
            self.logger.error(f"Error processing ul element: {e}")

    def get_title(self, url):
        try:
            self.driver.get(url)
            WebDriverWait(self.driver, 20).until(lambda d: d.title != "")  # Wait for title to load
            return self.driver.title
        except Exception as e:
            self.logger.error(f"Error visiting {url}: {str(e)}")
            return "Dead link"

    def write_results(self):
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
        results_file = f"{timestamp}_results.txt"
        with open(results_file, "w") as file:
            for link, title, status in self.visited_links:
                file.write(f"{link}, {title}, {status}\n")

    def closed(self, reason):
        self.driver.quit()
        self.write_results()
