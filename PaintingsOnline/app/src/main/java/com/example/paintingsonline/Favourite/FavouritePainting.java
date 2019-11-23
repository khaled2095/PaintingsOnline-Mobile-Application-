package com.example.paintingsonline.Favourite;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paintingsonline.Database.DataSource.FavoriteRepository;
import com.example.paintingsonline.Database.Local.FavoriteDataSource;
import com.example.paintingsonline.Database.Local.FavoriteDatabase;
import com.example.paintingsonline.Database.ModelDB.Favourites;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavouritePainting extends AppCompatActivity
{

    public static FavoriteDatabase favoriteDatabase;
    public static FavoriteRepository favoriteRepository;

    CompositeDisposable favcompositeDisposable;
    RecyclerView favrecyclerView;

    TextView emptyFav;

    List<Favourites> favList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_painting);

        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            Toast.makeText(this, "You Must Login To View Your Favourites", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
        }


        favoriteDatabase = FavoriteDatabase.getInstance(this);
        favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(favoriteDatabase.favoriteDAO()));

        emptyFav = findViewById(R.id.emptyfav);

        favcompositeDisposable = new CompositeDisposable();

        favrecyclerView = findViewById(R.id.recyclerfav);
        favrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favrecyclerView.setHasFixedSize(true);

//        if(favoriteRepository.CountFavouriteItems() == 0)
//        {
//            favrecyclerView.setVisibility(View.GONE);
//            emptyFav.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            favrecyclerView.setVisibility(View.VISIBLE);
//            emptyFav.setVisibility(View.GONE);
//        }


        /*setup backarrow for NAVIGATION */
        ImageView backarrow = findViewById(R.id.backarrow3);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadFavouriteItems();
    }


    private void loadFavouriteItems()
    {
        favcompositeDisposable.add(favoriteRepository.getFavoriteItems(SharedPrefManager.getInstance(FavouritePainting.this).getUserName())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<Favourites>>() {
            @Override
            public void accept(List<Favourites> favourites) throws Exception 
            {
                displayFavItems(favourites);
            }
        }));
    }


    private void displayFavItems(final List<Favourites> favourites)
    {
        favList = favourites;

        FavouriteAdapterView favouriteAdapterView = new FavouriteAdapterView(this, favourites, new FavouriteAdapterView.OnFavListener() {
            @Override
            public void OnDelete(final int pos)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(FavouritePainting.this);
                builder1.setMessage("Are you sure, you want to remove this Painting?.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FavoriteDatabase favd = FavoriteDatabase.getInstance(getApplicationContext());
                                FavoriteRepository favRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(favd.favoriteDAO()));
                                favRepository.deleteFavoriteItem(favList.get(pos));
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        favrecyclerView.setAdapter(favouriteAdapterView);


        if (favList.size() == 0)
        {
            favrecyclerView.setVisibility(View.GONE);
            emptyFav.setVisibility(View.VISIBLE);
        }
        else
        {
            favrecyclerView.setVisibility(View.VISIBLE);
            emptyFav.setVisibility(View.GONE);
        }
    }

}
