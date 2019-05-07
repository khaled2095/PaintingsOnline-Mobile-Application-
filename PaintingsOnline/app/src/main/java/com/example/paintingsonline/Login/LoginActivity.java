package com.example.paintingsonline.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Profile.ProfileActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Registration.RegistrationActivity;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    private static String URL_LOGIN = "https://jrnan.info/Painting/Login.php";

    EditText username, pass;
    Button login;
    TextView register;


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

        username = findViewById(R.id.loguname);
        pass = findViewById(R.id.logpass);
        login = findViewById(R.id.login);
        register = findViewById(R.id.regis);

        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }


    private void loginUser()
    {

        URL_LOGIN = "https://jrnan.info/Painting/Login.php";


        String e = username.getText().toString().trim();
        String p = pass.getText().toString().trim();


        URL_LOGIN += "?Username=" + e;
        URL_LOGIN += "&Password=" + p;


        StringRequest stringRequest = new StringRequest(URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() < 1){
                        Toast.makeText(LoginActivity.this, "Authontication failed" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        int id = jsonObject.getInt("id");
                        String uname = jsonObject.getString("username");
                        String email = jsonObject.getString("Email");
                        String address = jsonObject.getString("Address");
                        String fname = jsonObject.getString("name");
                        if (uname != null) {
                            Toast.makeText(LoginActivity.this, uname + " Logged in", Toast.LENGTH_SHORT).show();
//                            if (!jsonObject.getBoolean("error"))
//                            {
                            SharedPrefManager.getInstance(getApplicationContext()).userlogin(id, uname, pass.getText().toString() ,email, address, fname);
                            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                            finish();
//                            }
//                            else
//                            {
//                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                            }
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
                }){

        };

        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onClick(View view)
    {
        if (view == login)
        {
            loginUser();
        }

        if (view == register)
        {
            Intent intent2 = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent2);
        }

    }

}
