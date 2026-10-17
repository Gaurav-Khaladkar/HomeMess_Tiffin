package com.homemess.tiffinservice.bridge;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import com.homemess.tiffinservice.db.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Android JavaScript Bridge exposing native SQLite, Call Intents,
 * Haptic Vibration, and System Notifications to the Bootstrap UI.
 */
public class WebAppInterface {

    private final Context context;
    private final DatabaseHelper dbHelper;

    public WebAppInterface(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    // ==================== AUTHENTICATION ====================

    @JavascriptInterface
    public String loginUser(String identifier, String password) {
        JSONObject response = new JSONObject();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + 
                    " WHERE (" + DatabaseHelper.COL_USER_EMAIL + " = ? OR " + DatabaseHelper.COL_USER_PHONE + " = ?) " +
                    " AND " + DatabaseHelper.COL_USER_PASSWORD + " = ?";
            cursor = db.rawQuery(query, new String[]{identifier, identifier, password});

            if (cursor != null && cursor.moveToFirst()) {
                response.put("success", true);
                response.put("id", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)));
                response.put("name", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME)));
                response.put("email", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMAIL)));
                response.put("phone", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_PHONE)));
                response.put("role", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ROLE)));
                response.put("address", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ADDRESS)));
                response.put("area", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_AREA)));
                response.put("city", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_CITY)));
                response.put("pincode", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_PINCODE)));
            } else {
                response.put("success", false);
                response.put("message", "Invalid Email/Phone or Password. Please try again.");
            }
        } catch (Exception e) {
            try {
                response.put("success", false);
                response.put("message", "Database error: " + e.getMessage());
            } catch (Exception ignored) {}
        } finally {
            if (cursor != null) cursor.close();
        }
        return response.toString();
    }

    @JavascriptInterface
    public String registerUser(String name, String email, String phone, String password, 
                               String role, String address, String area, String pincode, String city) {
        JSONObject response = new JSONObject();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_USER_NAME, name);
            values.put(DatabaseHelper.COL_USER_EMAIL, email);
            values.put(DatabaseHelper.COL_USER_PHONE, phone);
            values.put(DatabaseHelper.COL_USER_PASSWORD, password);
            values.put(DatabaseHelper.COL_USER_ROLE, role);
            values.put(DatabaseHelper.COL_USER_ADDRESS, address);
            values.put(DatabaseHelper.COL_USER_AREA, area);
            values.put(DatabaseHelper.COL_USER_PINCODE, pincode);
            values.put(DatabaseHelper.COL_USER_CITY, city);

            long id = db.insert(DatabaseHelper.TABLE_USERS, null, values);
            if (id != -1) {
                response.put("success", true);
                response.put("id", id);
                response.put("message", "Registration successful! Welcome to Home Mess.");
            } else {
                response.put("success", false);
                response.put("message", "Account already exists with this email address.");
            }
        } catch (Exception e) {
            try {
                response.put("success", false);
                response.put("message", "Registration error: " + e.getMessage());
            } catch (Exception ignored) {}
        }
        return response.toString();
    }

    // ==================== DISHES / MENU ====================

    @JavascriptInterface
    public String getDishes(String categoryFilter) {
        JSONArray array = new JSONArray();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_DISHES;
            String[] args = null;
            if (categoryFilter != null && !categoryFilter.isEmpty() && !categoryFilter.equalsIgnoreCase("All")) {
                query += " WHERE " + DatabaseHelper.COL_DISH_CATEGORY + " = ?";
                args = new String[]{categoryFilter};
            }
            query += " ORDER BY " + DatabaseHelper.COL_DISH_ID + " DESC";
            cursor = db.rawQuery(query, args);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    JSONObject obj = new JSONObject();
                    obj.put("id", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_ID)));
                    obj.put("mess_id", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_MESS_ID)));
                    obj.put("name", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_NAME)));
                    obj.put("description", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_DESC)));
                    obj.put("category", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_CATEGORY)));
                    obj.put("price", cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_PRICE)));
                    obj.put("quantity", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_QTY)));
                    obj.put("image", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_IMAGE)));
                    obj.put("is_available", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DISH_AVAILABLE)) == 1);
                    array.put(obj);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return array.toString();
    }

    @JavascriptInterface
    public String postDish(int messId, String name, String desc, String category, double price, int qty, String imageBase64) {
        JSONObject response = new JSONObject();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_DISH_MESS_ID, messId);
            values.put(DatabaseHelper.COL_DISH_NAME, name);
            values.put(DatabaseHelper.COL_DISH_DESC, desc);
            values.put(DatabaseHelper.COL_DISH_CATEGORY, category);
            values.put(DatabaseHelper.COL_DISH_PRICE, price);
            values.put(DatabaseHelper.COL_DISH_QTY, qty);
            values.put(DatabaseHelper.COL_DISH_IMAGE, imageBase64 != null ? imageBase64 : "default_dish.jpg");
            values.put(DatabaseHelper.COL_DISH_AVAILABLE, 1);

            long id = db.insert(DatabaseHelper.TABLE_DISHES, null, values);
            if (id != -1) {
                response.put("success", true);
                response.put("id", id);
                response.put("message", "Dish posted successfully to Home Mess menu!");
            } else {
                response.put("success", false);
                response.put("message", "Failed to insert dish.");
            }
        } catch (Exception e) {
            try {
                response.put("success", false);
                response.put("message", e.getMessage());
            } catch (Exception ignored) {}
        }
        return response.toString();
    }

    // ==================== ORDERS ====================

    @JavascriptInterface
    public String placeOrder(int customerId, String customerName, String customerPhone, 
                             String deliveryAddress, String itemsJson, double totalPrice, String paymentMode) {
        JSONObject response = new JSONObject();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            String otp = String.format(Locale.getDefault(), "%04d", new Random().nextInt(10000));
            String date = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_ORDER_CUST_ID, customerId);
            values.put(DatabaseHelper.COL_ORDER_CUST_NAME, customerName);
            values.put(DatabaseHelper.COL_ORDER_CUST_PHONE, customerPhone);
            values.put(DatabaseHelper.COL_ORDER_ADDRESS, deliveryAddress);
            values.put(DatabaseHelper.COL_ORDER_ITEMS_JSON, itemsJson);
            values.put(DatabaseHelper.COL_ORDER_TOTAL, totalPrice);
            values.put(DatabaseHelper.COL_ORDER_STATUS, "PLACED");
            values.put(DatabaseHelper.COL_ORDER_PAYMENT, paymentMode);
            values.put(DatabaseHelper.COL_ORDER_DATE, date);
            values.put(DatabaseHelper.COL_ORDER_AGENT_ID, 3); // Default assigned agent
            values.put(DatabaseHelper.COL_ORDER_OTP, otp);

            long orderId = db.insert(DatabaseHelper.TABLE_ORDERS, null, values);
            if (orderId != -1) {
                response.put("success", true);
                response.put("orderId", orderId);
                response.put("otp", otp);
                response.put("message", "Tiffin order placed successfully! Delivery OTP is: " + otp);
                vibrateDevice(80);
            } else {
                response.put("success", false);
                response.put("message", "Could not create order.");
            }
        } catch (Exception e) {
            try {
                response.put("success", false);
                response.put("message", e.getMessage());
            } catch (Exception ignored) {}
        }
        return response.toString();
    }

    @JavascriptInterface
    public String getAllOrders(String role, int userId) {
        JSONArray array = new JSONArray();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_ORDERS;
            String[] args = null;
            if ("customer".equalsIgnoreCase(role)) {
                query += " WHERE " + DatabaseHelper.COL_ORDER_CUST_ID + " = ?";
                args = new String[]{String.valueOf(userId)};
            }
            query += " ORDER BY " + DatabaseHelper.COL_ORDER_ID + " DESC";
            cursor = db.rawQuery(query, args);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    JSONObject obj = new JSONObject();
                    obj.put("id", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)));
                    obj.put("customerId", cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_CUST_ID)));
                    obj.put("customerName", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_CUST_NAME)));
                    obj.put("customerPhone", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_CUST_PHONE)));
                    obj.put("address", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ADDRESS)));
                    obj.put("items", new JSONArray(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ITEMS_JSON))));
                    obj.put("total", cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_TOTAL)));
                    obj.put("status", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_STATUS)));
                    obj.put("payment", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_PAYMENT)));
                    obj.put("date", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)));
                    obj.put("otp", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_OTP)));
                    array.put(obj);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return array.toString();
    }

    @JavascriptInterface
    public String updateOrderStatus(int orderId, String newStatus) {
        JSONObject response = new JSONObject();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_ORDER_STATUS, newStatus);
            int rows = db.update(DatabaseHelper.TABLE_ORDERS, values, DatabaseHelper.COL_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});
            response.put("success", rows > 0);
            response.put("message", "Order #" + orderId + " updated to " + newStatus);
            vibrateDevice(50);
        } catch (Exception e) {
            try {
                response.put("success", false);
                response.put("message", e.getMessage());
            } catch (Exception ignored) {}
        }
        return response.toString();
    }

    // ==================== HARDWARE & SYSTEM UTILITIES ====================

    @JavascriptInterface
    public void makePhoneCall(String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(callIntent);
        } catch (Exception e) {
            showToast("Cannot dial phone: " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void vibrateDevice(long milliseconds) {
        try {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (v != null && v.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(milliseconds);
                }
            }
        } catch (Exception ignored) {}
    }

    @JavascriptInterface
    public String getAppMetadata() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("appName", "Home Mess Tiffin Service");
            obj.put("version", "1.0.0");
            obj.put("institute", "Sir Parashurambhau College (SP College), Pune");
            obj.put("authors", "Gaurav Kaluram Khaladkar & Aditay Balasaheb More");
            obj.put("guide", "Miss. Rutuja Mam");
            obj.put("degree", "M.Sc (Computer Science) Part I, Semester II");
        } catch (Exception ignored) {}
        return obj.toString();
    }
}
