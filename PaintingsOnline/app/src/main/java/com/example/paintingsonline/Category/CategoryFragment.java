package com.example.paintingsonline.Category;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.paintingsonline.Model.Category;
import com.example.paintingsonline.R;
import com.example.paintingsonline.Utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryFragment extends Fragment
{
    final ArrayList<Category> categories = new ArrayList<>();
    private String URL = "https://jrnan.info/Painting/ShowCategory.php";
    private ListView listView;
    private CategoryListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        JSONrequest(view);

        listView = view.findViewById(R.id.category_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("url","https://jrnan.info/Painting/ShowPaintings.php?Category=");
                editor.putInt("Selec", i);
                editor.apply();
                //sp.edit().commit();
                Intent paintingActivity = new Intent(getActivity(), PaintingActivity.class);
                startActivity(paintingActivity);
            }
        });

        return view;

    }

    private void JSONrequest(final View view)
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
                        String image = jsonObject.getString("URL");

                        Category category = new Category(id, title, image);
                        categories.add(category);
                        ListView listView = view.findViewById(R.id.category_list);
                        adapter = new CategoryListAdapter(getActivity(), R.layout.category_adapter_view, categories);
                        listView.setAdapter(adapter);
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

        MySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
