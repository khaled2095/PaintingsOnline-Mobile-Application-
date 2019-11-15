package com.example.paintingsonline.ArtistPanel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModifyPaintings extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ModifyingPaintingsAdapter.OnModifyListener
{
    private ModifyingPaintingsAdapter mpa;
    private List<Paintings> modifyPaintings;
    private String URL = "";
    private String modify_url = "/ShowMyPaintings.php?Artist=" + SharedPrefManager.getInstance(this).getUserName();
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_paintings);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ModifyPaintings.this);
        URL = sp.getString("mainurl", "");
        modify_url = URL + modify_url;

        modifyPaintings = new ArrayList<>();

        mSwipeRefreshLayout = findViewById(R.id.swipePaintings);

        /*setup backarrow for NAVIGATION */
        ImageView backarrow = findViewById(R.id.backarrow3);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                ModifyJSONrequest();
            }
        });


        setupBottomnavView();
    }



    private void ModifyJSONrequest()
    {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest request = new JsonArrayRequest(modify_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                modifyPaintings.clear();
                JSONObject jsonObject = null;

                Log.d("u", "url " + modifyPaintings);
                Log.d("u", "CountOFPaintings:" + response.length());
                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("painting_id");
                        String title = jsonObject.getString("painting_name");
                        String image = jsonObject.getString("painting_url");
                        String desc = jsonObject.getString("painting_description");
                        String owner = jsonObject.getString("painting_artist");
                        String paintingSize = jsonObject.getString("Size");
                        int quantity = jsonObject.getInt("Quantity");
                        int price = jsonObject.getInt("painting_price");

                        Paintings paintings = new Paintings(id, title, desc, image, price, quantity, owner, paintingSize);
                        modifyPaintings.add(paintings);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                initrecyclerView(modifyPaintings);

                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Stopping swipe refresh
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        Log.d("url", "ur: " + request);
        MySingleton.getInstance(ModifyPaintings.this).addToRequestQueue(request);
    }


    private void initrecyclerView(List<Paintings> pl)
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_modifypaintings);
        mpa = new ModifyingPaintingsAdapter(pl, this, this);
        recyclerView.setAdapter(mpa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(ModifyPaintings.this, this , bottomNavigationView);
        BottomNavViewHelper.setupBottomNavView(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

    }

    @Override
    public void onRefresh()
    {
        ModifyJSONrequest();
    }

    @Override
    public void OnmodifyClick(int pos)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ModifyPaintings.this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("painting_id", modifyPaintings.get(pos).getId());
        editor.putString("painting_name", modifyPaintings.get(pos).getName());
        editor.putString("painting_desc", modifyPaintings.get(pos).getDescription());
        editor.putString("painting_artist", modifyPaintings.get(pos).getpOwner());
//        editor.putString("painting_image", modifyPaintings.get(pos).getImage());
        editor.putString("painting_size", modifyPaintings.get(pos).getpSize());
//        editor.putInt("painting_quantity", modifyPaintings.get(pos).getQuantity());
//        editor.putInt("painting_price", modifyPaintings.get(pos).getPrice());
        editor.apply();
        Intent paintingActivity = new Intent(ModifyPaintings.this, EditDeletePaintings.class);
        startActivity(paintingActivity);
    }
}
