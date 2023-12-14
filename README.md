# Zetta Technical Interview Task - QA Engineer

## Overview of the tasks/projects

3 of the tasks have a separate branch:
- Acceptance criteria implementation - `dv/selenium-java-task`
- Simple Site Crawl - `dv/crawler-python-task`
- API Test - `dv/TODO`

For the last task "Git Usage, Best Practices, and Submission Details", I have followed most of the criteria, but not everything is perfect. For example, I wouldn't use the main branch for a single README file, and then have completly different code in separate branches.

For divercity I have used Java, JavaScript and Python.

### dv/selenium-java-task
For the UI tests I would have prefered to use JavaScript/TypeScript and Cypress or Playwright, even though with Cypress the approach would have been different, as you can't use multiple tabs there, the same I used them with Selenium. Maybe I had to index the dynamic results from the page, and then visit each URL and etc.

### dv/crawler-python-task
The crawler gets help from Selenium as well, but Amazon has a lot of dynamic content and it needed a UI framework to go through some menus. While I would prefer to use JavaScript for everything, Python was a good choice for this task.

### dv/TODO
TODO