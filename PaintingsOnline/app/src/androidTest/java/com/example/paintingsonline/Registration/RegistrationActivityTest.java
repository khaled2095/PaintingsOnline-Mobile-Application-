package com.example.paintingsonline.Registration;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.paintingsonline.Home.HomeActivity;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class RegistrationActivityTest
{
    @Rule
    public ActivityTestRule<RegistrationActivity> mreg = new ActivityTestRule<RegistrationActivity>(RegistrationActivity.class);

    Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(LoginActivity.class.getName(), null, false);

    private RegistrationActivity regActivity = null;

    private String fullname = "admin";
    private String username = "admin1";
    private String address = "";
    private String email = "admin@gmail.com";
    private String pass = "A1235";
    private String conpass = "A123";

    @Before
    public void setUp() throws Exception
    {
        regActivity = mreg.getActivity();
    }


    @Test
    public void testUserInput()
    {
        onView(withId(R.id.regfname)).perform(typeText(fullname));
        onView(withId(R.id.reguname)).perform(typeText(username));
        onView(withId(R.id.regaddress)).perform(typeText(address));
        onView(withId(R.id.regemail)).perform(typeText(email));
    }


    @Test
    public void testUserPassword()
    {
        if (!pass.equals(conpass))
        {
            regActivity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(regActivity, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            onView(withId(R.id.regpass)).perform(typeText(pass), closeSoftKeyboard());
            onView(withId(R.id.regconpass)).perform(typeText(conpass), closeSoftKeyboard());
        }
    }


    @Test
    public void ButtonsClickTest()
    {
        if(email.equals(""))
        {
            onView(withId(R.id.btn_signup)).perform(click());
        }

    }


    @After
    public void tearDown() throws Exception
    {
        regActivity = null;
    }
}