# Todoist API Automation Suite

This project is an automated API testing suite for the Todoist project management API. It is built using Java, Maven, TestNG, and RestAssured. The suite validates various project creation scenarios, including successful creation, missing or invalid input, and authorization errors.

## Features

- Automated API tests for Todoist endpoints
- Data-driven testing using POJOs
- TestNG for test orchestration
- ExtentReports for detailed test reporting
- Maven for build and dependency management

## Prerequisites

- Java 8 or higher
- Maven 3.x
- Internet connection (for accessing Todoist API)

## Setup

1. Clone the repository:
2. Navigate to the project directory:
3. Update the configuration in src/test/resources/config.properties with your Todoist API credentials:
   - baseUrl: Base URL for Todoist API
   - tokenUrl: OAuth token URL
   - clientId: Your Todoist client ID
   - clientSecret: Your Todoist client secret
   - api_token: Your Todoist API token

## Running Tests

To execute the test suite, run:

## Reports

After test execution, ExtentReports will be generated in the test-output directory.

## License

This project is licensed under the MIT License. create readme.md file  please rephrase this
