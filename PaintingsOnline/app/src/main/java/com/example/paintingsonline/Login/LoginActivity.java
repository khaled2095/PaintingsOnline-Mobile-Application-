package com.example.paintingsonline.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Home.HomeActivity;
import com.example.paintingsonline.Profile.ProfileActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Registration.RegistrationActivity;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String URL_LOGIN = "/Login.php";
    private String URL = "";

    EditText username, pass;
    Button login;
    TextView register, forgetpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }

        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        URL = sp2.getString("mainurl", "");
        URL_LOGIN = URL + URL_LOGIN;

        username = findViewById(R.id.loguname);
        pass = findViewById(R.id.logpass);
        login = findViewById(R.id.login);
        register = findViewById(R.id.signup);
        forgetpassword = findViewById(R.id.fpass);

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iforgetpass = new Intent(LoginActivity.this, forget_password.class);
                startActivity(iforgetpass);
                finish();
            }
        });

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }


    private void loginUser()
    {

        //URL_LOGIN = "https://jrnan.info/Painting/Login.php";


        final String e = username.getText().toString().trim();
        final String p = pass.getText().toString().trim();

        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        final String MacAddress = info.getMacAddress();

//        URL_LOGIN += "?Username=" + e;
//        URL_LOGIN += "&Password=" + p;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() < 1){
                        Toast.makeText(LoginActivity.this, "Wrong Password or Username" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        int id = jsonObject.getInt("id");
                        String uname = jsonObject.getString("username");
                        String email = jsonObject.getString("Email");
                        String address = jsonObject.getString("Address");
                        String fname = jsonObject.getString("name");
                        int userType = jsonObject.getInt("usertype");
                        int verifyUser = jsonObject.getInt("verified");

                        Log.d("v","v" + verifyUser);

                        if (uname != null) {
                            Toast.makeText(LoginActivity.this, uname + " Logged in", Toast.LENGTH_SHORT).show();
//                            if (!jsonObject.getBoolean("error"))
//                            {
                            SharedPrefManager.getInstance(getApplicationContext()).userlogin(id, uname, pass.getText().toString() ,email, address, fname, userType, verifyUser);
                            Log.d("test", "test " + response);
                            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            finish();

                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Username",e);
                params.put("Password",p);
                params.put("Mac",MacAddress);
                return params;
            }

        };

        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onClick(View v)
    {
        if (v == login)
        {
            loginUser();
        }

        if (v == register)
        {
            Intent intent2 = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent2);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }

}
