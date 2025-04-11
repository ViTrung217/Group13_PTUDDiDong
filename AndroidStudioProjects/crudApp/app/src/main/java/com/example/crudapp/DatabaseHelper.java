package com.example.crudapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_name = "Students.db";
    public static final String Table_name = "Student_table";
    public static final String col_id = "Id";
    public static final String col_name = "name";
    public static final String col_marks = "marks";
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";


    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table_name +" (Id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT , marks INTEGER)");
        db.execSQL("create table " + TABLE_USERS +" (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Table_name);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(sqLiteDatabase);
    }
    public boolean insertData(String name,String marks)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_name,name);
        cv.put(col_marks,marks);
        Long result = db.insert(Table_name,null,cv);
        if(result == -1 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor Showdata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " +Table_name,null);;
        return cursor;
    }
    public boolean update(String id,String name,String marks)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_id,id);
        cv.put(col_name,name);
        cv.put(col_marks,marks);
        db.update(Table_name,cv,"Id = ?",new String[] { id });
        return true;
    }
    public Integer delete(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_name,"Id = ?",new String[] {id});
    }
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                        COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
