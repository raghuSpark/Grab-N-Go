package com.dream.grabngo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dream.grabngo.R;

public class SearchActivity extends AppCompatActivity {

    private int searchTag = 0; // 0 -> By City, 1 -> By Item
    private CardView backButton;
    private SearchView searchView;
    private TextView byItemTag, byCityTag;
    private RecyclerView byItemRecyclerView, byCityRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        backButton = findViewById(R.id.search_back_button);
        searchView = findViewById(R.id.search_view_widget);
        byItemTag = findViewById(R.id.by_item_tag);
        byCityTag = findViewById(R.id.by_city_tag);
        byItemRecyclerView = findViewById(R.id.search_by_item_recycler_view);
        byCityRecyclerView = findViewById(R.id.search_by_city_recycler_view);

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, MainActivity.class));
            finish();
        });

        byCityTag.setOnClickListener(v -> {
            searchTag = 0;
            byCityTag.setBackgroundColor(getResources().getColor(R.color.primary_blue));
            byCityTag.setTextColor(getResources().getColor(R.color.white));
            byCityRecyclerView.setVisibility(View.VISIBLE);
            byItemTag.setBackgroundColor(getResources().getColor(R.color.white));
            byItemTag.setTextColor(getResources().getColor(R.color.primary_blue));
            byItemRecyclerView.setVisibility(View.GONE);
        });

        byItemTag.setOnClickListener(v -> {
            searchTag = 1;
            byItemTag.setBackgroundColor(getResources().getColor(R.color.primary_blue));
            byItemTag.setTextColor(getResources().getColor(R.color.white));
            byItemRecyclerView.setVisibility(View.VISIBLE);
            byCityTag.setBackgroundColor(getResources().getColor(R.color.white));
            byCityTag.setTextColor(getResources().getColor(R.color.primary_blue));
            byCityRecyclerView.setVisibility(View.GONE);
        });
    }
}