package com.example.paintingsonline.Order;

public class Order
{
    private String UserName;
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


    public Order(int orderId, String orderStatus, int orderPrice, String orderImage)
    {
        OrderId = orderId;
        OrderStatus = orderStatus;
        OrderPrice = orderPrice;
        OrderImage = orderImage;
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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
