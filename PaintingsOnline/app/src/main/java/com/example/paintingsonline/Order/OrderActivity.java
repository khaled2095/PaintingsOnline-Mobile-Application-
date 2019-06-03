package com.example.paintingsonline.Order;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.Model.Order;
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

public class OrderActivity extends AppCompatActivity {

    private List<Order> orderList1;
    private String URL = "https://jrnan.info/Painting/Orders.php";
    private OrderAdapterView orderAdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();


        orderList1 = new ArrayList<>();


        JSONrequest();
        setupBottomnavView();
    }


    private void JSONrequest()
    {

        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(OrderActivity.this);
            builder1.setMessage("You are not an Logged in. Click ok to Log In and view your orders");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                            startActivity(new Intent(OrderActivity.this, LoginActivity.class));
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        else
        {
            URL += "?Username=" + SharedPrefManager.getInstance(this).getUserName();


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

                            int OrderId = jsonObject.getInt("purchase_id");
                            String OrderStatus = jsonObject.getString("status");
                            String OrderImage = jsonObject.getString("PaintingUrl");
                            String POwner = jsonObject.getString("Artist");
                            int OrderPrice = jsonObject.getInt("Price");

                            Order orders = new Order(OrderId, OrderStatus, OrderPrice ,OrderImage, POwner);
                            orderList1.add(orders);

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    initrecyclerView(orderList1);


                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                }
            });

            MySingleton.getInstance(OrderActivity.this).addToRequestQueue(request);

        }


    }


    private void initrecyclerView(List<Order> orderList)
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        orderAdapterView = new OrderAdapterView(this, orderList);
        recyclerView.setAdapter(orderAdapterView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(OrderActivity.this, this , bottomNavigationView);
        BottomNavViewHelper.setupBottomNavView(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

    }

}
