package com.example.paintingsonline.Registration;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity
{

    //public String URL_REGISTER = "https://paintingsonline.000webhostapp.com/Register.php";

    private String url = "/Register.php";
    private String URL = "";

    private static final int REQUEST_LOCATIONCODE = 2000;
    private EditText username, address, email, password, conpass, fullname;
    private Button signUp, user, artist;
    private TextView signIn, UserAgreement, userDifference;
    int u = 0 ;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Geocoder geocoder;
    private List<Address> addresses;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_LOCATIONCODE:
                if (grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {

                    }
                    else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                    {

                    }
                }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(RegistrationActivity.this);
        URL = sp2.getString("mainurl", "");
        url = URL + url;

        fullname = findViewById(R.id.regfname);
        username = findViewById(R.id.reguname);
        address = findViewById(R.id.regaddress);
        email = findViewById(R.id.regemail);
        password = findViewById(R.id.regpass);
        conpass = findViewById(R.id.regconpass);
        UserAgreement = findViewById(R.id.agreement);




        signUp = findViewById(R.id.btn_signup);
        signIn = findViewById(R.id.signin);


        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerUser();
            }
        });


        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        UserAgreement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setUserAgreement();
            }
        });


        GetLocation();

    }



    public void GetLocation()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATIONCODE);
        }
        else
        {
            //if permission is granted
            BuildLocationRequest();
            BuildLocationCallBack();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            //Set Event for button
            if (ActivityCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATIONCODE);
                return;
            }

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
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



    private void BuildLocationCallBack()
    {
        geocoder = new Geocoder(this, Locale.getDefault());

        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                for (Location location: locationResult.getLocations())
                {

                    try {

                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        address.setText(address1);

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        };

    }


    private void BuildLocationRequest()
    {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }



//    public boolean buttonPressed(View v)
//    {
//        switch(v.getId()) {
//            case R.id.user:
//                user.setTextColor(Color.parseColor("White"));
//                artist.setTextColor(Color.parseColor("Black"));
//                userDifference.setText("Users can buy and rate Paintings after a purchase but they cant sell their paintings. You can Switch to artist later.");
//                break;
//            case R.id.artist1:
//                user.setTextColor(Color.parseColor("Black"));
//                artist.setTextColor(Color.parseColor("White"));
//                userDifference.setText("Artists Can sell and buy paintings, track their sales and create their mini store.");
//                break;
//        }
//
//        return false;
//    }


    private void registerUser()
    {

        final String fname = fullname.getText().toString().trim();
        final String uname = username.getText().toString().trim();
        final String addr = address.getText().toString().trim();
        final String em = email.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        String conp = conpass.getText().toString().trim();


        if (!pass.equals(conp))
        {
            //Toast.makeText(this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
            password.setError("Passwords Do Not Match");
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(em).matches())
        {
            email.setError("Please enter a valid email address");
        }
        else {
            if (!fname.isEmpty() && !uname.isEmpty() && !addr.isEmpty() && !em.isEmpty())
            {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegistrationActivity.this, " Registered Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("try", "try " + error.getMessage());
                            }
                        }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Username", uname);
                        params.put("Password", pass);
                        params.put("Address", addr);
                        params.put("Email", em);
                        params.put("Name", fname);
                        params.put("Usertype", "0");
                        return params;
                    }
                };

                MySingleton.getInstance(RegistrationActivity.this).addToRequestQueue(stringRequest);
            }
            else {
                fullname.setError("Fields Cannot Be Empty");
                username.setError("Fields Cannot Be Empty");
                address.setError("Fields Cannot Be Empty");
                email.setError("Fields Cannot Be Empty");
            }
        }

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        GetLocation();
    }
}
