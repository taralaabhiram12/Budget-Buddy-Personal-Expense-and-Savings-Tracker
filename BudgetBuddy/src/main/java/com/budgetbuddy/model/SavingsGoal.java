package com.budgetbuddy.model;

public class SavingsGoal {

    private int id;
    private String goalName;
    private double targetAmount;
    private double currentAmount;

    public SavingsGoal(int id, String goalName, double targetAmount, double currentAmount) {
        this.id = id;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
    }

    public SavingsGoal(String goalName, double targetAmount, double currentAmount) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
    }

    public int getId() {
        return id;
    }

    public String getGoalName() {
        return goalName;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }
}