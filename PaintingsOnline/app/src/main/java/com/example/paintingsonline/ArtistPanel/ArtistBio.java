package com.example.paintingsonline.ArtistPanel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.example.paintingsonline.Category.PaintingsDetails;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistBio extends AppCompatActivity implements ArtistPaintingsView.paintingsListener
{

    private String artist_url = "/LoadAccount.php?Username=";
    private String artistPaintings_url = "/ShowMyPaintings.php?Artist=";
    private String URL = "";
    NetworkImageView ArtistProfilePic;
    TextView ArtistName, ArtistBioText;
    SharedPreferences sharedPreferences;
    ArtistPaintingsView artistPaintingsView;
    List<Paintings> artistPaintings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_bio);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ArtistBio.this);
        URL = sp.getString("mainurl", "");
        artist_url = URL + artist_url;
        artistPaintings_url = URL + artistPaintings_url;

        /*setup backarrow for NAVIGATION */
        ImageView backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArtistProfilePic = findViewById(R.id.Artistimg);
        ArtistName = findViewById(R.id.artistName);
        ArtistBioText = findViewById(R.id.showownerBio);

        artistPaintings = new ArrayList<>();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ArtistName.setText(sharedPreferences.getString("paintingArtist", ""));
        String TmpArtist = ArtistName.getText().toString().substring(3,ArtistName.length());
        artist_url += TmpArtist;
        Log.d("New Artist", "New Artist" + artist_url);
        artistPaintings_url += TmpArtist;
        JSONRequestArtistProfile();
        JSONRequestArtistPaintings();
    }


    private void JSONRequestArtistProfile()
    {
        JsonArrayRequest profileRquest = new JsonArrayRequest(artist_url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);
                        String ArtistProfilePicture = jsonObject.getString("Image");
                        String Bio = jsonObject.getString("Bio");
                        Log.d("Artist","Artist " + artist_url);
                        Log.d("Artist","Artist " + ArtistProfilePicture);
                        Log.d("Artist","Artist " + Bio);
                        ArtistProfilePic.setImageUrl(ArtistProfilePicture, MySingleton.getInstance(ArtistBio.this).getImageLoader());
                        ArtistBioText.setText(Bio);

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

        Log.d("pro", "pro " + artist_url);
        MySingleton.getInstance(this).addToRequestQueue(profileRquest);
    }


    private void JSONRequestArtistPaintings()
    {
        JsonArrayRequest PaintingsRquest = new JsonArrayRequest(artistPaintings_url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("painting_id");
                        String title = jsonObject.getString("painting_name");
                        String image = jsonObject.getString("painting_url");
                        //String paintingOwner = jsonObject.getString("painting_artist");
                        //String paintingSize = jsonObject.getString("Size");
                        int price = jsonObject.getInt("painting_price");
                        //String desc = jsonObject.getString("painting_description");

                        Paintings ArtistPaints = new Paintings(id, title, image, price);
                        artistPaintings.add(ArtistPaints);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                initrecyclerView(artistPaintings);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Log.d("pro", "pro " + artistPaintings_url);
        MySingleton.getInstance(this).addToRequestQueue(PaintingsRquest);
    }


    private void initrecyclerView(List<Paintings> artistP)
    {
        RecyclerView recyclerView = findViewById(R.id.OwnerPaintings);
        artistPaintingsView = new ArtistPaintingsView(this, artistP, this);
        recyclerView.setAdapter(artistPaintingsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void clickPaintings(int pos)
    {
        SharedPreferences spHomeActivity = PreferenceManager.getDefaultSharedPreferences(ArtistBio.this);
        SharedPreferences.Editor editor = spHomeActivity.edit();
        editor.putString("painting_id", artistPaintings.get(pos).getId());
        editor.putString("painting_name", artistPaintings.get(pos).getName());
        editor.putString("painting_desc", artistPaintings.get(pos).getDescription());
        editor.putString("painting_artist", artistPaintings.get(pos).getpOwner());
        editor.apply();
        Intent homeActivity = new Intent(ArtistBio.this, PaintingsDetails.class);
        startActivity(homeActivity);
    }
}
