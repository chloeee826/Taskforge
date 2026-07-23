package com.taskforge.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taskforge.app.data.TaskforgeDbHelper;
import com.taskforge.app.model.Habit;
import com.taskforge.app.ui.HabitAdapter;

public class MainActivity extends Activity {
    private TaskforgeDbHelper dbHelper;
    private HabitAdapter habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TaskforgeDbHelper(this);
        habitAdapter = new HabitAdapter(this::logAmount, this::undoLastLog);

        RecyclerView habitRecyclerView = findViewById(R.id.habitRecyclerView);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        habitRecyclerView.setAdapter(habitAdapter);

        Button editGoalsButton = findViewById(R.id.editGoalsButton);
        editGoalsButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GoalSettingsActivity.class)));

        loadHabits();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (habitAdapter != null) {
            loadHabits();
        }
    }

    private void loadHabits() {
        habitAdapter.submitList(dbHelper.getTodayHabits());
    }

    private void logAmount(Habit habit, int amount) {
        dbHelper.addHabitEntry(habit.getId(), amount);
        loadHabits();
        Toast.makeText(this, "Added " + amount + " " + habit.getUnit(), Toast.LENGTH_SHORT).show();
    }

    private void undoLastLog(Habit habit) {
        boolean undone = dbHelper.deleteLatestTodayEntry(habit.getId());
        loadHabits();
        Toast.makeText(
                this,
                undone ? "Last " + habit.getName() + " log removed" : "Nothing to undo today",
                Toast.LENGTH_SHORT
        ).show();
    }
}
