package com.example.paintingsonline.Category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.DefaultRetryPolicy;
import com.example.paintingsonline.Home.HomeActivity;
import com.example.paintingsonline.Search.SearchActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.ArtistPanel.ArtistBio;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.DataSource.FavoriteRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Database.Local.FavoriteDataSource;
import com.example.paintingsonline.Database.Local.FavoriteDatabase;
import com.example.paintingsonline.Database.ModelDB.Cart;
import com.example.paintingsonline.Database.ModelDB.Favourites;
import com.example.paintingsonline.Favourite.FavouritePainting;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.Model.UserRating;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.PaintingsImageViewPager;
import com.example.paintingsonline.Utils.SharedPrefManager;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaintingsDetails extends AppCompatActivity implements RelativePaintingsAdapter.ClickRelative
{
    private String Size_URL = "/LoadSizes.php?painting_id=";
    private String Image_URL = "/LoadPictures.php?painting_id=";
    private String Comment_URL = "/LoadComments.php?painting_id=";
    private String Relative_URL = "/RelatedProducts.php?Username=";
    private String ShowPainting = "/ShowPaintings.php?Search=";
    private String URL = "";
    ImageView i2;
    TextView descriptionText, nameText, paintOwner, priceText;
    Button cartbtn, favbtn;
    //ImageView favbtn;
    CollapsingToolbarLayout collapsingToolbarLayout;
    NotificationBadge nb, nbFav;
    Paintings paintings;
    Spinner multipleSizeSpinner;
    PaintingsImageViewPager paintingsImageViewPager;
    ViewPager viewImagesurl;
    TabLayout tabLayout;
    private List<Paintings> plist;
    public static CartDatabase cartd;
    public static CartRepository cr;
    public static FavoriteDatabase favd;
    public static FavoriteRepository fr;
    public String imgurl;
    ArrayList<String> sizeLists = new ArrayList<>();
    ArrayList<String> priceLists = new ArrayList<>();
    ArrayList<String> imageurlLists = new ArrayList<>();
    ArrayList<String> quantityLists = new ArrayList<>();
    private String paintingID, paintingQuantity, size;
    private ListView mlist;
    private RateCommentListAdapter rcla;
    ArrayList<UserRating> userRatings = new ArrayList<>();
    ArrayList<Paintings> relativePaintings;
    RelativePaintingsAdapter relativePaintingsAdapter;
    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintings_details);

        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(PaintingsDetails.this);
        URL = sp2.getString("mainurl", "");
        Size_URL = URL + Size_URL;
        Image_URL = URL + Image_URL;
        Comment_URL = URL + Comment_URL;
        Relative_URL = URL + Relative_URL;



        sp = PreferenceManager.getDefaultSharedPreferences(PaintingsDetails.this);
        paintingID = sp.getString("painting_id", "");

        Size_URL += paintingID;
        Image_URL += paintingID;
        Comment_URL += paintingID;


        //i2 = findViewById(R.id.painting_img);
        nameText = findViewById(R.id.pname2);
        multipleSizeSpinner = findViewById(R.id.painting_Size2);
        descriptionText = findViewById(R.id.paintDesc);
        priceText = findViewById(R.id.PaintingPrice);
        paintOwner = findViewById(R.id.powner2);

        viewImagesurl = findViewById(R.id.vpagerimage);

        tabLayout = findViewById(R.id.tab_layout2);

        collapsingToolbarLayout = findViewById(R.id.collapsing);

        mlist = findViewById(R.id.RateComments);

        favbtn = findViewById(R.id.favbtn2);
        cartbtn = findViewById(R.id.cartbtn2);

        cartd = CartDatabase.getInstance(this);
        cr = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));

        favd = FavoriteDatabase.getInstance(this);
        fr = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(favd.favoriteDAO()));

