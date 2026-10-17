package com.homemess.tiffinservice.model;

/**
 * User Entity Model
 * Roles: customer, chef, delivery
 */
public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private String address;
    private String area;
    private String pincode;
    private String city;

    public User() {}

    public User(int id, String name, String email, String phone, String password, String role, String address, String area, String pincode, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.address = address;
        this.area = area;
        this.pincode = pincode;
        this.city = city;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
