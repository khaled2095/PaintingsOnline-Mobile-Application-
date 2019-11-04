package com.example.paintingsonline.Model;

public class UserRating
{
    private String orderId;
    private String rateValue;
    private String Comment;

    public UserRating(String orderId, String rateValue, String comment)
    {
        //this.userName = userName;
        this.orderId = orderId;
        this.rateValue = rateValue;
        Comment = comment;
    }

    public UserRating(String rateValue, String comment)
    {
        this.rateValue = rateValue;
        this.Comment = comment;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public String getComment() {
        return Comment;
    }
}
