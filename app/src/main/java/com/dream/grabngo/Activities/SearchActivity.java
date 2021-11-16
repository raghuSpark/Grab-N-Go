package com.dream.grabngo.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.Adapters.SearchRecyclerViewAdapter;
import com.dream.grabngo.Adapters.ShoppingItemsRecyclerViewAdapter;
import com.dream.grabngo.CustomClasses.ShoppingItemDetails;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private int searchTag = 0; // 0 -> By City, 1 -> By Item
    private CardView backButton;
    private EditText searchView;
    private TextView byItemTag, byCityTag, searchResult;
    private RecyclerView searchRecyclerView;
    private ArrayList<ShoppingItemDetails> searchItemsArrayList = new ArrayList<>();
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private RequestQueue requestQueue;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        requestQueue = Volley.newRequestQueue(this);

        backButton = findViewById(R.id.search_back_button);
        searchView = findViewById(R.id.search_edit_text);
        byItemTag = findViewById(R.id.by_item_tag);
        byCityTag = findViewById(R.id.by_city_tag);
        searchRecyclerView = findViewById(R.id.search_recycler_view);
        searchResult = findViewById(R.id.search_result_text_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            searchView.setFocusable(View.FOCUSABLE_AUTO);
        }

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    searchResult.setVisibility(View.VISIBLE);
                    searchResult.setText("Searching...");
                    if(searchTag==1) {
                        filterByItem(s.toString().trim());
                    } else {
                        filterByCart(s.toString().trim());
                    }
                }
            }
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, MainActivity.class));
            finish();
        });

        byCityTag.setOnClickListener(v -> {
            searchTag = 0;
            byCityTag.setBackgroundColor(getResources().getColor(R.color.primary_blue));
            byCityTag.setTextColor(getResources().getColor(R.color.white));
            byItemTag.setBackgroundColor(getResources().getColor(R.color.white));
            byItemTag.setTextColor(getResources().getColor(R.color.primary_blue));
        });

        byItemTag.setOnClickListener(v -> {
            searchTag = 1;
            byItemTag.setBackgroundColor(getResources().getColor(R.color.primary_blue));
            byItemTag.setTextColor(getResources().getColor(R.color.white));
            byCityTag.setBackgroundColor(getResources().getColor(R.color.white));
            byCityTag.setTextColor(getResources().getColor(R.color.primary_blue));
        });

        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(getApplicationContext(), getSupportFragmentManager(), searchItemsArrayList);
        searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
        searchRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void filterByItem(String filter) {
//        String URL = "http://192.168.1.111:3001/gng/v1/search-by-item";
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
//            try {
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }, error -> {
//            Log.d("TAG", "onErrorResponse: " + error.getMessage());
//        });
//        requestQueue.add(jsonObjectRequest);
    }

    private void filterByCart(String filter) {

    }
}