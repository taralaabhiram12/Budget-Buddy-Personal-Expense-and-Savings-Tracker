# Budget Buddy – Personal Budget & Savings Tracker

## Overview
Budget Buddy is a JavaFX desktop application for managing personal finances. It helps users track income and expenses, plan monthly budgets, monitor savings goals, view financial charts, search transactions, and export records as CSV files.

The application works offline and stores data locally using SQLite.

## Objectives
- Track and manage income and expenses.
- Create and monitor monthly budgets.
- Set and track savings goals.
- View financial summaries and charts.
- Search and filter transaction records.
- Export financial data to CSV.

## Technologies Used
| Technology | Purpose |
|---|---|
| Java | Core application development |
| JavaFX | Graphical user interface |
| FXML | UI layout design |
| CSS | UI styling |
| SQLite | Local database |
| JDBC | Database connectivity |
| Maven | Dependency and build management |

## Features
- Dashboard with balance, income, expenses, budget status, and savings progress.
- Transaction management with add, update, delete, search, and view operations.
- Budget planner with spending monitoring.
- Savings goals with progress tracking.
- Financial charts using JavaFX.
- Search and filter functionality.
- CSV export.
- Local SQLite database storage.

## Project Structure
```text
BudgetBuddy/
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/budgetbuddy/
│       │   ├── Main.java
│       │   ├── controller/
│       │   ├── model/
│       │   ├── dao/
│       │   ├── service/
│       │   └── util/
│       └── resources/
│           ├── fxml/
│           ├── css/
│           └── images/
└── budget_buddy.db
```

## Architecture
The project follows the MVC (Model–View–Controller) architecture.

- **Model:** Represents data such as Transaction, Budget, and SavingsGoal.
- **View:** JavaFX and FXML user interfaces.
- **Controller:** Handles user interactions and application flow.
- **DAO:** Performs database operations.
- **Service:** Contains business logic.
- **Database Manager:** Handles SQLite connectivity.

## How to Run

### Prerequisites
- Java JDK
- Maven
- JavaFX dependencies configured through Maven

### Run
Open the project folder in a terminal and execute:

```bash
mvn clean javafx:run
```

### Compile
```bash
mvn clean compile
```

### Package
```bash
mvn clean package
```

## Database
Budget Buddy uses SQLite for local storage of transactions, budgets, and savings goals. No separate database server is required.

## CSV Export
Transaction records can be exported to CSV format for viewing and analysis in applications such as Microsoft Excel or Google Sheets.

## Future Enhancements
- User authentication and multiple-user support.
- PDF report generation.
- Cloud synchronization.
- Automated financial reminders.
- Machine Learning-based expense prediction.
- Mobile application version.

## Project
**Budget Buddy – Personal Budget & Savings Tracker**

**Technologies:** Java • JavaFX • SQLite • JDBC • Maven • FXML • CSS
