package com.homemess.tiffinservice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * SQLite Database Manager for Home Mess Tiffin Service Android App
 * Handles Users, Menus/Dishes, Orders, and Customer Feedback.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HomeMessTiffin.db";
    private static final int DATABASE_VERSION = 1;

    // Table: Users
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PHONE = "phone";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_ROLE = "role"; // customer, chef, delivery
    public static final String COL_USER_ADDRESS = "address";
    public static final String COL_USER_AREA = "area";
    public static final String COL_USER_PINCODE = "pincode";
    public static final String COL_USER_CITY = "city";

    // Table: Dishes
    public static final String TABLE_DISHES = "dishes";
    public static final String COL_DISH_ID = "id";
    public static final String COL_DISH_MESS_ID = "mess_id";
    public static final String COL_DISH_NAME = "dish_name";
    public static final String COL_DISH_DESC = "description";
    public static final String COL_DISH_CATEGORY = "category";
    public static final String COL_DISH_PRICE = "price";
    public static final String COL_DISH_QTY = "quantity";
    public static final String COL_DISH_IMAGE = "image_url";
    public static final String COL_DISH_AVAILABLE = "is_available";

    // Table: Orders
    public static final String TABLE_ORDERS = "orders";
    public static final String COL_ORDER_ID = "id";
    public static final String COL_ORDER_CUST_ID = "customer_id";
    public static final String COL_ORDER_CUST_NAME = "customer_name";
    public static final String COL_ORDER_CUST_PHONE = "customer_phone";
    public static final String COL_ORDER_ADDRESS = "delivery_address";
    public static final String COL_ORDER_ITEMS_JSON = "items_json";
    public static final String COL_ORDER_TOTAL = "total_price";
    public static final String COL_ORDER_STATUS = "status"; // PLACED, ACCEPTED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    public static final String COL_ORDER_PAYMENT = "payment_mode";
    public static final String COL_ORDER_DATE = "order_date";
    public static final String COL_ORDER_AGENT_ID = "delivery_agent_id";
    public static final String COL_ORDER_OTP = "otp";

    // Table: Feedback
    public static final String TABLE_FEEDBACK = "feedback";
    public static final String COL_FB_ID = "id";
    public static final String COL_FB_ORDER_ID = "order_id";
    public static final String COL_FB_USER_NAME = "user_name";
    public static final String COL_FB_RATING = "rating";
    public static final String COL_FB_COMMENT = "comment";
    public static final String COL_FB_DATE = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_PHONE + " TEXT, " +
                COL_USER_PASSWORD + " TEXT, " +
                COL_USER_ROLE + " TEXT, " +
                COL_USER_ADDRESS + " TEXT, " +
                COL_USER_AREA + " TEXT, " +
                COL_USER_PINCODE + " TEXT, " +
                COL_USER_CITY + " TEXT)";
        db.execSQL(createUsers);

        // Create Dishes Table
        String createDishes = "CREATE TABLE " + TABLE_DISHES + " (" +
                COL_DISH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DISH_MESS_ID + " INTEGER, " +
                COL_DISH_NAME + " TEXT, " +
                COL_DISH_DESC + " TEXT, " +
                COL_DISH_CATEGORY + " TEXT, " +
                COL_DISH_PRICE + " REAL, " +
                COL_DISH_QTY + " INTEGER, " +
                COL_DISH_IMAGE + " TEXT, " +
                COL_DISH_AVAILABLE + " INTEGER DEFAULT 1)";
        db.execSQL(createDishes);

        // Create Orders Table
        String createOrders = "CREATE TABLE " + TABLE_ORDERS + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_CUST_ID + " INTEGER, " +
                COL_ORDER_CUST_NAME + " TEXT, " +
                COL_ORDER_CUST_PHONE + " TEXT, " +
                COL_ORDER_ADDRESS + " TEXT, " +
                COL_ORDER_ITEMS_JSON + " TEXT, " +
                COL_ORDER_TOTAL + " REAL, " +
                COL_ORDER_STATUS + " TEXT, " +
                COL_ORDER_PAYMENT + " TEXT, " +
                COL_ORDER_DATE + " TEXT, " +
                COL_ORDER_AGENT_ID + " INTEGER DEFAULT 0, " +
                COL_ORDER_OTP + " TEXT)";
        db.execSQL(createOrders);

        // Create Feedback Table
        String createFeedback = "CREATE TABLE " + TABLE_FEEDBACK + " (" +
                COL_FB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FB_ORDER_ID + " INTEGER, " +
                COL_FB_USER_NAME + " TEXT, " +
                COL_FB_RATING + " INTEGER, " +
                COL_FB_COMMENT + " TEXT, " +
                COL_FB_DATE + " TEXT)";
        db.execSQL(createFeedback);

        // Seed default demo accounts and rich Pune mess menu
        seedInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void seedInitialData(SQLiteDatabase db) {
        // 1. Seed Customer Demo User
        ContentValues cust = new ContentValues();
        cust.put(COL_USER_NAME, "Gaurav Khaladkar (Student)");
        cust.put(COL_USER_EMAIL, "customer@gmail.com");
        cust.put(COL_USER_PHONE, "9527298977");
        cust.put(COL_USER_PASSWORD, "pass123");
        cust.put(COL_USER_ROLE, "customer");
        cust.put(COL_USER_ADDRESS, "Flat 402, Sai Residency, Near SP College");
        cust.put(COL_USER_AREA, "Tilak Road, Sadashiv Peth");
        cust.put(COL_USER_PINCODE, "411030");
        cust.put(COL_USER_CITY, "Pune");
        db.insert(TABLE_USERS, null, cust);

        // 2. Seed Mess Owner / Chef Demo User
        ContentValues chef = new ContentValues();
        chef.put(COL_USER_NAME, "Annapurna Home Mess (Chef)");
        chef.put(COL_USER_EMAIL, "chef@gmail.com");
        chef.put(COL_USER_PHONE, "9822012345");
        chef.put(COL_USER_PASSWORD, "pass123");
        chef.put(COL_USER_ROLE, "chef");
        chef.put(COL_USER_ADDRESS, "Shop 12, Tilak Road, Opp. SP College");
        chef.put(COL_USER_AREA, "Sadashiv Peth");
        chef.put(COL_USER_PINCODE, "411030");
        chef.put(COL_USER_CITY, "Pune");
        db.insert(TABLE_USERS, null, chef);

        // 3. Seed Delivery Agent Demo User
        ContentValues rider = new ContentValues();
        rider.put(COL_USER_NAME, "Rahul Shinde (Rider)");
        rider.put(COL_USER_EMAIL, "delivery@gmail.com");
        rider.put(COL_USER_PHONE, "9765432100");
        rider.put(COL_USER_PASSWORD, "pass123");
        rider.put(COL_USER_ROLE, "delivery");
        rider.put(COL_USER_ADDRESS, "Near Alka Talkies, Pune");
        rider.put(COL_USER_AREA, "Shivajinagar");
        rider.put(COL_USER_PINCODE, "411005");
        rider.put(COL_USER_CITY, "Pune");
        db.insert(TABLE_USERS, null, rider);

        // Seed Authentic Pune Home Mess Dishes
        insertDish(db, "Special Maharashtrian Veg Thali", "3 Fresh Chapatis, Dal Tadka, Sukhi Bhaji, Patal Bhaji, Steamed Basmati Rice, Gulab Jamun & Papad.", "Thali", 120.0, 50, "thali_veg.jpg");
        insertDish(db, "Special Puran Poli Thali", "2 Puran Poli with Desi Ghee, Katachi Amti, Batata Bhaji, Rice, Papad, and Pickle.", "Special", 160.0, 30, "puran_poli.jpg");
        insertDish(db, "Student Budget Thali (Daily Mess)", "3 Chapatis, 1 Sabzi of the day, Dal Fry, Jeera Rice, and Fresh Green Salad.", "Thali", 80.0, 100, "budget_thali.jpg");
        insertDish(db, "Kolhapuri Chicken Tiffin", "Authentic Kolhapuri Chicken Curry, 3 Bhakri/Chapatis, Tambda Rassa, Indrayani Rice, and Solkadhi.", "Non-Veg", 180.0, 35, "chicken_thali.jpg");
        insertDish(db, "Kanda Poha & Masala Chai", "Fresh breakfast kanda poha garnished with roasted peanuts, fresh coriander, grated coconut, and hot tea.", "Breakfast", 40.0, 80, "poha.jpg");
        insertDish(db, "South Indian Idli Sambhar (4 Pcs)", "Steamed soft idlis served with hot vegetable sambhar and fresh coconut chutney.", "Breakfast", 50.0, 60, "idli.jpg");
        insertDish(db, "Healthy Diet Sprouts & Paneer Bowl", "Boiled moong sprouts, high-protein paneer cubes, cucumber, tomato, lime, and chaat masala.", "Diet", 90.0, 40, "diet_bowl.jpg");
        insertDish(db, "Egg Curry Deluxe Tiffin", "2 Boiled eggs in rich onion-tomato gravy, 3 Chapatis, Steamed Rice, and Fried Papad.", "Non-Veg", 130.0, 45, "egg_curry.jpg");

        // Seed Initial Sample Orders
        insertSampleOrder(db, 1, "Gaurav Khaladkar (Student)", "9527298977", "Flat 402, Sai Residency, Tilak Road, Pune", 
                "[{\"name\":\"Special Maharashtrian Veg Thali\",\"qty\":2,\"price\":120.0}]", 240.0, "PREPARING", "UPI (GPay)", "4829");
        insertSampleOrder(db, 1, "Gaurav Khaladkar (Student)", "9527298977", "Flat 402, Sai Residency, Tilak Road, Pune", 
                "[{\"name\":\"Kanda Poha & Masala Chai\",\"qty\":1,\"price\":40.0}]", 40.0, "OUT_FOR_DELIVERY", "Cash on Delivery", "7193");
    }

    private void insertDish(SQLiteDatabase db, String name, String desc, String category, double price, int qty, String img) {
        ContentValues v = new ContentValues();
        v.put(COL_DISH_MESS_ID, 1);
        v.put(COL_DISH_NAME, name);
        v.put(COL_DISH_DESC, desc);
        v.put(COL_DISH_CATEGORY, category);
        v.put(COL_DISH_PRICE, price);
        v.put(COL_DISH_QTY, qty);
        v.put(COL_DISH_IMAGE, img);
        v.put(COL_DISH_AVAILABLE, 1);
        db.insert(TABLE_DISHES, null, v);
    }

    private void insertSampleOrder(SQLiteDatabase db, int custId, String name, String phone, String address, String itemsJson, double total, String status, String payment, String otp) {
        ContentValues v = new ContentValues();
        v.put(COL_ORDER_CUST_ID, custId);
        v.put(COL_ORDER_CUST_NAME, name);
        v.put(COL_ORDER_CUST_PHONE, phone);
        v.put(COL_ORDER_ADDRESS, address);
        v.put(COL_ORDER_ITEMS_JSON, itemsJson);
        v.put(COL_ORDER_TOTAL, total);
        v.put(COL_ORDER_STATUS, status);
        v.put(COL_ORDER_PAYMENT, payment);
        v.put(COL_ORDER_DATE, new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date()));
        v.put(COL_ORDER_AGENT_ID, 3);
        v.put(COL_ORDER_OTP, otp);
        db.insert(TABLE_ORDERS, null, v);
    }
}
