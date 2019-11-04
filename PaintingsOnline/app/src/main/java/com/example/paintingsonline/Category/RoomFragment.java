package com.example.paintingsonline.Category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Model.Room;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RoomFragment extends Fragment
{

    final ArrayList<Room> rooms = new ArrayList<>();
    private String URL = "https://jrnan.info/Painting/ShowRoom.php";
    private ListView listView;
    private RoomListAdapter roomadapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room, container, false);

        JSONrequest(view);

        listView = view.findViewById(R.id.room_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("url","https://jrnan.info/Painting/ShowPaintings.php?Room=");
                editor.putInt("Selec", i);
                editor.apply();
                //sp.edit().commit();
                Intent paintingActivity = new Intent(getActivity(), PaintingActivity.class);
                startActivity(paintingActivity);
            }
        });

        return view;

    }

    private void JSONrequest(final View view)
    {
        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        int id = jsonObject.getInt("ID");
                        String title = jsonObject.getString("Name");
                        String image = jsonObject.getString("URL");

                        Room room = new Room(id, title, image);
                        rooms.add(room);
                        ListView listView = view.findViewById(R.id.room_list);
                        roomadapter = new RoomListAdapter(getActivity(), R.layout.room_listview, rooms);
                        listView.setAdapter(roomadapter);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
