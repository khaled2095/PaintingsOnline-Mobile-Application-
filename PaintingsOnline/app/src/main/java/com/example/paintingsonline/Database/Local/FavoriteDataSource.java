package com.example.paintingsonline.Database.Local;

import com.example.paintingsonline.Database.DataSource.IFavoriteDataSource;
import com.example.paintingsonline.Database.ModelDB.Favourites;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteDataSource implements IFavoriteDataSource
{

    private FavoriteDAO favoriteDAO;
    private static FavoriteDataSource instance;


    public FavoriteDataSource(FavoriteDAO favoriteDAO)
    {
        this.favoriteDAO = favoriteDAO;
    }


    public static FavoriteDataSource getInstance(FavoriteDAO favDAO)
    {
        if (instance == null)
        {
            instance = new FavoriteDataSource(favDAO);
        }

        return instance;
    }


    @Override
    public Flowable<List<Favourites>> getFavoriteItems(String uname) {
        return favoriteDAO.getFavoriteItems(uname);
    }

    @Override
    public int isFavorite(String paintId, String uname) {
        return favoriteDAO.isFavorite(paintId, uname);
    }

    @Override
    public void insertFavorite(Favourites... favourites) {
        favoriteDAO.insertFavorite(favourites);
    }

    @Override
    public void deleteFavoriteItem(Favourites favourites) {
        favoriteDAO.deleteFavoriteItem(favourites);
    }

    @Override
    public int CountFavouriteItems() {
        return favoriteDAO.CountFavouriteItems();
    }
}
