# Zetta Technical Interview Task - QA Engineer

## Crawler Python Project: Rudolph the crawler test

This project is Scrapy with Selenium, designed to crawl on amazon.com and check if all category links under Shop by Department are working.

### Prerequisites

To run this project, you need the following:

- Python 3.x** (Python 3.9.6 is used in this project)
- Scrapy (The web scraping framework)
- Selenium (Used to help scrappy interact with dynamic content)
- Chrome web browser (for running the crawler)

### Important Project Files

- `crawler_python/settings.py`: Where all the Scrapy settings are stored
- `crawler_python/crawler_python/spiders`: Where the web crawlers are created (e.g. rudolph_crawler.py)

### Setting Up the Project

1. **Install Python**: Ensure latest Python is installed on your system. You can download it from [Python's website](https://www.python.org/downloads/).

2. **Install Python requirements**: Install Scrapy and SElenium using
```
pip install -r requirements.txt
```
4. **Install Chrome**: If needed, you can download it from [Chrome's website](www.google.com/chrome).

3. **Clone the Repository**: Clone this project to your local machine.

### Running the crawler

To run the crawler:

1. Navigate to crawler_python/crawler_python in your terminal or command prompt.

2. Run the command `scrapy crawl rudolph_crawler`. This command will run the crawler in headed mode.

### Overview of `rudolph_crawler.pu`

- Goes to amazon.com, but amazon blocks crawlers so we fake the user-agent with a real looking one
- Amazon is a dynamic website and the crawler can't do anything alone, so we needed the help or some UI framework like Selenium
- We use selenium to click through the menus, so that the needed menus will load in the DOM
- So far so good, but scrapy was never able to catch the links that I wanted
- Scrapy fails his duties

### Notes

This is my first time creating a web crawler, and amazon is not a great site to be crawled by a bot. Crawlers often hit walls, at one point I was getting catpcha during my tries. The only success I had at one point is to get the links reported back from the homepage header. Scrapy is not the only cralwer I've tried and selenium is not the only framework, but no sucess on amazon. Another thing, based on the description of the task, I have a feeling that this task was written before the last redesign of amazon, because the “Shop By Department” is not a dropdown menu now.