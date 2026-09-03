package com.budgetbuddy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:budget_buddy.db";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void initializeDatabase() {
        String transactionsTable = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT NOT NULL,
                    category TEXT NOT NULL,
                    amount REAL NOT NULL,
                    note TEXT,
                    date TEXT NOT NULL
                );
                """;

        String savingsTable = """
                CREATE TABLE IF NOT EXISTS savings_goals (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    goal_name TEXT NOT NULL,
                    target_amount REAL NOT NULL,
                    current_amount REAL NOT NULL
                );
                """;

        String budgetTable = """
                CREATE TABLE IF NOT EXISTS monthly_budget (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    budget_amount REAL NOT NULL
                );
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(transactionsTable);
            statement.execute(savingsTable);
            statement.execute(budgetTable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}