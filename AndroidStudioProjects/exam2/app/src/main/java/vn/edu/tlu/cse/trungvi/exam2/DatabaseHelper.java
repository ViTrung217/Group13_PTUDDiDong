package vn.edu.tlu.cse.trungvi.exam2;

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

    // Bảng users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role"; // Thêm cột role

    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null, 2); // Cập nhật version lên 2 để onUpgrade chạy
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng sản phẩm
        db.execSQL("CREATE TABLE " + Table_name + " (" +
                col_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                col_product_name + " TEXT, " +
                col_brand + " TEXT, " +
                col_price + " INTEGER, " +
                col_stock + " INTEGER, " +
                col_description + " TEXT)");

        // Tạo bảng users với role
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT)");

        // Thêm tài khoản mẫu
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password, role) VALUES ('admin', '1234', 'employee')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password, role) VALUES ('khach1', 'abcd', 'customer')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Kiểm tra nếu chưa có cột role thì thêm vào
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_ROLE + " TEXT DEFAULT 'customer'");
        }
    }

    // Thêm tài khoản mới
    public boolean registerUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role); // Lưu role

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Kiểm tra tài khoản đăng nhập
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                        COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Lấy quyền (role) của người dùng
    public String getUserRole(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + "=?", new String[]{username});

        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }

    // Thêm sản phẩm
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

    // Hiển thị danh sách sản phẩm
    public Cursor showData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Table_name, null);
    }

    // Cập nhật sản phẩm
    public boolean update(String id, String productName, String brand, int price, int stock, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
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
}
