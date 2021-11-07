package com.dream.grabngo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;
import com.dream.grabngo.SingletonClass;
import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.AuthorizationException;
import com.ibm.cloud.appid.android.api.AuthorizationListener;
import com.ibm.cloud.appid.android.api.LoginWidget;
import com.ibm.cloud.appid.android.api.tokens.AccessToken;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;
import com.ibm.cloud.appid.android.api.tokens.RefreshToken;

public class GetStartedActivity extends AppCompatActivity {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    private Button registerButton, goToLoginPageButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        AppID.getInstance().initialize(getApplicationContext(), TENANT_ID, REGION);

        registerButton = findViewById(R.id.register_button);
        goToLoginPageButton = findViewById(R.id.go_to_login_page_button);
        progressBar = findViewById(R.id.get_started_progress_bar);

        registerButton.setOnClickListener(view -> {
            registerButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            goToLoginPageButton.setClickable(false);

            LoginWidget loginWidget = AppID.getInstance().getLoginWidget();
            loginWidget.launchSignUp(this, new AuthorizationListener() {
                @Override
                public void onAuthorizationCanceled() {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        registerButton.setVisibility(View.VISIBLE);
                        goToLoginPageButton.setClickable(true);
                        Toast.makeText(getApplicationContext(), "Authorization Cancelled!", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onAuthorizationFailure(AuthorizationException exception) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        registerButton.setVisibility(View.VISIBLE);
                        goToLoginPageButton.setClickable(true);
                        Toast.makeText(getApplicationContext(), "Authorization Failed!", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
                    runOnUiThread(() -> {
                        if (accessToken != null && identityToken != null) {
                            SharedPrefConfig.writeIsLoggedIn(getApplicationContext(), true);
                            SharedPrefConfig.writeRefreshTokenRaw(getApplicationContext(), refreshToken.getRaw());

                            SingletonClass singleToneClass = com.dream.grabngo.SingletonClass.getInstance();
                            singleToneClass.setIdentityToken(identityToken);

                            Toast.makeText(getApplicationContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(GetStartedActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please verify your Email-ID!", Toast.LENGTH_SHORT).show();
                            // TODO: Verify your emailID
                            progressBar.setVisibility(View.GONE);
                            registerButton.setVisibility(View.VISIBLE);
                            goToLoginPageButton.setClickable(true);
                        }
                    });
                }
            });
        });

        goToLoginPageButton.setOnClickListener(view -> {
            startActivity(new Intent(GetStartedActivity.this, LoginActivity.class));
            finish();
        });
    }
}