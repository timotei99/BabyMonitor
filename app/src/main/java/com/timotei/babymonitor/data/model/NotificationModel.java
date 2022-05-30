package com.timotei.babymonitor.data.model;

public class NotificationModel {

    private String userId;
    private String title;
    private String description;
    private String date;
    private String type;

    public NotificationModel() {
    }

    public NotificationModel(String title, String description, String date, String type) {
        this.userId=userId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
