package com.example.paintingsonline.Add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.paintingsonline.Model.MultipleSizePriceQuantity;
import com.example.paintingsonline.R;

import java.util.ArrayList;

public class multipleEntryListAdapter extends ArrayAdapter<MultipleSizePriceQuantity>
{
    private Context mcontext;
    private int mResource;
    OnEntryListener onEntryListener;


    private static class ViewHolder
    {
        TextView length1, width1, quantity1, price1;
        Button removebtn;

    }



    public multipleEntryListAdapter(Context context, int resource, ArrayList<MultipleSizePriceQuantity> objects)
    {
        super(context, resource, objects);
        this.mcontext = context;
        this.mResource = resource;
    }

    public multipleEntryListAdapter(Context context, int resource, ArrayList<MultipleSizePriceQuantity> objects, OnEntryListener monEntryListener)
    {
        super(context, resource, objects);
        this.mcontext = context;
        this.mResource = resource;
        this.onEntryListener = monEntryListener;
    }


    /*GettingView and Attach to ListView*/
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        // setupImageLoader();

        String length = getItem(position).getLength();
        String width = getItem(position).getWidth();
        String quantity = getItem(position).getQuantity();
        String price = getItem(position).getPrice();

        multipleEntryListAdapter.ViewHolder holder;

        if (convertView == null)
        {

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();

            holder.length1 =  convertView.findViewById(R.id.len2);
            holder.width1 = convertView.findViewById(R.id.wid2);
            holder.quantity1 = convertView.findViewById(R.id.quantity2);
            holder.price1 = convertView.findViewById(R.id.pri2);
            holder.removebtn = convertView.findViewById(R.id.removeEntries);

            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.length1.setText(length + new StringBuilder(" cm"));
        holder.width1.setText(width + new StringBuilder(" cm"));
        holder.quantity1.setText(quantity);
        holder.price1.setText(new StringBuilder("$ ").append(price));

        holder.removebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onEntryListener.OnClickRemove(position);
                notifyDataSetChanged();
            }
        });


        return convertView;
    }


    public interface OnEntryListener
    {
        void OnClickRemove(int pos);
    }


}
