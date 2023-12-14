# Zetta Technical Interview Task - QA Engineer

## Crawler Python Project: Rudolph the crawler test

This project uses Scrapy with Selenium, designed to crawl amazon.com and check if all category links under Shop by Department are working.

### Prerequisites

To run this project, you need the following:

- Python 3.x**
- Scrapy (The web scraping framework)
- Selenium (Used to help scrappy interact with dynamic content)
- Chrome web browser (for running the crawler)

### Important Project Files

- `crawler_python/settings.py`: Where all the Scrapy settings are stored
- `crawler_python/crawler_python/spiders`: Where the web crawlers are created (e.g. rudolph_crawler.py)

### Setting Up the Project

1. **Install Python**: Ensure latest Python is installed on your system. You can download it from [Python's website](https://www.python.org/downloads/).

2. **Clone the Repository**: Clone this project to your local machine.

3. **Install Python requirements**: Install Scrapy and Selenium by running from the root folder:
```
pip install -r requirements.txt
```
4. **Install Chrome**: If needed, you can download it from [Chrome's website](www.google.com/chrome).

### Running the crawler

To run the crawler:

1. Navigate to folder crawler_python in your terminal.

2. Run the command `scrapy crawl rudolph_crawler` to run the crawler.

### Overview of `rudolph_crawler.py`

- Goes to amazon.com in headless mode, with a real-looking user-agent to reduce the chance of being blocked.
- Sometimes Amazon will not load properly or break on interaction, and the crawler needs to be rerun.
- Amazon is a dynamic website and the crawler can't do anything alone, so we needed the help of a UI framework like Selenium.
- We use Selenium to click through the menus, so that the needed links will load in the DOM.
- The crawler, now also known as Rudolph, is happy to visit and check if all links are working.
- Unfortunately, Amazon is not so welcoming and will often block Rudolph.
- Rudolph is stubborn and will visit every one of the category links as per instructions, and will generate a report that includes link, title, and status.
- Most of the statuses will return as 503, but some will go through with 200. Rudolph has tried different approaches for simulating more human-like behavior, but Amazon is not so easy to fool.

### Notes
I'm sure there is a way to get the correct codes for each URL, but this crawler has already passed the scope of "simple", and using a different IP address for each URL check, or loading each page in full with Selenium, is not very efficient.