package com.example.paintingsonline.Category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Model.Room;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import java.util.ArrayList;

public class RoomListAdapter extends ArrayAdapter<Room>
{
    private Context mcontext;
    private int mResource;

    private static class ViewHolder
    {
        TextView roomname;
        NetworkImageView roomimage;
    }

    /*Default Constructor For the CategoryListAdaptor*/
    public RoomListAdapter(Context context, int resource, ArrayList<Room> objects)
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

        String roomName = getItem(position).getRoomName();
        String roomImage = getItem(position).getImageURL();

        RoomListAdapter.ViewHolder holder;

        if (convertView == null)
        {

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.roomname =  convertView.findViewById(R.id.room_name);
            holder.roomimage = convertView.findViewById(R.id.room_pics);
            convertView.setTag(holder);

        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }


        holder.roomimage.setImageUrl(roomImage, MySingleton.getInstance(mcontext).getImageLoader());
        holder.roomname.setText(roomName);

        return convertView;
    }
}
