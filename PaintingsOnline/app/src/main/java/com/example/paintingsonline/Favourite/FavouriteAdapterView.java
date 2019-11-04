package com.example.paintingsonline.Favourite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.paintingsonline.Database.ModelDB.Favourites;
import com.example.paintingsonline.R;

import java.util.List;

public class FavouriteAdapterView extends RecyclerView.Adapter<FavouriteAdapterView.FavouriteViewHolder>
{

    private Context favcon;
    private List<Favourites> favourites;
    private OnFavListener ofl;

    public FavouriteAdapterView(Context favcon, List<Favourites> favourites, OnFavListener mofl)
    {
        this.favcon = favcon;
        this.favourites = favourites;
        this.ofl = mofl;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View favview = LayoutInflater.from(favcon).inflate(R.layout.row_favourite, viewGroup, false);
        return new FavouriteViewHolder(favview, ofl);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder favouriteViewHolder, int i) {

        Favourites fav = favourites.get(i);

        favouriteViewHolder.favNameText.setText(new StringBuilder("Painting Name: ").append(fav.paintname));
        favouriteViewHolder.favPriceText.setText(new StringBuilder("$").append(fav.paintprice));
        favouriteViewHolder.favSizeText.setText(new StringBuilder("Size: ").append(fav.paintingSize));

        Glide.with(favcon).load(fav.paintingimg).into(favouriteViewHolder.favimage);


    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }



    class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        Button removeFav;
        ImageView favimage;
        TextView favPriceText, favNameText, favSizeText;
        OnFavListener monFavListener;

        public FavouriteViewHolder(@NonNull View itemView, OnFavListener onFavListener)
        {
            super(itemView);
            favimage = itemView.findViewById(R.id.favimg);
            favNameText = itemView.findViewById(R.id.favname);
            favPriceText = itemView.findViewById(R.id.favprice);
            favSizeText = itemView.findViewById(R.id.favsize);
            removeFav = itemView.findViewById(R.id.favremove);
            monFavListener = onFavListener;

            removeFav.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            monFavListener.OnDelete(getAdapterPosition());
        }
    }

    public interface OnFavListener{
        void OnDelete(int pos);
    }

}
