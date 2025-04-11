package com.example.menuapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "menuapp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        String createUsersTable = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)";
        db.execSQL(createUsersTable);

        //menu create
        String createMenuTable = "CREATE TABLE menu (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER, description TEXT, image TEXT)";
        db.execSQL(createMenuTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // Thêm nhân viên
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    // Kiểm tra đăng nhập
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    // Thêm món ăn vào database
    public boolean insertMenu(String name, int price, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", description);

        long result = db.insert("menu", null, values);
        return result != -1;
    }

    // Lấy danh sách thực đơn
    public Cursor getAllMenu() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM menu", null);
    }
    // Lấy món ăn theo ID
    public Cursor getMenuById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM menu WHERE id = ?", new String[]{String.valueOf(id)});
    }

    // Cập nhật món ăn
    public boolean updateMenu(int id, String name, int price, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", description);

        int result = db.update("menu", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Xóa món ăn
    public void deleteMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("menu", "id=?", new String[]{String.valueOf(id)});
    }



}
