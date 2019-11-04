package com.example.paintingsonline.Model;

public class PaintingOrders
{
    private int order_id;
    private int paintings_id;
    private String order_status;
    private String Artist;
    private String Buyer;
    private String Address;
    private int Price;
    private String order_detail;
    private int quantity;

    public PaintingOrders()
    {

    }


    public int getOrder_id() {
        return order_id;
    }

    public int getPaintings_id() {
        return paintings_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getArtist() {
        return Artist;
    }

    public String getBuyer() {
        return Buyer;
    }

    public String getAddress() {
        return Address;
    }

    public int getPrice() {
        return Price;
    }

    public String getOrder_detail() {
        return order_detail;
    }

    public int getQuantity() {
        return quantity;
    }
}
