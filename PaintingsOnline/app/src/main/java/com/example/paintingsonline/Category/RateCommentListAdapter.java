package com.example.paintingsonline.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.paintingsonline.Model.UserRating;
import com.example.paintingsonline.R;

import java.util.ArrayList;

public class RateCommentListAdapter extends ArrayAdapter<UserRating>
{
    private Context mcontext;
    private int mResource;

    private static class ViewHolder
    {
        TextView showcomments;
        RatingBar showRating;
    }

    public RateCommentListAdapter(Context context, int resource, ArrayList<UserRating> objects)
    {
        super(context, resource, objects);
        this.mcontext = context;
        this.mResource = resource;
    }


    /*GettingView and Attach to ListView*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // setupImageLoader();

        String comment = getItem(position).getComment();
        String rate = getItem(position).getRateValue();

        RateCommentListAdapter.ViewHolder holder;

        if (convertView == null)
        {

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.showcomments =  convertView.findViewById(R.id.commentsfeedback);
            holder.showRating = convertView.findViewById(R.id.ratingBarfeedback);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }


        holder.showcomments.setText(comment);
        holder.showRating.setRating(Float.valueOf(rate));

        return convertView;
    }
}
