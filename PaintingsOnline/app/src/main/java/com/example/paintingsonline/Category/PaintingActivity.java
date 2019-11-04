package com.example.paintingsonline.Category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaintingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{

    private List<Paintings> paintingsList2;
    private String URL = "https://jrnan.info/Painting/ShowPaintings.php";
    public static CartDatabase cartd;
    public static CartRepository cr;
    private PaintingsAdapterView pav;
    NotificationBadge nb;
    SwipeRefreshLayout refreshPaintings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting);


        nb = findViewById(R.id.badge);
        cartd = CartDatabase.getInstance(this);
        cr = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));
        refreshPaintings = findViewById(R.id.swipePaintings);

        /*setup backarrow for NAVIGATION */
        ImageView backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setupToolbar();

        setupBottomnavView();


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        URL = sp.getString("url", "");
        URL += sp.getInt("Selec", 0);
        Log.d("url", URL);


        paintingsList2 = new ArrayList<>();

        refreshPaintings.setOnRefreshListener(this);
        refreshPaintings.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        refreshPaintings.post(new Runnable() {

            @Override
            public void run() {

                refreshPaintings.setRefreshing(true);

                // Fetching data from server
                JSONrequest();
            }
        });


        updateCart();

    }


    public void updateCart() {


        if (nb == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cr.CountCartItems() == 0) {
                    nb.setVisibility(View.INVISIBLE);
                } else {
                    nb.setVisibility(View.VISIBLE);
                    nb.setText(String.valueOf(cr.CountCartItems()));
                }
            }
        });
    }





    private void setupToolbar()
    {
        Toolbar t1 = findViewById(R.id.paintingtoolbar);
        setSupportActionBar(t1);

        ImageView CartPage = findViewById(R.id.cart);

        CartPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaintingActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }



    private void JSONrequest()
    {

        // Showing refresh animation before making http call
        refreshPaintings.setRefreshing(true);

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                paintingsList2.clear();
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        int id = jsonObject.getInt("painting_id");
                        String title = jsonObject.getString("painting_name");
                        String image = jsonObject.getString("painting_url");
                        String paintingOwner = jsonObject.getString("painting_artist");
                        String paintingSize = jsonObject.getString("Size");
                        int price = jsonObject.getInt("painting_price");
                        int quantity = jsonObject.getInt("Quantity");
                        String desc = jsonObject.getString("painting_description");

                        Paintings paintings = new Paintings(id, title, desc, image, price, quantity, paintingOwner, paintingSize);
                        paintingsList2.add(paintings);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                initrecyclerView(paintingsList2);

                // Stopping swipe refresh
                refreshPaintings.setRefreshing(false);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                // Stopping swipe refresh
                refreshPaintings.setRefreshing(false);
            }
        });

        MySingleton.getInstance(PaintingActivity.this).addToRequestQueue(request);
    }



    private void initrecyclerView(List<Paintings> paintingsList2)
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_painting);
        pav = new PaintingsAdapterView(getApplicationContext(), paintingsList2,this);
        recyclerView.setAdapter(pav);
        pav.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }




    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(getApplicationContext(), this , bottomNavigationView);
        BottomNavViewHelper.setupBottomNavView(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();

    }

    @Override
    public void onRefresh() {
        JSONrequest();
    }

}
