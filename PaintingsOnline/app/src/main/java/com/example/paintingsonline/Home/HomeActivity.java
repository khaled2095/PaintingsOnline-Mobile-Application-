package com.example.paintingsonline.Home;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    private List<Paintings> paintingsList;
    private final String URL = "https://jrnan.info/Painting/ShowPaintings.php";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        paintingsList = new ArrayList<>();
        JSONrequest();
        setupBottomnavView();

    }

    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(HomeActivity.this, this ,bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }


    private void JSONrequest()
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

                        int id = jsonObject.getInt("painting_id");
                        String title = jsonObject.getString("painting_name");
                        String image = jsonObject.getString("painting_url");
                        int price = jsonObject.getInt("painting_price");

                        Paintings paintings1 = new Paintings(id, title, image, price);
                        paintingsList.add(paintings1);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                initrecyclerView(paintingsList);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(request);
    }


    private void initrecyclerView(List<Paintings> pl)
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        HomePaintingsAdapter homePaintingsAdapter = new HomePaintingsAdapter(this, pl);
        recyclerView.setAdapter(homePaintingsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
