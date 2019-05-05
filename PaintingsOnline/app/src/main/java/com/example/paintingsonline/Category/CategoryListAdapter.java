package com.example.paintingsonline.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Model.Category;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Category>
{
    private Context mcontext;
    private int mResource;

    private static class ViewHolder
    {
        TextView catname;
        NetworkImageView catimage;
    }

    /*Default Constructor For the CategoryListAdaptor*/
    public CategoryListAdapter(Context context, int resource, ArrayList<Category> objects)
    {
        super(context, resource, objects);
        this.mcontext = context;
        this.mResource = resource;
    }


    /*GettingView and Attach to ListView*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        String categoryName = getItem(position).getCategoryName();
        String categoryImage = getItem(position).getImageURL();

        ViewHolder holder;

        if (convertView == null)
        {

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.catname =  convertView.findViewById(R.id.category_name);
            holder.catimage = convertView.findViewById(R.id.category_pics);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.catimage.setImageUrl(categoryImage, MySingleton.getInstance(mcontext).getImageLoader());
        holder.catname.setText(categoryName);

        return convertView;
    }


}
