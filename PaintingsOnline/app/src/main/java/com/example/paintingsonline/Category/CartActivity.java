package com.example.paintingsonline.Category;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Database.ModelDB.Cart;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {

    private String URL = "";
    private String payment_url = "/Payment/index.php";
    private String token_URL = "/Pay2/pay.php";
    private String showpaintings_url = "/ShowPaintings.php";
    private String size_URL = "https://jrnan.info/Painting/LoadSizes.php?painting_id=";
    private String image_URL = "https://jrnan.info/Painting/LoadPictures.php?painting_id=";
    private static final int PAYMENT_REQUEST_CODE = 4444;
    public static CartDatabase cartd;
    public static CartRepository cr;


    List<Cart> cartsList = new ArrayList<>();
    Button placeorder1;
    CompositeDisposable compositeDisposable;
    RecyclerView recyclerView;
    CartAdapterView cartAdapterView;
    String totalPrice, token = "sandbox_gpwbc8yj_m5jfgdsggqkp5yyw";
    TextView TotPrice, emptyView;
    String paintingid, paintingname, paintingprice;
    SharedPreferences spReceiveRoomAndCategory;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        SharedPreferences sp3 = PreferenceManager.getDefaultSharedPreferences(CartActivity.this);
        URL = sp3.getString("mainurl", "");
        payment_url = URL + payment_url;
        token_URL = URL + token_URL;
        showpaintings_url = URL + showpaintings_url;

        ImageView backarrow2 = findViewById(R.id.backarrow2);
        backarrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initDB();

        placeorder1 = findViewById(R.id.placeorder);
        TotPrice = findViewById(R.id.tPrice);

        progressBar = findViewById(R.id.progressBar2);
        emptyView = findViewById(R.id.empty_view);


        recyclerView = findViewById(R.id.recyclercart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

//        if (cr.CountCartItems() == 0)
//        {
//            recyclerView.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            recyclerView.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
//        }

        spReceiveRoomAndCategory = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        showpaintings_url = spReceiveRoomAndCategory.getString("url", "");
        showpaintings_url += spReceiveRoomAndCategory.getInt("Selec", 0);


        //sendOrderToServer();
        placeOrder();

    }


    private void placeOrder()
    {
        placeorder1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (cr.CountCartItems() == 0)
                {
                    Toast.makeText(CartActivity.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!SharedPrefManager.getInstance(CartActivity.this).isLoggedIn())
                    {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CartActivity.this);
                        builder1.setMessage("You are not an Logged in. Click ok to Log In");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                        startActivity(new Intent(CartActivity.this, LoginActivity.class));
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else
                    {
                        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
                        startActivityForResult(dropInRequest.getIntent(CartActivity.this), PAYMENT_REQUEST_CODE);
                    }
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                final String strnonce = nonce.getNonce();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, token_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CartActivity.this, "Transaction successful", Toast.LENGTH_SHORT).show();
                        Log.d("amount", "nounce: " + response);
                        sendPayment();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> AmountNounceparams = new HashMap<>();
                        AmountNounceparams.put("amount", totalPrice);
                        AmountNounceparams.put("payment_method_nonce", strnonce);
                        return AmountNounceparams;
                    }
                };

                MySingleton.getInstance(CartActivity.this).addToRequestQueue(stringRequest);

            } else {
                Toast.makeText(this, "Payment Amount is 0", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
        }
    }


    private void sendPayment()
    {
        compositeDisposable.add(cr.getCartItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        sendOrderToServer(carts);
                        cr.emptycart();
                    }
                }));
    }

    Integer CounterInt = 0;

    private void sendOrderToServer(final List<Cart> carts)
    {
        CounterInt = 0 ;
        //DropInRequest dropInRequest = new DropInRequest();
        for (final Cart c:carts)
        {
            showProgressBar();

        if (carts.size() > 0)
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, payment_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    CounterInt += 1 ;
                    if (CounterInt == carts.size())
                    {
                        hideProgressBar();
                        Toast.makeText(CartActivity.this, "Purchased Successfully", Toast.LENGTH_SHORT).show();
                        Log.d("try", "onr: " + response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String CartID = String.valueOf(Math.random());


                    Log.d("s", "painting_id" + String.valueOf(c.paintingid));
                    params.put("painting_id", String.valueOf(c.paintingid));

                    Log.d("s", "artist" + c.paintingartist);
                    params.put("artist", "by " + c.paintingartist);

                    Log.d("s", "buyer" + SharedPrefManager.getInstance(CartActivity.this).getUserName());
                    params.put("buyer", SharedPrefManager.getInstance(CartActivity.this).getUserName());

                    Log.d("s", "Address" + SharedPrefManager.getInstance(CartActivity.this).getUserAddress());
                    params.put("Address", SharedPrefManager.getInstance(CartActivity.this).getUserAddress());

                    Log.d("s", "Price" + String.valueOf(c.price * c.qty));
                    params.put("Price", String.valueOf(c.price * c.qty));

                    Log.d("s", "PaintingUrl" + c.paintingimg);
                    params.put("PaintingUrl", c.paintingimg);

                    Log.d("s", "CartID" + CartID);
                    params.put("CartID", CartID);

                    Log.d("s", "TXN_ID" + CartID);
                    params.put("TXN_ID", CartID);

                    Log.d("s", "Quantity" + String.valueOf(c.qty));
                    params.put("Quantity", String.valueOf(c.qty));

                    Log.d("s", "painting_name" + c.paintingname);
                    params.put("painting_name", c.paintingname);

                    params.put("Size", c.paintingsize);


                    return params;
                }
            };

            Log.d("body", "body" + stringRequest);

            MySingleton.getInstance(CartActivity.this).addToRequestQueue(stringRequest);
            }
        }
    }


