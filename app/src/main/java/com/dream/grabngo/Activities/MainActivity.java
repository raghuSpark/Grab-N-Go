package com.dream.grabngo.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dream.grabngo.MainFragments.CartFragment;
import com.dream.grabngo.MainFragments.HomeFragment;
import com.dream.grabngo.MainFragments.ItemExpandedFragment;
import com.dream.grabngo.MainFragments.PayFragment;
import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.LocationService;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        if (getIntent().getBooleanExtra("IS_FROM_SEARCH", false)) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragments_container, new ItemExpandedFragment(getSupportFragmentManager(), SharedPrefConfig.readSearchExpandItemDetails(this), null)).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.main_fragments_container, new HomeFragment(getSupportFragmentManager())).commit();
        }
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
                fragment = new HomeFragment(getSupportFragmentManager());
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction("startLocationService");
            getApplicationContext().startService(intent);
            Toast.makeText(getApplicationContext(), "Location service started!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(serviceInfo.service.getClassName())) {
                    if (serviceInfo.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
}