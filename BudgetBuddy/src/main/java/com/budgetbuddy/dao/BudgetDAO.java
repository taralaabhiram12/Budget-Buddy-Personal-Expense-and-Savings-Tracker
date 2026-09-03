package com.budgetbuddy.dao;

import com.budgetbuddy.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BudgetDAO {

    public void saveBudget(double budgetAmount) {
        String deleteSql = "DELETE FROM monthly_budget";
        String insertSql = "INSERT INTO monthly_budget(budget_amount) VALUES (?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            deleteStatement.executeUpdate();

            insertStatement.setDouble(1, budgetAmount);
            insertStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getBudgetAmount() {
        String sql = "SELECT budget_amount FROM monthly_budget LIMIT 1";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble("budget_amount");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}