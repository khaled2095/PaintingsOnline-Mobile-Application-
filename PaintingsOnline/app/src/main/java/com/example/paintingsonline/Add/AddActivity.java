package com.example.paintingsonline.Add;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Model.Category;
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


    //Image Base 64 string From Gallery
    @Override
    public void getimageBase64String(String image64)
    {
        mselectedBitmap = null;
        mselectedImage64 = image64;
    }



    private String URL = "https://jrnan.info/Painting/ShowCategory.php";
    private String UPLOAD_URL = "https://jrnan.info/Painting/testUpload.php";
    private static final int REQUEST_CODE = 1;
    private ImageView uploadimage;
    private EditText paintingtitle, paintingprice, paintingdesc;
    private Button uploadButton;
    private Spinner s1;
    List<Category> categoryList;
    int usertype = -1;
    int verifieduser = -1;


    private Bitmap mselectedBitmap;
    private Uri mselectedURI;
    private String mselectedImage64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
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
