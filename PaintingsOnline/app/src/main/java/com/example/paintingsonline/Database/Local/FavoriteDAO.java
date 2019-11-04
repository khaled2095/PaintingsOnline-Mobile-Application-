package com.example.paintingsonline.Database.Local;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.paintingsonline.Database.ModelDB.Favourites;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FavoriteDAO
{
    @Query("SELECT * FROM Favourite WHERE username=:uname")
    Flowable<List<Favourites>> getFavoriteItems(String uname);

    @Query("SELECT EXISTS (SELECT 1 FROM Favourite WHERE paintingId=:paintId and username=:uname)")
    int isFavorite(String paintId, String uname);

    @Query("SELECT COUNT(*) FROM Favourite")
    int CountFavouriteItems();

    @Insert
    void insertFavorite(Favourites... favourites);

    @Delete
    void deleteFavoriteItem(Favourites favourites);
}
