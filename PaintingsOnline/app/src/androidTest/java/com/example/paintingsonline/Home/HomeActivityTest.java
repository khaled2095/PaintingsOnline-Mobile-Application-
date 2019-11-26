package com.example.paintingsonline.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.example.paintingsonline.R;
import com.nex3z.notificationbadge.NotificationBadge;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class HomeActivityTest
{


    @Rule
    public ActivityTestRule<HomeActivity> mhomeAct = new ActivityTestRule<HomeActivity>(HomeActivity.class);


    private HomeActivity homeActivity = null;


    @Before
    public void setUp() throws Exception
    {
        homeActivity = mhomeAct.getActivity();

    }


    @Test
    public void onCreate()
    {
        View view = homeActivity.findViewById(R.id.slider);
        assertNotNull(view);
    }

    @Test
    public void updateHomeCart()
    {
        String cartItems = "3";

        NotificationBadge nb = homeActivity.findViewById(R.id.badge3);
        nb.setText(cartItems);
        assertNotNull(nb);
    }


    @Test
    public void TestForRecyclerView()
    {

        onView(withId(R.id.horizontalBestList)).check(matches(not(isDisplayed())));
        onView(withId(R.id.horizontalList)).check(matches(not(isDisplayed())));
        onView(withId(R.id.recycler_view)).check(matches(not(isDisplayed())));
    }


    @After
    public void tearDown() throws Exception
    {
        homeActivity = null;
    }




}