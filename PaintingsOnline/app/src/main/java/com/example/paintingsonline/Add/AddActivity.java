package com.example.paintingsonline.Add;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Home.HomeActivity;
import com.example.paintingsonline.Login.LoginActivity;
import com.example.paintingsonline.Model.Category;
import com.example.paintingsonline.Model.MultipleSizePriceQuantity;
import com.example.paintingsonline.Model.Room;
import com.example.paintingsonline.Profile.ProfileActivity;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.BottomNavViewHelper;
import com.example.paintingsonline.Utils.MySingleton;
import com.example.paintingsonline.Utils.SharedPrefManager;
import com.example.paintingsonline.Utils.ViewPagerAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddActivity extends AppCompatActivity implements PhotoDialogueBox.OnPhotoSelectedListener, ViewPagerAdapter.OnImageClickListener
{

    @Override
    public void getBytes(List<byte[]> imgBytes)
    {
        mUploadBytes = imgBytes;
        viewPagerAdapter = new ViewPagerAdapter(AddActivity.this, mUploadBytes, oldimages,this);
        viewPager.setAdapter(viewPagerAdapter);
        tablayout.setupWithViewPager(viewPager, true);
    }


    private String URL = "";
    private String URL_Category = "/ShowCategory.php";
    private String URL_ROOM = "/ShowRoom.php";
    private String UPLOAD_URL = "/testUpload.php";
    private static final int REQUEST_CODE = 1;
    private ImageView uploadimage;
    private EditText paintingtitle, paintingprice, paintingdesc, length, width, qty, width1, height1, multipleprice1, multipleQty1;
    private Button uploadButton, addSizePrice, cancel1, ok1, addspq;
    TextView len, wid, quantity1, price;
    private Spinner s1, s2;
    List<Category> categoryList;
    List<Room> roomList;
    List<String> oldimages = new ArrayList<>();
    int usertype = -1;
    int verifieduser = -1;
    ViewPager viewPager;
    TabLayout tablayout;
    ViewPagerAdapter viewPagerAdapter;
    ArrayList<MultipleSizePriceQuantity> savedValues;
    private ListView listView;
    private multipleEntryListAdapter multipleEntryListAdapter;
   // SharedPreferenceIntegerLiveData sharedPreferenceIntegerLiveData;
    int MyCount = 0 ;
    ProgressBar mProgressBar;


    private Bitmap mselectedBitmap;
    private Uri mselectedURI;
    //private byte[] mUploadBytes;

    //private ArrayList<Uri> mselectedURI;
    private List<byte[]> mUploadBytes = new ArrayList<>();
    String emptyimage = "";
    int count = 0;
    String Urls = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(AddActivity.this);
        URL = sp2.getString("mainurl", "");
        URL_Category = URL + URL_Category;
        URL_ROOM = URL + URL_ROOM;
        UPLOAD_URL = URL + UPLOAD_URL;

        loginUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        uploadimage = findViewById(R.id.post_image);
        paintingtitle = findViewById(R.id.input_title);
        //paintingprice = findViewById(R.id.input_price);
        paintingdesc = findViewById(R.id.input_description);
//        length = findViewById(R.id.length);
//        width = findViewById(R.id.width);
//        qty = findViewById(R.id.quantity);
        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        uploadButton = findViewById(R.id.btn_post);
        viewPager = findViewById(R.id.vpager);
        addSizePrice = findViewById(R.id.addSizes);
        tablayout = findViewById(R.id.tab_layout);
        mProgressBar = findViewById(R.id.progressBar);
        //linearLayout = findViewById(R.id.size);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(AddActivity.this, LoginActivity.class));
            Toast.makeText(this, "You are Not Logged In", Toast.LENGTH_SHORT).show();
        }


        usertype = SharedPrefManager.getInstance(this).getUserType();
        verifieduser = SharedPrefManager.getInstance(this).getVerifiedUser();

