package com.example.paintingsonline.ArtistPanel;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Model.Order;
import com.example.paintingsonline.Model.SellingPanelChart;
import com.example.paintingsonline.NotificationSystem;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.paintingsonline.NotificationSystem.CHANNEL_1_ID;

public class SellingPanel extends AppCompatActivity implements MarkStatusAdapter.OnModifyMarkListener {


    private String URL = "";
    private String shippingURL = "/SetAsShipped.php?Username=";
    private String getOrderURL = "/MyOrders.php?Username=" + SharedPrefManager.getInstance(this).getUserName();
    private String url = "/Seller.php?Username=" + SharedPrefManager.getInstance(this).getUserName();
    private String BestArtistUrl = "/Seller.php?BestSeller=";

    ArrayList<SellingPanelChart> sp;
    BarChart bc;
    List<BarEntry> barEntries = new ArrayList<>();
    List<Order> markPaintings;
    MarkStatusAdapter markStatusAdapter;
    int totalSales = 0;
    double totalRevenue = 0;
    TextView Sales, Revenue, ArtistName, ArtistSales;
    Button test;

    //Capital Y-axis(Sum) and X-axis(Month)

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_panel);


        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String MacAddress = info.getMacAddress();


        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(SellingPanel.this);
        URL = sp2.getString("mainurl", "");
        url = URL + url;
        BestArtistUrl = URL + BestArtistUrl;


        getOrderURL = URL + getOrderURL + "&Mac=" + MacAddress;


        markPaintings = new ArrayList<>();

        bc = findViewById(R.id.BarChart);
        Sales = findViewById(R.id.tSales);
        Revenue = findViewById(R.id.tRevenue);
        ArtistName = findViewById(R.id.tbestArtistName);
        ArtistSales = findViewById(R.id.AmountEarned);


        sp = new ArrayList<>();

        JSONBarValuesRequest();
        getOrderJsonrequest();
        setupBottomnavView();
        JSONForBestArtist();

    }


    private void JSONBarValuesRequest()
    {
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {


                JSONObject jsonObject = null;
                int [] sums = new int[12];
                for (int i = 0 ; i < 12 ; i++) {
                    sums[i] = 0;
                }
                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String month = jsonObject.getString("Month");
                        int sum = Integer.parseInt(jsonObject.getString("Sum"));
                        sums[Integer.parseInt(month)] = sum;

                        totalSales += sum;
                        Sales.setText("Total Sales: " + totalSales);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                totalRevenue = totalSales * 0.8;
                Revenue.setText("Total Revenue: " + totalRevenue);

                barEntries.add(new BarEntry(1, sums[0]));
                barEntries.add(new BarEntry(2, sums[1]));
                barEntries.add(new BarEntry(3, sums[2]));
                barEntries.add(new BarEntry(4, sums[3]));
                barEntries.add(new BarEntry(5, sums[4]));
                barEntries.add(new BarEntry(6, sums[5]));
                barEntries.add(new BarEntry(7, sums[6]));
                barEntries.add(new BarEntry(8, sums[7]));
                barEntries.add(new BarEntry(9, sums[8]));
                barEntries.add(new BarEntry(10, sums[9]));
                barEntries.add(new BarEntry(11, sums[10]));
                barEntries.add(new BarEntry(12, sums[11]));


                BarDataSet barDataSet = new BarDataSet(barEntries, "Growth");
                barDataSet.setColor(getColor(R.color.blue4));

                BarData barData = new BarData(barDataSet);
                barData.setBarWidth(0.9f);

                bc.setVisibility(View.VISIBLE);
                bc.animateY(5000);
                bc.setData(barData);
                bc.setFitBars(true);

                Description description = new Description();
                description.setText("Growth Rate Per Month");
                bc.setDescription(description);
                bc.invalidate();


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        });

        Log.d("url", "ur: " + request);
        MySingleton.getInstance(SellingPanel.this).addToRequestQueue(request);
    }




    private void JSONForBestArtist()
    {
        JsonArrayRequest request = new JsonArrayRequest(BestArtistUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {


                JSONObject jsonObject = null;
                int [] sums = new int[12];
                for (int i = 0 ; i < 12 ; i++) {
                    sums[i] = 0;
                }
                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String Artist = jsonObject.getString("Artist");
                        int sum = Integer.parseInt(jsonObject.getString("Sum"));

                        ArtistName.setText("Achieved " + Artist);
                        ArtistSales.setText("Who Made $ " + sum);



                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

//                totalRevenue = totalSales * 0.8;
//                Revenue.setText("Total Revenue: " + totalRevenue);



            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
            }
        });

        Log.d("url", "ur: " + request);
        MySingleton.getInstance(SellingPanel.this).addToRequestQueue(request);
    }



    private void getOrderJsonrequest()
    {
        JsonArrayRequest markRequest = new JsonArrayRequest(getOrderURL, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;
                markPaintings.clear();
                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("paintings_id");
                        int orderid = jsonObject.getInt("purchase_id");
                        String title = jsonObject.getString("painting_name");
                        String status = jsonObject.getString("status");
                        int price = jsonObject.getInt("Price");
                        int qty = jsonObject.getInt("Quantity");
                        String PaymentStatus = jsonObject.getString("PaidArtist");

                        Order bestpaintings = new Order(id, title, orderid, status, price, qty, PaymentStatus);
                        markPaintings.add(bestpaintings);
                        Log.d("size", "size" + markPaintings.size());

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    Log.d("size", "size" + markPaintings.size());
                    initrecyclerView(markPaintings);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        Log.d("m", "m " + getOrderURL);
        MySingleton.getInstance(SellingPanel.this).addToRequestQueue(markRequest);
    }


    private void initrecyclerView(List<Order> marklists)
    {
        RecyclerView recyclerView = findViewById(R.id.deliveryrecycler);
        markStatusAdapter = new MarkStatusAdapter(marklists, this, this);
        recyclerView.setAdapter(markStatusAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(SellingPanel.this, this , bottomNavigationView);
        BottomNavViewHelper.setupBottomNavView(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

    }

    @Override
    public void OnmodifyClickMark(int pos)
    {

        String ShippingURL1 = URL + shippingURL;

        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String MacAddress2 = info.getMacAddress();

        int orderID = markPaintings.get(pos).getOrderId();

        ShippingURL1 += orderID;
        ShippingURL1 += "&Mac=" + MacAddress2;


        StringRequest stringRequest = new StringRequest(ShippingURL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(SellingPanel.this, "Status Updated", Toast.LENGTH_SHORT).show();
                getOrderJsonrequest();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(SellingPanel.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

        };

        Log.d("status", "stat" + ShippingURL1);
        MySingleton.getInstance(SellingPanel.this).addToRequestQueue(stringRequest);
//        int orderID = markPaintings.get(pos).getOrderId();
//        int paintingPrice = markPaintings.get(pos).getOrderPrice();
//        int qunatity = markPaintings.get(pos).getOrderQty();
//        String paintingStatus = markPaintings.get(pos).getOrderStatus();
    }


}
