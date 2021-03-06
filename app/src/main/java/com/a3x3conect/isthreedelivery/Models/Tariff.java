package com.a3x3conect.isthreedelivery.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hari on 15/2/18.
 */

public class Tariff {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category")
    @Expose
    private String type;
    @SerializedName("price")
    @Expose
    private String price;

    public String getHangerPrice() {
        return hangerPrice;
    }

    public void setHangerPrice(String hangerPrice) {
        this.hangerPrice = hangerPrice;
    }

    @SerializedName("hangerPrice")
    @Expose
    private String hangerPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}