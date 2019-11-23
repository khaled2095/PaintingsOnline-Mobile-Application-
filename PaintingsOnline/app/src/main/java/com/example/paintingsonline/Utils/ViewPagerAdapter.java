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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.paintingsonline.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter
{

    private Context context;
    List<byte[]> img1;
    List<String> image;
    LayoutInflater layoutInflater;
    OnImageClickListener monImageClickListener;


    public ViewPagerAdapter(Context context, List<byte[]> images, List<String> oldimage ,OnImageClickListener onImageClickListener)
    {
        Log.d("ol", "ol" + oldimage.size());

            this.context = context;
            this.image = oldimage;
            this.img1 = images;
            this.monImageClickListener = onImageClickListener;
    }


    @Override
    public int getCount()
    {
        Log.d("img", "oldimg" + img1.size() + " " + image.size());
        if (image.size() != 0)
        {
            return image.size();
        }

        return img1.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position)
    {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.vp_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.p);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monImageClickListener.onGalleryClick(position, v);
            }
        });

        if (image.size() != 0)
        {
            Log.d("old", "old" + image.size());
            Glide.with(context).load(image.get(position)).into(imageView);

        }
        else
        {
            Log.d("old", "old" + img1.size());
            Glide.with(context).load(img1.get(position)).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(imageView);

        }

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


    public interface OnImageClickListener{
        void onGalleryClick(int position, View view);
    }

}
