package com.a3x3conect.isthreedelivery.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Modelmanagertransactions {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jobId")
    @Expose
    private Object jobId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("amount_payable")
    @Expose
    private Object amountPayable;
    @SerializedName("transaction_amount")
    @Expose
    private String transactionAmount;
    @SerializedName("balance_amount_to_pay")
    @Expose
    private Object balanceAmountToPay;
    @SerializedName("balanceAddedToWallet")
    @Expose
    private Object balanceAddedToWallet;
    @SerializedName("transaction_time")
    @Expose
    private String transactionTime;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("customerId")
    @Expose
    private String customerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getJobId() {
        return jobId;
    }

    public void setJobId(Object jobId) {
        this.jobId = jobId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getAmountPayable() {
        return amountPayable;
    }

    public void setAmountPayable(Object amountPayable) {
        this.amountPayable = amountPayable;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Object getBalanceAmountToPay() {
        return balanceAmountToPay;
    }

    public void setBalanceAmountToPay(Object balanceAmountToPay) {
        this.balanceAmountToPay = balanceAmountToPay;
    }

    public Object getBalanceAddedToWallet() {
        return balanceAddedToWallet;
    }

    public void setBalanceAddedToWallet(Object balanceAddedToWallet) {
        this.balanceAddedToWallet = balanceAddedToWallet;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}
