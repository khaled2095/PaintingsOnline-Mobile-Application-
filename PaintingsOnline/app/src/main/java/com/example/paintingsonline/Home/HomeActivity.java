package com.example.paintingsonline.Home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.paintingsonline.Category.CartActivity;
import com.example.paintingsonline.Category.PaintingsDetails;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Model.DiscountBanner;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Search.SearchActivity;
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

import io.reactivex.disposables.CompositeDisposable;

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,
        HomePaintingsAdapter.OnClickPainting,
        FeaturedPaintingsAdapter.ClickFeatured,
        BestSellingPaintingsAdapter.ClickBestSelling
{


    private HomePaintingsAdapter homeAdapterView;
    private FeaturedPaintingsAdapter featuredPaintingsAdapter;
    private BestSellingPaintingsAdapter bestSellingPaintingsAdapter;
    private List<Paintings> paintingsList;
    private List<Paintings> featuredList;
    private List<Paintings> bestList;
    private String urldiscount = "/LoadDiscount.php";
    private int mResults;

    private String URL = "https://jrnan.info/Paintings";
    private final String featured_URL = "/ShowPaintings.php?Search=&Type=Featured";
    private final String best_URL = "/ShowPaintings.php?Search=&Type=BestSelling";
    NotificationBadge n2;
    public static CartDatabase cartd;
    public static CartRepository cr;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SliderLayout sliderLayout;
    List<DiscountBanner> banners;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (!isConnected(HomeActivity.this))
        {
            buildDialog(HomeActivity.this);
        }
        else
        {
            setContentView(R.layout.activity_home);
            getDiscountBanner();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("mainurl", URL);
            editor.putInt("discount", 0);
            editor.apply();

            URL = sp.getString("mainurl", "");






            // OneSignal Initialization
            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();


            cartd = CartDatabase.getInstance(this);
            cr = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));

            mSwipeRefreshLayout = findViewById(R.id.swiperecycler);
            sliderLayout = findViewById(R.id.slider);

            paintingsList = new ArrayList<>();
            featuredList = new ArrayList<>();
            bestList = new ArrayList<>();
            banners = new ArrayList<>();



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


            BestSellingJsonrequest();
            featuredJsonrequest();
            //updateCart();
            setupBottomnavView();
            setupToolbar();
        }

    }


    public boolean isConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
        {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnected()) || (wifi != null && wifi.isConnected()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public AlertDialog.Builder buildDialog(Context c)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You must have mobile data or wifi connection to use the application. Click OK to exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });

        AlertDialog alert11 = builder.create();
        alert11.show();
        return builder;
    }


    private void getDiscountBanner()
    {
        JsonArrayRequest DiscountRequest = new JsonArrayRequest(URL + urldiscount, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("ID");
                        String bannerURL = jsonObject.getString("Url");
                        String discount = jsonObject.getString("Discount");
                        String active = jsonObject.getString("Active");

                        if (active.equals("1"))
                        {
                            DiscountBanner db = new DiscountBanner(id, bannerURL, discount, Integer.parseInt(active));
                            banners.add(db);


                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putInt("discount", Integer.parseInt(discount));
                            editor.apply();

                            TextSliderView textSliderView = new TextSliderView(getApplication());
                            textSliderView.description(discount).image(bannerURL).setScaleType(BaseSliderView.ScaleType.Fit);
                            sliderLayout.addSlider(textSliderView);

                        }
                        else
                        {
                            sliderLayout.setVisibility(View.INVISIBLE);
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    BestSellingPaintingRecyclerView(bestList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("durl", "durl " + URL + urldiscount);
        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(DiscountRequest);
    }

//    private void displayDiscounts(List<DiscountBanner> discountBanners)
//    {
//        HashMap<String, String> bannermap = new HashMap<>();
//
//        for (DiscountBanner disb: discountBanners)
//        {
//            bannermap.put(disb.getDiscounturl(), disb.getDiscount());
//        }
//
//        for (String name: bannermap.keySet())
//        {
//            TextSliderView textSliderView = new TextSliderView(this);
//            textSliderView.description(name).image(bannermap.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);
//            sliderLayout.addSlider(textSliderView);
//        }
//    }


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


        View cartview = menu.findItem(R.id.c).getActionView();

        n2 = cartview.findViewById(R.id.badge3);
        updateCart();

        cartview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });


        return true;

//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s)
//            {
//                homeAdapterView.getFilter().filter(s);
//                return false;
//            }
//        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.s)
        {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void BestSellingJsonrequest()
    {
        JsonArrayRequest featuredRequest = new JsonArrayRequest(URL + best_URL, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("painting_id");
                        String title = jsonObject.getString("painting_name");
                        String image = jsonObject.getString("painting_url");
                        String owner = jsonObject.getString("painting_artist");
                        int price = jsonObject.getInt("painting_price");

                        Paintings bestpaintings = new Paintings(id, title, image, owner, price);
                        bestList.add(bestpaintings);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    BestSellingPaintingRecyclerView(bestList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(featuredRequest);
    }



    private void featuredJsonrequest()
    {
        JsonArrayRequest featuredRequest = new JsonArrayRequest(URL + featured_URL, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("painting_id");
                        String title = jsonObject.getString("painting_name");
                        String image = jsonObject.getString("painting_url");
                        String owner = jsonObject.getString("painting_artist");
                        int price = jsonObject.getInt("painting_price");

                        Paintings featuredpaintings = new Paintings(id, title, image, owner, price);
                        featuredList.add(featuredpaintings);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    FeaturedPaintingRecyclerView(featuredList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(HomeActivity.this).addToRequestQueue(featuredRequest);
    }


    private void JSONrequest()
    {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest request = new JsonArrayRequest(URL + "/ShowPaintings.php?Search=", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                paintingsList.clear();
                JSONObject jsonObject = null;

                Log.d("u", "url " + URL);
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
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void FeaturedPaintingRecyclerView(List<Paintings> featuredPaintings)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.horizontalList);
        featuredPaintingsAdapter = new FeaturedPaintingsAdapter(featuredPaintings, this, this);
        recyclerView.setAdapter(featuredPaintingsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    private void BestSellingPaintingRecyclerView(List<Paintings> bestPaintings)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.horizontalBestList);
        bestSellingPaintingsAdapter = new BestSellingPaintingsAdapter(bestPaintings, this, this);
        recyclerView.setAdapter(bestSellingPaintingsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
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

    @Override
    public void clickPainting(int pos)
    {
        SharedPreferences spHomeActivity = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        SharedPreferences.Editor editor = spHomeActivity.edit();
        editor.putString("painting_id", paintingsList.get(pos).getId());
        editor.putString("painting_name", paintingsList.get(pos).getName());
        editor.putString("painting_desc", paintingsList.get(pos).getDescription());
        editor.putString("painting_artist", paintingsList.get(pos).getpOwner());
        editor.apply();
        Intent homeActivity = new Intent(HomeActivity.this, PaintingsDetails.class);
        startActivity(homeActivity);

    }

    @Override
    public void onClickFeatured(int pos)
    {
        SharedPreferences spFeatured = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        SharedPreferences.Editor editor = spFeatured.edit();
        editor.putString("painting_id", featuredList.get(pos).getId());
        editor.putString("painting_name", featuredList.get(pos).getName());
        editor.putString("painting_desc", featuredList.get(pos).getDescription());
        editor.putString("painting_artist", featuredList.get(pos).getpOwner());
        editor.apply();
        Intent homeActivity = new Intent(HomeActivity.this, PaintingsDetails.class);
        startActivity(homeActivity);
    }

    @Override
    public void onClickBest(int pos)
    {
        SharedPreferences spBest = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        SharedPreferences.Editor editor = spBest.edit();
        editor.putString("painting_id", bestList.get(pos).getId());
        editor.putString("painting_name", bestList.get(pos).getName());
        editor.putString("painting_desc", bestList.get(pos).getDescription());
        editor.putString("painting_artist", bestList.get(pos).getpOwner());
        editor.apply();
        Intent homeActivity = new Intent(HomeActivity.this, PaintingsDetails.class);
        startActivity(homeActivity);
    }
}
