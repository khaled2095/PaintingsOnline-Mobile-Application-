package com.example.paintingsonline.Model;

public class Order
{
    private String PaintingOwner;
    private int PaintingID;
    private int OrderId;
    private String OrderStatus;
    private int OrderPrice;
    private String OrderDeatils;
    private String OrderAddressx;
    private String OrderImage;



    public Order()
    {

    }

    public Order(int orderId, String orderStatus, int orderPrice, String orderImage, String paintingowner)
    {
        OrderId = orderId;
        OrderStatus = orderStatus;
        OrderPrice = orderPrice;
        OrderImage = orderImage;
        PaintingOwner = paintingowner;

    }

    public String getPaintingOwner() {
        return PaintingOwner;
    }

    public void setPaintingOwner(String paintingowner) {
        PaintingOwner = paintingowner;
    }

    public int getPaintingID() {
        return PaintingID;
    }

    public void setPaintingID(int paintingID) {
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
}
