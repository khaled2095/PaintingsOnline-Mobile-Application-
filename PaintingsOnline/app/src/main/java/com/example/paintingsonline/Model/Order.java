package com.example.paintingsonline.Model;

public class Order
{
    private String PaintingOwner;
    private String PaintingID;
    private String PaintingName;
    private int OrderId;
    private String OrderStatus;
    private int OrderPrice;
    private int OrderQty;
    private String OrderDeatils;
    private String OrderAddressx;
    private String OrderImage;
    private String Rating;
    private String Comment;



    public Order()
    {

    }

    //To mark painting shipment status in selling panel
    public Order(String paintingID, String paintingName, int orderId, String orderStatus, int orderPrice, int orderqty) {
        PaintingID = paintingID;
        PaintingName = paintingName;
        OrderId = orderId;
        OrderStatus = orderStatus;
        OrderPrice = orderPrice;
        OrderQty = orderqty;
    }

    public Order(int orderId, String orderStatus, int orderPrice, String orderImage, String paintingowner, String rating)
    {
        OrderId = orderId;
        OrderStatus = orderStatus;
        OrderPrice = orderPrice;
        OrderImage = orderImage;
        PaintingOwner = paintingowner;
        Rating = rating;

    }

    public String getPaintingOwner() {
        return PaintingOwner;
    }

    public void setPaintingOwner(String paintingowner) {
        PaintingOwner = paintingowner;
    }

    public String getPaintingID() {
        return PaintingID;
    }

    public void setPaintingID(String paintingID) {
        PaintingID = paintingID;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public int getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderDeatils() {
        return OrderDeatils;
    }

    public void setOrderDeatils(String orderDeatils) {
        OrderDeatils = orderDeatils;
    }

    public String getOrderAddressx() {
        return OrderAddressx;
    }

    public void setOrderAddressx(String orderAddressx) {
        OrderAddressx = orderAddressx;
    }

    public String getOrderImage() {
        return OrderImage;
    }

    public void setOrderImage(String orderImage) {
        OrderImage = orderImage;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getPaintingName() {
        return PaintingName;
    }

    public void setPaintingName(String paintingName) {
        PaintingName = paintingName;
    }

    public int getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(int orderQty) {
        OrderQty = orderQty;
    }
}
