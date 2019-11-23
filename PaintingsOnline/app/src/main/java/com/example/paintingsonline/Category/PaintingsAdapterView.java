package com.example.paintingsonline.Category;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Database.ModelDB.Cart;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import java.util.List;

public class PaintingsAdapterView extends RecyclerView.Adapter<PaintingsAdapterView.PaintingHolder>
{

    private Context context;
    private List<Paintings> plist;
    private List<Cart> carts;
    //private PaintingActivity ParentClass;
    private OnPaintingListener onPaintingListener1;



    public PaintingsAdapterView(Context context, List<Paintings> plist, OnPaintingListener onPaintingListener)
    {
        this.context = context;
        this.plist = plist;
        //this.ParentClass = parentclass;
        onPaintingListener1 = onPaintingListener;
    }

    @NonNull
    @Override
    public PaintingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_painting_item, viewGroup, false);
        return new PaintingHolder(view, onPaintingListener1);
    }

    @Override
    public void onBindViewHolder(@NonNull final PaintingHolder paintingHolder, final int i)
    {
        SharedPreferences sp3 = PreferenceManager.getDefaultSharedPreferences(context);
        int tempdiscount = sp3.getInt("discount", 0);

        final Paintings p2 = plist.get(i);
        paintingHolder.paintOwner.setText("By: " + p2.getpOwner());
        paintingHolder.nameText.setText("ArtWork Name: " + p2.getName());
        paintingHolder.paintingPrice.setText("Starting From: " + p2.getPrice());

        if (tempdiscount > 0 )
        {
            int OldValue = (int)(p2.getPrice() / ((100.0 - tempdiscount )/100.0));
            SpannableString text = new SpannableString("Starting From: " + String.valueOf(OldValue) + "  " +  String.valueOf(p2.getPrice()));
            text.setSpan(new StrikethroughSpan(), 15, 15 + String.valueOf(OldValue).length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            paintingHolder.paintingPrice.setText(text);
        }
        else {
            paintingHolder.paintingPrice.setText("Starting From: " + p2.getPrice());
        }

        paintingHolder.i2.setImageUrl(p2.getImage(), MySingleton.getInstance(context).getImageLoader());

    }

    @Override
    public int getItemCount()
    {
        return plist.size();
    }

    /*View Holder*/
    class PaintingHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        NetworkImageView i2;
        TextView paintingPrice, nameText, paintOwner;
        OnPaintingListener monPaintingListener;


        public PaintingHolder(@NonNull View itemView, OnPaintingListener onPaintingListener)
        {
            super(itemView);
            i2 = itemView.findViewById(R.id.imgpaintings2);
            nameText = itemView.findViewById(R.id.painting_Name2);
            paintOwner = itemView.findViewById(R.id.owner2);
            paintingPrice = itemView.findViewById(R.id.painting_Price2);
            this.monPaintingListener = onPaintingListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            monPaintingListener.onPaintingClick(getAdapterPosition());
        }
    }

    public interface OnPaintingListener{
        void onPaintingClick(int position);
    }

}
