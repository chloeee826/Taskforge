package com.taskforge.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        private final ProgressBar progressBar;
        private final Button add250Button;
        private final Button add500Button;

        HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitNameText = itemView.findViewById(R.id.habitNameText);
            progressText = itemView.findViewById(R.id.progressText);
            statusText = itemView.findViewById(R.id.statusText);
            progressBar = itemView.findViewById(R.id.progressBar);
            add250Button = itemView.findViewById(R.id.add250Button);
            add500Button = itemView.findViewById(R.id.add500Button);
        }

        void bind(Habit habit, OnLogAmountClickListener listener) {
            habitNameText.setText(habit.getName());
            progressText.setText(String.format(
                    Locale.US,
                    "%d / %d %s today",
                    habit.getTodayAmount(),
                    habit.getTargetAmount(),
                    habit.getUnit()
            ));
            progressBar.setProgress(habit.getProgressPercent());
            statusText.setText(habit.isCompleteToday()
                    ? "Goal reached for today"
                    : "Keep going, you are building the streak");

            add250Button.setOnClickListener(v -> listener.onLogAmount(habit, 250));
            add500Button.setOnClickListener(v -> listener.onLogAmount(habit, 500));
        }
    }
}
