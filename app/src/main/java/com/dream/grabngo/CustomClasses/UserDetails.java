package com.dream.grabngo.CustomClasses;

import android.graphics.Bitmap;

public class UserDetails {
    private String customerId,
            customerName,
            emailId,
            phoneNo;
    private Bitmap profileImage;

    public UserDetails(String customerId, String customerName, String emailId, String phoneNo, Bitmap profileImage) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.emailId = emailId;
        this.phoneNo = phoneNo;
        this.profileImage = profileImage;
    }

    public UserDetails() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}
