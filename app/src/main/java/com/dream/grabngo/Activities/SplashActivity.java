package com.dream.grabngo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.dream.grabngo.utils.SingletonClass;
import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.AuthorizationException;
import com.ibm.cloud.appid.android.api.AuthorizationListener;
import com.ibm.cloud.appid.android.api.tokens.AccessToken;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;
import com.ibm.cloud.appid.android.api.tokens.RefreshToken;

public class SplashActivity extends AppCompatActivity {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppID.getInstance().initialize(getApplicationContext(), TENANT_ID, REGION);

        if (SharedPrefConfig.readIsLoggedIn(this)) {
            AppID.getInstance().signinWithRefreshToken(getApplicationContext(), SharedPrefConfig.readRefreshTokenRaw(getApplicationContext()), new AuthorizationListener() {
                @Override
                public void onAuthorizationCanceled() {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Authorization Cancelled!", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    });
                }

                @Override
                public void onAuthorizationFailure(AuthorizationException exception) {
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Authorization Failed!", Toast.LENGTH_SHORT).show();
                        goToMainActivity();
                    });
                }

                @Override
                public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
                    runOnUiThread(() -> {
                        SharedPrefConfig.writeRefreshTokenRaw(getApplicationContext(), refreshToken.getRaw());
                        SingletonClass singleToneClass = SingletonClass.getInstance();
                        singleToneClass.setIdentityToken(identityToken);

                        goToMainActivity();
                    });
                }
            });
        } else {
            Intent intent = new Intent(SplashActivity.this, GetStartedActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra("IS_FROM_SEARCH",false);
        startActivity(intent);
        finish();
    }
}