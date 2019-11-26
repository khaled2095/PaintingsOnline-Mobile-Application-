package com.example.paintingsonline.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

public class forget_password extends AppCompatActivity {

    EditText enteremail;
    TextView resetpass, backtologin;
    private String url;
    private String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(forget_password.this);
        URL = sp2.getString("mainurl", "");
        url = URL + url;

        enteremail = findViewById(R.id.email1);
        resetpass = findViewById(R.id.resetpass1);
        backtologin = findViewById(R.id.backtologin1);


        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i1 = new Intent(forget_password.this, LoginActivity.class);
                startActivity(i1);
                finish();
            }
        });


        resetPassword();


    }

    private void resetPassword()
    {

        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!isEmpty(enteremail.getText().toString()))
                {
                    String email = enteremail.getText().toString().trim();

                    url = String.format("/recoverPassword.php?Email=" + email);

                    StringRequest passrequest = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            Toast.makeText(forget_password.this, "u " + url, Toast.LENGTH_SHORT).show();
                            Log.d("r", "r " + response);
                            Log.d("ur", "ur " + url);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(forget_password.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    MySingleton.getInstance(forget_password.this).addToRequestQueue(passrequest);
                }
                else
                {
                    Toast.makeText(forget_password.this, "You must enter your email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }


}
