package com.example.paintingsonline.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class ProfileActivity extends AppCompatActivity  {

    private EditText fname, address, email, password, conpassword;
    private Button updatepassword, updatedetails, logout;
    private String URL = "https://jrnan.info/Painting/UpdateAccount.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.logout);
        email = findViewById(R.id.editemail);
        address = findViewById(R.id.editaddr);
        fname = findViewById(R.id.editfullName);
        updatepassword = findViewById(R.id.updatepass);
        updatedetails = findViewById(R.id.updatedetails);
        password = findViewById(R.id.editpass);
        conpassword = findViewById(R.id.editconpass);

        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        setupToolbar();


        //final String uname = SharedPrefManager.getInstance(this).getUserName();
        fname.setText(SharedPrefManager.getInstance(this).getFullName());
        email.setText(SharedPrefManager.getInstance(this).getUserEmail());
        address.setText(SharedPrefManager.getInstance(this).getUserAddress());




        updatedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateDetails();
            }

        });


        updatepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePass();
            }
        });



        setupBottomnavView();
        logout();
    }


    public void updateDetails()
    {
        final String uname = SharedPrefManager.getInstance(this).getUserName();
        String updatefullname = fname.getText().toString().trim();
        String updatedadd = address.getText().toString().trim();
        String updateemail = email.getText().toString().trim();

        URL += "?Username=" + uname + "&Password=" + password.getText() + "&Email=" + updateemail + "&Address=" + updatedadd + "&Name=" + updatefullname;


        StringRequest stringRequest = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
//                        try
//                        {
//                            JSONArray jsonArray = new JSONArray(response);
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//                            String res = jsonObject.getString("address");
//                            if (res.equals("Null")){
//                                Toast.makeText(Profile.this, "address mot updated", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(Profile.this, res + " Updated Successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);

    }


    public void updatePass()
    {
        final String uname = SharedPrefManager.getInstance(this).getUserName();
        String newpass = password.getText().toString().trim();
        String conpass = conpassword.getText().toString().trim();

        if (!newpass.equals(conpass))
        {
            Toast.makeText(ProfileActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            URL += "?Username=" + uname + "&Password=" + SharedPrefManager.getInstance(this).password() + "&Email=" + SharedPrefManager.getInstance(this).getUserEmail() + "&Address=" + SharedPrefManager.getInstance(this).getUserAddress() + "&Name=" + SharedPrefManager.getInstance(this).getFullName() + "&NewPassword=" + newpass;

            StringRequest updatePassRequest = new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    Toast.makeText(ProfileActivity.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(updatePassRequest);
        }

    }


    public void logout()
    {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(ProfileActivity.this).logout();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }


    private void setupToolbar()
    {
        Toolbar t1 = findViewById(R.id.profiletoolbar);
        setSupportActionBar(t1);

    }

    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(ProfileActivity.this, this , bottomNavigationView);
        BottomNavViewHelper.setupBottomNavView(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

    }
}
