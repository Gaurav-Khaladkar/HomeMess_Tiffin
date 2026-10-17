package com.homemess.tiffinservice.model;

/**
 * Dish / Menu Item Model
 * Categories: Thali, Breakfast, Special, Non-Veg, Diet
 */
public class Dish {
    private int id;
    private int messId;
    private String name;
    private String description;
    private String category;
    private double price;
    private int quantity;
    private String imageUrl;
    private boolean isAvailable;

    public Dish() {}

    public Dish(int id, int messId, String name, String description, String category, double price, int quantity, String imageUrl, boolean isAvailable) {
        this.id = id;
        this.messId = messId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMessId() { return messId; }
    public void setMessId(int messId) { this.messId = messId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
