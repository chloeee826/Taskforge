package com.taskforge.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taskforge.app.data.TaskforgeDbHelper;

public class GoalSettingsActivity extends Activity {
    private static final String WATER_HABIT = "Drink Water";
    private static final String EXERCISE_HABIT = "Exercise";
    private static final String SLEEP_HABIT = "Sleep";

    private TaskforgeDbHelper dbHelper;
    private EditText waterGoalInput;
    private EditText exerciseGoalInput;
    private EditText sleepGoalInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_settings);

        dbHelper = new TaskforgeDbHelper(this);
        waterGoalInput = findViewById(R.id.waterGoalInput);
        exerciseGoalInput = findViewById(R.id.exerciseGoalInput);
        sleepGoalInput = findViewById(R.id.sleepGoalInput);

        Button saveGoalsButton = findViewById(R.id.saveGoalsButton);
        saveGoalsButton.setOnClickListener(v -> saveGoals());

        loadGoals();
    }

    private void loadGoals() {
        waterGoalInput.setText(String.valueOf(dbHelper.getTargetAmountByName(WATER_HABIT)));
        exerciseGoalInput.setText(String.valueOf(dbHelper.getTargetAmountByName(EXERCISE_HABIT)));
        sleepGoalInput.setText(String.valueOf(dbHelper.getTargetAmountByName(SLEEP_HABIT)));
    }

    private void saveGoals() {
        int waterGoal = readPositiveGoal(waterGoalInput);
        int exerciseGoal = readPositiveGoal(exerciseGoalInput);
        int sleepGoal = readPositiveGoal(sleepGoalInput);

        if (waterGoal <= 0 || exerciseGoal <= 0 || sleepGoal <= 0) {
            Toast.makeText(this, "Please enter goals greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.updateTargetAmountByName(WATER_HABIT, waterGoal);
        dbHelper.updateTargetAmountByName(EXERCISE_HABIT, exerciseGoal);
        dbHelper.updateTargetAmountByName(SLEEP_HABIT, sleepGoal);
        Toast.makeText(this, "Daily goals saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int readPositiveGoal(EditText input) {
        String value = input.getText().toString().trim();
        if (value.isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
