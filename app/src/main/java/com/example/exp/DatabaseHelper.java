package com.example.exp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExpenseApp.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USER + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);            // Creates the user table
        db.execSQL(TABLE_EXPENSES_CREATE);   // Creates the expenses table
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
    public long addUser(String name, String email, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        return db.insert(TABLE_USER, null, values);
    }
    public String getNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM user WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        cursor.close();
        return null;
    }
    private static final String TABLE_EXPENSES_CREATE =
            "CREATE TABLE expenses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "date TEXT NOT NULL, " +
                    "image BLOB, " +
                    "username TEXT NOT NULL);";

    public boolean insertExpense(String name, double amount, String date, byte[] image, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("amount", amount);
        values.put("date", date);
        values.put("image", image);
        values.put("username", username);
        long result = db.insert("expenses", null, values);
        return result != -1;
    }
    public List<Expense> getAllExpenses(String username) {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT name, amount, date, image FROM expenses WHERE username = ?",
                new String[]{username}
        );

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                double amount = cursor.getDouble(1);
                String date = cursor.getString(2);
                byte[] image = cursor.getBlob(3);
                expenses.add(new Expense(name, amount, date, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenses;
    }


    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    // Get total expenditure for current month
    public double getMonthlyTotal(String username, String currentMonth) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(amount) FROM expenses WHERE username = ? AND strftime('%Y-%m', date) = ?",
                new String[]{username, currentMonth}
        );
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // Get total per date in current month
    public List<DailyTotal> getDailyTotals(String username, String currentMonth) {
        List<DailyTotal> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT date, SUM(amount) FROM expenses " +
                        "WHERE username = ? AND strftime('%Y-%m', date) = ? " +
                        "GROUP BY date ORDER BY date DESC",
                new String[]{username, currentMonth}
        );

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                double total = cursor.getDouble(1);
                results.add(new DailyTotal(date, total));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return results;
    }
    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        int result = db.update(TABLE_USER, values, COLUMN_EMAIL + "=?", new String[]{email});
        return result > 0;
    }
}