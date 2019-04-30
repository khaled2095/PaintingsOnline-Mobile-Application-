package com.example.paintingsonline.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Paintings
{

    private int id;
    private String name, description, image;
    private int price;
    private int quantity;


    public Paintings()
    {

    }

    public Paintings(int id, String name, String image, int price)
    {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public Paintings(int id, String name, String description, String image, int price)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public Paintings(String name, String image, int quantity , int price)
    {
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


}
