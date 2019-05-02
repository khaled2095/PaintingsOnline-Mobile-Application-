package com.example.paintingsonline.Category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaintingActivity extends AppCompatActivity implements PaintingsAdapterView.OnPaintListener
{

    private List<Paintings> paintingsList2;
    private String URL = "https://jrnan.info/Painting/ShowPaintings.php?Category=";
    public static CartDatabase cartd;
    public static CartRepository cr;
    NotificationBadge nb;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painting);

        nb = findViewById(R.id.badge);

        /*setup backarrow for NAVIGATION */
        ImageView backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        setupToolbar();

        initDB();

        loadPaintings();

        paintingsList2 = new ArrayList<>();

        JSONrequest();

        updateCart();

    }

    public void updateCart()
    {
        if (nb == null)
        {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (cr.CountCartItems() == 0)
                {
                    nb.setVisibility(View.INVISIBLE);
                }
                else
                {
                    nb.setVisibility(View.VISIBLE);
                    nb.setText(String.valueOf(cr.CountCartItems()));
                }
            }
        });
    }


    private void initDB()
    {
        cartd = CartDatabase.getInstance(this);
        cr = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.cart, menu);
//        View view = menu.findItem(R.id.cartmenu).getActionView();
////        bg = view.findViewById(R.id.badge);
//        return true;
//    }

    /*load paintings by Category*/
    private void loadPaintings()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int Category = sp.getInt("Selected",0);
        URL += Category;
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
                        String desc = jsonObject.getString("painting_description");

                        Paintings paintings = new Paintings(id, title, desc, image, price);
                        paintingsList2.add(paintings);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                initrecyclerView(paintingsList2);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(PaintingActivity.this).addToRequestQueue(request);
    }



    private void initrecyclerView(List<Paintings> paintingsList2)
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_painting);
        PaintingsAdapterView pav = new PaintingsAdapterView(this, paintingsList2 , this);
        recyclerView.setAdapter(pav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

//    @Override
//    public void onPaintClick(int pos)
//    {
//        Intent intent = new Intent(this, PaintingDetail.class);
//          intent.putExtra("img", paintingsList2.get(pos).getImage());
//        intent.putExtra("paintname", paintingsList2.get(pos).getName());
//        intent.putExtra("paintprice", paintingsList2.get(pos).getPrice());
//        intent.putExtra("paintdesc", paintingsList2.get(pos).getDescription());
//        intent.putExtra("paint", paintingsList2.get(pos));
//        startActivity(intent);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
    }

}
