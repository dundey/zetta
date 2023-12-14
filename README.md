# Zetta Technical Interview Task - QA Engineer

## API JavaScript Project: Santa helper

This project consists of a suite of API tests written in JavaScript, designed to validate the functionality of a mock API endpoint simulating the retrieval of blog posts. It demonstrates the use of the popular JavaScript testing frameworks - Mocha and Chai.

### Prerequisites

To run this project, you need:
- Node.js. If you don't have Node.js installed, you can download and install it from [Node.js official website](https://nodejs.org/en). This will also install npm (Node Package Manager), which is essential for managing the project's dependencies. 

### Important files

- `package.json`: This file is used by npm to manage the project's modules, scripts, and to identify the project and its dependencies.
- `santaHelperTests.js`: This file contains the API test scenarios for the project.

### Setting Up the Project

1. **Clone the Repository**: Clone this project to your local machine.

2. **Install Dependencies**: Run `npm install` in the root folder

### Running the Test

To run the tests, run the command `npm test`.

### Test Scenarios for `santaHelper.js`

- Check that each of the 10 users has 10 posts
- Check that each post has a unique ID
- Count how many posts each user has