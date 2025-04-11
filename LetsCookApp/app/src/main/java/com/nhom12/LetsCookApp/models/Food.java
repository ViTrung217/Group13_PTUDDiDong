package com.nhom12.LetsCookApp.models;

public class Food {
    private int id, categoryId, views;
    private String name, description, image, ingredients, instructions, createdAt, updatedAt, timestamp;
    private boolean isFavourite;
    private String categoryName;
    private int likes;

    // Constructor cho HomeFragment, DishListFragment (không cần timestamp)
    public Food(int categoryId, String categoryName, String description, int id, String image, String ingredients, String instructions, String name, int views) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.id = id;
        this.image = image;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.name = name;
        this.views = views;
    }

    // Constructor cho HistoryFragment, FavoritesFragment (có timestamp)
    public Food(int id, String name, String timestamp) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
    }

    // Constructor đầy đủ
    public Food(int categoryId, String categoryName, String createdAt, String description, int id, String image, String ingredients, String instructions, boolean isFavourite, String name, String updatedAt, int views, String timestamp, int likes) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.description = description;
        this.id = id;
        this.image = image;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.isFavourite = isFavourite;
        this.name = name;
        this.updatedAt = updatedAt;
        this.views = views;
        this.timestamp = timestamp;
        this.likes = likes;
    }

    // Constructor mới cho AdminFoodActivity
    public Food(int id, String name, String description, String image, int views, int categoryId, String ingredients, String instructions, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.views = views;
        this.categoryId = categoryId;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryName = null; // Không có categoryName trong AdminFoodActivity
        this.timestamp = null;
        this.isFavourite = false; // Mặc định là false
        this.likes = 0; // Sẽ được cập nhật sau
    }

    // Getters và Setters
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public boolean isFavourite() { return isFavourite; }
    public void setFavourite(boolean favourite) { isFavourite = favourite; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
}