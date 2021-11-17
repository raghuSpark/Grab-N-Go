package com.dream.grabngo.ProfileSubFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.CustomClasses.UserDetails;
import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.dream.grabngo.utils.SingletonClass;
import com.dream.grabngo.utils.VolleyMultipartRequest;
import com.dream.grabngo.utils.VolleySingleton;
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
import com.ibm.cloud.appid.android.api.tokens.AccessToken;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;
import com.ibm.cloud.appid.android.api.tokens.RefreshToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class EditProfileFragment extends Fragment {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;
    private static final String ROOT_URL = "https://grabngo-api.herokuapp.com/gng/v1/send-image";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    private final FragmentManager supportFragmentManager;
    private final Context context;
    String customer_name = "", mobile_no = "";
    private View groupFragmentView;
    private ImageView editProfileImage;
    private CardView backButton, changeProfileImageButton;
    private TextView usernameEditButton, mobileNumberEditButton, passwordEditButton, emailIdTextView;
    private IdentityToken identityToken;
    private UserDetails userDetails;
    private RequestQueue requestQueue;
    private String filePath;

    public EditProfileFragment(Context context, FragmentManager supportFragmentManager) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingletonClass singleToneClass = SingletonClass.getInstance();
        identityToken = singleToneClass.getIdentityToken();

        AppID.getInstance().initialize(requireContext(), TENANT_ID, REGION);
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        groupFragmentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        backButton = groupFragmentView.findViewById(R.id.edit_profile_back_button);
        emailIdTextView = groupFragmentView.findViewById(R.id.edit_profile_email_id_text_view);
        usernameEditButton = groupFragmentView.findViewById(R.id.edit_profile_change_user_name_button);
        mobileNumberEditButton = groupFragmentView.findViewById(R.id.edit_profile_change_mobile_number_button);
        passwordEditButton = groupFragmentView.findViewById(R.id.edit_profile_change_password_button);
        editProfileImage = groupFragmentView.findViewById(R.id.edit_profile_image);
        changeProfileImageButton = groupFragmentView.findViewById(R.id.change_profile_image_button);

        userDetails = SharedPrefConfig.readUserDetails(requireContext());

        customer_name = userDetails.getCustomerName();
        mobile_no = userDetails.getPhoneNo();

        usernameEditButton.setText(customer_name);
        mobileNumberEditButton.setText(mobile_no);
        emailIdTextView.setText(identityToken.getEmail());

        changeProfileImageButton.setOnClickListener(v -> {
            if ((ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                if ((ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE))) {

                } else {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS);
                }
            } else {
                showFileChooser();
            }
        });

        passwordEditButton.setOnClickListener(v -> {
            LoginWidget loginWidget = AppID.getInstance().getLoginWidget();
            loginWidget.launchForgotPassword(requireActivity(), new AuthorizationListener() {
                @Override
                public void onAuthorizationFailure(AuthorizationException exception) {
                    Toast.makeText(getContext(), "Password change failed!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthorizationCanceled() {
                    Toast.makeText(getContext(), "Password change cancelled!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthorizationSuccess(AccessToken accessToken, IdentityToken identityToken, RefreshToken refreshToken) {
                    // Forgot password finished, in this case accessToken and identityToken will be null.
                    Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        backButton.setOnClickListener(v -> supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ProfileFragment(context, supportFragmentManager)).commit());

        usernameEditButton.setOnClickListener(v -> showEditUserNameBottomSheetDialog(userDetails.getCustomerName()));

        mobileNumberEditButton.setOnClickListener(v -> showEditMobileNumberBottomSheetDialog(mobile_no));
        return groupFragmentView;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            if (filePath != null) {
                try {
                    Log.d("filePath", filePath);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    uploadBitmap(bitmap);
                    editProfileImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(
                        requireContext(), "no image selected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(new String(response.data));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("GotError", "" + error.getMessage())) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imageName = System.currentTimeMillis();
                params.put("image", new DataPart(imageName + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        //adding the request to volley
        VolleySingleton.getInstance(context).addToRequestQueue(volleyMultipartRequest);
    }

    private void showEditUserNameBottomSheetDialog(String userName) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_user_name_bottom_sheet_layout);

        TextInputEditText user_name_edit_text = dialog.findViewById(R.id.bottom_sheet_user_name_edit_text);
        Button saveButton = dialog.findViewById(R.id.bottom_sheet_user_name_save_button),
                cancelButton = dialog.findViewById(R.id.bottom_sheet_user_name_cancel_button);
        user_name_edit_text.setText(userName);

        cancelButton.setOnClickListener(v -> dialog.cancel());
        saveButton.setOnClickListener(v -> {
            saveButton.setClickable(false);
            updateUserDetails(dialog, saveButton, userName, userDetails.getPhoneNo());
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showEditMobileNumberBottomSheetDialog(String mobile_no) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        TextView mobileNumberPreview = dialog.findViewById(R.id.mobile_number_preview);
        EditText mobileNumberEditText = dialog.findViewById(R.id.dialog_mobile_number_edit_text),
                otpEditText = dialog.findViewById(R.id.otp_edit_text);
        ProgressBar getOtpProgressBar = dialog.findViewById(R.id.get_otp_progress_bar),
                verifyOtpProgressBar = dialog.findViewById(R.id.verify_otp_progress_bar);
        LinearLayout userNameLL = dialog.findViewById(R.id.user_name_linear_layout),
                getOtpLL = dialog.findViewById(R.id.get_otp_linear_layout),
                verifyOtpLL = dialog.findViewById(R.id.verify_otp_linear_layout);

        mobileNumberEditText.setText(String.valueOf(mobile_no));

        final String[] VERIFICATION_ID = new String[1];

        userNameLL.setVisibility(View.GONE);
        getOtpLL.setVisibility(View.VISIBLE);

        Button getOtpButton = dialog.findViewById(R.id.get_otp_button);
        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOtpButton.setVisibility(View.INVISIBLE);
                getOtpProgressBar.setVisibility(View.VISIBLE);

                String PHONE_NO = mobileNumberEditText.getText().toString();

                if (isValid(PHONE_NO)) {
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber("+91" + PHONE_NO)
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(requireActivity())
                                    .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                            getOtpProgressBar.setVisibility(View.INVISIBLE);
                                            getOtpButton.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            mobileNumberPreview.setText("+91-" + PHONE_NO);
                                        }
                                    })
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);

                } else {
                    Toast.makeText(requireContext(), "Please check the number you entered!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(requireContext(), "Please enter a valid code!", Toast.LENGTH_SHORT).show();
            } else {
                if (VERIFICATION_ID[0] != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VERIFICATION_ID[0], code);
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Verification successful!", Toast.LENGTH_SHORT).show();
                                    // update the customer to the customer table
                                    updateUserDetails(dialog, null, customer_name, mobileNumberEditText.getText().toString());
                                } else {
                                    verifyOtpButton.setVisibility(View.VISIBLE);
                                    verifyOtpProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(requireContext(), "Incorrect OTP!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(requireContext(), "You must provide a valid mobile number to use the application!", Toast.LENGTH_SHORT).show();
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
                                .setActivity(requireActivity())
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onCodeSent(@NonNull String newVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        super.onCodeSent(newVerificationId, forceResendingToken);
                                        VERIFICATION_ID[0] = newVerificationId;
                                        Toast.makeText(requireContext(), "OTP sent!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void updateUserDetails(Dialog dialog, Button saveButton, String userName, String mobileNumber) {
        String URL = "https://grabngo-api.herokuapp.com/gng/v1/update-customer-details";
        JSONObject postData = new JSONObject();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            userDetails.getProfileImage().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            postData.put("CUSTOMER_ID", userDetails.getCustomerId());
            postData.put("CUSTOMER_NAME", userName);
            postData.put("CUSTOMER_IMAGE", encoded);
            postData.put("EMAIL_ID", userDetails.getEmailId());
            postData.put("PHONE_NO", mobileNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
            Toast.makeText(requireContext(), "Changes saved successfully!", Toast.LENGTH_SHORT).show();
            try {
                byte[] decodedString = Base64.decode(response.getString("CUSTOMER_IMAGE"), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                SharedPrefConfig.writeUserDetails(requireContext(), new UserDetails(
                                response.getString("CUSTOMER_ID"),
                                response.getString("CUSTOMER_NAME"),
                                response.getString("EMAIL_ID"),
                                response.getString("PHONE_NO"),
                                decodedByte
                        )
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            usernameEditButton.setText(userName);
            mobileNumberEditButton.setText(mobileNumber);
            customer_name = userName;
            mobile_no = mobileNumber;
            dialog.cancel();
        }, error -> {
            Toast.makeText(requireContext(), "Unable to save the changes! Try again!", Toast.LENGTH_SHORT).show();
            if (saveButton != null) saveButton.setClickable(true);
        });
        requestQueue.add(jsonObjectRequest);
    }
}