package com.example.paintingsonline.Add;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.Model.Category;
import com.example.paintingsonline.Model.Room;
import com.example.paintingsonline.Profile.ProfileActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity implements PhotoDialogueBox.OnPhotoSelectedListener
{

    //Image From Gallery
    @Override
    public void getimagepath(Uri imagepath)
    {
        uploadimage.setImageURI(imagepath);
        mselectedBitmap = null;
        mselectedURI = imagepath;
    }

    @Override
    public void getBytes(byte[] imgBytes) {
        mselectedBitmap = null;
        mUploadBytes = imgBytes;
    }

    private String URL = "https://jrnan.info/Painting/ShowCategory.php";
    private String URL_ROOM = "https://jrnan.info/Painting/ShowRoom.php";
    private String UPLOAD_URL = "https://jrnan.info/Painting/testUpload.php";
    private static final int REQUEST_CODE = 1;
    private ImageView uploadimage;
    private EditText paintingtitle, paintingprice, paintingdesc, length, width, qty;
    private Button uploadButton;
    private Spinner s1, s2;
    List<Category> categoryList;
    List<Room> roomList;
    int usertype = -1;
    int verifieduser = -1;


    private Bitmap mselectedBitmap;
    private Uri mselectedURI;
    private byte[] mUploadBytes;
    String emptyimage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);



        uploadimage = findViewById(R.id.post_image);
        paintingtitle = findViewById(R.id.input_title);
        paintingprice = findViewById(R.id.input_price);
        paintingdesc = findViewById(R.id.input_description);
        length = findViewById(R.id.length);
        width = findViewById(R.id.width);
        qty = findViewById(R.id.quantity);
        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        uploadButton = findViewById(R.id.btn_post);


        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(AddActivity.this, LoginActivity.class));
            Toast.makeText(this, "You are Not Logged In", Toast.LENGTH_SHORT).show();
        }

        usertype = SharedPrefManager.getInstance(this).getUserType();
        verifieduser = SharedPrefManager.getInstance(this).getVerifiedUser();


        if (!SharedPrefManager.getInstance(this).isLoggedIn())
        {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddActivity.this);
            builder1.setMessage("You are not an Logged in. Click ok to Log In");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                            startActivity(new Intent(AddActivity.this, LoginActivity.class));
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
        else {
            if(usertype == 0)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddActivity.this);
                builder1.setMessage("You are not an artist. Click ok to Continue as an User");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                startActivity(new Intent(AddActivity.this, ProfileActivity.class));
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                //Toast.makeText(this, "You have to be logged in as an artist", Toast.LENGTH_SHORT).show();
            }
            else if  (usertype == 1 )
            {
                if (verifieduser == 0)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddActivity.this);
                    builder1.setMessage("You are not a verified artist yet. Click ok to Continue as an User");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                    startActivity(new Intent(AddActivity.this, ProfileActivity.class));
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    //Toast.makeText(this, "Oops, we havent vertified you yet!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Welcome, we are excited to see what you made!!", Toast.LENGTH_SHORT).show();
                }
            }
        }


        categoryList = new ArrayList<>();

        roomList = new ArrayList<>();

        JSONrequestCategory();

        JSONrequestRoom();

        initialize();

        setupBottomnavView();

    }




    private void JSONrequestCategory()
    {
        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        int id = jsonObject.getInt("ID");
                        String title = jsonObject.getString("Name");

                        Category category = new Category(id, title);
                        categoryList.add(category);
                        initSpinner();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }



    private void JSONrequestRoom()
    {
        JsonArrayRequest request = new JsonArrayRequest(URL_ROOM, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        int id = jsonObject.getInt("ID");
                        String title = jsonObject.getString("Name");

                        Room room = new Room(id, title);
                        roomList.add(room);
                        initSpinner2();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(request);
    }



    public void initSpinner2()
    {
        ArrayAdapter<Room> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter);


        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Room room = (Room) s2.getSelectedItem();
                // Toast.makeText(AddActivity.this, "ID: " + category.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }



    public void initSpinner()
    {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) s1.getSelectedItem();
                // Toast.makeText(AddActivity.this, "ID: " + category.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    public void initialize()
    {
        uploadimage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                verifyPermissions();

                PhotoDialogueBox photoDialogueBox = new PhotoDialogueBox();
                photoDialogueBox.show(getSupportFragmentManager(), getString(R.string.dialogue));
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {



                FirebaseApp.initializeApp(AddActivity.this);

                final String postID = FirebaseDatabase.getInstance().getReference().push().getKey();

                final StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("posts/" + postID);

                UploadTask uploadTask = storageReference.putFile(mselectedURI);




                final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            String title = paintingtitle.getText().toString().trim();
                            String desc = paintingdesc.getText().toString().trim();
                            String price = paintingprice.getText().toString().trim();
                            String len = length.getText().toString().trim();
                            String wid = width.getText().toString().trim();
                            String quantity = qty.getText().toString().trim();
                            long id = s1.getSelectedItemId();
                            long id2 = s2.getSelectedItemId();


                            UPLOAD_URL += "?painting_url=" + downloadUri;
                            UPLOAD_URL += "&painting_name=" + title;
                            UPLOAD_URL += "&painting_price=" + price;
                            UPLOAD_URL += "&painting_description=" + desc;
                            UPLOAD_URL += "&painting_artist=" + SharedPrefManager.getInstance(AddActivity.this).getUserName();
                            UPLOAD_URL += "&Painting_category=" + id;
                            UPLOAD_URL += "&Password=" + SharedPrefManager.getInstance(AddActivity.this).password();
                            UPLOAD_URL += "&Room=" + id2;
                            UPLOAD_URL += "&Size=" + len + "x" + wid ;
                            UPLOAD_URL += "&Quantity="+ quantity ;

                            Log.d("u", "url: " + UPLOAD_URL);

                            StringRequest uploadrequest = new StringRequest(UPLOAD_URL, new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(AddActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            MySingleton.getInstance(AddActivity.this).addToRequestQueue(uploadrequest);

                        } else {
                            // Handle failures
                            // ...
                        }

                    }
                });



                if (!isEmpty(paintingtitle.getText().toString())
                        && !isEmpty(paintingdesc.getText().toString())
                        && !isEmpty(paintingprice.getText().toString()))
                {
                    if (mselectedURI != null && mselectedBitmap == null)
                    {

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "You must Fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void resetFields()
    {
        paintingtitle.setText("");
        paintingprice.setText("");
        paintingdesc.setText("");
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }


    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(AddActivity.this, this , bottomNavigationView);
        BottomNavViewHelper.setupBottomNavView(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

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
            ActivityCompat.requestPermissions(AddActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }
}
