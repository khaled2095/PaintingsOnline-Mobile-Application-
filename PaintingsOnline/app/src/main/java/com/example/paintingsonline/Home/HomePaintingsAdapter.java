package com.example.paintingsonline.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;

import java.util.List;

public class HomePaintingsAdapter extends RecyclerView.Adapter<HomePaintingsAdapter.PaintingViewHolder>
{
    private Context mctx;
    private List<Paintings> paintingsList;


    public HomePaintingsAdapter(Context mctx, List<Paintings> paintingsList)
    {
        this.mctx = mctx;
        this.paintingsList = paintingsList;
    }

    @NonNull
    @Override
    public PaintingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mctx);
        View view = layoutInflater.inflate(R.layout.row_home_item, viewGroup, false);
        return new PaintingViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PaintingViewHolder paintingViewHolder, int i)
    {
        Paintings paintings = paintingsList.get(i);
        paintingViewHolder.paintingName.setText(paintings.getName());
        paintingViewHolder.paintingPrice.setText(String.valueOf(paintings.getPrice()));

        Glide.with(mctx).load(paintings.getImage()).into(paintingViewHolder.i1);

    }

    @Override
    public int getItemCount()
    {
        return paintingsList.size();
    }

    /*View Holder*/
    class PaintingViewHolder extends RecyclerView.ViewHolder
    {

        ImageView i1;
        TextView paintingName, paintingPrice;

        public PaintingViewHolder(@NonNull View itemView)
        {
            super(itemView);

            i1 = itemView.findViewById(R.id.imgpaintings);
            paintingName = itemView.findViewById(R.id.painting_Name);
            paintingPrice = itemView.findViewById(R.id.price);
        }
    }
}
