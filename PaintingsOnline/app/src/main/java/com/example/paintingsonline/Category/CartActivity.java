package com.example.paintingsonline.Category;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paintingsonline.Database.DataSource.CartRepository;
import com.example.paintingsonline.Database.Local.CartDataSource;
import com.example.paintingsonline.Database.Local.CartDatabase;
import com.example.paintingsonline.Database.ModelDB.Cart;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {

    private String URL = "https://jrnan.info/Painting/checkout.php?";
    TextView totalPrice;
    public static CartDatabase cartd;
    public static CartRepository cr;
    List<Cart> cartsList = new ArrayList<>();
    Button placeorder;
    CompositeDisposable compositeDisposable;
    RecyclerView recyclerView;
    CartAdapterView cartAdapterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        ImageView backarrow2 = findViewById(R.id.backarrow2);
        backarrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initDB();

        placeorder = findViewById(R.id.placeorder);
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // placeOrder();
                String paintingIDs = "";
                String paintingQunatity = "";

                for (Cart cart : cartsList)
                {
                    if (paintingIDs.isEmpty())
                    {
                        paintingIDs = String.valueOf(cart.paintingid);
                    }
                    else
                    {
                        paintingIDs += "," + cart.paintingid;
                    }

                    if (paintingQunatity.isEmpty())
                    {
                        paintingQunatity = String.valueOf(cart.qty);
                    }
                    else
                    {
                        paintingQunatity += "," + cart.qty;
                    }

                    Log.d("hhhhhhhh", "pid: " + cart.paintingid);
                    Log.d("hhhhhhhh", "pid: " + cart.qty);
                }

                String requestURL = URL +
                        "Username=" +  SharedPrefManager.getInstance(CartActivity.this).getUserName() +
                        "&Password=" + SharedPrefManager.getInstance(CartActivity.this).password() +
                        "&Paintings=" + paintingIDs + "&Quantity=" + paintingQunatity;
                //Log.d("url", "finalURL = " + requestURL);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(requestURL));
                startActivity(i);

//                Log.d("car", "username: " + SharedPrefManager.getInstance(CartActivity.this).getUserName());
//                Log.d("car", "password: " + SharedPrefManager.getInstance(CartActivity.this).password());

            }
        });

        recyclerView = findViewById(R.id.recyclercart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        loadCartItems();

    }



    private void placeOrder()
    {
        //submit Order
        compositeDisposable.add(
                cr.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception
                            {
                                // sendOrderToServer(cr.sumPrice(), cartsList);
                            }
                        })
        );

    }

//    private void sendOrderToServer(int sumPrice, List<Cart> cartsList)
//    {
//        if (cartsList.size() > 0)
//        {
//            mService.placeOrder("new", 1).enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    Toast.makeText(CartActivity.this, "Order Submitted", Toast.LENGTH_SHORT).show();
//
//                    //clear Checkout
//                    cr.emptycart();
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    Log.e("ERROR", t.getMessage());
//                }
//            });
//        }
//
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
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                displayCartItems(carts);
                            }
                        })
        );

    }

    private void displayCartItems(List<Cart> carts)
    {
        cartsList = carts;
        cartAdapterView =  new CartAdapterView(this, carts, new CartAdapterView.OnCartListener() {
            @Override
            public void OnDeleteClick(int pos) {
                CartDatabase cartd = CartDatabase.getInstance(getApplicationContext());
                CartRepository cartRepository = CartRepository.getInstance(CartDataSource.getInstance(cartd.cartDAO()));
                cartRepository.deleteCartItem(cartsList.get(pos));
            }
        });
        recyclerView.setAdapter(cartAdapterView);
        cartAdapterView.notifyDataSetChanged();
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
