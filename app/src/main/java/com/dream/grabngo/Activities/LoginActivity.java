package com.dream.grabngo.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.HideKeyBoard;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.dream.grabngo.utils.SingletonClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.AuthorizationException;
import com.ibm.cloud.appid.android.api.AuthorizationListener;
import com.ibm.cloud.appid.android.api.LoginWidget;
import com.ibm.cloud.appid.android.api.TokenResponseListener;
import com.ibm.cloud.appid.android.api.tokens.AccessToken;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;
import com.ibm.cloud.appid.android.api.tokens.RefreshToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    final String[] CUSTOMER_NAME = new String[1];
    final String[] PHONE_NO = new String[1];
    private Button loginButton;
    private TextInputEditText emailIDEditText, passwordEditText;
    private ProgressBar loginProgressBar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppID.getInstance().initialize(getApplicationContext(), TENANT_ID, REGION);
        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        TextView goToRegister = findViewById(R.id.go_to_register);
        TextView forgotPassword = findViewById(R.id.forgot_password);

        loginButton = findViewById(R.id.login_button);
        emailIDEditText = findViewById(R.id.email_id_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginProgressBar = findViewById(R.id.login_progress_bar);

        goToRegister.setOnClickListener(view1 -> {
            startActivity(new Intent(this, GetStartedActivity.class));
            finish();
        });

        loginButton.setOnClickListener(view -> {
            HideKeyBoard.hideKeyboardFromActivity(LoginActivity.this);
            loginProgressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            String email = Objects.requireNonNull(emailIDEditText.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "EmailID or Password can't be empty!", Toast.LENGTH_SHORT).show();
                loginProgressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            } else {
                AppID.getInstance().signinWithResourceOwnerPassword(getApplicationContext(), email, password, new TokenResponseListener() {
                    @Override
                    public void onAuthorizationFailure(AuthorizationException exception) {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            loginProgressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                        });
                    }

                    @Override
                    public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
                        runOnUiThread(() -> {
                            SharedPrefConfig.writeRefreshTokenRaw(getApplicationContext(), refreshToken.getRaw());

                            SingletonClass singleToneClass = SingletonClass.getInstance();
                            singleToneClass.setIdentityToken(identityToken);

                            String customer_id = "";
                            try {
                                customer_id = identityToken.getIdentities().getJSONObject(0).getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // Check if details were in the database or not
                            if (!SharedPrefConfig.readAreDetailsGiven(getApplicationContext())) {
                                checkForTheUser(customer_id);
                            } else {
                                loginProgressBar.setVisibility(View.GONE);
                                loginButton.setVisibility(View.VISIBLE);
                                loginButton.setActivated(false);
                                SharedPrefConfig.writeIsLoggedIn(getApplicationContext(), true);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
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
                    Toast.makeText(getApplicationContext(), "Password change failed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthorizationCanceled() {
                    Toast.makeText(getApplicationContext(), "Password change cancelled!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
                    // Forgot password finished, in this case accessToken and identityToken will be null.
                    Toast.makeText(getApplicationContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void checkForTheUser(String customerId) {
        String URL = "http://192.168.1.111:3001/gng/v1/get-customer-details";
        JSONObject postData = new JSONObject();
        try {
            postData.put("CUSTOMER_ID", customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "checkForTheUser: ");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
            try {
                loginProgressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                loginButton.setActivated(false);
                if (response.getString("STATUS").equals("FAILURE")) {
                    showBottomSheetDialog(customerId);
                } else {
                    SharedPrefConfig.writeUserDetails(this, response);
                    SharedPrefConfig.writeIsLoggedIn(getApplicationContext(), true);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            loginProgressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            Log.d("TAG", "onErrorResponse: " + error.getMessage());
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void addNewCustomer(String customerId, String customer_name, String email, String phone_number) {
        Bitmap bitmap = getBitmap(getApplicationContext(), R.drawable.ic_profile_active);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        String URL = "http://192.168.1.111:3001/gng/v1/create-new-customer";
        JSONObject postData = new JSONObject();
        try {
            postData.put("CUSTOMER_ID", customerId);
            postData.put("CUSTOMER_NAME", customer_name);
            postData.put("CUSTOMER_IMAGE",encoded);
            postData.put("EMAIL_ID", email);
            postData.put("PHONE_NO", phone_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

            SharedPrefConfig.writeUserDetails(this, response);
            SharedPrefConfig.writeIsLoggedIn(getApplicationContext(), true);
            SharedPrefConfig.writeAreDetailsGiven(getApplicationContext(), true);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, error -> {
            Toast.makeText(LoginActivity.this, "Login failed! Try again!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, GetStartedActivity.class);
            startActivity(intent);
            finish();
        });
        requestQueue.add(jsonObjectRequest);
    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        Log.d("TAG", "getBitmap: 1");
        return bitmap;
    }

    private static Bitmap getBitmap(Context context, int drawableId) {
        Log.d("TAG", "getBitmap: 2");
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    private void showBottomSheetDialog(String customerId) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        TextView mobileNumberPreview = dialog.findViewById(R.id.mobile_number_preview);
        EditText usernameEditText = dialog.findViewById(R.id.dialog_username_edit_text),
                mobileNumberEditText = dialog.findViewById(R.id.dialog_mobile_number_edit_text),
                otpEditText = dialog.findViewById(R.id.otp_edit_text);
        ProgressBar getOtpProgressBar = dialog.findViewById(R.id.get_otp_progress_bar),
                verifyOtpProgressBar = dialog.findViewById(R.id.verify_otp_progress_bar);
        LinearLayout userNameLL = dialog.findViewById(R.id.user_name_linear_layout),
                getOtpLL = dialog.findViewById(R.id.get_otp_linear_layout),
                verifyOtpLL = dialog.findViewById(R.id.verify_otp_linear_layout);

        final String[] VERIFICATION_ID = new String[1];

        Button userNameSaveButton = dialog.findViewById(R.id.user_name_save_button);
        userNameSaveButton.setOnClickListener(view -> {
            if (usernameEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Username can't be empty", Toast.LENGTH_SHORT).show();
            } else {
                CUSTOMER_NAME[0] = usernameEditText.getText().toString().trim();
                userNameLL.setVisibility(View.GONE);
                getOtpLL.setVisibility(View.VISIBLE);
            }
        });

        Button getOtpButton = dialog.findViewById(R.id.get_otp_button);
        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOtpButton.setVisibility(View.INVISIBLE);
                getOtpProgressBar.setVisibility(View.VISIBLE);

                PHONE_NO[0] = mobileNumberEditText.getText().toString();

                if (isValid(PHONE_NO[0])) {
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber("+91" + PHONE_NO[0])
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(LoginActivity.this)
                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            getOtpProgressBar.setVisibility(View.INVISIBLE);
                                            getOtpButton.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            getOtpProgressBar.setVisibility(View.INVISIBLE);
                                            getOtpButton.setVisibility(View.VISIBLE);
                                        }

                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            super.onCodeSent(verificationId, forceResendingToken);
                                            getOtpProgressBar.setVisibility(View.INVISIBLE);
                                            getOtpButton.setVisibility(View.VISIBLE);

                                            VERIFICATION_ID[0] = verificationId;

                                            getOtpLL.setVisibility(View.GONE);
                                            verifyOtpLL.setVisibility(View.VISIBLE);
                                            mobileNumberPreview.setText("+91-" + PHONE_NO[0]);
                                        }
                                    })
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                } else {
                    Toast.makeText(LoginActivity.this, "Please check the number you entered!", Toast.LENGTH_SHORT).show();
                    getOtpProgressBar.setVisibility(View.INVISIBLE);
                    getOtpButton.setVisibility(View.VISIBLE);
                }
            }

            private boolean isValid(String number) {
                if (number.length() < 10) return false;
                for (int i = 0; i < 10; i++) {
                    if (number.charAt(i) < '0' || number.charAt(i) > '9') return false;
                }
                return true;
            }
        });

        Button verifyOtpButton = dialog.findViewById(R.id.verify_otp_button);
        verifyOtpButton.setOnClickListener(view -> {
            verifyOtpButton.setVisibility(View.GONE);
            verifyOtpProgressBar.setVisibility(View.VISIBLE);

            String code = otpEditText.getText().toString().trim();
            if (code.length() < 6) {
                verifyOtpButton.setVisibility(View.VISIBLE);
                verifyOtpProgressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Please enter a valid code!", Toast.LENGTH_SHORT).show();
            } else {
                if (VERIFICATION_ID[0] != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VERIFICATION_ID[0], code);
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(task -> {
                                verifyOtpButton.setVisibility(View.VISIBLE);
                                verifyOtpProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                    // Add the customer to the customer table
                                    if (CUSTOMER_NAME[0] != null && PHONE_NO[0] != null) {
                                        SingletonClass singleToneClass = SingletonClass.getInstance();
                                        IdentityToken identityToken = singleToneClass.getIdentityToken();
                                        addNewCustomer(customerId, CUSTOMER_NAME[0], identityToken.getEmail(), PHONE_NO[0]);
                                    }
                                } else {
                                    Toast.makeText(this, "Incorrect OTP!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(this, "You must provide a valid mobile number to use the application!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        mobileNumberPreview.setOnClickListener(view -> {
            verifyOtpLL.setVisibility(View.GONE);
            getOtpLL.setVisibility(View.VISIBLE);
        });

        dialog.findViewById(R.id.resend_otp_text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView mobileNumberPreview = dialog.findViewById(R.id.mobile_number_preview);

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+91" + mobileNumberPreview.getText().toString().substring(4))
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(LoginActivity.this)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(newVerificationId, forceResendingToken);
                                        VERIFICATION_ID[0] = newVerificationId;
                                        Toast.makeText(LoginActivity.this, "OTP sent!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}