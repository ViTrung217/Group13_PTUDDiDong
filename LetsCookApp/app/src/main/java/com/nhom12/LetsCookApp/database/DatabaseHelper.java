package com.nhom12.LetsCookApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nhom12.LetsCookApp.models.Food;
import com.nhom12.LetsCookApp.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Tên và phiên bản cơ sở dữ liệu
    private static final String DATABASE_NAME = "LetsCook.db";
    private static final int DATABASE_VERSION = 9;

    // Định nghĩa bảng users
    public static final String TABLE_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ROLE = "role";

    // Định nghĩa bảng foods
    public static final String TABLE_FOODS = "foods";
    public static final String COL_FOOD_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_IMAGE = "image";
    public static final String COL_VIEWS = "views";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_INGREDIENTS = "ingredients";
    public static final String COL_INSTRUCTIONS = "instructions";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";

    // Định nghĩa bảng categories
    public static final String TABLE_CATEGORIES = "categories";

    // Định nghĩa bảng feedback
    public static final String TABLE_FEEDBACK = "feedback";
    public static final String COL_FEEDBACK_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_FOOD_ID_FK = "food_id";
    public static final String COL_MESSAGE = "message";
    public static final String COL_TIMESTAMP = "timestamp";
    public static final String COL_STATUS = "status";

    // Định nghĩa bảng view_history
    public static final String TABLE_VIEW_HISTORY = "view_history";
    public static final String COL_VIEW_ID = "id";
    public static final String COL_VIEW_USER_ID = "user_id";
    public static final String COL_VIEW_FOOD_ID = "food_id";
    public static final String COL_VIEW_TIMESTAMP = "timestamp";

    // Định nghĩa bảng favorites
    public static final String TABLE_FAVORITES = "favorites";
    public static final String COL_FAVORITE_ID = "id";
    public static final String COL_FAV_USER_ID = "user_id";
    public static final String COL_FAV_FOOD_ID = "food_id";
    public static final String COL_FAV_TIMESTAMP = "timestamp";

    // Constructor
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        String userSql = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_ROLE + " TEXT)";
        db.execSQL(userSql);

        // Tạo bảng categories
        String categorySql = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_DESCRIPTION + " TEXT)";
        db.execSQL(categorySql);

        // Tạo bảng foods với khóa ngoại tham chiếu đến categories
        String foodSql = "CREATE TABLE " + TABLE_FOODS + " (" +
                COL_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

        // Tạo bảng feedback với khóa ngoại tham chiếu đến users và foods
        String feedbackSql = "CREATE TABLE " + TABLE_FEEDBACK + " (" +
                COL_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER NOT NULL, " +
                COL_FOOD_ID_FK + " INTEGER, " +
                COL_MESSAGE + " TEXT NOT NULL, " +
                COL_TIMESTAMP + " TEXT NOT NULL, " +
                COL_STATUS + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_ID + "), " +
                "FOREIGN KEY (" + COL_FOOD_ID_FK + ") REFERENCES " + TABLE_FOODS + "(" + COL_FOOD_ID + "))";
        db.execSQL(feedbackSql);

        // Tạo bảng view_history với khóa ngoại tham chiếu đến users và foods
        String viewHistorySql = "CREATE TABLE " + TABLE_VIEW_HISTORY + " (" +
                COL_VIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_VIEW_USER_ID + " INTEGER NOT NULL, " +
                COL_VIEW_FOOD_ID + " INTEGER NOT NULL, " +
                COL_VIEW_TIMESTAMP + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COL_VIEW_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_ID + "), " +
                "FOREIGN KEY (" + COL_VIEW_FOOD_ID + ") REFERENCES " + TABLE_FOODS + "(" + COL_FOOD_ID + "))";
        db.execSQL(viewHistorySql);

        // Tạo bảng favorites với khóa ngoại tham chiếu đến users và foods
        String favoritesSql = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COL_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FAV_USER_ID + " INTEGER NOT NULL, " +
                COL_FAV_FOOD_ID + " INTEGER NOT NULL, " +
                COL_FAV_TIMESTAMP + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + COL_FAV_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_ID + "), " +
                "FOREIGN KEY (" + COL_FAV_FOOD_ID + ") REFERENCES " + TABLE_FOODS + "(" + COL_FOOD_ID + "))";
        db.execSQL(favoritesSql);

        // Thêm dữ liệu mẫu vào bảng users
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('admin', 'a@gmail.com', '123', 'Quản trị viên')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('admin1', 'admin1@example.com', 'admin123', 'Quản trị viên')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('user1', 'user1@example.com', 'user123', 'Khách hàng')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('user2', 'user2@example.com', 'user456', 'Khách hàng')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('chef1', 'chef1@example.com', 'chef789', 'Khách hàng')");
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, email, password, role) VALUES ('guest1', 'guest1@example.com', 'guest000', 'Khách hàng')");

        // Thêm dữ liệu mẫu vào bảng categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Món chính', 'Các món ăn chính trong bữa cơm')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Món tráng miệng', 'Các món ngọt dùng sau bữa ăn')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Món khai vị', 'Các món ăn nhẹ trước bữa chính')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Món ăn vặt', 'Các món ăn nhẹ nhàng, dễ làm')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES ('Đồ uống', 'Các loại thức uống giải khát')");

        // Thêm dữ liệu mẫu vào bảng foods
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

        // Thêm dữ liệu mẫu vào bảng favorites
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (2, 1, '1698765432600')"); // user1 thích Gà nướng mật ong
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (3, 2, '1698765432700')"); // user2 thích Kem dâu tây
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (4, 4, '1698765432800')"); // chef1 thích Bánh khoai chiên
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (2, 5, '1698765432900')"); // user1 thích Trà sữa trân châu
        db.execSQL("INSERT INTO " + TABLE_FAVORITES + " (user_id, food_id, timestamp) VALUES (5, 3, '1698765433000')"); // guest1 thích Salad rau củ

        // Thêm dữ liệu mẫu vào bảng feedback
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
        // Xóa các bảng cũ và tạo lại khi nâng cấp phiên bản
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // Thêm người dùng mới
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

    // Lấy thông tin người dùng theo ID
    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COL_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    // Cập nhật mật khẩu người dùng
    public boolean updatePassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, newPassword);
        int rowsAffected = db.update(TABLE_USERS, values, COL_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }

    // Xóa toàn bộ người dùng
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.close();
    }

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

    // Thêm phản hồi mới
    public boolean addFeedback(int userId, int foodId, String message, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_FOOD_ID_FK, foodId);
        values.put(COL_MESSAGE, message);
        values.put(COL_TIMESTAMP, timestamp);
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
                "fb." + COL_STATUS + " as status, " +
                "fb." + COL_FOOD_ID_FK + " as food_id " +
                "FROM " + TABLE_FEEDBACK + " fb " +
                "LEFT JOIN " + TABLE_USERS + " u ON fb." + COL_USER_ID + " = u." + COL_ID + " " +
                "LEFT JOIN " + TABLE_FOODS + " f ON fb." + COL_FOOD_ID_FK + " = f." + COL_ID + " " +
                "ORDER BY fb." + COL_TIMESTAMP + " DESC";
        return db.rawQuery(query, null);
    }

    // Lấy phản hồi của một người dùng
    public Cursor getFeedbackByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FEEDBACK, null, COL_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, COL_TIMESTAMP + " DESC");
    }

    // Cập nhật nội dung phản hồi
    public boolean updateFeedback(int feedbackId, String newMessage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MESSAGE, newMessage);
        int rowsAffected = db.update(TABLE_FEEDBACK, values, COL_FEEDBACK_ID + "=?", new String[]{String.valueOf(feedbackId)});
        db.close();
        return rowsAffected > 0;
    }

    // Xóa phản hồi
    public boolean deleteFeedback(int feedbackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_FEEDBACK, COL_FEEDBACK_ID + "=?", new String[]{String.valueOf(feedbackId)});
        db.close();
        return rowsDeleted > 0;
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

    // Thêm lượt xem vào lịch sử và tăng lượt xem món ăn
    public boolean addViewHistory(int userId, int foodId, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_VIEW_USER_ID, userId);
            values.put(COL_VIEW_FOOD_ID, foodId);
            values.put(COL_VIEW_TIMESTAMP, timestamp);
            long result = db.insert(TABLE_VIEW_HISTORY, null, values);
            if (result != -1) {
                db.execSQL("UPDATE " + TABLE_FOODS + " SET " + COL_VIEWS + " = " + COL_VIEWS + " + 1 WHERE " + COL_FOOD_ID + " = ?",
                        new String[]{String.valueOf(foodId)});
                return true;
            }
            return false;
        } finally {
            db.close();
        }
    }

    // Lấy lịch sử xem của người dùng
    public Cursor getViewHistoryByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_VIEW_HISTORY, null, COL_VIEW_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, COL_VIEW_TIMESTAMP + " DESC");
    }

    // Thêm món ăn vào danh sách yêu thích
    public boolean addFavorite(int userId, int foodId, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.query(TABLE_FAVORITES, null,
                    COL_FAV_USER_ID + "=? AND " + COL_FAV_FOOD_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(foodId)}, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.close();
                return true; // Món ăn đã được thích
            }
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(COL_FAV_USER_ID, userId);
            values.put(COL_FAV_FOOD_ID, foodId);
            values.put(COL_FAV_TIMESTAMP, timestamp);
            long result = db.insert(TABLE_FAVORITES, null, values);
            return result != -1;
        } finally {
            db.close();
        }
    }

    // Xóa món ăn khỏi danh sách yêu thích
    public boolean removeFavorite(int userId, int foodId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rowsDeleted = db.delete(TABLE_FAVORITES, COL_FAV_USER_ID + "=? AND " + COL_FAV_FOOD_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(foodId)});
            return rowsDeleted > 0;
        } finally {
            db.close();
        }
    }

    // Lấy danh sách món ăn yêu thích của người dùng
    public Cursor getFavoritesByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FAVORITES, null, COL_FAV_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, COL_FAV_TIMESTAMP + " DESC");
    }

    // Đếm số lượt thích của một món ăn
    public int getLikesCount(int foodId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FAVORITES + " WHERE " + COL_FAV_FOOD_ID + " = ?",
                new String[]{String.valueOf(foodId)});
        int likesCount = 0;
        if (cursor != null && cursor.moveToFirst()) {
            likesCount = cursor.getInt(0);
            cursor.close();
        }
        return likesCount;
    }

    // Lấy xếp hạng món ăn yêu thích theo số lượt thích
    public Cursor getFavoriteFoodsRanking() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT f." + COL_ID + " as food_id, f." + COL_NAME + " as name, COUNT(fav." + COL_FAV_FOOD_ID + ") as favorite_count " +
                "FROM " + TABLE_FOODS + " f " +
                "LEFT JOIN " + TABLE_FAVORITES + " fav ON f." + COL_ID + " = fav." + COL_FAV_FOOD_ID + " " +
                "GROUP BY f." + COL_ID + ", f." + COL_NAME + " " +
                "ORDER BY favorite_count DESC";
        return db.rawQuery(query, null);
    }
}