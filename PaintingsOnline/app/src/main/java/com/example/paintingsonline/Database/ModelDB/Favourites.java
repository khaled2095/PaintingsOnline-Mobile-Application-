package com.example.paintingsonline.Database.ModelDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "Favourite" , primaryKeys = {"paintingId","username"})
public class Favourites
{
    @NonNull
    @ColumnInfo(name = "paintingId")
    public String paintID;

    @NonNull
    @ColumnInfo(name = "username")
    public String userName;

    @ColumnInfo(name = "paintingName")
    public String paintname;

    @ColumnInfo(name = "paintingImage")
    public String paintingimg;

    @ColumnInfo(name = "paintingPrice")
    public String paintprice;

    @ColumnInfo(name = "paintingSize")
    public String paintingSize;

}
