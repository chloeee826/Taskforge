package com.taskforge.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.taskforge.app.model.Habit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskforgeDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "taskforge.db";
    private static final int DATABASE_VERSION = 2;

    public TaskforgeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE habits ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "tracking_type TEXT NOT NULL,"
                + "icon_name TEXT NOT NULL,"
                + "target_amount INTEGER NOT NULL,"
                + "unit TEXT NOT NULL,"
                + "small_log_amount INTEGER NOT NULL,"
                + "large_log_amount INTEGER NOT NULL,"
                + "created_at INTEGER NOT NULL"
                + ")");

        db.execSQL("CREATE TABLE habit_entries ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "habit_id INTEGER NOT NULL,"
                + "amount INTEGER NOT NULL,"
                + "entry_date TEXT NOT NULL,"
                + "created_at INTEGER NOT NULL,"
                + "FOREIGN KEY(habit_id) REFERENCES habits(id)"
                + ")");

        seedStarterHabits(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE habits ADD COLUMN icon_name TEXT NOT NULL DEFAULT 'water'");
            db.execSQL("ALTER TABLE habits ADD COLUMN small_log_amount INTEGER NOT NULL DEFAULT 250");
            db.execSQL("ALTER TABLE habits ADD COLUMN large_log_amount INTEGER NOT NULL DEFAULT 500");
        }
        seedStarterHabits(db);
    }

    public List<Habit> getTodayHabits() {
        SQLiteDatabase db = getReadableDatabase();
        List<Habit> habits = new ArrayList<>();
        String today = todayKey();

        String sql = "SELECT h.id, h.name, h.icon_name, h.unit, h.target_amount, "
                + "h.small_log_amount, h.large_log_amount, "
                + "COALESCE(SUM(e.amount), 0) AS today_amount "
                + "FROM habits h "
                + "LEFT JOIN habit_entries e ON h.id = e.habit_id AND e.entry_date = ? "
                + "GROUP BY h.id, h.name, h.icon_name, h.unit, h.target_amount, "
                + "h.small_log_amount, h.large_log_amount "
                + "ORDER BY h.created_at ASC";

        try (Cursor cursor = db.rawQuery(sql, new String[]{today})) {
            while (cursor.moveToNext()) {
                habits.add(new Habit(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getInt(7),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));
            }
        }

        return habits;
    }

    public void addHabitEntry(long habitId, int amount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("habit_id", habitId);
        values.put("amount", amount);
        values.put("entry_date", todayKey());
        values.put("created_at", System.currentTimeMillis());
        db.insert("habit_entries", null, values);
    }

    public boolean deleteLatestTodayEntry(long habitId) {
        SQLiteDatabase db = getWritableDatabase();
        String latestEntrySql = "SELECT id FROM habit_entries "
                + "WHERE habit_id = ? AND entry_date = ? "
                + "ORDER BY created_at DESC, id DESC LIMIT 1";

        try (Cursor cursor = db.rawQuery(latestEntrySql, new String[]{String.valueOf(habitId), todayKey()})) {
            if (!cursor.moveToFirst()) {
                return false;
            }

            long entryId = cursor.getLong(0);
            int deletedRows = db.delete("habit_entries", "id = ?", new String[]{String.valueOf(entryId)});
            return deletedRows > 0;
        }
    }

    public int getTargetAmountByName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.rawQuery(
                "SELECT target_amount FROM habits WHERE name = ? LIMIT 1",
                new String[]{name}
        )) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        }
        return 0;
    }

    public void updateTargetAmountByName(String name, int targetAmount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("target_amount", targetAmount);
        db.update("habits", values, "name = ?", new String[]{name});
    }

    private void seedStarterHabits(SQLiteDatabase db) {
        seedHabitIfMissing(db, "Drink Water", "quantity", "water", 2000, "ml", 250, 500);
        seedHabitIfMissing(db, "Exercise", "duration", "exercise", 30, "min", 10, 20);
        seedHabitIfMissing(db, "Sleep", "duration", "sleep", 8, "hr", 1, 2);
    }

    private void seedHabitIfMissing(
            SQLiteDatabase db,
            String name,
            String trackingType,
            String iconName,
            int targetAmount,
            String unit,
            int smallLogAmount,
            int largeLogAmount
    ) {
        try (Cursor cursor = db.rawQuery("SELECT id FROM habits WHERE name = ? LIMIT 1", new String[]{name})) {
            if (cursor.moveToFirst()) {
                return;
            }
        }

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("tracking_type", trackingType);
        values.put("icon_name", iconName);
        values.put("target_amount", targetAmount);
        values.put("unit", unit);
        values.put("small_log_amount", smallLogAmount);
        values.put("large_log_amount", largeLogAmount);
        values.put("created_at", System.currentTimeMillis());
        db.insert("habits", null, values);
    }

    private String todayKey() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }
}
