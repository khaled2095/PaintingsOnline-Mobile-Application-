package com.example.paintingsonline.Database.DataSource;



import com.example.paintingsonline.Database.ModelDB.Favourites;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource
{
    Flowable<List<Favourites>> getFavoriteItems(String uname);

    int isFavorite(String paintId, String uname);

    int CountFavouriteItems();

    void insertFavorite(Favourites...favourites);

    void deleteFavoriteItem(Favourites favourites);
}
