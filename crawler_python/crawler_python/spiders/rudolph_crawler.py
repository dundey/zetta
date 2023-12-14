import scrapy
from scrapy.selector import Selector
from urllib.parse import urljoin
import requests
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from datetime import datetime

class RudolphCrawlerSpider(scrapy.Spider):
    name = "rudolph_crawler"

    def __init__(self):
        self.start_urls = ["https://www.amazon.com/"]
        options = Options()
        options.add_argument('--headless') # run the crawler in headless mode
        self.driver = webdriver.Chrome(options=options) # Initialize the Chrome driver
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

        # Move the cursor to the 'Accessories & Supplies' element to trigger an additional event
        action = ActionChains(self.driver)
        action.move_to_element(accessories_supplies_element).perform()

        # Click on the Back to Main Menu button
        element = WebDriverWait(self.driver, 10).until(
            EC.element_to_be_clickable((By.XPATH, '//*[@id="hmenu-content"]/ul[5]/li[1]/a/div'))
        )
        self.driver.execute_script("arguments[0].click();", element)

        # Iterate through the list elements
        for ul_number in range(5, 27):
            ul_xpath = f'/html/body/div[3]/div[2]/div/ul[{ul_number}]'
            ul_element = WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.XPATH, ul_xpath)))
            self.process_ul(ul_element)

        self.driver.quit()

    def process_ul(self, ul_element):
        base_url = self.driver.current_url
        ul_html = ul_element.get_attribute('outerHTML')
        response = Selector(text=ul_html)

        # Extract the URL and title from the list
        for li in response.xpath("//li"):
            link_element = li.xpath("./a/@href").get()
            title_element = li.xpath("./a/text()").get()
            if link_element and title_element:
                full_link = urljoin(base_url, link_element)
                title = title_element.strip()
                status = self.get_link_status(full_link)
                self.visited_links.append((full_link, title, status))


    def get_link_status(self, url):
        # How to write the different status codes in the results file
        try:
            response = requests.get(url, allow_redirects=True, stream=True)
            if response.status_code == 200:
                return "OK"
            elif response.status_code == 503:
                return "Crawler Blocked"
            else:
                return "Dead link"
        except Exception as e:
            self.logger.error(f"Error visiting {url}: {str(e)}")
            return "Dead link"


    def write_results(self):
        # Writing the results to a file
        timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
        results_file = f"{timestamp}_results.txt"
        with open(results_file, "w") as file:
            for link, title, status in self.visited_links:
                file.write(f"{link}, {title}, {status}\n")

    def closed(self, reason):
        # Closing the crawler and saving the results
        self.write_results()