package com.example.paintingsonline.ArtistPanel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.paintingsonline.Add.PhotoDialogueBox;
import com.example.paintingsonline.Add.multipleEntryListAdapter;
import com.example.paintingsonline.Model.Category;
import com.example.paintingsonline.Model.MultipleSizePriceQuantity;
import com.example.paintingsonline.Model.Room;
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
import java.util.List;

public class EditDeletePaintings extends AppCompatActivity implements PhotoDialogueBox.OnPhotoSelectedListener, ViewPagerAdapter.OnImageClickListener
{
    private String category_url = "/ShowCategory.php";
    private String room_url = "/ShowRoom.php";
    private String update_url = "/Update.php";
    private String receiveSize_URL = "/LoadSizes.php?painting_id=";
    private String receiveimage_URL = "/LoadPictures.php?painting_id=";
    private String URL = "";
    private String update_urls = "";
    EditText updatetitle, updatedesc, updatewidht, updatelength, updateprice, updateQuantity;
    Button addEntries, updatebtn, deletebtn, updatespq;
    Spinner updateCategory, updateRoom;
    SharedPreferences updateSP;
    String pid, artist, pimg, receiveSize, length, width;
    int receivePrice, receiveQuantity;
    ListView listView;
    multipleEntryListAdapter MmultipleEntryListAdapter;
    ArrayList<MultipleSizePriceQuantity> updateValues;
    List<Category> ucategory;
    List<Room> uroom;
    ViewPager viewPager;
    TabLayout tablayout;
    ViewPagerAdapter viewPagerAdapter;
    private List<byte[]> mUploadBytes = new ArrayList<>();
    private List<String> oldPaintingImage = new ArrayList<>();
    private List<String> OldsizeLists = new ArrayList<>();
    private List<String> OldpriceLists = new ArrayList<>();
    private List<String> OldquantityLists = new ArrayList<>();
    int count = 0;
    ProgressBar progressBar;
    String[] oldSizes;

    List<String> Length;
    List<String> Widths;

//    String[] len1;
//    String[] wid1;
    //private ArrayList<MultipleSizePriceQuantity> receivemspq;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_paintings);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(EditDeletePaintings.this);
        URL = sp.getString("mainurl", "");



        room_url = URL + room_url;
        update_url = URL + update_url;
        category_url = URL + category_url;
        receiveSize_URL = URL + receiveSize_URL;
        receiveimage_URL = URL + receiveimage_URL;

        progressBar = findViewById(R.id.progressBarEdit);

        updatetitle = findViewById(R.id.update_title);
        updatedesc = findViewById(R.id.update_description);
        addEntries = findViewById(R.id.updateSizes);
        updatebtn = findViewById(R.id.btn_update);
        deletebtn = findViewById(R.id.btn_delete);
        updateCategory = findViewById(R.id.updatespinner1);
        updateRoom = findViewById(R.id.updatespinner2);

        viewPager = findViewById(R.id.vpager);
        tablayout = findViewById(R.id.tab_layout);

        //updateValues = new ArrayList<>();
        ucategory = new ArrayList<>();
        uroom = new ArrayList<>();

        updateSP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pid = updateSP.getString("painting_id", "");
        artist = updateSP.getString("painting_artist", "");
        updatetitle.setText(updateSP.getString("painting_name",""));
        updatedesc.setText(updateSP.getString("painting_desc",""));


        //Receiving Sizes and separating using String.split


//        receiveSize = updateSP.getString("painting_size", "");
//        String[] result = receiveSize.split(" X");
//        Log.d("r","r" + receiveSize);
//        Log.d("r","r" + result);
//        length = result[0];
//        Log.d("r","l" + length);
//        width = result[1];
//        Log.d("r","w" + width);



//        receiveQuantity = updateSP.getInt("painting_quantity",0);
//        receivePrice = updateSP.getInt("painting_price", 0);



        Log.d("lis", "old" + oldPaintingImage.size());
        Log.d("img", "img" + pimg);




