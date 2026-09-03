package com.budgetbuddy.model;

public class Transaction {

    private int id;
    private String type;
    private String category;
    private double amount;
    private String note;
    private String date;

    public Transaction(int id, String type, String category, double amount, String note, String date) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    public Transaction(String type, String category, double amount, String note, String date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }
}