package com.dream.grabngo.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.MainFragments.CartFragment;
import com.dream.grabngo.MainFragments.HomeFragment;
import com.dream.grabngo.MainFragments.PayFragment;
import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragments_container, new HomeFragment(getSupportFragmentManager(), 0)).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            HandleFragments(item.getItemId());
            return true;
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void HandleFragments(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment(getSupportFragmentManager(), 0);
                break;
            case R.id.nav_cart:
                fragment = new CartFragment(getSupportFragmentManager());
                break;
            case R.id.nav_pay:
                fragment = new PayFragment(getApplicationContext(), getSupportFragmentManager());
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment(getApplicationContext(), getSupportFragmentManager());
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments_container, Objects.requireNonNull(fragment)).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            updateUserDetails(uri);
        }
    }

    private void updateUserDetails(Uri imageUri) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject userDetails = SharedPrefConfig.readUserDetails(getApplicationContext());
        String URL = "http://192.168.1.111:3001/gng/v1/update-customer-details";
        JSONObject postData = new JSONObject();
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            String encodedImage = encodeImage(selectedImage);

            postData.put("CUSTOMER_ID", userDetails.get("CUSTOMER_ID"));
            postData.put("CUSTOMER_NAME", userDetails.get("CUSTOMER_NAME"));
            postData.put("CUSTOMER_IMAGE", encodedImage);
            postData.put("EMAIL_ID", userDetails.get("EMAIL_ID"));
            postData.put("PHONE_NO", userDetails.get("PHONE_NO"));
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
            Toast.makeText(getApplicationContext(), "Changes saved successfully!", Toast.LENGTH_SHORT).show();
            SharedPrefConfig.writeUserDetails(getApplicationContext(), response);
        }, error -> {
            Toast.makeText(getApplicationContext(), "Unable to save the changes! Try again!", Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(jsonObjectRequest);
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        return  Base64.encodeToString(b, Base64.DEFAULT);
    }
}