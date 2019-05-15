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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.paintingsonline.Model.Category;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
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
