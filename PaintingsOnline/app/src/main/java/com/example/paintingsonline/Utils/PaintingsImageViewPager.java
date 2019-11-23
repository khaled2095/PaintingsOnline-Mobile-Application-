package com.example.paintingsonline.Utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.paintingsonline.R;

import java.util.ArrayList;

public class PaintingsImageViewPager extends PagerAdapter
{
    private Context context;
    ArrayList<String> multipleImageUrl;
    LayoutInflater layoutInflater;

    public PaintingsImageViewPager(Context context, ArrayList<String> multipleImageUrl)
    {
        this.context = context;
        this.multipleImageUrl = multipleImageUrl;
    }

    @Override
    public int getCount()
    {

        Log.d("size", "size " + multipleImageUrl.size());
        return multipleImageUrl.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position)
    {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.vp_multiple_image_urls, container, false);

        ImageView imageView = itemView.findViewById(R.id.paintingURLs);

        Glide.with(context).load(multipleImageUrl.get(position)).into(imageView);


        ViewPager vp = (ViewPager) container;
        vp.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o)
    {
        return view == o;
    }


}