        JsonMultipleSizeRequest();
        OldPaintingImage();
        addMultipleEntries();
        JSONrequestCategory();
        JSONrequestRoom();
        DeletePainting();
        UpdatePainting();
        setupBottomnavView();
    }



    public void JsonMultipleSizeRequest()
    {
        JsonArrayRequest request = new JsonArrayRequest(receiveSize_URL + pid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;


                Widths = new ArrayList<>();
                Length = new ArrayList<>();

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);


                        OldsizeLists.add(jsonObject.getString("Size"));
                        OldpriceLists.add(jsonObject.getString("Price"));
                        OldquantityLists.add(jsonObject.getString("Quantity"));

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                oldSizes = new String[OldsizeLists.size()];
                //oldPrices = new String[OldpriceLists.size()];
                //oldQuantity = new String[OldquantityLists.size()];

                Log.d("chk", "os " + oldSizes.length);

                for (int i = 0; i < OldsizeLists.size(); i++)
                {
                    oldSizes[i] = OldsizeLists.get(i);
                    Log.d("chk", "chk " + oldSizes[i]);

                    String[] result = oldSizes[i].split(" X");

                   Length.add(result[0]);
                   Widths.add(result[1]);
                    Log.d("a","Length:" + Length.get(i) + " Width: " + Widths.get(i));
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


    private void DeletePainting()
    {
        deletebtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(EditDeletePaintings.this);
                builder1.setMessage("Are you sure you want to delete this painting?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                                ExecuteDeletePaintings();
                            }
                        });

                builder1.setNegativeButton(
                        "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }


    private void ExecuteDeletePaintings()
    {

        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        final String MacAddress = info.getMacAddress();
        update_url = URL + "/Update.php";
        update_url += "?painting_id=" + pid + "&Mac=" + MacAddress;
        update_url += "&Delete=" + "&Mac=" + MacAddress;

        JsonArrayRequest request = new JsonArrayRequest(update_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {

                Toast.makeText(EditDeletePaintings.this, "Painting Deleted Successfully", Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        Log.d("url", "ur: " + request);
        MySingleton.getInstance(EditDeletePaintings.this).addToRequestQueue(request);



        Log.d("u", "U" + update_url);

        startActivity(new Intent(EditDeletePaintings.this, ModifyPaintings.class));
    }


    private void OldPaintingImage()
    {

        JsonArrayRequest pictureRquest = new JsonArrayRequest(receiveimage_URL + pid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        oldPaintingImage.add(jsonObject.getString("painting_url"));
                        viewPagerAdapter = new ViewPagerAdapter(EditDeletePaintings.this, mUploadBytes, oldPaintingImage, new ViewPagerAdapter.OnImageClickListener() {
                            @Override
                            public void onGalleryClick(int position, View view)
                            {

                                PhotoDialogueBox photoDialogueBox = new PhotoDialogueBox();
                                photoDialogueBox.show(getSupportFragmentManager(), getString(R.string.dialogue));
                            }
                        });
                        viewPager.setAdapter(viewPagerAdapter);
                        tablayout.setupWithViewPager(viewPager, true);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(pictureRquest);
    }


    private void addMultipleEntries()
    {
        addEntries.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateMultipleEntries();
            }
        });
    }

    private void updateMultipleEntries()
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditDeletePaintings.this);
        alertDialog.setTitle("Add Multiple Entries");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_size_price_qty, null);

        listView = add_menu_layout.findViewById(R.id.size_price_quantity_list);




//        Log.d("re","old" + updateValues);
//
//        Log.d("re","old" + updateValues.size());
//        Log.d("re","old" + updateValues.size());





        if (updateValues == null)
        {
            //updateValues.clear();
            updateValues = new ArrayList<>();


            //Old Size Price Quantity
            for (int i = 0 ; i < Widths.size() ; i++)
            {
                MultipleSizePriceQuantity rmspq = new MultipleSizePriceQuantity(Length.get(i), Widths.get(i), OldquantityLists.get(i), OldpriceLists.get(i));
                //MultipleSizePriceQuantity receivemspq = new MultipleSizePriceQuantity(oldSizes, oldPrices, oldQuantity);
                updateValues.add(rmspq);
            }
                //updateValues.add(rmspq);
                MmultipleEntryListAdapter = new multipleEntryListAdapter(getApplicationContext(), R.layout.multiple_entries_list, updateValues, new multipleEntryListAdapter.OnEntryListener()
                {
                    @Override
                    public void OnClickRemove(int pos) {
                        updateValues.remove(pos);
                    }
                });
                listView.setAdapter(MmultipleEntryListAdapter);
                Log.d("re", "reold" + updateValues.size());

                SharedPreferences prefs = getSharedPreferences("newValues", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(updateValues);
                editor.putString("NewValues", json);
                editor.apply();

        }
        else
        {
            //New Sizes Saved in the updateValues List
            SharedPreferences settings = this.getSharedPreferences("newValues", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String JSON = settings.getString("NewValues", null);
            Type type = new TypeToken<ArrayList<MultipleSizePriceQuantity>>(){}.getType();
            updateValues = gson.fromJson(JSON, type);

            //updateValues.add(rmspq);
            MmultipleEntryListAdapter = new multipleEntryListAdapter(getApplicationContext(), R.layout.multiple_entries_list, updateValues, new multipleEntryListAdapter.OnEntryListener() {
                @Override
                public void OnClickRemove(int pos)
                {
                    updateValues.remove(pos);
                }
            });
            listView.setAdapter(MmultipleEntryListAdapter);
            Log.d("re","re" + updateValues.size());
        }



        updatespq = add_menu_layout.findViewById(R.id.addSizes);
        updatewidht = add_menu_layout.findViewById(R.id.multiplewidth);
        updatelength = add_menu_layout.findViewById(R.id.multiplelength);
        updateprice = add_menu_layout.findViewById(R.id.multiplePrices);
        updateQuantity = add_menu_layout.findViewById(R.id.multiplequantity);


        //Add New Size Price Quantity
        updatespq.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (!isEmpty(updatewidht.getText().toString()) && !isEmpty(updatelength.getText().toString()) && !isEmpty(updateprice.getText().toString()) && !isEmpty(updateQuantity.getText().toString()))
                {

                    MultipleSizePriceQuantity mspq = new MultipleSizePriceQuantity(updatelength.getText().toString(), updatewidht.getText().toString(), updateQuantity.getText().toString(), updateprice.getText().toString());
                    updateValues.add(mspq);

                    MmultipleEntryListAdapter = new multipleEntryListAdapter(getApplicationContext(), R.layout.multiple_entries_list, updateValues, new multipleEntryListAdapter.OnEntryListener() {
                        @Override
                        public void OnClickRemove(int pos) {
                            updateValues.remove(pos);
                        }
                    });
                    listView.setAdapter(MmultipleEntryListAdapter);
                }
                else
                {
                    Toast.makeText(EditDeletePaintings.this, "You Must Fill the Required Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d("total Size", "ts " + updateValues.size());
        Log.d("total Size", "ts " + updateValues);

        if (updateValues == null)
        {
            Toast.makeText(EditDeletePaintings.this, "Cannot Save Empty Fields", Toast.LENGTH_SHORT).show();
        }
        else {
            alertDialog.setPositiveButton("SAVE", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {


                    Log.d("uv", "uv" + updateValues.size());


                    SharedPreferences prefs = getSharedPreferences("newValues", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(updateValues);
                    editor.putString("NewValues", json);
                    editor.apply();
                    dialog.dismiss();
                }
            });
        }

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(EditDeletePaintings.this, "No Entries are saved", Toast.LENGTH_SHORT).show();
                Log.d("sv","sv" + updateValues.size());
                dialog.dismiss();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.show();
    }


    private void JSONrequestCategory()
    {
        JsonArrayRequest request = new JsonArrayRequest(category_url, new Response.Listener<JSONArray>() {
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
                        ucategory.add(category);
                        updateinitSpinner();

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
        JsonArrayRequest request = new JsonArrayRequest(room_url, new Response.Listener<JSONArray>() {
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
                        uroom.add(room);
                        updateinitSpinner2();

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


    public void updateinitSpinner2()
    {
        ArrayAdapter<Room> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, uroom);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateRoom.setAdapter(adapter);


        updateRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Room room = (Room) updateRoom.getSelectedItem();
                // Toast.makeText(AddActivity.this, "ID: " + category.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void updateinitSpinner()
    {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ucategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateCategory.setAdapter(adapter);


        updateCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = (Category) updateCategory.getSelectedItem();
                // Toast.makeText(AddActivity.this, "ID: " + category.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private boolean isEmpty(String string){
        return string.equals("");
    }


    /* bottom navigation view setup */
    private void setupBottomnavView()
    {
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom);
        BottomNavViewHelper.enableNavigation(EditDeletePaintings.this, this , bottomNavigationView);
        BottomNavViewHelper.setupBottomNavView(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

    }


    @Override
    public void onGalleryClick(int position, View view)
    {
        PhotoDialogueBox photoDialogueBox = new PhotoDialogueBox();
        photoDialogueBox.show(getSupportFragmentManager(), getString(R.string.dialogue));
    }


    @Override
    public void getBytes(List<byte[]> imgBytes)
    {
        if (imgBytes != null)
        {
            oldPaintingImage.clear();
            mUploadBytes = imgBytes;
            viewPagerAdapter = new ViewPagerAdapter(EditDeletePaintings.this, mUploadBytes, oldPaintingImage,this);
            viewPager.setAdapter(viewPagerAdapter);
            tablayout.setupWithViewPager(viewPager, true);
        }

    }


    public void resetFields()
    {
        updatetitle.setText("");
//        paintingprice.setText("");
        updatedesc.setText("");
        //      length.setText("");
        //     width.setText("");
        //    qty.setText("");
    }


    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgressBar(){
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    Integer CountOfUrl = 0;
    Integer ImageCount = 0;

    private void UpdatePainting()
    {

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (!isEmpty(updatetitle.getText().toString())
                        && !isEmpty(updatedesc.getText().toString()))
                {
                    update_urls = "";
                    final int TotalCount = mUploadBytes.size();
                    count = 0 ;

                    update_url = URL + "/Update.php";
                    update_url += "?painting_id=" + pid;

                    WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = manager.getConnectionInfo();
                    final String MacAddress = info.getMacAddress();

                    update_url += "&Mac=" + MacAddress;



                    if (mUploadBytes.size()>0)
                    {
                        while (count < TotalCount)
                        {
                            showProgressBar();

                            FirebaseApp.initializeApp(EditDeletePaintings.this);

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

                                    update_urls += "Url" + String.valueOf(CountOfUrl) + "=" + task.getResult() + "&";
                                    CountOfUrl += 1;
                                    ImageCount += 1 ;
                                    if (task.isSuccessful() && ImageCount == TotalCount) {
                                        String title = updatetitle.getText().toString().trim();
                                        String desc = updatedesc.getText().toString().trim();
                                        String price = "";
                                        String Size = "";
                                        String quantity = "";
                                        for (MultipleSizePriceQuantity mspq: updateValues)
                                        {
                                            price += mspq.getPrice() + ",";
                                            quantity += mspq.getQuantity() + ",";
                                            Size += mspq.getWidth() + "cm X" + mspq.getLength() + "cm,";
                                        }


                                        long id = updateCategory.getSelectedItemId();
                                        long id2 = updateRoom.getSelectedItemId();

                                        update_url +=  "&" + update_urls;
                                        update_url += "&painting_name=" + title;
                                        update_url += "&painting_price=" + price;
                                        update_url += "&painting_description=" + desc;
                                        update_url += "&painting_artist=" + SharedPrefManager.getInstance(EditDeletePaintings.this).getUserName();
                                        update_url += "&Painting_category=" + id;
                                        update_url += "&Password=" + SharedPrefManager.getInstance(EditDeletePaintings.this).password();
                                        update_url += "&Room=" + id2;
                                        update_url += "&Size=" + Size;
                                        update_url += "&Quantity=" + quantity;
                                        update_url += "&Email=" + SharedPrefManager.getInstance(EditDeletePaintings.this).getUserEmail();
                                        update_url += "&CountOfImage=" + CountOfUrl;

                                        Log.d("u", "url1: " + update_url);

                                        StringRequest uploadrequest = new StringRequest(update_url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response)
                                            {
                                                hideProgressBar();
                                                Toast.makeText(EditDeletePaintings.this, "Painting Updated Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(EditDeletePaintings.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        MySingleton.getInstance(EditDeletePaintings.this).addToRequestQueue(uploadrequest);
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

}
