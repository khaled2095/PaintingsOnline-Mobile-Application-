package com.example.paintingsonline.Home;

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

public class FeaturedPaintingsAdapter extends RecyclerView.Adapter<FeaturedPaintingsAdapter.FeaturedViewHolder>
{
    private List<Paintings> paintingsArrayList;
    private Context mcontext;
    private ClickFeatured clf;


    public FeaturedPaintingsAdapter(List<Paintings> paintingsArrayList, Context mcontext, ClickFeatured mclf)
    {
        this.paintingsArrayList = paintingsArrayList;
        this.mcontext = mcontext;
        clf = mclf;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.row_featured_products, viewGroup, false);
        return new FeaturedViewHolder(view, clf);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder featuredViewHolder, int i)
    {
        final Paintings paintings = paintingsArrayList.get(i);
        featuredViewHolder.ownert1.setText(paintings.getpOwner());
        featuredViewHolder.namet2.setText(paintings.getName());
        featuredViewHolder.pricet3.setText(String.valueOf(paintings.getPrice()));
        featuredViewHolder.networkImageView.setImageUrl(paintings.getImage(), MySingleton.getInstance(mcontext).getImageLoader());
    }

    @Override
    public int getItemCount()
    {
        return paintingsArrayList.size();
    }

    public class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        NetworkImageView networkImageView;
        TextView ownert1, namet2, pricet3;
        ClickFeatured mclickFeatured;

        public FeaturedViewHolder(@NonNull View itemView, ClickFeatured clickFeatured)
        {
            super(itemView);

            networkImageView = itemView.findViewById(R.id.featuredImage);
            ownert1 = itemView.findViewById(R.id.featuredowner);
            namet2 = itemView.findViewById(R.id.featuredName);
            pricet3 = itemView.findViewById(R.id.featuredPrice);
            mclickFeatured = clickFeatured;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            mclickFeatured.onClickFeatured(getAdapterPosition());
        }
    }

    public interface ClickFeatured{
        void onClickFeatured(int pos);
    }
}
