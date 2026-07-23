package com.taskforge.app;

import android.app.Activity;
import android.os.Bundle;
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
        habitAdapter = new HabitAdapter(this::logAmount);

        RecyclerView habitRecyclerView = findViewById(R.id.habitRecyclerView);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        habitRecyclerView.setAdapter(habitAdapter);

        loadHabits();
    }

    private void loadHabits() {
        habitAdapter.submitList(dbHelper.getTodayHabits());
    }

    private void logAmount(Habit habit, int amount) {
        dbHelper.addHabitEntry(habit.getId(), amount);
        loadHabits();
        Toast.makeText(this, "Added " + amount + " " + habit.getUnit(), Toast.LENGTH_SHORT).show();
    }
}
