package com.budgetbuddy.controller;

import com.budgetbuddy.dao.BudgetDAO;
import com.budgetbuddy.dao.SavingsGoalDAO;
import com.budgetbuddy.dao.TransactionDAO;
import com.budgetbuddy.model.SavingsGoal;
import com.budgetbuddy.model.Transaction;
import com.budgetbuddy.service.FinanceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {

    @FXML
    private Label balanceLabel;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expenseLabel;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField noteField;

    @FXML
    private TextField dateField;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> noteColumn;

    @FXML
    private TextField goalNameField;

    @FXML
    private TextField targetAmountField;

    @FXML
    private TextField savedAmountField;

    @FXML
    private Label goalStatusLabel;

    @FXML
    private ProgressBar goalProgressBar;

    @FXML
    private TextField budgetAmountField;

    @FXML
    private Label budgetStatusLabel;

    @FXML
    private ProgressBar budgetProgressBar;

    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final FinanceService financeService = new FinanceService();
    private final SavingsGoalDAO savingsGoalDAO = new SavingsGoalDAO();
    private final BudgetDAO budgetDAO = new BudgetDAO();

    private Transaction selectedTransactionForEdit;

    @FXML
    public void initialize() {
        typeBox.setItems(FXCollections.observableArrayList("Income", "Expense"));
        typeBox.setValue("Expense");
        dateField.setText(getCurrentDateTime());

        setupTransactionTable();
        loadDashboardData();
        loadSavingsGoal();
        loadBudgetStatus();
    }

    private void setupTransactionTable() {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    @FXML
    private void handleAddTransaction() {
        try {
            String type = typeBox.getValue();
            String category = categoryField.getText();
            String amountText = amountField.getText();
            String note = noteField.getText();
            String date = dateField.getText();

            if (type == null || category.isBlank() || amountText.isBlank() || date.isBlank()) {
                messageLabel.setText("Please fill type, category, amount, and date.");
                return;
            }

            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                messageLabel.setText("Amount must be greater than 0.");
                return;
            }

            Transaction transaction = new Transaction(type, category, amount, note, date);
            transactionDAO.addTransaction(transaction);

            categoryField.clear();
            amountField.clear();
            noteField.clear();
            dateField.setText(getCurrentDateTime());

            messageLabel.setText("Transaction added successfully.");
            loadDashboardData();

        } catch (NumberFormatException e) {
            messageLabel.setText("Amount must be a valid number.");
        } catch (Exception e) {
            messageLabel.setText("Something went wrong.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoadSelectedTransaction() {
        selectedTransactionForEdit = transactionTable.getSelectionModel().getSelectedItem();

        if (selectedTransactionForEdit == null) {
            messageLabel.setText("Please select a transaction to edit.");
            return;
        }

        typeBox.setValue(selectedTransactionForEdit.getType());
        categoryField.setText(selectedTransactionForEdit.getCategory());
        amountField.setText(formatAmount(selectedTransactionForEdit.getAmount()));
        noteField.setText(safeText(selectedTransactionForEdit.getNote()));
        dateField.setText(selectedTransactionForEdit.getDate());

        messageLabel.setText("Selected transaction loaded for editing.");
    }

    @FXML
    private void handleUpdateTransaction() {
        if (selectedTransactionForEdit == null) {
            messageLabel.setText("Please load a selected transaction first.");
            return;
        }

        try {
            String type = typeBox.getValue();
            String category = categoryField.getText();
            String amountText = amountField.getText();
            String note = noteField.getText();
            String date = dateField.getText();

            if (type == null || category.isBlank() || amountText.isBlank() || date.isBlank()) {
                messageLabel.setText("Please fill type, category, amount, and date.");
                return;
            }

            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                messageLabel.setText("Amount must be greater than 0.");
                return;
            }

            Transaction updatedTransaction = new Transaction(
                    selectedTransactionForEdit.getId(),
                    type,
                    category,
                    amount,
                    note,
                    date
            );

            transactionDAO.updateTransaction(updatedTransaction);

            selectedTransactionForEdit = null;
            categoryField.clear();
            amountField.clear();
            noteField.clear();
            dateField.setText(getCurrentDateTime());

            messageLabel.setText("Transaction updated successfully.");
            loadDashboardData();

        } catch (NumberFormatException e) {
            messageLabel.setText("Amount must be a valid number.");
        } catch (Exception e) {
            messageLabel.setText("Could not update transaction.");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDeleteTransaction() {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            messageLabel.setText("Please select a transaction to delete.");
            return;
        }

        transactionDAO.deleteTransaction(selectedTransaction.getId());
        messageLabel.setText("Transaction deleted successfully.");
        loadDashboardData();
    }

    @FXML
    private void handleSearchTransactions() {
        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isBlank()) {
            loadDashboardData();
            return;
        }

        List<Transaction> transactions = transactionDAO.getAllTransactions();
        List<Transaction> filteredTransactions = transactions.stream()
                .filter(transaction ->
                        transaction.getType().toLowerCase().contains(keyword)
                                || transaction.getCategory().toLowerCase().contains(keyword)
                                || String.valueOf(transaction.getAmount()).contains(keyword)
                                || transaction.getDate().toLowerCase().contains(keyword)
                                || safeText(transaction.getNote()).toLowerCase().contains(keyword))
                .toList();

        transactionTable.setItems(FXCollections.observableArrayList(filteredTransactions));
        messageLabel.setText(filteredTransactions.size() + " transaction(s) found.");
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        messageLabel.setText("");
        loadDashboardData();
    }

    @FXML
    private void handleSaveGoal() {
        try {
            String goalName = goalNameField.getText();
            String targetText = targetAmountField.getText();
            String savedText = savedAmountField.getText();

            if (goalName.isBlank() || targetText.isBlank() || savedText.isBlank()) {
                messageLabel.setText("Please fill all savings goal fields.");
                return;
            }

            double targetAmount = Double.parseDouble(targetText);
            double savedAmount = Double.parseDouble(savedText);

            if (targetAmount <= 0 || savedAmount < 0) {
                messageLabel.setText("Goal amounts are invalid.");
                return;
            }

            SavingsGoal goal = new SavingsGoal(goalName, targetAmount, savedAmount);
            savingsGoalDAO.saveGoal(goal);

            messageLabel.setText("Savings goal saved successfully.");
            loadSavingsGoal();

        } catch (NumberFormatException e) {
            messageLabel.setText("Goal amounts must be valid numbers.");
        } catch (Exception e) {
            messageLabel.setText("Something went wrong while saving goal.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSaveBudget() {
        try {
            String budgetText = budgetAmountField.getText();

            if (budgetText.isBlank()) {
                messageLabel.setText("Please enter monthly budget amount.");
                return;
            }

            double budgetAmount = Double.parseDouble(budgetText);

            if (budgetAmount <= 0) {
                messageLabel.setText("Budget amount must be greater than 0.");
                return;
            }

            budgetDAO.saveBudget(budgetAmount);
            messageLabel.setText("Monthly budget saved successfully.");
            loadBudgetStatus();

        } catch (NumberFormatException e) {
            messageLabel.setText("Budget amount must be a valid number.");
        }
    }


    @FXML
    private void handleExportCsv() {
        List<Transaction> transactions = transactionDAO.getAllTransactions();

        if (transactions.isEmpty()) {
            messageLabel.setText("No transactions available to export.");
            return;
        }

        String fileName = "budget_buddy_report.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("ID,Type,Category,Amount,Date,Note");

            for (Transaction transaction : transactions) {
                writer.println(
                        transaction.getId() + ","
                                + csvValue(transaction.getType()) + ","
                                + csvValue(transaction.getCategory()) + ","
                                + transaction.getAmount() + ","
                                + csvValue(transaction.getDate()) + ","
                                + csvValue(safeText(transaction.getNote()))
                );
            }

            messageLabel.setText("CSV report exported: " + fileName);

        } catch (Exception e) {
            messageLabel.setText("Could not export CSV report.");
            e.printStackTrace();
        }
    }
    private void loadDashboardData() {
        List<Transaction> transactions = transactionDAO.getAllTransactions();

        double income = financeService.calculateTotalIncome(transactions);
        double expense = financeService.calculateTotalExpense(transactions);
        double balance = financeService.calculateBalance(transactions);

        balanceLabel.setText("Rs. " + formatAmount(balance));
        incomeLabel.setText("Rs. " + formatAmount(income));
        expenseLabel.setText("Rs. " + formatAmount(expense));

        transactionTable.setItems(FXCollections.observableArrayList(transactions));
        loadBudgetStatus();
    }

    private void loadSavingsGoal() {
        SavingsGoal goal = savingsGoalDAO.getGoal();

        if (goal == null) {
            goalStatusLabel.setText("No goal set");
            goalProgressBar.setProgress(0);
            return;
        }

        goalNameField.setText(goal.getGoalName());
        targetAmountField.setText(formatAmount(goal.getTargetAmount()));
        savedAmountField.setText(formatAmount(goal.getCurrentAmount()));

        double progress = goal.getCurrentAmount() / goal.getTargetAmount();
        progress = Math.min(progress, 1);

        goalProgressBar.setProgress(progress);
        goalStatusLabel.setText(goal.getGoalName() + " - " + formatAmount(progress * 100) + "% completed");
    }

    private void loadBudgetStatus() {
        double budgetAmount = budgetDAO.getBudgetAmount();

        if (budgetAmount <= 0) {
            budgetStatusLabel.setText("No budget set");
            budgetProgressBar.setProgress(0);
            return;
        }

        List<Transaction> transactions = transactionDAO.getAllTransactions();
        double totalExpense = financeService.calculateTotalExpense(transactions);

        budgetAmountField.setText(formatAmount(budgetAmount));

        double progress = totalExpense / budgetAmount;
        budgetProgressBar.setProgress(Math.min(progress, 1));

        if (totalExpense > budgetAmount) {
            double exceeded = totalExpense - budgetAmount;
            budgetStatusLabel.setText("Budget exceeded by Rs. " + formatAmount(exceeded));
        } else {
            double remaining = budgetAmount - totalExpense;
            budgetStatusLabel.setText("Spent Rs. " + formatAmount(totalExpense)
                    + " | Remaining Rs. " + formatAmount(remaining));
        }
    }

    private String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }

    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }


    private String csvValue(String text) {
        String value = safeText(text).replace("\"", "\"\"");
        return "\"" + value + "\"";
    }
    private String safeText(String text) {
        return text == null ? "" : text;
    }
}
