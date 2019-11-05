package com.example.paintingsonline.Database.ModelDB;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cart")
public class Cart
{

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;


    @ColumnInfo(name = "paintingId")
    public String paintingid;

    @ColumnInfo(name = "paintingName")
    public String paintingname;

    @ColumnInfo(name = "paintingImage")
    public String paintingimg;

    @ColumnInfo(name = "paintingDesc")
    public String paintingdesc;

    @ColumnInfo(name = "paintingArtist")
    public String paintingartist;

    @ColumnInfo(name = "paintingSize")
    public String paintingsize;

    @ColumnInfo(name = "paintingPrice")
    public int price;

    @ColumnInfo(name = "quantity")
    public int qty;

    @ColumnInfo(name = "paintingstock")
    public int stock;





}
