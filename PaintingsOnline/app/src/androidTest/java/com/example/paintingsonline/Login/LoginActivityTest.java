package com.example.paintingsonline.Login;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.example.paintingsonline.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class LoginActivityTest
{

    @Rule
    public ActivityTestRule<LoginActivity> mlogin = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private String uname = "admin";
    private String pass = "Mohammed1994";


    @Before
    public void setUp() throws Exception
    {

    }


    @Test
    public void testUserInput()
    {
        Espresso.onView(withId(R.id.loguname)).perform(typeText(uname));
        Espresso.onView(withId(R.id.logpass)).perform(typeText(pass), closeSoftKeyboard());
        Espresso.onView(withId(R.id.login)).perform(click());
    }


    @Test
    public void ButtonsClickTest()
    {
        Espresso.onView(withId(R.id.login)).perform(click());
    }





    @After
    public void tearDown() throws Exception
    {
    }
}