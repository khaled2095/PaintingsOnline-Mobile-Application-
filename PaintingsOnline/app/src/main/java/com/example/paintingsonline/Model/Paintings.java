package com.example.paintingsonline.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Paintings implements Parcelable
{
    private String id;
    private String name;
    private String description;
    private String image;
    private String pOwner;
    private String pSize;


    private String small;
    private int price;
    private int quantity;
    private int distance;


    //Constructor for Featured Products and Best Selling
    public Paintings(String id, String name, String image, String pOwner, int price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.pOwner = pOwner;
        this.price = price;
    }


    public Paintings(String id, String small, int distance)
    {
        this.id = id;
        this.small = small;
        this.distance = distance;

    }

    public Paintings()
    {

    }


    //For Search Activity
    public Paintings(String id, String name, String description, String pOwner, String image)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pOwner = pOwner;
        this.image = image;
    }

    public Paintings(String id, String name, String description, String image, int price, String pOwner)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.pOwner = pOwner;
    }

    public Paintings(String id, String name, String image, int price)
    {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public Paintings(String id, String name, String description, String image, int price, int quantity, String pOwner, String size)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.pOwner = pOwner;
        this.pSize = size;
    }

    public Paintings(String name, String image, int price, int quantity)
    {
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }


    public String getSmall() {
        return small;
    }

    public int getDistance() {
        return distance;
    }



    protected Paintings(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        image = in.readString();
        pOwner = in.readString();
        pSize = in.readString();
        price = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<Paintings> CREATOR = new Creator<Paintings>() {
        @Override
        public Paintings createFromParcel(Parcel in) {
            return new Paintings(in);
        }

        @Override
        public Paintings[] newArray(int size) {
            return new Paintings[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getpOwner() {
        return pOwner;
    }

    public void setpOwner(String pOwner) {
        this.pOwner = pOwner;
    }

    public String getpSize() {
        return pSize;
    }

    public void setpSize(String pSize) {
        this.pSize = pSize;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(pOwner);
        dest.writeString(pSize);
        dest.writeInt(price);
        dest.writeInt(quantity);
    }
}