//    private void sendOrderToServer()
//    {
//        placeorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (!SharedPrefManager.getInstance(CartActivity.this).isLoggedIn())
//                {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(CartActivity.this);
//                    builder1.setMessage("You are not an Logged in. Click ok to Log In");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    finish();
//                                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                }
//                else
//                {
//                    if (cartsList.size() > 0)
//                    {
//                        // placeOrder();
//                        String paintingIDs = "";
//                        String paintingQunatity = "";
//
//                        for (Cart cart : cartsList)
//                        {
//                            if (paintingIDs.isEmpty())
//                            {
//                                paintingIDs = String.valueOf(cart.paintingid);
//                            }
//                            else
//                            {
//                                paintingIDs += "," + cart.paintingid;
//                            }
//
//                            if (paintingQunatity.isEmpty())
//                            {
//                                paintingQunatity = String.valueOf(cart.qty);
//                            }
//                            else
//                            {
//                                paintingQunatity += "," + cart.qty;
//                            }
//
//                            Log.d("hhhhhhhh", "pid: " + cart.paintingid);
//                            Log.d("hhhhhhhh", "pid: " + cart.qty);
//                        }
//
//                        String requestURL = URL +
//                                "Username=" +  SharedPrefManager.getInstance(CartActivity.this).getUserName() +
//                                "&Password=" + SharedPrefManager.getInstance(CartActivity.this).password() +
//                                "&Paintings=" + paintingIDs + "&Quantity=" + paintingQunatity;
//                        //Log.d("url", "finalURL = " + requestURL);
//                        cr.emptycart();
//
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse(requestURL));
//                        startActivity(i);
//
//                    }
//                    else
//                    {
//                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CartActivity.this);
//                        builder1.setMessage("No Items in the cart to be checked out");
//                        builder1.setCancelable(true);
//
//                        builder1.setPositiveButton(
//                                "OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                        finish();
//                                        startActivity(new Intent(CartActivity.this, LoginActivity.class));
//                                    }
//                                });
//
//                        AlertDialog alert11 = builder1.create();
//                        alert11.show();
//                    }
//
//                }
//
//
//            }
//        });
//
//
//
//
////        if (cartsList.size() > 0)
////        {
////            mService.placeOrder("new", 1).enqueue(new Callback<String>() {
////                @Override
////                public void onResponse(Call<String> call, Response<String> response) {
////                    Toast.makeText(CartActivity.this, "Order Submitted", Toast.LENGTH_SHORT).show();
////
////                    //clear Checkout
////                    cr.emptycart();
////                }
////
////                @Override
////                public void onFailure(Call<String> call, Throwable t) {
////                    Log.e("ERROR", t.getMessage());
////                }
////            });
////        }
////
//    }


    private void initDB()
    {
        cartd = CartDatabase.getInstance(this);
        cr = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));

    }


    public void loadCartItems()
    {
        //int total = 0;
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                cr.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>()
                        {
                            @Override
                            public void accept(List<Cart> carts) throws Exception
                            {
                                totalPrice = String.valueOf(cr.sumPrice());
                                TotPrice.setText("Total: " + totalPrice);
                                displayCartItems(carts);
                            }
                        })
        );

    }


    private void displayCartItems(final List<Cart> carts)
    {
        //Checking if painting Names are the same before checking out.
        JsonArrayRequest request = new JsonArrayRequest(showpaintings_url, new Response.Listener<JSONArray>() {
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
                        Log.d("pname",  "pname" + id);

                        String title = jsonObject.getString("painting_name");
                        Log.d("pname", "pname " + title);

                        paintingid = id;
                        paintingname = title;


                        for (Cart c: carts)
                        {
                            if (c.paintingid.equals(paintingid))
                            {
                                if (!c.paintingname.equals(paintingname))
                                {
                                    cr.updatePaintingName(paintingname,paintingid);

                                }
                            }
                        }

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
            public void onErrorResponse(VolleyError error)
            {
            }
        });

        Log.d("j", "JSONRequestNameAndId: " + showpaintings_url);
        MySingleton.getInstance(CartActivity.this).addToRequestQueue(request);

        cartsList = carts;
        cartAdapterView =  new CartAdapterView(this, carts, new CartAdapterView.OnCartListener() {
            @Override
            public void OnDeleteClick(final int pos)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CartActivity.this);
                builder1.setMessage("Are you sure, you want to remove this Painting?.");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CartDatabase cartd = CartDatabase.getInstance(getApplicationContext());
                                CartRepository cartRepository = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));
                                cartRepository.deleteCartItem(cartsList.get(pos));
                                dialog.cancel();

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        recyclerView.setAdapter(cartAdapterView);
        cartAdapterView.notifyDataSetChanged();


        if (cartsList.size() == 0)
        {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onDestroy()
    {
        compositeDisposable.clear();
        super.onDestroy();

    }

    @Override
    protected void onStop()
    {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadCartItems();
    }

}
