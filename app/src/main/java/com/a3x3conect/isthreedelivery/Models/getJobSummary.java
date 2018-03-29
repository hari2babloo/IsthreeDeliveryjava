package com.a3x3conect.isthreedelivery.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private String category;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("subTotal")
    @Expose
    private String subTotal;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("GSTPercentage")
    @Expose
    private String gSTPercentage;
    @SerializedName("count")
    @Expose
    private String count;
    @SerializedName("grandTotal")
    @Expose
    private String grandTotal;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
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

}