//        nb = findViewById(R.id.badge);
       // nbFav = findViewById(R.id.badge4);

        /*setup backarrow for NAVIGATION */
        ImageView backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        relativePaintings = new ArrayList<>();

        nameText.setText(sp.getString("painting_name", ""));
        descriptionText.setText(sp.getString("painting_desc", ""));
        paintOwner.setText(sp.getString("painting_artist", ""));



        relativePaintingsJsonrequest();
        JsonMultiplleImageRequest();
        JsonMultipleSizeRequest();
        ShowRateComments();
        AddToCart();
        AddToFavourite();
        setupBottomnavView();
        setupToolbar();




        paintOwner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(PaintingsDetails.this);
                SharedPreferences.Editor editor = sp1.edit();
                editor.putString("paintingArtist", paintOwner.getText().toString());
                editor.apply();
                startActivity(new Intent(PaintingsDetails.this, ArtistBio.class));
                finish();
            }
        });

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

    public void updateFav()
    {
        if (nbFav == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (fr.CountFavouriteItems() == 0) {
                    nbFav.setVisibility(View.INVISIBLE);
                } else {
                    nbFav.setVisibility(View.VISIBLE);
                    nbFav.setText(String.valueOf(fr.CountFavouriteItems()));
                }
            }
        });
    }


    private void ShowRateComments()
    {

        JsonArrayRequest pictureRquest = new JsonArrayRequest(Comment_URL, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);
                        String comment = jsonObject.getString("Comment");
                        String rate = jsonObject.getString("Rating");


                        UserRating ur = new UserRating(rate, comment);
                        userRatings.add(ur);


                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }


                rcla = new RateCommentListAdapter(getApplicationContext(), R.layout.ratecomment_listview, userRatings);
                mlist.setAdapter(rcla);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(pictureRquest);
    }



    public void JsonMultiplleImageRequest()
    {
        JsonArrayRequest pictureRquest = new JsonArrayRequest(Image_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);
                        imageurlLists.add(jsonObject.getString("painting_url"));
                        paintingsImageViewPager = new PaintingsImageViewPager(PaintingsDetails.this, imageurlLists);
                        viewImagesurl.setAdapter(paintingsImageViewPager);
                        tabLayout.setupWithViewPager(viewImagesurl);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(pictureRquest);
    }

    public void JsonMultipleSizeRequest()
    {
        JsonArrayRequest request = new JsonArrayRequest(Size_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);


                        sizeLists.add(jsonObject.getString("Size"));
                        priceLists.add(jsonObject.getString("Price"));
                        quantityLists.add(jsonObject.getString("Quantity"));
                        spinnerSizes();

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

        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void spinnerSizes()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeLists);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        multipleSizeSpinner.setAdapter(adapter);


        multipleSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                size = (String) multipleSizeSpinner.getSelectedItem();
                priceText.setText(priceLists.get(i));
                paintingQuantity = quantityLists.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    public void relativePaintingsJsonrequest()
    {

        Log.d("issue","issue" + Relative_URL + sp.getString("painting_img", ""));
        JsonArrayRequest RelativeRequestPaintings = new JsonArrayRequest(Relative_URL + sp.getString("painting_img", ""), new Response.Listener<JSONArray>()
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
                        int distance = jsonObject.getInt("Distance");
                        String id = jsonObject.getString("painting_id");
                        String image = jsonObject.getString("Small");

                        ShowRelativePaintingsFromID(id);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

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

                Toast.makeText(PaintingsDetails.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        RelativeRequestPaintings.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(PaintingsDetails.this).addToRequestQueue(RelativeRequestPaintings);
    }


    public void ShowRelativePaintingsFromID(String id)
    {
        JsonArrayRequest request = new JsonArrayRequest(URL + "/ShowPaintings.php?Search=" + id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
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

                        Paintings Relatedpaintings = new Paintings(id, title, desc, image, price, quantity, owner, paintingSize);
                        relativePaintings.add(Relatedpaintings);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                RelativePaintingRecyclerView(relativePaintings);


            }
        }, new Response.ErrorListener()
        {
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

                Toast.makeText(PaintingsDetails.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        MySingleton.getInstance(PaintingsDetails.this).addToRequestQueue(request);

    }



    public void AddToCart()
    {
//        if (getIntent().hasExtra("selected_painting"))
//        {
//            paintings = getIntent().getParcelableExtra("selected_painting");
//
//            Glide.with(this).load(paintings.getImage()).into(i2);
//            nameText.setText(paintings.getName());
//            //paintingSize.setText(paintings.getpSize());
//            JsonMultipleSizeRequest();
//            descriptionText.setText(paintings.getDescription());
//
//            if(paintings.getQuantity() == 1)
//            {
//                cartbtn.setText("OUT OF STOCK");
//                //paintingHolder.cartbtn.setTextColor(Color.parseColor("red"));
//                cartbtn.setEnabled(false);
//            }
//            else
//            {

                //Add to cart method
                cartbtn.setEnabled(true);
                //cartbtn.setText("$ " + String.valueOf(paintings.getPrice()));
                cartbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        //Add to SQLite Room Persistance
                        //Create New Checkout Item
                        try {

                            Cart cart = new Cart();
                            cart.paintingid = paintingID;
                            cart.paintingimg = imageurlLists.get(0);
                            cart.paintingname = nameText.getText().toString();
                            cart.paintingartist = paintOwner.getText().toString();
                            cart.paintingsize = size;
                            cart.stock = Integer.valueOf(paintingQuantity);
                            cart.price = Integer.parseInt(priceText.getText().toString());
                            cart.paintingsize = size;
                            cart.qty = 1;

                            CartDatabase cartd = CartDatabase.getInstance(getApplication());
                            CartRepository cartRepository = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));

                            //if there is no count of an identical painting
                            if (cartRepository.checkIfPaintingExists(paintingID).equals("0"))
                            {
                                cartRepository.insertToCart(cart);

                                Toast.makeText(getApplication(), "Saved Item to Checkout", Toast.LENGTH_SHORT).show();
                                updateCart();

                            } else {
                                Toast.makeText(getApplication(), "Item already in the cart", Toast.LENGTH_SHORT).show();
                            }

                            Log.d("cart", new Gson().toJson(cart));

                            // onPaintListener.onPaintClick();


                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getApplication(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            //}
    }


    public void AddToFavourite()
    {

//        if (getIntent().hasExtra("selected_painting"))
//        {
//            paintings = getIntent().getParcelableExtra("selected_painting");
//
//            Glide.with(this).load(paintings.getImage()).into(i2);
//            nameText.setText(paintings.getName());
//            //paintingSize.setText(paintings.getpSize());
//            //MultipleSizeSpinner();
//            descriptionText.setText(paintings.getDescription());


            //Favourite Method
            FavoriteDatabase favoriteDatabase = FavoriteDatabase.getInstance(getApplication());
            final FavoriteRepository favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(favoriteDatabase.favoriteDAO()));

            if (favoriteRepository.isFavorite(paintingID, SharedPrefManager.getInstance(this).getUserName()) == 1)
            {
                favbtn.setText("REMOVE FROM WISHLIST");
                //favbtn.setImageResource(R.drawable.ic_fav);
            }
            else
            {
                favbtn.setText("ADD TO WISHLIST");
                //favbtn.setImageResource(R.drawable.ic_fav_border);
            }


            favbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (!SharedPrefManager.getInstance(getApplication()).isLoggedIn())
                    {
                        Toast.makeText(getApplication(), "You Must Log In to make this Painting Favourite", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (favoriteRepository.isFavorite(paintingID, SharedPrefManager.getInstance(getApplication()).getUserName()) != 1)
                        {
                            AddorRemoveFavorite(true);
                            favbtn.setText("REMOVE FROM WISHLIST");
                            updateFav();
                            //favbtn.setImageResource(R.drawable.ic_fav);
                        }
                        else
                        {
                            AddorRemoveFavorite(false);
                            favbtn.setText("ADD TO WISHLIST");
                            updateFav();
                            //favbtn.setImageResource(R.drawable.ic_fav_border);
                        }

                    }

                }
            });

        //}
    }


    public void AddorRemoveFavorite(boolean b)
    {
        FavoriteDatabase favoriteDatabase = FavoriteDatabase.getInstance(getApplication());
        FavoriteRepository favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(favoriteDatabase.favoriteDAO()));

        Favourites favourites = new Favourites();
        favourites.userName = SharedPrefManager.getInstance(getApplication()).getUserName();
        favourites.paintID = paintingID;//String.valueOf(paintings.getId());
        favourites.paintname = nameText.getText().toString();//paintings.getName();
        favourites.paintingimg = imageurlLists.get(0);//paintings.getImage();
        favourites.paintprice = priceText.getText().toString();//String.valueOf(paintings.getPrice());
        favourites.paintingSize = size;

        if (b)
        {
            favoriteRepository.insertFavorite(favourites);
            Log.d("fav", "fav " + new Gson().toJson(favourites));
        }
        else
        {
            favoriteRepository.deleteFavoriteItem(favourites);
        }

    }


    private void setupToolbar()
    {
        Toolbar t1 = findViewById(R.id.paintingtoolbar);
        setSupportActionBar(t1);

//        ImageView CartPage = findViewById(R.id.cart);
//        ImageView FavouritePage = findViewById(R.id.fav);
//
//        CartPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PaintingsDetails.this, CartActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        FavouritePage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PaintingsDetails.this, FavouritePainting.class);
//                startActivity(intent);
//            }
//        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_cart_fav, menu);

        View cartview2 = menu.findItem(R.id.c1).getActionView();
        View favview = menu.findItem(R.id.f1).getActionView();

        nb = cartview2.findViewById(R.id.badge3);
        nbFav = favview.findViewById(R.id.badge4);

        updateCart();
        updateFav();


        cartview2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(PaintingsDetails.this, CartActivity.class));
            }
        });

        favview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(PaintingsDetails.this, FavouritePainting.class));
            }
        });


        return true;

    }




    private void RelativePaintingRecyclerView(List<Paintings> relativePaintings)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.relatedPaintingsView);
        relativePaintingsAdapter = new RelativePaintingsAdapter(relativePaintings, this, this);
        recyclerView.setAdapter(relativePaintingsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
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
        updateFav();
        AddToFavourite();
    }

    @Override
    public void onClickRelative(int pos)
    {
        Toast.makeText(this, "Relative Paintings", Toast.LENGTH_SHORT).show();
    }
}
