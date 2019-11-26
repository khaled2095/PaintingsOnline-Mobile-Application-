package com.example.paintingsonline.Category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.paintingsonline.Home.HomeActivity;
import com.example.paintingsonline.R;
import com.nex3z.notificationbadge.NotificationBadge;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.*;

public class PaintingActivityTest
{

    private int resId = R.id.recycler_view_painting;

    private PaintingsAdapterView paintingsAdapterView;

    /** and it's item count */
    private int itemCount = 0;

    @Rule
    public ActivityTestRule<PaintingActivity> mpaintingAct = new ActivityTestRule<PaintingActivity>(PaintingActivity.class){

        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            Bundle extras = new Bundle();
            intent.putExtras(extras);
            return intent;
        }
    };


    private PaintingActivity pActivity = null;


    @Before
    public void setUp() throws Exception
    {
        pActivity = mpaintingAct.getActivity();

    }

    @Test
    public void viewItems()
    {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }



    @Test
    public void updatePaintingsActivityCart()
    {
        final String cartItems = "2";

        final NotificationBadge nb = pActivity.findViewById(R.id.badge);

        pActivity.runOnUiThread(new Runnable() {

            @Override
            public void run()
            {
                nb.setText(cartItems);
            }
        });

        assertNotNull(nb);

    }

    @Test
    public void updatePaintingsActivityFav()
    {
        final String FavItems = "5";

        final NotificationBadge nb = pActivity.findViewById(R.id.badge2);

        pActivity.runOnUiThread(new Runnable() {

            @Override
            public void run()
            {
                nb.setText(FavItems);
            }
        });

        assertNotNull(nb);

    }

    @After
    public void tearDown() throws Exception
    {
        pActivity = null;
    }

}