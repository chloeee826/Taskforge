package com.taskforge.app.model;

public class Habit {
    private final long id;
    private final String name;
    private final String iconName;
    private final String unit;
    private final int targetAmount;
    private final int todayAmount;
    private final int smallLogAmount;
    private final int largeLogAmount;

    public Habit(
            long id,
            String name,
            String iconName,
            String unit,
            int targetAmount,
            int todayAmount,
            int smallLogAmount,
            int largeLogAmount
    ) {
        this.id = id;
        this.name = name;
        this.iconName = iconName;
        this.unit = unit;
        this.targetAmount = targetAmount;
        this.todayAmount = todayAmount;
        this.smallLogAmount = smallLogAmount;
        this.largeLogAmount = largeLogAmount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconName() {
        return iconName;
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

    public int getSmallLogAmount() {
        return smallLogAmount;
    }

    public int getLargeLogAmount() {
        return largeLogAmount;
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
