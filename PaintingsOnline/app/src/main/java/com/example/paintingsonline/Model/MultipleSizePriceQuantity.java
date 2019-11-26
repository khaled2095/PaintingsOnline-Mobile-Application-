package com.example.paintingsonline.Model;

public class MultipleSizePriceQuantity
{
    private String length;
    private String width;
    private String quantity;
    private String price;
    private String[] receiveSize;
    private String[] receivePrices;
    private String[] receiveQuantity;

    public MultipleSizePriceQuantity(String length, String width, String quantity, String price)
    {
        this.length = length;
        this.width = width;
        this.quantity = quantity;
        this.price = price;
    }


    public MultipleSizePriceQuantity(String[] receiveSize, String[] receivePrices, String[] receiveQuantity)
    {
        this.receiveSize = receiveSize;
        this.receivePrices = receivePrices;
        this.receiveQuantity = receiveQuantity;
    }

    public String[] getReceiveSize() {
        return receiveSize;
    }

    public String[] getReceivePrices() {
        return receivePrices;
    }

    public String[] getReceiveQuantity() {
        return receiveQuantity;
    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }
}
