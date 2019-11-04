package com.example.paintingsonline.Registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
    private TextView signIn, UserAgreement;
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
        UserAgreement = findViewById(R.id.agreement);



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
        signIn = findViewById(R.id.signin);

        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);
        UserAgreement.setOnClickListener(this);


    }

    public void setUserAgreement()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        builder1.setTitle("User Agreement");
        builder1.setCancelable(true);

        builder1.setMessage("By accessing or using the Services and systems of Paintings Online (Including the UI interactions\n" +
                "\n" +
                "available in IOS and Android Operating systems) and their communications with the service\n" +
                "\n" +
                "hosting the service, you agree to the concluded terms and conditions of use:\n" +
                "\n" +
                "1- The logo, design and software used is in whole an ownership of its respected owners and the\n" +
                "\n" +
                "user will only be allowed to interact with the system without copying, distribution or manipulation of\n" +
                "\n" +
                "the source code and Company Identity.\n" +
                "\n" +
                "2- Paintings Online is solely and agent/Medium/connection between the Artists and their peers of\n" +
                "\n" +
                "buyers and does not hold any Legal liability (Civil/Criminal/Enforcement or notarial form), and the\n" +
                "\n" +
                "uploaders/sellers of painting should take the full responsibility in case of any copyright/fraud/theft.\n" +
                "\n" +
                "3- In addition to term number 2, Paintings Online has the uncontrolled power to Suspend, cancel,\n" +
                "\n" +
                "Delete, or unapprove any account regardless of its nature (User/Artist) to what it sees suitable for\n" +
                "\n" +
                "the best interest of the Artist community.\n" +
                "\n" +
                "4- Paintings Online, Is a free to use service and is provided as is with no usage warranties and can\n" +
                "\n" +
                "be discontinued at any given time without prior notice.\n" +
                "\n" +
                "5- In case of any Damage/Loss/miss-location or Extreme delay of goods and shipments, The\n" +
                "\n" +
                "insurance company agreed with will handle the expenses and compensations caused to the user.\n" +
                "\n" +
                "6- The User might not use any (Loophole, Glitch, Unintended feature, Flaw, miscommunication)\n" +
                "\n" +
                "found in the system to his own unlawful gain (This will be judged by commonsense and to what the\n" +
                "\n" +
                "law define as electrical fraud or by what marked by society as unlawful).\n" +
                "\n" +
                "7- Information collected through the app might be shared with third parties, Including Shipping\n" +
                "\n" +
                "companies and payment gateways but will not be shared with advertisement companies and any\n" +
                "\n" +
                "company that is expected to misuse the information.\n" +
                "\n" +
                "8- This app is developed and executed for educational purposes and the ownership of its content is\n" +
                "\n" +
                "preserved to its original developers..");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "AGREE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

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



    private void registerUser(View v)
    {

        URL_REGISTER = "https://jrnan.info/Painting/Register.php";


        String fname = fullname.getText().toString().trim();
        String uname = username.getText().toString().trim();
        String addr = address.getText().toString().trim();
        String em = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String conp = conpass.getText().toString().trim();


        if (!pass.equals(conp))
        {
            Toast.makeText(this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!fname.isEmpty() && !uname.isEmpty() && !addr.isEmpty() && !em.isEmpty())
            {
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
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
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
            else {
                Toast.makeText(this, "You Must Fill In the FIELDS", Toast.LENGTH_SHORT).show();
            }
        }

    }




    @Override
    public void onClick(View v)
    {

        if (v == signUp)
        {
            registerUser(v);
        }

        if (v == signIn)
        {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        if (v == UserAgreement)
        {
            setUserAgreement();
        }

    }

}
