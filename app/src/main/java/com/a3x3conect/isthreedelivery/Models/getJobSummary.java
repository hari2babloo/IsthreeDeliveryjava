package com.a3x3conect.isthreedelivery.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hari on 20/3/18.
 */

public class getJobSummary {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("jobid")
    @Expose
    private String jobid;
    @SerializedName("invoiceId")
    @Expose
    private String invoiceId;
    @SerializedName("CustId")
    @Expose
    private String custId;
    @SerializedName("shift")
    @Expose
    private Object shift;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("category")
    @Expose
    private List<String> category = null;
    @SerializedName("price")
    @Expose
    private List<String> price = null;
    @SerializedName("qty")
    @Expose
    private List<String> qty = null;
    @SerializedName("subTotal")
    @Expose
    private List<String> subTotal = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("GSTPercentage")
    @Expose
    private String gSTPercentage;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("grandTotal")
    @Expose
    private String grandTotal;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("payableAmount")
    @Expose
    private String payableAmount;
    @SerializedName("amountPaid")
    @Expose
    private String amountPaid;
    @SerializedName("balanceAmountToPay")
    @Expose
    private String balanceAmountToPay;

    @SerializedName("expressDelivery")
    @Expose
    private String expressDelivery;

    @SerializedName("expressDeliveryCharge")
    @Expose
    private String expressDeliveryCharge;

    public String getgSTPercentage() {
        return gSTPercentage;
    }

    public void setgSTPercentage(String gSTPercentage) {
        this.gSTPercentage = gSTPercentage;
    }

    public String getExpressDelivery() {
        return expressDelivery;
    }

    public void setExpressDelivery(String expressDelivery) {
        this.expressDelivery = expressDelivery;
    }

    public String getExpressDeliveryCharge() {
        return expressDeliveryCharge;
    }

    public void setExpressDeliveryCharge(String expressDeliveryCharge) {
        this.expressDeliveryCharge = expressDeliveryCharge;
    }

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

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Object getShift() {
        return shift;
    }

    public void setShift(Object shift) {
        this.shift = shift;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<String> getQty() {
        return qty;
    }

    public void setQty(List<String> qty) {
        this.qty = qty;
    }

    public List<String> getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(List<String> subTotal) {
        this.subTotal = subTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGSTPercentage() {
        return gSTPercentage;
    }

    public void setGSTPercentage(String gSTPercentage) {
        this.gSTPercentage = gSTPercentage;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
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



}
