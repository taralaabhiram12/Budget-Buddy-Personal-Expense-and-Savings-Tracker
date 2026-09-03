package com.budgetbuddy.service;

import java.util.List;

import com.budgetbuddy.model.Transaction;

public class FinanceService {

    public double calculateTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.getType().equalsIgnoreCase("Income"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double calculateTotalExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(transaction -> transaction.getType().equalsIgnoreCase("Expense"))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double calculateBalance(List<Transaction> transactions) {
        return calculateTotalIncome(transactions) - calculateTotalExpense(transactions);
    }
}