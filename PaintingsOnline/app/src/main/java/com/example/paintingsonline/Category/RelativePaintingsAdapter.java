package com.example.paintingsonline.Category;

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

public class RelativePaintingsAdapter extends RecyclerView.Adapter<RelativePaintingsAdapter.RelativeViewHolder>
{
    private List<Paintings> relativePaintingList;
    private Context mcontext;
    private ClickRelative clr;


    public RelativePaintingsAdapter(List<Paintings> paintingsArrayList, Context mcontext, ClickRelative mclr)
    {
        this.relativePaintingList = paintingsArrayList;
        this.mcontext = mcontext;
        clr = mclr;
    }

    @NonNull
    @Override
    public RelativeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.row_relativepaintings, viewGroup, false);
        return new RelativeViewHolder(view, clr);
    }

    @Override
    public void onBindViewHolder(@NonNull RelativeViewHolder featuredViewHolder, int i)
    {
        final Paintings paintings = relativePaintingList.get(i);
        //featuredViewHolder.ownert1.setText(paintings.getpOwner());
        featuredViewHolder.namet2.setText(paintings.getName());
        featuredViewHolder.pricet3.setText(String.valueOf(paintings.getPrice()));
        featuredViewHolder.networkImageView.setImageUrl(paintings.getImage(), MySingleton.getInstance(mcontext).getImageLoader());
    }

    @Override
    public int getItemCount()
    {
        return relativePaintingList.size();
    }

    public class RelativeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        NetworkImageView networkImageView;
        TextView ownert1, namet2, pricet3;
        ClickRelative mclickRelative;

        public RelativeViewHolder(@NonNull View itemView, ClickRelative clickRelative)
        {
            super(itemView);

            networkImageView = itemView.findViewById(R.id.relativeImage);
            ownert1 = itemView.findViewById(R.id.relativeowner);
            namet2 = itemView.findViewById(R.id.relativeName);
            pricet3 = itemView.findViewById(R.id.relativePrice);
            mclickRelative = clickRelative;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            mclickRelative.onClickRelative(getAdapterPosition());
        }
    }

    public interface ClickRelative{
        void onClickRelative(int pos);
    }
}
