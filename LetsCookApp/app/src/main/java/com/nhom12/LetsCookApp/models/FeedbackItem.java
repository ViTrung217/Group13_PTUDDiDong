package com.nhom12.LetsCookApp.models;

public class FeedbackItem {
    private int feedbackId;
    private String foodName;
    private String message;
    private String timestamp;

    public FeedbackItem(int feedbackId, String foodName, String message, String timestamp) {
        this.feedbackId = feedbackId;
        this.foodName = foodName;
        this.message = message;
        this.timestamp = timestamp;

    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}