//        sharedPreferenceIntegerLiveData = SharedPrefManager.getInstance(this).getSpInt();
//        sharedPreferenceIntegerLiveData.getIntegerLiveData("verify", verifieduser).observe(this, verify -> {
//            Toast.makeText(this, "v " + verifieduser, Toast.LENGTH_SHORT).show();
//        });


        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddActivity.this);
            builder1.setMessage("You are not an Logged in. Click ok to Log In");
            builder1.setCancelable(false);

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

        } else {
            if (usertype == 0) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddActivity.this);
                builder1.setMessage("You are not an artist. Click ok to Continue as an User");
                builder1.setCancelable(false);

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
            } else if (usertype == 1) {
                if (verifieduser == 0) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(AddActivity.this);
                    builder1.setMessage("You are not a verified artist yet. Click ok to Continue as an User");
                    builder1.setCancelable(false);

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
                } else {
                    Toast.makeText(this, "Welcome, we are excited to see what you made!!", Toast.LENGTH_SHORT).show();
                }
            }
        }


        categoryList = new ArrayList<>();

        roomList = new ArrayList<>();

        addMoreSizePriceQuantity();

        JSONrequestCategory();

        JSONrequestRoom();

        initialize();

        setupBottomnavView();

        //viewPager.setAdapter(viewPagerAdapter);

    }


    private void addMoreSizePriceQuantity()
    {
        addSizePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddingMultipleSizePriceQty();
            }
        });
    }


    private void showAddingMultipleSizePriceQty()
    {




//        Set<String> fetchWidhs = settings.getStringSet("Widths", null);
//        Set<String> fetchHeight = settings.getStringSet("Height", null);
//        Set<String> fetchPrices = settings.getStringSet("Prices", null);
//        Set<String> fetchQuantity = settings.getStringSet("Quantity", null);


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddActivity.this);
        alertDialog.setTitle("Add Multiple Entries");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_size_price_qty, null);

        //savedValues.clear();

        listView = add_menu_layout.findViewById(R.id.size_price_quantity_list);

        SharedPreferences settings = this.getSharedPreferences("packageName", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String JSON = settings.getString("values", null);
        Type type = new TypeToken<ArrayList<MultipleSizePriceQuantity>>(){}.getType();
        savedValues = gson.fromJson(JSON, type);


        if (savedValues == null)
        {
            savedValues = new ArrayList<>();
        }
        else
        {
            multipleEntryListAdapter = new multipleEntryListAdapter(getApplicationContext(), R.layout.multiple_entries_list, savedValues, new multipleEntryListAdapter.OnEntryListener() {
                @Override
                public void OnClickRemove(int pos)
                {
                    savedValues.remove(pos);
                }
            });
            listView.setAdapter(multipleEntryListAdapter);
        }

//        if (fetchWidhs != null)
//        {
//            Object[] TempWidth = fetchWidhs.toArray();
//            Object[] TempHeight = fetchHeight.toArray();
//            Object[] TempPrices = fetchPrices.toArray();
//            Object[] TempQuantity = fetchQuantity.toArray();
//
//            Toast.makeText(this, "w" + TempWidth, Toast.LENGTH_SHORT).show();
//
//            for(int l=0; l< fetchWidhs.size() ; l++)
//            {
//                Log.d("fetch", "fetch" + fetchWidhs);
//                MultipleSizePriceQuantity temp = new MultipleSizePriceQuantity(TempHeight[l].toString(), TempWidth[l].toString() , TempQuantity[l].toString(), TempPrices[l].toString());
//                savedValues.add(temp);
//            }
//
//            multipleEntryListAdapter = new multipleEntryListAdapter(getApplicationContext(), R.layout.multiple_entries_list, savedValues);
//            listView.setAdapter(multipleEntryListAdapter);
//        }

        addspq = add_menu_layout.findViewById(R.id.addSizes);
        width1 = add_menu_layout.findViewById(R.id.multiplewidth);
        length = add_menu_layout.findViewById(R.id.multiplelength);
        multipleprice1 = add_menu_layout.findViewById(R.id.multiplePrices);
        multipleQty1 = add_menu_layout.findViewById(R.id.multiplequantity);

//        price = add_menu_layout.findViewById(R.id.t4);


            addspq.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (!isEmpty(width1.getText().toString()) && !isEmpty(length.getText().toString()) && !isEmpty(multipleprice1.getText().toString()) && !isEmpty(multipleQty1.getText().toString()))
                    {

                        MultipleSizePriceQuantity mspq = new MultipleSizePriceQuantity(length.getText().toString(), width1.getText().toString(), multipleQty1.getText().toString(), multipleprice1.getText().toString());
                        savedValues.add(mspq);

                        multipleEntryListAdapter = new multipleEntryListAdapter(getApplicationContext(), R.layout.multiple_entries_list, savedValues, new multipleEntryListAdapter.OnEntryListener() {
                            @Override
                            public void OnClickRemove(int pos) {
                                savedValues.remove(pos);
                            }
                        });
                        listView.setAdapter(multipleEntryListAdapter);
                    }
                    else
                    {
                        Toast.makeText(AddActivity.this, "You Must Fill in all the Fields", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            //setButton
            alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    SharedPreferences prefs = getSharedPreferences("packageName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(savedValues);
                    editor.putString("values", json);
                    editor.apply();
                    dialog.dismiss();
                }
            });


        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(AddActivity.this, "No Entries are saved", Toast.LENGTH_SHORT).show();
                Log.d("sv","sv" + savedValues.size());
                dialog.dismiss();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.show();

    }



    private void JSONrequestCategory()
    {
        JsonArrayRequest request = new JsonArrayRequest(URL_Category, new Response.Listener<JSONArray>() {
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


    @Override
    public void onGalleryClick(int position, View view)
    {
        verifyPermissions();
        PhotoDialogueBox photoDialogueBox = new PhotoDialogueBox();
        photoDialogueBox.show(getSupportFragmentManager(), getString(R.string.dialogue));
    }

    Integer CountOfUrl = 0;
    Integer ImageCount = 0;

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
                uploadimage.setVisibility(View.INVISIBLE);
            }
        });


        uploadButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (!isEmpty(paintingtitle.getText().toString())
                        && !isEmpty(paintingdesc.getText().toString()))
                {
                    Urls = "";
                    final int TotalCount = mUploadBytes.size();
                    count = 0 ;

                    UPLOAD_URL = URL + "/testUpload.php";
                    if (mUploadBytes.size()>0)
                    {
                        while (count < TotalCount)
                        {
                            showProgressBar();

                            FirebaseApp.initializeApp(AddActivity.this);

                            final String postID = FirebaseDatabase.getInstance().getReference().push().getKey();

                            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("posts/" + postID);

                            UploadTask uploadTask = storageReference.putBytes(mUploadBytes.get(count));


                            final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful())
                                    {
                                        throw task.getException();
                                    }

                                    return storageReference.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    Urls += "Url" + String.valueOf(CountOfUrl) + "=" + task.getResult() + "&";
                                    CountOfUrl += 1;
                                    ImageCount += 1 ;
                                    if (task.isSuccessful() && ImageCount == TotalCount) {
                                        String title = paintingtitle.getText().toString().trim();
                                        String desc = paintingdesc.getText().toString().trim();
                                        String price = "";
                                        String Size = "";
                                        String quantity = "";
                                        for (MultipleSizePriceQuantity mspq: savedValues)
                                        {
                                            price += mspq.getPrice() + ",";
                                            quantity += mspq.getQuantity() + ",";
                                            Size += mspq.getWidth() + "cm X" + mspq.getLength() + "cm,";
                                        }


                                        long id = s1.getSelectedItemId();
                                        long id2 = s2.getSelectedItemId();
                                        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                                        WifiInfo info = manager.getConnectionInfo();
                                        final String MacAddress = info.getMacAddress();



                                        UPLOAD_URL = URL + "/testUpload.php";
                                        UPLOAD_URL +=  "?" + Urls;
                                        UPLOAD_URL += "&painting_name=" + title;
                                        UPLOAD_URL += "&painting_price=" + price;
                                        UPLOAD_URL += "&painting_description=" + desc;
                                        UPLOAD_URL += "&painting_artist=" + SharedPrefManager.getInstance(AddActivity.this).getUserName();
                                        UPLOAD_URL += "&Painting_category=" + id;
                                        UPLOAD_URL += "&Password=" + SharedPrefManager.getInstance(AddActivity.this).password();
                                        UPLOAD_URL += "&Room=" + id2;
                                        UPLOAD_URL += "&Size=" + Size;
                                        UPLOAD_URL += "&Quantity=" + quantity;
                                        UPLOAD_URL += "&Email=" + SharedPrefManager.getInstance(AddActivity.this).getUserEmail();
                                        UPLOAD_URL += "&CountOfImage=" + CountOfUrl;
                                        UPLOAD_URL += "&Mac=" + MacAddress;

                                        Log.d("u", "url: " + UPLOAD_URL);

                                        StringRequest uploadrequest = new StringRequest(UPLOAD_URL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response)
                                            {
                                                hideProgressBar();
                                                Toast.makeText(AddActivity.this, "Painting Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AddActivity.this, HomeActivity.class));
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(AddActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        MySingleton.getInstance(AddActivity.this).addToRequestQueue(uploadrequest);
                                        resetFields();
                                    } else {
                                        // Handle failures
                                        // ...
                                    }

                                }
                            });
                            count += 1 ;
                        }
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
        viewPager.setCurrentItem(0);
        paintingtitle.setText("");
//        paintingprice.setText("");
        paintingdesc.setText("");
  //      length.setText("");
   //     width.setText("");
    //    qty.setText("");
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }


    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
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



    public void loginUser()
    {
        String URL_LOGIN = "/Login.php";
        URL_LOGIN = URL + URL_LOGIN;

        final String e = SharedPrefManager.getInstance(this).getUserName();
        final String p = SharedPrefManager.getInstance(this).password();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray.length() < 1){
                        Toast.makeText(AddActivity.this, "Authontication failed" , Toast.LENGTH_SHORT).show();
                    }
                    else {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String uname = jsonObject.getString("username");
                        int userType = jsonObject.getInt("usertype");
                        int verifyUser = jsonObject.getInt("verified");

                        Log.d("test", "test1 " + verifyUser);

                        if (uname != null)
                        {
                            SharedPrefManager.getInstance(getApplicationContext()).SetVerify(verifyUser);
                            Log.d("test", "test1" + userType + verifyUser);
//                            Toast.makeText(AddActivity.this, verifyUser, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Username",e);
                params.put("Password",p);
                return params;
            }
        };

        Log.d("lu", "lu" + URL_LOGIN);
        MySingleton.getInstance(AddActivity.this).addToRequestQueue(stringRequest);
    }


}
