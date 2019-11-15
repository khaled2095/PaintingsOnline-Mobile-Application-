package com.example.paintingsonline.ArtistPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paintingsonline.Model.Order;
import com.example.paintingsonline.R;

import java.util.List;

public class MarkStatusAdapter extends RecyclerView.Adapter<MarkStatusAdapter.MarkViewHolder>
{
    private List<Order> statuspaintingsArrayList;
    private Context mcontext;
    private  OnModifyMarkListener omml;


    public MarkStatusAdapter(List<Order> statuspaintingsArrayList, Context mcontext, OnModifyMarkListener momml)
    {
        this.statuspaintingsArrayList = statuspaintingsArrayList;
        this.mcontext = mcontext;
        this.omml = momml;
    }

    @NonNull
    @Override
    public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        View view = layoutInflater.inflate(R.layout.row_mark, viewGroup, false);
        return new MarkViewHolder(view, omml);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder markViewHolder, int i)
    {
        final Order orderpaintings = statuspaintingsArrayList.get(i);
        markViewHolder.mpname.setText(orderpaintings.getPaintingName());
        markViewHolder.mstatus1.setText(orderpaintings.getOrderStatus());
        markViewHolder.mprice2.setText(String.valueOf(orderpaintings.getOrderPrice()));
        markViewHolder.mqty3.setText(String.valueOf(orderpaintings.getOrderQty()));
    }

    @Override
    public int getItemCount() {
        return statuspaintingsArrayList.size();
    }

    public class MarkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView mpname, mstatus1, mprice2, mqty3, markStatus;
        OnModifyMarkListener onModifyMarkListener;

        public MarkViewHolder(@NonNull View itemView, OnModifyMarkListener mmodifyMarkListener)
        {
            super(itemView);

            mpname = itemView.findViewById(R.id.showname);
            mstatus1 = itemView.findViewById(R.id.showstatus);
            mprice2 = itemView.findViewById(R.id.showprice2);
            mqty3 = itemView.findViewById(R.id.showqty);
            markStatus = itemView.findViewById(R.id.marktext);
            onModifyMarkListener = mmodifyMarkListener;

            markStatus.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            onModifyMarkListener.OnmodifyClickMark(getAdapterPosition());
        }
    }

    public interface OnModifyMarkListener
    {
        void OnmodifyClickMark(int pos);
    }
}
