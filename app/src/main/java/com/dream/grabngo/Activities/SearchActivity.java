package com.dream.grabngo.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.Adapters.SearchRecyclerViewAdapter;
import com.dream.grabngo.CustomClasses.ShoppingItemDetails;
import com.dream.grabngo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private ArrayList<ShoppingItemDetails> searchItemsArrayList = new ArrayList<>();
    protected Double latitude, longitude;
    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    };
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    private int searchTag = 0; // 0 -> By City, 1 -> By Item
    private CardView backButton;
    private EditText searchView;
    private TextView byItemTag, byCityTag, searchResult;
    private RecyclerView searchRecyclerView;
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private RequestQueue requestQueue;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        requestQueue = Volley.newRequestQueue(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        backButton = findViewById(R.id.search_back_button);
        searchView = findViewById(R.id.search_edit_text);
        byItemTag = findViewById(R.id.by_item_tag);
        byCityTag = findViewById(R.id.by_city_tag);
        searchRecyclerView = findViewById(R.id.search_recycler_view);
        searchResult = findViewById(R.id.search_result_text_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            searchView.setFocusable(View.FOCUSABLE_AUTO);
        }

        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchResult.setText("Searching...");
                searchResult.setVisibility(View.VISIBLE);
                if (searchTag == 0) filterByItem(searchView.getText().toString().trim());
                else filterByCity();
                return true;
            }
            return false;
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, MainActivity.class));
            finish();
        });

        byCityTag.setOnClickListener(v -> {
            if (searchTag == 1) return;
            searchTag = 1;
            searchView.setText("");
            byCityTag.setBackgroundColor(getResources().getColor(R.color.primary_blue));
            byCityTag.setTextColor(getResources().getColor(R.color.white));
            byItemTag.setBackgroundColor(getResources().getColor(R.color.white));
            byItemTag.setTextColor(getResources().getColor(R.color.primary_blue));
        });

        byItemTag.setOnClickListener(v -> {
            if (searchTag == 0) return;
            searchView.setText("");
            searchTag = 0;
            byItemTag.setBackgroundColor(getResources().getColor(R.color.primary_blue));
            byItemTag.setTextColor(getResources().getColor(R.color.white));
            byCityTag.setBackgroundColor(getResources().getColor(R.color.white));
            byCityTag.setTextColor(getResources().getColor(R.color.primary_blue));
        });

        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(SearchActivity.this,getApplicationContext(), getSupportFragmentManager(), searchItemsArrayList);
        searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
        searchRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    private void filterByItem(String filter) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getAddressLine(0);
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);

        String URL = "http://192.168.1.111:3001/gng/v1/search-by-item";
        JSONArray postDataArray = new JSONArray();
        JSONObject postData = new JSONObject();
        try {
            postData.put("item", filter);
            postData.put("city", cityName);
            postDataArray.put(postData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL, postDataArray, response -> {
            searchItemsArrayList.clear();
            for (int i=0;i<response.length();i++) {
                try {
                    JSONObject item = response.getJSONObject(i);
                    searchItemsArrayList.add(new ShoppingItemDetails(
                            item.getString("SHOP_ID"),
                            item.getString("SHOP_NAME"),
                            item.getString("CITY"),
                            item.getString("ITEM_NAME"),
                            item.getInt("ITEM_QUANTITY"),
                            item.getInt("SHOP_RATING"),
                            item.getDouble("ITEM_PRICE")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (response.length()==0) searchResult.setText("No results found!");
            else searchResult.setVisibility(View.GONE);
            searchRecyclerViewAdapter.notifyDataSetChanged();
        }, error -> {
            searchResult.setText("No results found!");
            searchResult.setVisibility(View.VISIBLE);
        });
        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterByCity() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getAddressLine(0);
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);

        String URL = "http://192.168.1.111:3001/gng/v1/search-by-city";
        JSONArray postDataArray = new JSONArray();
        JSONObject postData = new JSONObject();
        try {
            if (searchView.getText().toString().trim().isEmpty())
                postData.put("city", cityName);
            else postData.put("city", searchView.getText().toString().trim());
            postDataArray.put(postData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL, postDataArray, response -> {
            searchItemsArrayList.clear();
            for (int i=0;i<response.length();i++) {
                try {
                    JSONObject item = response.getJSONObject(i);
                    searchItemsArrayList.add(new ShoppingItemDetails(
                            item.getString("SHOP_ID"),
                            item.getString("SHOP_NAME"),
                            item.getString("CITY"),
                            item.getString("ITEM_NAME"),
                            item.getInt("ITEM_QUANTITY"),
                            item.getInt("SHOP_RATING"),
                            item.getDouble("ITEM_PRICE")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (response.length()==0) searchResult.setText("No results found!");
            else searchResult.setVisibility(View.GONE);
            searchRecyclerViewAdapter.notifyDataSetChanged();
        }, error -> {
            searchResult.setText("No results found!");
            searchResult.setVisibility(View.VISIBLE);
            Log.d("TAG", "onErrorResponse: " + error.getMessage());
        });
        requestQueue.add(jsonArrayRequest);
    }
}