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
    private static final int DATABASE_VERSION = 1;

    public TaskforgeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE habits ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "tracking_type TEXT NOT NULL,"
                + "target_amount INTEGER NOT NULL,"
                + "unit TEXT NOT NULL,"
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

        seedDrinkWaterHabit(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS habit_entries");
        db.execSQL("DROP TABLE IF EXISTS habits");
        onCreate(db);
    }

    public List<Habit> getTodayHabits() {
        SQLiteDatabase db = getReadableDatabase();
        List<Habit> habits = new ArrayList<>();
        String today = todayKey();

        String sql = "SELECT h.id, h.name, h.unit, h.target_amount, "
                + "COALESCE(SUM(e.amount), 0) AS today_amount "
                + "FROM habits h "
                + "LEFT JOIN habit_entries e ON h.id = e.habit_id AND e.entry_date = ? "
                + "GROUP BY h.id, h.name, h.unit, h.target_amount "
                + "ORDER BY h.created_at ASC";

        try (Cursor cursor = db.rawQuery(sql, new String[]{today})) {
            while (cursor.moveToNext()) {
                habits.add(new Habit(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4)
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

    private void seedDrinkWaterHabit(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("name", "Drink Water");
        values.put("tracking_type", "quantity");
        values.put("target_amount", 2000);
        values.put("unit", "ml");
        values.put("created_at", System.currentTimeMillis());
        db.insert("habits", null, values);
    }

    private String todayKey() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }
}
