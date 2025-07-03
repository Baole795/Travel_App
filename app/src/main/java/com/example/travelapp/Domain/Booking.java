package com.example.travelapp.Domain;

import java.io.Serializable;

public class Booking implements Serializable {
    private String bookingId;
    private String userId;
    private String itemId;
    private String bookingDate;
    private String status;

    private String userFullName;
    private String userEmail;
    private String userPhoneNumber;
    private String itemTitle;
    private String itemPicUrl;
    private int itemPrice;

    // ▼▼▼ THÊM 2 TRƯỜNG MỚI ▼▼▼
    private String itemDuration;
    private int itemBed;
    // ▲▲▲ KẾT THÚC PHẦN THÊM MỚI ▲▲▲

    public Booking() {
        // Constructor rỗng
    }

    // Getters and Setters (toàn bộ)

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserPhoneNumber() { return userPhoneNumber; }
    public void setUserPhoneNumber(String userPhoneNumber) { this.userPhoneNumber = userPhoneNumber; }

    public String getItemTitle() { return itemTitle; }
    public void setItemTitle(String itemTitle) { this.itemTitle = itemTitle; }

    public String getItemPicUrl() { return itemPicUrl; }
    public void setItemPicUrl(String itemPicUrl) { this.itemPicUrl = itemPicUrl; }

    public int getItemPrice() { return itemPrice; }
    public void setItemPrice(int itemPrice) { this.itemPrice = itemPrice; }

    // ▼▼▼ THÊM GETTER/SETTER CHO 2 TRƯỜNG MỚI ▼▼▼
    public String getItemDuration() { return itemDuration; }
    public void setItemDuration(String itemDuration) { this.itemDuration = itemDuration; }

    public int getItemBed() { return itemBed; }
    public void setItemBed(int itemBed) { this.itemBed = itemBed; }
    // ▲▲▲ KẾT THÚC PHẦN THÊM MỚI ▲▲▲
}