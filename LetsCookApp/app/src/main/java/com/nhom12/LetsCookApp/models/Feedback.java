package com.nhom12.LetsCookApp.models;

public class Feedback {
    private int feedbackId;
    private String username;
    private int foodId;
    private String foodName;
    private String message;
    private String timestamp;
    private int status;

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Feedback(int feedbackId, String username, int foodId, String foodName, String message, String timestamp, int status) {
        this.feedbackId = feedbackId;
        this.username = username;
        this.foodId = foodId;
        this.foodName = foodName;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;

    }


}
