# Todoist API Automation Suite

This project is an automated API testing suite for the **Todoist** project management API.  
It is built using **Java**, **Maven**, **TestNG**, and **RestAssured**.  
The suite validates various project creation scenarios, including:

- Successful project creation
- Missing or invalid input
- Authorization errors

---

## 📌 Features

- Automated API tests for **Todoist** endpoints
- **Data-driven testing** using POJOs
- **TestNG** for test orchestration
- **ExtentReports** for detailed reporting
- **Maven** for build and dependency management

---

## 🔧 Prerequisites

- **Java 21** or higher
- **Maven 3.x**
- **Internet connection** (for accessing Todoist API)

---

## ⚙️ Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/shailesh6363/api_todoist_automation_rest_assured

📂 Project Structure
todoist-api-automation/
│
├── pom.xml                         # Maven dependencies and build config
├── README.md                       # Project documentation
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── pojo                 # POJO classes for request/response
│   │   └── resources                # Application resources if needed
│   │
│   └── test
│       ├── java
│       │   ├── tests                # Test classes for Todoist APIs
│       │   ├── utils                # Utility classes (helpers, constants)
│       │   └── base                 # Base test setup/config
│       │
│       └── resources
│           ├── config.properties    # API credentials and configurations
│           └── testdata             # Test data files (JSON, CSV, etc.)
│
└── test-output                      # ExtentReports output after execution



🏗 Framework Architecture
           ┌───────────────────────────┐
           │       TestNG Runner        │
           │   (test orchestration)     │
           └─────────────┬─────────────┘
                         │
                         ▼
               ┌─────────────────┐
               │  Test Classes   │
               │ (Todoist APIs)  │
               └───────┬─────────┘
                       │
                       ▼
        ┌────────────────────────────────┐
        │      RestAssured Library        │
        │ (HTTP Requests + Validations)   │
        └─────────────┬──────────────────┘
                      │
                      ▼
             ┌───────────────────┐
             │   POJO Classes     │
             │ (Request/Response) │
             └─────────┬─────────┘
                       │
                       ▼
              ┌────────────────────┐
              │   Todoist API       │
              │ (Live Endpoints)    │
              └─────────┬──────────┘
                        │
                        ▼
              ┌────────────────────┐
              │  ExtentReports      │
              │ (HTML Reports)      │
              └────────────────────┘

   
