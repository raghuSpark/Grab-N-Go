package com.dream.grabngo.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dream.grabngo.MainFragments.CartFragment;
import com.dream.grabngo.MainFragments.HomeFragment;
import com.dream.grabngo.MainFragments.PayFragment;
import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        getSupportFragmentManager().beginTransaction().add(R.id.main_fragments_container, new HomeFragment()).commit();
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
                fragment = new HomeFragment();
                break;
            case R.id.nav_cart:
                fragment = new CartFragment();
                break;
            case R.id.nav_pay:
                fragment = new PayFragment();
                break;
            case R.id.nav_profile:
                fragment = new ProfileFragment(getSupportFragmentManager());
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments_container, Objects.requireNonNull(fragment)).commit();
    }
}