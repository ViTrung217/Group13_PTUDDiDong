package vn.edu.tlu.cse.trungvi.exam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String Database_name = "products.db";
    public static final String Table_name = "Products";

    // Các cột của bảng Products
    public static final String col_id = "ID";
    public static final String col_product_name = "product_name";
    public static final String col_brand = "brand";
    public static final String col_price = "price";
    public static final String col_stock = "stock";
    public static final String col_description = "description";
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Products
        db.execSQL("CREATE TABLE " + Table_name + " (" +
                col_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_product_name + " TEXT, " +
                col_brand + " TEXT, " +
                col_price + " INTEGER, " +
                col_stock + " INTEGER, " +
                col_description + " TEXT)");
        db.execSQL("create table " + TABLE_USERS +" (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_name);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USERS);
        onCreate(sqLiteDatabase);
    }

    // Thêm dữ liệu sản phẩm
    public boolean insertData(String productName, String brand, int price, int stock, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_product_name, productName);
        cv.put(col_brand, brand);
        cv.put(col_price, price);
        cv.put(col_stock, stock);
        cv.put(col_description, description);
        long result = db.insert(Table_name, null, cv);
        return result != -1;
    }

    // Hiển thị dữ liệu sản phẩm
    public Cursor showData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Table_name, null);
    }

    // Cập nhật thông tin sản phẩm
    public boolean update(String id, String productName, String brand, int price, int stock, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col_id, id);
        cv.put(col_product_name, productName);
        cv.put(col_brand, brand);
        cv.put(col_price, price);
        cv.put(col_stock, stock);
        cv.put(col_description, description);
        db.update(Table_name, cv, "ID = ?", new String[]{id});
        return true;
    }

    // Xóa sản phẩm
    public Integer delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_name, "ID = ?", new String[]{id});
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
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }
}
