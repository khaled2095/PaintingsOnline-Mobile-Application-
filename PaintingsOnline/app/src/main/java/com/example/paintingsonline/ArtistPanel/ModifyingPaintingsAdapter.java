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

public class ModifyingPaintingsAdapter extends RecyclerView.Adapter<ModifyingPaintingsAdapter.ModifyViewHolder>
{
    private List<Paintings> modifypaintingsArrayList;
    private Context mcontext;
    private OnModifyListener onModifyListener;


    public ModifyingPaintingsAdapter(List<Paintings> modifypaintingsArrayList, Context mcontext, OnModifyListener monModifyListener)
    {
        this.modifypaintingsArrayList = modifypaintingsArrayList;
        this.mcontext = mcontext;
        this.onModifyListener = monModifyListener;
    }

    @NonNull
    @Override
    public ModifyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.row_modifying_paintings, viewGroup, false);
        return new ModifyViewHolder(view, onModifyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ModifyViewHolder modifyViewHolder, int i)
    {
        final Paintings paintings = modifypaintingsArrayList.get(i);
        modifyViewHolder.mownert1.setText(paintings.getpOwner());
        modifyViewHolder.mnamet2.setText(paintings.getName());
        modifyViewHolder.mpricet3.setText(String.valueOf(paintings.getPrice()));
        modifyViewHolder.mnetworkImageView.setImageUrl(paintings.getImage(), MySingleton.getInstance(mcontext).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return modifypaintingsArrayList.size();
    }

    public class ModifyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        NetworkImageView mnetworkImageView;
        TextView mownert1, mnamet2, mpricet3;
        OnModifyListener onModifyListener;

        public ModifyViewHolder(@NonNull View itemView, OnModifyListener modifyListener)
        {
            super(itemView);

            mnetworkImageView = itemView.findViewById(R.id.modifyimgpaintings);
            mownert1 = itemView.findViewById(R.id.modifyowner);
            mnamet2 = itemView.findViewById(R.id.modifypainting_Name);
            mpricet3 = itemView.findViewById(R.id.modifypainting_Price);
            onModifyListener = modifyListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            onModifyListener.OnmodifyClick(getAdapterPosition());
        }
    }

    public interface OnModifyListener
    {
        void OnmodifyClick(int pos);
    }
}
