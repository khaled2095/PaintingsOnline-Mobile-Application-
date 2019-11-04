package com.example.paintingsonline.Home;

import android.content.Intent;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Category.CartActivity;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nex3z.notificationbadge.NotificationBadge;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{


    private HomePaintingsAdapter homeAdapterView;
    private List<Paintings> paintingsList;
    private final String URL = "https://jrnan.info/Painting/ShowPaintings.php";
    NotificationBadge n2;
    public static CartDatabase cartd;
    public static CartRepository cr;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        cartd = CartDatabase.getInstance(this);
        cr = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));

        mSwipeRefreshLayout = findViewById(R.id.swiperecycler);

        paintingsList = new ArrayList<>();



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
                JSONrequest();
            }
        });


        updateCart();
        setupBottomnavView();
        setupToolbar();
    }





    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(HomeActivity.this, this ,bottomNavigationView);
        BottomNavViewHelper.setupBottomNavView(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }

    private void setupToolbar()
    {
        Toolbar t1 = findViewById(R.id.paintingtoolbarhome);
        setSupportActionBar(t1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.s);

        View cartview = menu.findItem(R.id.c).getActionView();

        n2 = cartview.findViewById(R.id.badge2);

        cartview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                homeAdapterView.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }



    public void updateCart()
    {
        if (n2 == null)
        {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cr.CountCartItems() == 0) {
                    n2.setVisibility(View.GONE);
                } else {
                    n2.setVisibility(View.VISIBLE);
                    n2.setText(String.valueOf(cr.CountCartItems()));
                }
            }
        });


    }

    private void JSONrequest()
    {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                paintingsList.clear();
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        int id = jsonObject.getInt("painting_id");
                        String title = jsonObject.getString("painting_name");
                        String image = jsonObject.getString("painting_url");
                        String desc = jsonObject.getString("painting_description");
                        String owner = jsonObject.getString("painting_artist");
                        String paintingSize = jsonObject.getString("Size");
                        int quantity = jsonObject.getInt("Quantity");
                        int price = jsonObject.getInt("painting_price");

                        Paintings paintings = new Paintings(id, title, desc, image, price, quantity, owner, paintingSize);
                        paintingsList.add(paintings);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                initrecyclerView(paintingsList);

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

        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(request);
    }


    private void initrecyclerView(List<Paintings> pl)
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        homeAdapterView= new HomePaintingsAdapter(this, pl, this);
        recyclerView.setAdapter(homeAdapterView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
    }

    @Override
    public void onRefresh()
    {
        JSONrequest();
    }
}
