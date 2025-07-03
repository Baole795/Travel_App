package com.example.travelapp.Domain;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private String role;
    private boolean locked = false;
    private String uid;

    // Constructor rỗng cho Firebase
    public User() {
    }

    // Constructor đầy đủ
    public User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName; // Sửa lại từ this.lastName = firstName
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = "user"; // Mặc định là user
        this.locked = false;// mặc định là không bị khoá
    }

    // Getters và Setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }


}


