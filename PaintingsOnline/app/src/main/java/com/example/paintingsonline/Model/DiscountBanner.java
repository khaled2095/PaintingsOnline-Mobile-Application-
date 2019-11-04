package com.example.paintingsonline.Model;

public class DiscountBanner
{
    String id;
    String discounturl;
    String Discount;
    int active;

    public DiscountBanner(String id, String discounturl, String discount, int active)
    {
        this.id = id;
        this.discounturl = discounturl;
        Discount = discount;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getDiscounturl() {
        return discounturl;
    }

    public String getDiscount() {
        return Discount;
    }

    public int getActive() {
        return active;
    }
}
