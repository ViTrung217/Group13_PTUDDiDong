package com.nhom12.LetsCookApp.models;

public class Feedback {
    private int feedbackId;
    private String username;
    private int foodId;
    private String foodName;
    private String message;
    private String timestamp;
    private int status;

    public Feedback(int feedbackId, String username, int foodId, String foodName, String message, String timestamp, int status) {
        this.feedbackId = feedbackId;
        this.username = username;
        this.foodId = this.foodId;
        this.foodName = foodName;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;

    }

    public int getFeedbackId() { return feedbackId; }
    public String getUsername() { return username; }
    public int getFoodId() { return foodId; }
    public String getFoodName() { return foodName; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
