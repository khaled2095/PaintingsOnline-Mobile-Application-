package com.example.paintingsonline.Database.Local;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.paintingsonline.Database.ModelDB.Favourites;


@Database(entities = {Favourites.class}, version = 1)
public abstract class FavoriteDatabase extends RoomDatabase
{
    public abstract FavoriteDAO favoriteDAO();
    private static FavoriteDatabase instance;

    public static FavoriteDatabase getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context, FavoriteDatabase.class, "favDB").allowMainThreadQueries().build();

        }

        return instance;
    }
}
