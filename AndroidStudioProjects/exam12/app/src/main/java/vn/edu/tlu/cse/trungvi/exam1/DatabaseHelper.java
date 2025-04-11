package vn.edu.tlu.cse.trungvi.exam1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "LaptopStore.db";

    // Bảng Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";
    private static final String COLUMN_FULLNAME = "fullname";

    // Bảng Products
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_STOCK = "stock";
    private static final String COLUMN_DESCRIPTION = "description";

    // Bảng Cart
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_CART_ID = "id";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_PRODUCT_ID_FK = "product_id";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_TOTAL = "total";

    // Bảng Orders
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "id";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_ORDER_TOTAL = "total_price";
    private static final String COLUMN_ORDER_STATUS = "status";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT, " +
                COLUMN_FULLNAME + " TEXT)");

        // Tạo bảng Products
        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_PRICE + " INTEGER, " +
                COLUMN_STOCK + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT)");

        // Tạo bảng Cart
        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_PRODUCT_ID_FK + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER, " +
                COLUMN_TOTAL + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "), " +
                "FOREIGN KEY(" + COLUMN_PRODUCT_ID_FK + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + "))");

        // Tạo bảng Orders
        db.execSQL("CREATE TABLE " + TABLE_ORDERS + " (" +
                COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_ORDER_DATE + " TEXT, " +
                COLUMN_ORDER_TOTAL + " INTEGER, " +
                COLUMN_ORDER_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_USER_EMAIL + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_EMAIL + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public String checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ROLE + " FROM " + TABLE_USERS + " WHERE " +
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return null;
    }

    // Thêm sản phẩm (Chỉ dành cho nhân viên)
    public boolean addProduct(String name, String brand, int price, int stock, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_BRAND, brand);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_STOCK, stock);
        values.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;
    }


    public Cursor getProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    // Thêm vào giỏ hàng
    public boolean addToCart(String userEmail, int productId, int quantity, int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, userEmail);
        values.put(COLUMN_PRODUCT_ID_FK, productId);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_TOTAL, total);

        long result = db.insert(TABLE_CART, null, values);
        return result != -1;
    }

    // Đặt hàng từ giỏ hàng
    public boolean placeOrder(String userEmail, String date, int totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, userEmail);
        values.put(COLUMN_ORDER_DATE, date);
        values.put(COLUMN_ORDER_TOTAL, totalPrice);
        values.put(COLUMN_ORDER_STATUS, "Đang xử lý");

        long result = db.insert(TABLE_ORDERS, null, values);
        return result != -1;
    }


    public Cursor getOrders(String userEmail, boolean isStaff) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (isStaff) {
            return db.rawQuery("SELECT * FROM " + TABLE_ORDERS, null);
        } else {
            return db.rawQuery("SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_USER_EMAIL + "=?", new String[]{userEmail});
        }
    }
}
