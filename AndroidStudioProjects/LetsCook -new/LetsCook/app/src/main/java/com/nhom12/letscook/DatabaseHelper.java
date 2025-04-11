package com.nhom12.letscook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LetsCook.db";
    private static final int DATABASE_VERSION = 11;

    // Constants cho bảng users
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    // Constants cho bảng feedback
    public static final String TABLE_FEEDBACK = "feedback";
    public static final String COL_FEEDBACK_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_FOOD_ID = "food_id";
    public static final String COL_MESSAGE = "message";
    public static final String COL_TIMESTAMP = "timestamp";
    public static final String COL_STATUS = "status";

    // Constants cho bảng favorites
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COL_FAVORITE_ID = "id";

    // Constants cho bảng foods
    public static final String TABLE_FOODS = "foods";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_IMAGE = "image";
    public static final String COL_VIEWS = "views";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_INGREDIENTS = "ingredients";
    public static final String COL_INSTRUCTIONS = "instructions";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";

    // Constants cho bảng categories
    public static final String TABLE_CATEGORIES = "categories";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users để lưu thông tin người dùng
        String userSql = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(userSql);

        // Tạo bảng categories để lưu danh mục món ăn
        String categorySql = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DESCRIPTION + " TEXT)";
        db.execSQL(categorySql);

        // Tạo bảng foods để lưu thông tin món ăn
        String foodSql = "CREATE TABLE " + TABLE_FOODS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_IMAGE + " TEXT, " +
                COL_VIEWS + " INTEGER DEFAULT 0, " +
                COL_CATEGORY_ID + " INTEGER, " +
                COL_INGREDIENTS + " TEXT, " +
                COL_INSTRUCTIONS + " TEXT, " +
                COL_CREATED_AT + " TEXT, " +
                COL_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY (" + COL_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_ID + "))";
        db.execSQL(foodSql);

        // Tạo bảng favorites để lưu món ăn yêu thích của người dùng
        String favoriteSql = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COL_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER, " +
                COL_FOOD_ID + " INTEGER, " +
                COL_TIMESTAMP + " TEXT, " +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_ID + "), " +
                "FOREIGN KEY (" + COL_FOOD_ID + ") REFERENCES " + TABLE_FOODS + "(" + COL_ID + "))";
        db.execSQL(favoriteSql);

        // Tạo bảng feedback để lưu phản hồi từ người dùng
        String feedbackSql = "CREATE TABLE " + TABLE_FEEDBACK + " (" +
                COL_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER, " +
                COL_FOOD_ID + " INTEGER, " +
                COL_MESSAGE + " TEXT, " +
                COL_TIMESTAMP + " TEXT, " +
                COL_STATUS + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_ID + "), " +
                "FOREIGN KEY (" + COL_FOOD_ID + ") REFERENCES " + TABLE_FOODS + "(" + COL_ID + "))";
        db.execSQL(feedbackSql);

        // Thêm dữ liệu mẫu (đã được giữ nguyên như trong code gốc)
        // Users
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('admin1', 'admin1@example.com', 'admin123', 'Quản trị viên')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('user1', 'user1@example.com', 'user123', 'Khách hàng')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('user2', 'user2@example.com', 'user456', 'Khách hàng')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('chef1', 'chef1@example.com', 'chef789', 'Khách hàng')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('guest1', 'guest1@example.com', 'guest000', 'Khách hàng')");

        // Categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Món chính', 'Các món ăn chính trong bữa cơm')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Món tráng miệng', 'Các món ngọt dùng sau bữa ăn')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Món khai vị', 'Các món ăn nhẹ trước bữa chính')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Món ăn vặt', 'Các món ăn nhẹ nhàng, dễ làm')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Đồ uống', 'Các loại thức uống giải khát')");

        // Foods
        db.execSQL("INSERT INTO " + TABLE_FOODS + " (name, description, image, views, category_id, ingredients, instructions, created_at, updated_at) VALUES " +
                "('Gà nướng mật ong', 'Gà nướng thơm ngon với mật ong và gia vị', 'ga_nuong.jpg', 150, 1, 'Gà 1kg, mật ong 2 muỗng, tỏi, muối, tiêu', " +
                "'1. Ướp gà với gia vị trong 2 giờ. 2. Nướng ở 180 độ C trong 40 phút.', '1698765432100', '1698765432100')");
        db.execSQL("INSERT INTO " + TABLE_FOODS + " (name, description, image, views, category_id, ingredients, instructions, created_at, updated_at) VALUES " +
                "('Kem dâu tây', 'Kem mát lạnh với hương vị dâu tây tự nhiên', 'kem_dau.jpg', 80, 2, 'Dâu tây 200g, kem tươi 300ml, đường 100g', " +
                "'1. Xay dâu với đường. 2. Trộn kem tươi và cho vào tủ lạnh 4 giờ.', '1698765432200', '1698765432200')");
        db.execSQL("INSERT INTO " + TABLE_FOODS + " (name, description, image, views, category_id, ingredients, instructions, created_at, updated_at) VALUES " +
                "('Salad rau củ', 'Salad tươi ngon, bổ dưỡng', 'salad.jpg', 50, 3, 'Xà lách, cà chua, dưa leo, dầu oliu', " +
                "'1. Rửa sạch rau củ. 2. Trộn đều với dầu oliu.', '1698765432300', '1698765432300')");
        db.execSQL("INSERT INTO " + TABLE_FOODS + " (name, description, image, views, category_id, ingredients, instructions, created_at, updated_at) VALUES " +
                "('Bánh khoai chiên', 'Bánh khoai giòn rụm, dễ làm', 'banh_khoai.jpg', 200, 4, 'Khoai lang 500g, bột mì 100g, dầu ăn', " +
                "'1. Cắt khoai thành lát mỏng. 2. Nhúng bột và chiên vàng.', '1698765432400', '1698765432400')");
        db.execSQL("INSERT INTO " + TABLE_FOODS + " (name, description, image, views, category_id, ingredients, instructions, created_at, updated_at) VALUES " +
                "('Trà sữa trân châu', 'Thức uống thơm ngon với trân châu dai', 'tra_sua.jpg', 300, 5, 'Trà đen 50g, sữa 200ml, trân châu 100g, đường', " +
                "'1. Pha trà với nước nóng. 2. Thêm sữa và trân châu.', '1698765432500', '1698765432500')");

        // Favorites
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (2, 1, '1698765432600')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (3, 1, '1698765432610')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (3, 2, '1698765432700')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (4, 4, '1698765432800')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (2, 4, '1698765432810')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (5, 4, '1698765432820')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (2, 5, '1698765432900')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (3, 5, '1698765432910')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (4, 5, '1698765432920')");
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (5, 5, '1698765432930')");

        // Feedback
        db.execSQL("INSERT INTO " + TABLE_FEEDBACK + " (user_id, food_id, message, timestamp, status) VALUES " +
                "(2, 1, 'Món gà nướng rất ngon, nhưng hơi ngọt!', '1698765433100', 0)");
        db.execSQL("INSERT INTO " + TABLE_FEEDBACK + " (user_id, food_id, message, timestamp, status) VALUES " +
                "(3, 2, 'Kem dâu tây tuyệt vời, sẽ làm lại.', '1698765433200', 1)");
        db.execSQL("INSERT INTO " + TABLE_FEEDBACK + " (user_id, food_id, message, timestamp, status) VALUES " +
                "(4, 4, 'Bánh khoai chiên giòn, dễ làm.', '1698765433300', 0)");
        db.execSQL("INSERT INTO " + TABLE_FEEDBACK + " (user_id, food_id, message, timestamp, status) VALUES " +
                "(2, 5, 'Trà sữa ngon nhưng trân châu hơi cứng.', '1698765433400', 1)");
        db.execSQL("INSERT INTO " + TABLE_FEEDBACK + " (user_id, food_id, message, timestamp, status) VALUES " +
                "(5, 3, 'Salad tươi nhưng cần thêm sốt.', '1698765433500', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa các bảng cũ và tạo lại khi nâng cấp phiên bản database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    // --- Phương thức CRUD cho bảng users ---
    // Đăng ký người dùng mới
    public boolean registerUser(String username, String email, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " =?", new String[]{email});
        if (c.getCount() > 0) {
            c.close();
            return false; // Email đã tồn tại
        }
        c.close();

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Lấy thông tin người dùng theo email và mật khẩu (đăng nhập)
    public Cursor getUserByEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password});
    }

    // Xóa toàn bộ người dùng
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.close();
    }

    // --- Phương thức CRUD cho bảng foods ---
    // Thêm món ăn mới
    public boolean addFood(String name, String description, String image, int categoryId, String ingredients, String instructions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_IMAGE, image);
        values.put(COL_VIEWS, 0);
        values.put(COL_CATEGORY_ID, categoryId);
        values.put(COL_INGREDIENTS, ingredients);
        values.put(COL_INSTRUCTIONS, instructions);
        values.put(COL_CREATED_AT, String.valueOf(System.currentTimeMillis()));
        values.put(COL_UPDATED_AT, String.valueOf(System.currentTimeMillis()));

        long result = db.insert(TABLE_FOODS, null, values);
        db.close();
        return result != -1;
    }

    // Lấy tất cả món ăn
    public Cursor getAllFoods() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FOODS, null);
    }

    // Xóa toàn bộ món ăn
    public void deleteAllFoods() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOODS, null, null);
        db.close();
    }

    // Lấy xếp hạng món ăn yêu thích dựa trên số lượt thích
    public Cursor getFavoriteFoodsRanking() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT f." + COL_ID + " as food_id, f." + COL_NAME + " as name, COUNT(*) as favorite_count " +
                "FROM " + TABLE_FOODS + " f " +
                "LEFT JOIN " + TABLE_FAVORITES + " fav ON f." + COL_ID + " = fav." + COL_FOOD_ID + " " +
                "GROUP BY f." + COL_ID + ", f." + COL_NAME + " " +
                "ORDER BY favorite_count DESC";
        return db.rawQuery(query, null);
    }

    // --- Phương thức CRUD cho bảng feedback ---
    // Thêm phản hồi từ người dùng
    public boolean addFeedback(int userId, int foodId, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_FOOD_ID, foodId);
        values.put(COL_MESSAGE, message);
        values.put(COL_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        values.put(COL_STATUS, 0);

        long result = db.insert(TABLE_FEEDBACK, null, values);
        db.close();
        return result != -1;
    }

    // Lấy tất cả phản hồi
    public Cursor getAllFeedback() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT fb." + COL_FEEDBACK_ID + " as feedback_id, " +
                "u." + COL_USERNAME + " as username, " +
                "f." + COL_NAME + " as food_name, " +
                "fb." + COL_MESSAGE + " as message, " +
                "fb." + COL_TIMESTAMP + " as timestamp, " +
                "fb." + COL_STATUS + " as status " +
                "FROM " + TABLE_FEEDBACK + " fb " +
                "JOIN " + TABLE_USERS + " u ON fb." + COL_USER_ID + " = u." + COL_ID + " " +
                "JOIN " + TABLE_FOODS + " f ON fb." + COL_FOOD_ID + " = f." + COL_ID + " " +
                "ORDER BY fb." + COL_TIMESTAMP + " DESC";
        return db.rawQuery(query, null);
    }

    // Cập nhật trạng thái phản hồi (0: chưa xem, 1: đã xem)
    public boolean updateFeedbackStatus(int feedbackId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STATUS, status);

        int rowsAffected = db.update(TABLE_FEEDBACK, values, COL_FEEDBACK_ID + "=?", new String[]{String.valueOf(feedbackId)});
        db.close();
        return rowsAffected > 0;
    }

    // Xóa phản hồi
    public boolean deleteFeedback(int feedbackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_FEEDBACK, COL_FEEDBACK_ID + "=?", new String[]{String.valueOf(feedbackId)});
        db.close();
        return rowsAffected > 0;
    }
}