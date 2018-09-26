package com.a3x3conect.isthreedelivery.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class getPickupDeliveryOrders {

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
    @SerializedName("pickupScheduledAt")
    @Expose
    private String pickupScheduledAt;
    @SerializedName("pickupConfirmedDate")
    @Expose
    private String pickupConfirmedDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("count")
    @Expose
    private Object count;
    @SerializedName("grandTotal")
    @Expose
    private Object grandTotal;
    @SerializedName("paymentMode")
    @Expose
    private Object paymentMode;
    @SerializedName("payableAmount")
    @Expose
    private String payableAmount;
    @SerializedName("amountPaid")
    @Expose
    private String amountPaid;
    @SerializedName("balanceAmountToPay")
    @Expose
    private String balanceAmountToPay;
    @SerializedName("balanceAddedToWallet")
    @Expose
    private Object balanceAddedToWallet;
    @SerializedName("pickupCancelledDate")
    @Expose
    private String pickupCancelledDate;
    @SerializedName("orderProcessedDate")
    @Expose
    private String orderProcessedDate;
    @SerializedName("jobFinishedDate")
    @Expose
    private String jobFinishedDate;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @SerializedName("serviceName")
    @Expose
    private String serviceName;



    @SerializedName("expressDelivery")
    @Expose
    private String expressDelivery;

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

    public String getPickupScheduledAt() {
        return pickupScheduledAt;
    }

    public void setPickupScheduledAt(String pickupScheduledAt) {
        this.pickupScheduledAt = pickupScheduledAt;
    }

    public String getPickupConfirmedDate() {
        return pickupConfirmedDate;
    }

    public void setPickupConfirmedDate(String pickupConfirmedDate) {
        this.pickupConfirmedDate = pickupConfirmedDate;
    }

    public String getExpressDelivery() {
        return expressDelivery;
    }

    public void setExpressDelivery(String expressDelivery) {
        this.expressDelivery = expressDelivery;
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

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getBalanceAmountToPay() {
        return balanceAmountToPay;
    }

    public void setBalanceAmountToPay(String balanceAmountToPay) {
        this.balanceAmountToPay = balanceAmountToPay;
    }

    public Object getBalanceAddedToWallet() {
        return balanceAddedToWallet;
    }

    public void setBalanceAddedToWallet(Object balanceAddedToWallet) {
        this.balanceAddedToWallet = balanceAddedToWallet;
    }

    public String getPickupCancelledDate() {
        return pickupCancelledDate;
    }

    public void setPickupCancelledDate(String pickupCancelledDate) {
        this.pickupCancelledDate = pickupCancelledDate;
    }

    public String getOrderProcessedDate() {
        return orderProcessedDate;
    }

    public void setOrderProcessedDate(String orderProcessedDate) {
        this.orderProcessedDate = orderProcessedDate;
    }

    public String getJobFinishedDate() {
        return jobFinishedDate;
    }

    public void setJobFinishedDate(String jobFinishedDate) {
        this.jobFinishedDate = jobFinishedDate;
    }
}
