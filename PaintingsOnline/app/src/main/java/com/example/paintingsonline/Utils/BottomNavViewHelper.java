package com.example.paintingsonline.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.paintingsonline.Add.AddActivity;
import com.example.paintingsonline.Category.CategoryActivity;
import com.example.paintingsonline.Home.HomeActivity;
import com.example.paintingsonline.Order.OrderActivity;
import com.example.paintingsonline.Profile.ProfileActivity;
import com.example.paintingsonline.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavViewHelper
{

    public static void setupBottomNavView(BottomNavigationViewEx bottomNavigationView)
    {

    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        Intent i1 = new Intent(context, HomeActivity.class); //0
                        context.startActivity(i1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.category:
                        Intent i2 = new Intent(context, CategoryActivity.class); //1
                        context.startActivity(i2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.add:
                        Intent i3 = new Intent(context, AddActivity.class); //2
                        context.startActivity(i3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.profile:
                        Intent i4 = new Intent(context, ProfileActivity.class); //3
                        context.startActivity(i4);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;

                    case R.id.order:
                        Intent i5 = new Intent(context, OrderActivity.class); //4
                        context.startActivity(i5);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }

                return false;
            }
        });

    }

}
