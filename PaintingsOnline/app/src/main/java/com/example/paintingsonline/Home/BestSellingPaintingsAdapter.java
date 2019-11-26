package com.example.paintingsonline.Home;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import java.util.List;

public class BestSellingPaintingsAdapter extends RecyclerView.Adapter<BestSellingPaintingsAdapter.BestSellingViewHolder>
{
    private List<Paintings> paintingsArrayList;
    private Context mcontext;
    private ClickBestSelling cbs;


    public BestSellingPaintingsAdapter(List<Paintings> paintingsArrayList, Context mcontext, ClickBestSelling mcbs)
    {
        this.paintingsArrayList = paintingsArrayList;
        this.mcontext = mcontext;
        this.cbs = mcbs;
    }

    @NonNull
    @Override
    public BestSellingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.row_bestselling, viewGroup, false);
        return new BestSellingViewHolder(view, cbs);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellingViewHolder bestSellingViewHolder, int i)
    {
        final Paintings paintings = paintingsArrayList.get(i);
        bestSellingViewHolder.ownert1.setText(paintings.getpOwner());
        bestSellingViewHolder.namet2.setText(paintings.getName());
        bestSellingViewHolder.pricet3.setText(String.valueOf(paintings.getPrice()));
        bestSellingViewHolder.networkImageView.setImageUrl(paintings.getImage(), MySingleton.getInstance(mcontext).getImageLoader());
    }

    @Override
    public int getItemCount()
    {
        return paintingsArrayList.size();
    }


    public class BestSellingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        NetworkImageView networkImageView;
        TextView ownert1, namet2, pricet3;
        ClickBestSelling mclickBestSelling;

        public BestSellingViewHolder(@NonNull View itemView, ClickBestSelling clickBestSelling)
        {
            super(itemView);

            networkImageView = itemView.findViewById(R.id.bestImage);
            ownert1 = itemView.findViewById(R.id.bestowner);
            namet2 = itemView.findViewById(R.id.bestName);
            pricet3 = itemView.findViewById(R.id.bestPrice);
            mclickBestSelling = clickBestSelling;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            mclickBestSelling.onClickBest(getAdapterPosition());
        }
    }


    public interface ClickBestSelling{
        void onClickBest(int pos);
    }
}
