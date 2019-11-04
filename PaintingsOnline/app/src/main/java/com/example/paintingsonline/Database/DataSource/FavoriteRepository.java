package com.example.paintingsonline.Database.DataSource;

import com.example.paintingsonline.Database.ModelDB.Favourites;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRepository implements IFavoriteDataSource
{

    private IFavoriteDataSource favoriteDataSource;


    public FavoriteRepository(IFavoriteDataSource favoriteDataSource)
    {
        this.favoriteDataSource = favoriteDataSource;
    }


    private static FavoriteRepository instance;

    public static FavoriteRepository getInstance(IFavoriteDataSource iFavoriteDataSource)
    {
        if (instance == null)
        {
            instance = new FavoriteRepository(iFavoriteDataSource);
        }

        return instance;
    }


    @Override
    public Flowable<List<Favourites>> getFavoriteItems(String uname)
    {
        return favoriteDataSource.getFavoriteItems(uname);
    }

    @Override
    public int isFavorite(String paintId, String uname)
    {
        return favoriteDataSource.isFavorite(paintId, uname);
    }

    @Override
    public void insertFavorite(Favourites... favourites) {
        favoriteDataSource.insertFavorite(favourites);
    }

    @Override
    public void deleteFavoriteItem(Favourites favourites)
    {
        favoriteDataSource.deleteFavoriteItem(favourites);
    }

    @Override
    public int CountFavouriteItems() {
        return favoriteDataSource.CountFavouriteItems();
    }
}
