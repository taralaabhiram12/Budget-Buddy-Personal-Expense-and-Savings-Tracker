package com.budgetbuddy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.budgetbuddy.model.SavingsGoal;
import com.budgetbuddy.util.DatabaseManager;

public class SavingsGoalDAO {

    public void saveGoal(SavingsGoal goal) {
        String deleteSql = "DELETE FROM savings_goals";
        String insertSql = "INSERT INTO savings_goals(goal_name, target_amount, current_amount) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            deleteStatement.executeUpdate();

            insertStatement.setString(1, goal.getGoalName());
            insertStatement.setDouble(2, goal.getTargetAmount());
            insertStatement.setDouble(3, goal.getCurrentAmount());
            insertStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SavingsGoal getGoal() {
        String sql = "SELECT * FROM savings_goals LIMIT 1";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return new SavingsGoal(
                        resultSet.getInt("id"),
                        resultSet.getString("goal_name"),
                        resultSet.getDouble("target_amount"),
                        resultSet.getDouble("current_amount")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}