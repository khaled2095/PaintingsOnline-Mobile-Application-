package com.example.paintingsonline.Database.ModelDB;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Cart")
public class Cart
{

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;


    @ColumnInfo(name = "paintingId")
    public int paintingid;

    @ColumnInfo(name = "paintingName")
    public String paintingname;

    @ColumnInfo(name = "paintingImage")
    public String paintingimg;

    @ColumnInfo(name = "paintingDesc")
    public String paintingdesc;

    @ColumnInfo(name = "paintingPrice")
    public int price;

    @ColumnInfo(name = "quantity")
    public int qty;





}
