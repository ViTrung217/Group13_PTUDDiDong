package com.nhom12.letscook.Model;


public class Food {
    private int id;
    private String name;
    private String description;
    private String image;
    private int views;
    private int categoryId;
    private String ingredients;
    private String instructions;
    private String createdAt;
    private String updatedAt;

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
    }

    // Getter và Setter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
    public int getViews() { return views; }
    public int getCategoryId() { return categoryId; }
    public String getIngredients() { return ingredients; }
    public String getInstructions() { return instructions; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}