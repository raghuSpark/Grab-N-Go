package com.dream.grabngo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;
import com.dream.grabngo.SingletonClass;
import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.AuthorizationException;
import com.ibm.cloud.appid.android.api.AuthorizationListener;
import com.ibm.cloud.appid.android.api.LoginWidget;
import com.ibm.cloud.appid.android.api.TokenResponseListener;
import com.ibm.cloud.appid.android.api.tokens.AccessToken;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;
import com.ibm.cloud.appid.android.api.tokens.RefreshToken;

public class LoginActivity extends AppCompatActivity {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    private TextView goToRegister, forgotPassword;
    private Button loginButton;
    private EditText emailIDEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppID.getInstance().initialize(getApplicationContext(), TENANT_ID, REGION);

        goToRegister = findViewById(R.id.goToRegister);
        forgotPassword = findViewById(R.id.forgotPassword);
        loginButton = findViewById(R.id.loginButton);
        emailIDEditText = findViewById(R.id.emailId);
        passwordEditText = findViewById(R.id.password);

        goToRegister.setOnClickListener(view1 -> {
            startActivity(new Intent(this, GetStartedActivity.class));
            finish();
        });

        loginButton.setOnClickListener(view -> {
            String email = emailIDEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "EmailID or Password can't be empty!", Toast.LENGTH_SHORT).show();
            } else {
                AppID.getInstance().signinWithResourceOwnerPassword(getApplicationContext(), email, password, new TokenResponseListener() {
                    @Override
                    public void onAuthorizationFailure(AuthorizationException exception) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();

                            SharedPrefConfig.writeIsLoggedIn(getApplicationContext(), true);
                            SharedPrefConfig.writeRefreshTokenRaw(getApplicationContext(), refreshToken.getRaw());

                            SingletonClass singleToneClass = com.dream.grabngo.SingletonClass.getInstance();
                            singleToneClass.setIdentityToken(identityToken);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }
                });
            }
        });

        forgotPassword.setOnClickListener(v -> {
            LoginWidget loginWidget = AppID.getInstance().getLoginWidget();
            loginWidget.launchForgotPassword(this, new AuthorizationListener() {
                @Override
                public void onAuthorizationFailure(AuthorizationException exception) {
                    //Exception occurred
                    Toast.makeText(getApplicationContext(), "Password change failed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthorizationCanceled() {
                    Toast.makeText(getApplicationContext(), "Password change failed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
                    // Forgot password finished, in this case accessToken and identityToken will be null.
                    Toast.makeText(getApplicationContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}