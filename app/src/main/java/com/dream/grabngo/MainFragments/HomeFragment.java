package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.dream.grabngo.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private View groupFragmentView;
    private ImageView routesButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        routesButton = groupFragmentView.findViewById(R.id.home_map_button);

        routesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });
        return groupFragmentView;
    }

    private void showBottomSheetDialog() {
        Dialog dialog = new Dialog(requireContext());
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
                Toast.makeText(requireContext(), "Username can't be empty", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: DB2 code to written
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

                String phoneNumber = mobileNumberEditText.getText().toString();

                if (isValid(phoneNumber)) {
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber("+91" + phoneNumber)
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
                                            mobileNumberPreview.setText("+91-" + phoneNumber);
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
                                verifyOtpButton.setVisibility(View.VISIBLE);
                                verifyOtpProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Verification successful!", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                } else {
                                    Toast.makeText(requireContext(), "Incorrect OTP!", Toast.LENGTH_SHORT).show();
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
}