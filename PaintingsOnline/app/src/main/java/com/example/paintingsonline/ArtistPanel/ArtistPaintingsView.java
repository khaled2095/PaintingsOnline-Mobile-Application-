package com.example.paintingsonline.ArtistPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import java.util.List;

public class ArtistPaintingsView extends RecyclerView.Adapter<ArtistPaintingsView.ArtistPaintingHolder>
{

    private Context context;
    private List<Paintings> plist;
    private paintingsListener pl;



    public ArtistPaintingsView(Context context, List<Paintings> plist, paintingsListener mpl)
    {
        this.context = context;
        this.plist = plist;
        this.pl = mpl;
    }

    @NonNull
    @Override
    public ArtistPaintingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_bio_paintings, viewGroup, false);
        return new ArtistPaintingHolder(view, pl);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistPaintingHolder artistPaintingHolder, int i)
    {
        Paintings p2 = plist.get(i);
        artistPaintingHolder.nameText.setText("ArtWork Name: " + p2.getName());
        artistPaintingHolder.paintingPrice.setText("Starting From: " + String.valueOf(p2.getPrice()));

        artistPaintingHolder.i3.setImageUrl(p2.getImage(), MySingleton.getInstance(context).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return plist.size();
    }


    /*View Holder*/
    class ArtistPaintingHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        NetworkImageView i3;
        TextView paintingPrice, nameText;
        paintingsListener mpaintingsListener;


        public ArtistPaintingHolder(@NonNull View itemView, paintingsListener paintingsListener)
        {
            super(itemView);
            i3 = itemView.findViewById(R.id.Artistpaintings);
            nameText = itemView.findViewById(R.id.Artist_painting_Name);
            paintingPrice = itemView.findViewById(R.id.Artist_painting_Price);
            mpaintingsListener = paintingsListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            mpaintingsListener.clickPaintings(getAdapterPosition());
        }
    }

    public interface paintingsListener{
        void clickPaintings(int pos);
    }


}
