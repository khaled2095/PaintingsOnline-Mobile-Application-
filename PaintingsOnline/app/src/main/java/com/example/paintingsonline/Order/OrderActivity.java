package com.example.paintingsonline.Order;

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
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private List<Order> orderList1;
    private final String URL = "https://jrnan.info/Painting//Orders.php?Username=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        orderList1 = new ArrayList<>();
        JSONrequest();

        setupBottomnavView();
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

                        int OrderId = jsonObject.getInt("painting_id");
                        String OrderStatus = jsonObject.getString("painting_name");
                        String OrderImage = jsonObject.getString("painting_url");
                        int OrderPrice = jsonObject.getInt("painting_price");

                        Order orders = new Order(OrderId, OrderStatus, OrderPrice ,OrderImage);
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
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(OrderActivity.this).addToRequestQueue(request);
    }




    private void initrecyclerView(List<Order> orderList)
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        OrderAdapterView orderAdapterView = new OrderAdapterView(this, orderList);
        recyclerView.setAdapter(orderAdapterView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(OrderActivity.this, this , bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

    }

}
