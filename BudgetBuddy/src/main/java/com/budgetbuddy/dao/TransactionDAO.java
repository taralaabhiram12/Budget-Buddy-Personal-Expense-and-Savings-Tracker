package com.budgetbuddy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.budgetbuddy.model.Transaction;
import com.budgetbuddy.util.DatabaseManager;

public class TransactionDAO {

    public void addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions(type, category, amount, note, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.getType());
            statement.setString(2, transaction.getCategory());
            statement.setDouble(3, transaction.getAmount());
            statement.setString(4, transaction.getNote());
            statement.setString(5, transaction.getDate());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getString("category"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("note"),
                        resultSet.getString("date")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }
    public void deleteTransaction(int id) {
    String sql = "DELETE FROM transactions WHERE id = ?";

    try (Connection connection = DatabaseManager.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, id);
        statement.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void updateTransaction(Transaction transaction) {
    String sql = "UPDATE transactions SET type = ?, category = ?, amount = ?, note = ?, date = ? WHERE id = ?";

    try (Connection connection = DatabaseManager.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, transaction.getType());
        statement.setString(2, transaction.getCategory());
        statement.setDouble(3, transaction.getAmount());
        statement.setString(4, transaction.getNote());
        statement.setString(5, transaction.getDate());
        statement.setInt(6, transaction.getId());

        statement.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}