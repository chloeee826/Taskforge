package com.taskforge.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.taskforge.app.R;
import com.taskforge.app.model.Habit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {
    public interface OnLogAmountClickListener {
        void onLogAmount(Habit habit, int amount);
    }

    private final OnLogAmountClickListener listener;
    private final List<Habit> habits = new ArrayList<>();

    public HabitAdapter(OnLogAmountClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Habit> nextHabits) {
        habits.clear();
        habits.addAll(nextHabits);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.bind(habit, listener);
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        private final TextView habitNameText;
        private final TextView progressText;
        private final TextView statusText;
        private final TextView goalText;
        private final ImageView habitIconImage;
        private final ProgressBar progressBar;
        private final Button smallLogButton;
        private final Button largeLogButton;

        HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitIconImage = itemView.findViewById(R.id.habitIconImage);
            habitNameText = itemView.findViewById(R.id.habitNameText);
            progressText = itemView.findViewById(R.id.progressText);
            statusText = itemView.findViewById(R.id.statusText);
            goalText = itemView.findViewById(R.id.goalText);
            progressBar = itemView.findViewById(R.id.progressBar);
            smallLogButton = itemView.findViewById(R.id.smallLogButton);
            largeLogButton = itemView.findViewById(R.id.largeLogButton);
        }

        void bind(Habit habit, OnLogAmountClickListener listener) {
            habitIconImage.setImageResource(iconForHabit(habit));
            habitNameText.setText(habit.getName());
            progressText.setText(String.format(
                    Locale.US,
                    "%d / %d %s",
                    habit.getTodayAmount(),
                    habit.getTargetAmount(),
                    habit.getUnit()
            ));
            goalText.setText(String.format(Locale.US, "Daily goal: %d %s", habit.getTargetAmount(), habit.getUnit()));
            progressBar.setProgress(habit.getProgressPercent());
            statusText.setText(habit.isCompleteToday()
                    ? "Completed today"
                    : "In progress");

            smallLogButton.setText(buttonLabel(habit, habit.getSmallLogAmount()));
            largeLogButton.setText(buttonLabel(habit, habit.getLargeLogAmount()));
            smallLogButton.setOnClickListener(v -> listener.onLogAmount(habit, habit.getSmallLogAmount()));
            largeLogButton.setOnClickListener(v -> listener.onLogAmount(habit, habit.getLargeLogAmount()));
        }

        private int iconForHabit(Habit habit) {
            switch (habit.getIconName()) {
                case "exercise":
                    return R.drawable.ic_exercise;
                case "sleep":
                    return R.drawable.ic_sleep;
                case "water":
                default:
                    return R.drawable.ic_water_bottle;
            }
        }

        private String buttonLabel(Habit habit, int amount) {
            return String.format(Locale.US, "+%d %s", amount, habit.getUnit());
        }
    }
}
