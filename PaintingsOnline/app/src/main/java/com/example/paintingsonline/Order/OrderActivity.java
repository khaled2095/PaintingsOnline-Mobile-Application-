package com.example.paintingsonline.Order;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.Model.Order;
import com.example.paintingsonline.Model.UserRating;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity implements RatingDialogListener
{

    private List<Order> orderList1;
    private String URL = "";
    private String url_order = "/Orders.php";
    private String rating_URL = "/PostReview.php";
    private OrderAdapterView orderAdapterView;
    public int positionSetting ;
    //private RatingDialogListener ratingDialogListener;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(OrderActivity.this);
        URL = sp2.getString("mainurl", "");
        url_order = URL + url_order;
        rating_URL = URL + rating_URL;

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
            url_order += "?Username=" + SharedPrefManager.getInstance(this).getUserName();


            JsonArrayRequest request = new JsonArrayRequest(url_order, new Response.Listener<JSONArray>() {
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
                            String Rating = jsonObject.getString("Rating");
                            int OrderPrice = jsonObject.getInt("Price");

                            Order orders = new Order(OrderId, OrderStatus, OrderPrice ,OrderImage, POwner, Rating);
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
            Log.d("o", "orderurl" + url_order);
            MySingleton.getInstance(OrderActivity.this).addToRequestQueue(request);

        }


    }



    private void initrecyclerView(List<Order> orderList)
    {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        orderAdapterView = new OrderAdapterView(this, orderList , new OrderAdapterView.OnRatingListener() {
            @Override
            public void ratingSystem(List<UserRating> userRatings, int position) {
                new AppRatingDialog.Builder()
                        .setPositiveButtonText("Submit")
                        .setNegativeButtonText("Cancel")
                        .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excellent"))
                        .setDefaultRating(1)
                        .setTitle("Rate Our Service")
                        .setDescription("Please select some stars and give your feedback")
                        .setTitleTextColor(R.color.colorPrimary)
                        .setDescriptionTextColor(R.color.colorPrimary)
                        .setHint("Please Write your comment here....")
                        .setHintTextColor(R.color.White)
                        .setCommentTextColor(R.color.white)
                        .setCommentBackgroundColor(R.color.colorPrimaryDark)
                        .setWindowAnimation(R.style.RatingDialogFadeAnim)
                        .create(OrderActivity.this)
                        .show();
                positionSetting = position;
            }
        });
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


    @Override
    public void onNegativeButtonClicked()
    {

    }

    @Override
    public void onPositiveButtonClicked(final int ratingValue, @NotNull final String Comments)
    {
        StringRequest sendrating = new StringRequest(Request.Method.POST, rating_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("purchase_id", String.valueOf(orderList1.get(positionSetting).getOrderId()));
                params.put("Rating", String.valueOf(ratingValue));
                params.put("Comment", Comments);
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(sendrating);

        Toast.makeText(this, "Thank you for Your Rating", Toast.LENGTH_SHORT).show();
    }
}
