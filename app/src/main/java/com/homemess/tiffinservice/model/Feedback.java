package com.homemess.tiffinservice.model;

/**
 * Customer Feedback Model
 */
public class Feedback {
    private int id;
    private int orderId;
    private String userName;
    private int rating;
    private String comment;
    private String createdAt;

    public Feedback() {}

    public Feedback(int id, int orderId, String userName, int rating, String comment, String createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
