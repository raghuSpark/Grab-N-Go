package com.dream.grabngo.ProfileChildFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.dream.grabngo.R;
import com.dream.grabngo.SingletonClass;
import com.google.android.material.textfield.TextInputEditText;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;

public class EditProfileFragment extends Fragment {

    private View groupFragmentView;
    private IdentityToken identityToken;
    private TextInputEditText usernameEditText, mobileNumberEditText, newPasswordEditText;
    private Button saveChangesButton;

    public EditProfileFragment() {
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
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        usernameEditText = groupFragmentView.findViewById(R.id.edit_profile_user_name_edit_text);
        mobileNumberEditText = groupFragmentView.findViewById(R.id.edit_profile_mobile_number_edit_text);
        newPasswordEditText = groupFragmentView.findViewById(R.id.edit_profile_password_edit_text);
        saveChangesButton = groupFragmentView.findViewById(R.id.edit_profile_save_changes_button);

        usernameEditText.setText(identityToken.getName());
        return groupFragmentView;
    }
}