package com.example.paintingsonline.Search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Category.PaintingsDetails;
import com.example.paintingsonline.Home.HomePaintingsAdapter;
import com.example.paintingsonline.Model.Paintings;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements HomePaintingsAdapter.OnClickPainting
{


    List<Paintings> paintingsList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    RecyclerView recyclerView;
    HomePaintingsAdapter hpa, se;
    String url;
    String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SharedPreferences sp2 = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
        URL = sp2.getString("mainurl", "");


        recyclerView = findViewById(R.id.recyclerSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        materialSearchBar = findViewById(R.id.matsearch);
        materialSearchBar.setHint("Enter Your Paintings Name or Artist Name");

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                {
                    //Restore Full List of Paintings
                    recyclerView.setAdapter(hpa);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text)
            {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


        String searchKey = materialSearchBar.getText().trim();
        url = String.format("/ShowPaintings.php?Search=", searchKey);
        url = URL + url;

        JSONrequest();


    }

    private void startSearch(CharSequence text)
    {
        List<Paintings> pl = new ArrayList<>();
        for (Paintings p: paintingsList)
        {

            if (p.getName().contains(text) || p.getpOwner().contains(text))
            {
                pl.add(p);
                se = new HomePaintingsAdapter(this, pl, this);
                recyclerView.setAdapter(se);
            }
        }
    }

    private void JSONrequest()
    {

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {

                JSONObject jsonObject = null;

                for (int i=0; i < response.length(); i++)
                {
                    try
                    {
                        jsonObject = response.getJSONObject(i);

                        String id = jsonObject.getString("painting_id");
                        String image = jsonObject.getString("painting_url");
                        String title = jsonObject.getString("painting_name");
                        String desc = jsonObject.getString("painting_description");
                        String owner = jsonObject.getString("painting_artist");

                        Paintings paintings = new Paintings(id, title, desc, owner, image);
                        paintingsList.add(paintings);

                        displayDrinksList(paintingsList);

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
            public void onErrorResponse(VolleyError error)
            {
            }
        });

        MySingleton.getInstance(SearchActivity.this).addToRequestQueue(request);
    }

    private void displayDrinksList(List<Paintings> paintingsList1)
    {
        paintingsList = paintingsList1;
        hpa = new HomePaintingsAdapter(this, paintingsList1,this);
        recyclerView.setAdapter(hpa);
    }


    @Override
    public void clickPainting(int pos)
    {
        SharedPreferences spSearchActivity = PreferenceManager.getDefaultSharedPreferences(SearchActivity.this);
        SharedPreferences.Editor editor = spSearchActivity.edit();
        editor.putString("painting_id", paintingsList.get(pos).getId());
        editor.putString("painting_name", paintingsList.get(pos).getName());
        editor.putString("painting_desc", paintingsList.get(pos).getDescription());
        editor.putString("painting_artist", paintingsList.get(pos).getpOwner());
        editor.apply();
        Intent searcActivity = new Intent(SearchActivity.this, PaintingsDetails.class);
        startActivity(searcActivity);
    }
}
