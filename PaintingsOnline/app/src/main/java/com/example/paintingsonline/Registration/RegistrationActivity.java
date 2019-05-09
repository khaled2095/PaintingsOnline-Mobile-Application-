package com.example.paintingsonline.Registration;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    public  String URL_REGISTER = "https://jrnan.info/Painting/Register.php";

    private EditText username, address, email, password, conpass, fullname;
    private Button signUp,  user, artist;
    private TextView signIn;
    int u = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        fullname = findViewById(R.id.regfname);
        username = findViewById(R.id.reguname);
        address = findViewById(R.id.regaddress);
        email = findViewById(R.id.regemail);
        password = findViewById(R.id.regpass);
        conpass = findViewById(R.id.regconpass);

        user = findViewById(R.id.user);
        artist = findViewById(R.id.artist1);



        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                buttonPressed(view);
                u = 0;
            }
        });

        artist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                buttonPressed(view);
                u = 1;
            }
        });



        signUp = findViewById(R.id.btn_signup);
        signIn = findViewById(R.id.tvSignIn);

        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);

    }


    public boolean buttonPressed(View v)
    {
        switch(v.getId()) {
            case R.id.user:
                user.setTextColor(Color.parseColor("White"));
                artist.setTextColor(Color.parseColor("Black"));
                break;
            case R.id.artist1:
                user.setTextColor(Color.parseColor("Black"));
                artist.setTextColor(Color.parseColor("White"));
                break;
        }

        return false;
    }



    private void registerUser()
    {

        URL_REGISTER = "https://jrnan.info/Painting/Register.php";


        String fname = fullname.getText().toString().trim();
        String uname = username.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String em = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String conp = conpass.getText().toString().trim();


        URL_REGISTER += "?Username=" + uname;
        URL_REGISTER += "&Password=" + pass;
        URL_REGISTER += "&Email=" + em;
        URL_REGISTER += "&Name=" + fname;
        URL_REGISTER += "&Usertype=" + u;
        URL_REGISTER += "&Address=" + addr;




        StringRequest stringRequest = new StringRequest(URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("username");
                    if (res.equals("Null")){
                        Toast.makeText(RegistrationActivity.this, "The username or email already exists", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(RegistrationActivity.this, res + " Registered Successfully", Toast.LENGTH_SHORT).show();
                        Log.d("url", "url: " + URL_REGISTER);
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
                        Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

        };

        MySingleton.getInstance(RegistrationActivity.this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onClick(View view)
    {
        if (view == signUp)
        {
            registerUser();
        }

        if (view == signIn)
        {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        }


    }

}
