package com.example.paintingsonline.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import java.util.List;

public class HomePaintingsAdapter extends RecyclerView.Adapter<HomePaintingsAdapter.PaintingViewHolder>
{
    private Context mctx;
    private List<Paintings> paintingsList;
    private OnClickPainting ocp;


    public HomePaintingsAdapter(Context mctx, List<Paintings> paintingsList, OnClickPainting mocp)
    {
        this.mctx = mctx;
        this.paintingsList = paintingsList;
        ocp = mocp;
    }

//    public HomePaintingsAdapter(Context mctx, List<Paintings> paintingsList)
//    {
//        this.mctx = mctx;
//        this.paintingsList = paintingsList;
//    }

    @NonNull
    @Override
    public PaintingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mctx);
        View view = layoutInflater.inflate(R.layout.row_home_item, viewGroup, false);
        return new PaintingViewHolder(view, ocp);
    }

    @Override
    public void onBindViewHolder(@NonNull PaintingViewHolder paintingViewHolder, int i)
    {
        SharedPreferences sp3 = PreferenceManager.getDefaultSharedPreferences(mctx);
        int tempdiscount = sp3.getInt("discount", 0);
        Log.d("temp", "t" + tempdiscount);
        final Paintings paintings = paintingsList.get(i);
        paintingViewHolder.owner.setText("By: " + paintings.getpOwner());
        paintingViewHolder.paintingName.setText("Artwork Name: " + paintings.getName());
        if (tempdiscount > 0 ) {
            int OldValue = (int)(paintings.getPrice() / ((100.0 - tempdiscount )/100.0));
            SpannableString text = new SpannableString("Starting From: " + String.valueOf(OldValue) + "  " +  String.valueOf(paintings.getPrice()));
            text.setSpan(new StrikethroughSpan(), 15, 15 + String.valueOf(OldValue).length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            paintingViewHolder.paintingPrice.setText(text);
        }
        else {
            paintingViewHolder.paintingPrice.setText("Starting From: " + String.valueOf(paintings.getPrice()));
        }


        //paintingViewHolder.paintingPrice.setPaintFlags(paintingViewHolder.paintingPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        paintingViewHolder.i1.setImageUrl(paintings.getImage(), MySingleton.getInstance(mctx).getImageLoader());

    }



    @Override
    public int getItemCount()
    {
        return paintingsList.size();
    }




    /*View Holder*/
    class PaintingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        NetworkImageView i1;
        TextView paintingName, owner, paintingPrice;
        OnClickPainting monClickPainting;

        public PaintingViewHolder(@NonNull View itemView, OnClickPainting onClickPainting)
        {
            super(itemView);

            i1 = itemView.findViewById(R.id.imgpaintings);
            paintingName = itemView.findViewById(R.id.painting_Name);
            paintingPrice = itemView.findViewById(R.id.painting_Price);
            owner = itemView.findViewById(R.id.owner);
            monClickPainting = onClickPainting;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            monClickPainting.clickPainting(getAdapterPosition());
            //monClickPainting.clickSearchedPainting(getAdapterPosition());
        }
    }

    public interface OnClickPainting
    {
        void clickPainting(int pos);
        //void clickSearchedPainting(int pos);
    }

}
