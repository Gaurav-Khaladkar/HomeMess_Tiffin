package com.homemess.tiffinservice.util;

/**
 * Global Constants for Home Mess Tiffin Service Application
 */
public final class Constants {

    private Constants() {}

    public static final String APP_NAME = "Home Mess Tiffin Service";
    public static final String COLLEGE = "Sir Parashurambhau College (SP College), Pune";
    public static final String AUTHORS = "Gaurav Kaluram Khaladkar & Aditay More";

    // Roles
    public static final String ROLE_CUSTOMER = "customer";
    public static final String ROLE_CHEF = "chef";
    public static final String ROLE_DELIVERY = "delivery";

    // Order Statuses
    public static final String STATUS_PLACED = "PLACED";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_PREPARING = "PREPARING";
    public static final String STATUS_OUT_FOR_DELIVERY = "OUT_FOR_DELIVERY";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    // Payment Modes
    public static final String PAY_UPI = "UPI (Google Pay/PhonePe)";
    public static final String PAY_COD = "Cash on Delivery";
}
