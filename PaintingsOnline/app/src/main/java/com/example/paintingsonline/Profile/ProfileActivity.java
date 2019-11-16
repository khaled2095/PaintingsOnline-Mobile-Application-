package com.example.paintingsonline.Profile;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.paintingsonline.Add.PhotoDialogueBox;
import com.example.paintingsonline.ArtistPanel.ModifyPaintings;
import com.example.paintingsonline.ArtistPanel.SellingPanel;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity implements PhotoDialogueBox.OnPhotoSelectedListener {

    private EditText fname, address, email, password, conpassword, bio;
    private ImageView artistImage, addressimg;
    private Button updatepassword, updatedetails, logout, switchToArtist;
    private String URL = "";
    private String updateDetails_url = "/UpdateAccount.php";
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_LOCATIONCODE = 1000;
    private List<byte[]> images = new ArrayList<>();
    private List<Address> addresses;
    RelativeLayout relforArtistImage, relForArtistBio;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Geocoder geocoder;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


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
        setContentView(R.layout.activity_profile);



        logout = findViewById(R.id.logout);
        email = findViewById(R.id.editemail);
        address = findViewById(R.id.editaddr);
        fname = findViewById(R.id.editfullName);
        updatepassword = findViewById(R.id.updatepass);
        updatedetails = findViewById(R.id.updatedetails);
        password = findViewById(R.id.editpass);
        conpassword = findViewById(R.id.editconpass);
        bio = findViewById(R.id.input_bio);
        artistImage = findViewById(R.id.ArtistProfile);
        relforArtistImage = findViewById(R.id.pictureRelative);
        relForArtistBio = findViewById(R.id.relbio);
        switchToArtist = findViewById(R.id.switchtoartist);
        addressimg = findViewById(R.id.address);

        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        URL = sp2.getString("mainurl", "");

        updateDetails_url = URL + updateDetails_url ;

        setupToolbar();


        //final String uname = SharedPrefManager.getInstance(this).getUserName();
        fname.setText(SharedPrefManager.getInstance(this).getFullName());
        email.setText(SharedPrefManager.getInstance(this).getUserEmail());
        address.setText(SharedPrefManager.getInstance(this).getUserAddress());




        updatedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateDetails();
                artistImage.setImageResource(0);
            }

        });


            updatepassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (!isEmpty(password.getText().toString()) && !isEmpty(conpassword.getText().toString()))
                    {
                        updatePass();
                    }
                    else
                    {
                        Toast.makeText(ProfileActivity.this, "Cannot Update Empty Password", Toast.LENGTH_SHORT).show();
                    }

                }
            });


            if (SharedPrefManager.getInstance(this).getUserType() == 1)
            {
                relforArtistImage.setVisibility(View.VISIBLE);
                relForArtistBio.setVisibility(View.VISIBLE);
                switchToArtist.setVisibility(View.INVISIBLE);
                updateProfileImage();
            }
            else
            {
                relforArtistImage.setVisibility(View.GONE);
                relForArtistBio.setVisibility(View.GONE);
                switchToArtist.setVisibility((View.VISIBLE));
            }


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
                addressimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATIONCODE);
                            return;
                        }

                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                });
            }


            SwitchToArtist();
            setupBottomnavView();
            logout();
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
//                    String latitude = String.valueOf(location.getLatitude());
//                    String longitude = String.valueOf(location.getLongitude());

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



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (SharedPrefManager.getInstance(this).getUserType() == 1)
        {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_artist, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
            switch (item.getItemId())
            {
                case R.id.spanel:
                    startActivity(new Intent(ProfileActivity.this, SellingPanel.class));
                    return true;

                case R.id.mpaintings:
                    startActivity(new Intent(ProfileActivity.this, ModifyPaintings.class));
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
    }




    private void SwitchToArtist()
    {
        switchToArtist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String uname = SharedPrefManager.getInstance(ProfileActivity.this).getUserName();

                WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                final String MacAddress = info.getMacAddress();

                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, updateDetails_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.d("kk","URL1" + response);
                        //Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileActivity.this);
                        builder1.setMessage("You will receive a verification email as soon as you are verified. Thank you");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error)
                            {

                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Username",uname);
                        params.put("ConvertMe","");
                        params.put("Mac", MacAddress);
                        return params;
                    }
                };

                MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest1);

            }
        });
    }


    public void updateProfileImage()
    {
        artistImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                verifyPermissions();
                PhotoDialogueBox photoDialogueBox = new PhotoDialogueBox();
                photoDialogueBox.show(getSupportFragmentManager(), getString(R.string.dialogue));
            }
        });
    }

    private void verifyPermissions()
    {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {
            ActivityCompat.requestPermissions(ProfileActivity.this, permissions, REQUEST_CODE);
        }
    }

    public void updateDetails()
    {
        final int id = SharedPrefManager.getInstance(this).getUserID();
        final int usertype = SharedPrefManager.getInstance(this).getUserType();
        final int vertify = SharedPrefManager.getInstance(this).getVerifiedUser();
        final String uname = SharedPrefManager.getInstance(this).getUserName();
        final String oldPass = SharedPrefManager.getInstance(this).password();
        final String updatefullname = fname.getText().toString().trim();
        final String updatedadd = address.getText().toString().trim();
        final String updateemail = email.getText().toString().trim();

        //URL += "?Username=" + uname + "&Password=" + password.getText() + "&Email=" + updateemail + "&Address=" + updatedadd + "&Name=" + updatefullname;


        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        final String MacAddress = info.getMacAddress();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateDetails_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                Log.d("kk","url2" + response);
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Username",uname);
                params.put("Password",oldPass);
                params.put("Address",updatedadd);
                params.put("Email",updateemail);
                params.put("Name",updatefullname);
                params.put("Mac",MacAddress);
                SharedPrefManager.getInstance(getApplicationContext()).userlogin(id, uname, oldPass ,updateemail, updatedadd, updatefullname, usertype, vertify);
                return params;
            }
        };

        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);

    }



    public void updatePass()
    {
        final String uname = SharedPrefManager.getInstance(this).getUserName();
        final String newpass = password.getText().toString().trim();
        String conpass = conpassword.getText().toString().trim();


        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        final String MacAddress = info.getMacAddress();

        if (!newpass.equals(conpass))
        {
            //Toast.makeText(ProfileActivity.this, "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
            password.setError("Passwords Do Not Match");
        }
        else if (!PASSWORD_PATTERN.matcher(newpass).matches())
        {
            password.setError("Password too weak");
        }
        else
        {
            //URL += "?Username=" + uname + "&Password=" + SharedPrefManager.getInstance(this).password() + "&Email=" + SharedPrefManager.getInstance(this).getUserEmail() + "&Address=" + SharedPrefManager.getInstance(this).getUserAddress() + "&Name=" + SharedPrefManager.getInstance(this).getFullName() + "&NewPassword=" + newpass;

            StringRequest updatePassRequest = new StringRequest(Request.Method.POST, updateDetails_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    Toast.makeText(ProfileActivity.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Username",uname);
                    params.put("Password", SharedPrefManager.getInstance(getApplication()).password());
                    params.put("Address", String.valueOf(address));
                    params.put("Email", String.valueOf(email));
                    params.put("Name", String.valueOf(fname));
                    params.put("NewPassword",newpass);
                    params.put("Mac",MacAddress);
                    return params;
                }
            };

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

    private boolean isEmpty(String string){
        return string.equals("");
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

    @Override
    public void getBytes(List<byte[]> imgBytes)
    {
        images = imgBytes;
        Glide.with(this).load(images.get(0)).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(artistImage);
    }
}
