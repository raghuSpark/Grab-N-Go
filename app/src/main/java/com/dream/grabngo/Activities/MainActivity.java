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
import com.ibm.cloud.appid.android.api.AppID;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    private BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AppID.getInstance().initialize(getApplicationContext(), TENANT_ID, REGION);
//        AppID.getInstance().signinWithRefreshToken(getApplicationContext(), SharedPrefConfig.readRefreshTokenRaw(getApplicationContext()), new AuthorizationListener() {
//            @Override
//            public void onAuthorizationCanceled() {
//                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Authorization Cancelled!", Toast.LENGTH_SHORT).show());
//            }
//
//            @Override
//            public void onAuthorizationFailure(AuthorizationException exception) {
//                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Authorization Failed!", Toast.LENGTH_SHORT).show());
//            }
//
//            @Override
//            public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
//                runOnUiThread(() -> {
//                    SharedPrefConfig.writeRefreshTokenRaw(getApplicationContext(), refreshToken.getRaw());
//                    idToken = identityToken;
//                });
//            }
//        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments_container, new HomeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
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
                    fragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments_container, Objects.requireNonNull(fragment)).commit();
            return true;
        });
    }
}