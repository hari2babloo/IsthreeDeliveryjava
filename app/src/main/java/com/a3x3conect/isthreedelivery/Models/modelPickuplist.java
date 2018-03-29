package com.a3x3conect.isthreedelivery.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by b on 4/3/18.
 */

public class modelPickuplist {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jobid")
    @Expose
    private String jobid;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("count")
    @Expose
    private Object count;
    @SerializedName("grandTotal")
    @Expose
    private Object grandTotal;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("pic")
    @Expose
    private String pic;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("landMark")
    @Expose
    private String landMark;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("pincode")
    @Expose
    private String pincode;

    public String getPickupScheduledAt() {
        return pickupScheduledAt;
    }

    public void setPickupScheduledAt(String pickupScheduledAt) {
        this.pickupScheduledAt = pickupScheduledAt;
    }

    @SerializedName("pickupScheduledAt")
    @Expose
    private String pickupScheduledAt;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("longi")
    @Expose
    private String longi;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobid() {
        return jobid;
    }

    public void setJobid(String jobid) {
        this.jobid = jobid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getCount() {
        return count;
    }

    public void setCount(Object count) {
        this.count = count;
    }

    public Object getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Object grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
