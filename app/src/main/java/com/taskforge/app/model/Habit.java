package com.taskforge.app.model;

public class Habit {
    private final long id;
    private final String name;
    private final String unit;
    private final int targetAmount;
    private final int todayAmount;

    public Habit(long id, String name, String unit, int targetAmount, int todayAmount) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.targetAmount = targetAmount;
        this.todayAmount = todayAmount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public int getTodayAmount() {
        return todayAmount;
    }

    public int getProgressPercent() {
        if (targetAmount <= 0) {
            return 0;
        }
        return Math.min(100, Math.round((todayAmount * 100f) / targetAmount));
    }

    public boolean isCompleteToday() {
        return todayAmount >= targetAmount;
    }
}
