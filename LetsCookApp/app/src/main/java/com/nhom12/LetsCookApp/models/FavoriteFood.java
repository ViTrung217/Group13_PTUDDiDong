package com.nhom12.LetsCookApp.models;

public class FavoriteFood {
    private int foodId;
    private String foodName;
    private int favoriteCount;

    public FavoriteFood(int foodId, String foodName, int favoriteCount) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.favoriteCount = favoriteCount;
    }

    public int getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }
}