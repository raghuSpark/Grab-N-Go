package com.dream.grabngo.ProfileSubFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;
import com.dream.grabngo.SingletonClass;
import com.google.android.material.textfield.TextInputEditText;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileFragment extends Fragment {

    String customer_name = "", mobile_no = "";
    private View groupFragmentView;
    private CardView backButton;
    private TextInputEditText usernameEditText, mobileNumberEditText;
    private Button saveChangesButton;

    private IdentityToken identityToken;
    private JSONObject userDetails;
    private final FragmentManager supportFragmentManager;

    public EditProfileFragment(androidx.fragment.app.FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SingletonClass singleToneClass = com.dream.grabngo.SingletonClass.getInstance();
        identityToken = singleToneClass.getIdentityToken();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupFragmentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        backButton = groupFragmentView.findViewById(R.id.edit_profile_back_button);
        usernameEditText = groupFragmentView.findViewById(R.id.edit_profile_user_name_edit_text);
        mobileNumberEditText = groupFragmentView.findViewById(R.id.edit_profile_mobile_number_edit_text);
        saveChangesButton = groupFragmentView.findViewById(R.id.edit_profile_save_changes_button);

        userDetails = SharedPrefConfig.readUserDetails(requireContext());
        try {
            customer_name = userDetails.getString("CUSTOMER_NAME");
            mobile_no = userDetails.getString("PHONE_NO");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        usernameEditText.setText(customer_name);
        mobileNumberEditText.setText(mobile_no);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ProfileFragment(supportFragmentManager)).commit();
            }
        });

//        saveChangesButton.setOnClickListener(view -> {
//            usernameEditText.clearFocus();
//            mobileNumberEditText.clearFocus();
//            HideKeyBoard.hideKeyboardFromFragment(requireContext(), groupFragmentView);
//
//            if (!Objects.requireNonNull(usernameEditText.getText()).toString().trim().equals(customer_name) ||
//                    !Objects.requireNonNull(mobileNumberEditText.getText()).toString().equals(mobile_no)) {
//                updateUserDetails(userDetails);
//            } else {
//                Toast.makeText(requireContext(), "Nothing to save!", Toast.LENGTH_SHORT).show();
//            }
//        });
        return groupFragmentView;
    }

//    private void updateUserDetails(JSONObject userDetails) {
//        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
//
//        String URL = "http://192.168.43.54:3001/gng/v1/update-customer-details";
//        JSONObject postData = new JSONObject();
//        try {
//            postData.put("CUSTOMER_ID", userDetails.get("CUSTOMER_ID"));
//            postData.put("CUSTOMER_NAME", Objects.requireNonNull(usernameEditText.getText()).toString().trim());
//            postData.put("EMAIL_ID", userDetails.get("EMAIL_ID"));
//            postData.put("PHONE_NO", Objects.requireNonNull(mobileNumberEditText.getText()).toString().trim());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
//            Toast.makeText(requireContext(), "Changes saved successfully!", Toast.LENGTH_SHORT).show();
//            SharedPrefConfig.writeUserDetails(requireContext(), response);
//            try {
//                TextView username = requireActivity().findViewById(R.id.user_name_text_view);
//                username.setText(response.getString("CUSTOMER_NAME"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(requireContext(), "Unable to save the changes! Try again!", Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(jsonObjectRequest);
//    }
}