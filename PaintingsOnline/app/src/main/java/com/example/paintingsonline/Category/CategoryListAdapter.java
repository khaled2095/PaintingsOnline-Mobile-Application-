package com.example.paintingsonline.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paintingsonline.Model.Category;
import com.example.paintingsonline.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

public class CategoryListAdapter extends ArrayAdapter<Category>
{
    private Context mcontext;
    private int mResource;

    private static class ViewHolder
    {
        TextView catname;
        ImageView catimage;
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
        setupImageLoader();

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

        ImageLoader imageLoader = ImageLoader.getInstance();

        int defaultImage = mcontext.getResources().getIdentifier("@drawable/image_failed", null, mcontext.getPackageName());

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(defaultImage)
                .showImageOnFail(defaultImage)
                .showImageOnLoading(defaultImage).build();

        imageLoader.displayImage(categoryImage, holder.catimage, options);

        holder.catname.setText(categoryName);

        return convertView;
    }

    private void setupImageLoader()
    {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mcontext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

}
