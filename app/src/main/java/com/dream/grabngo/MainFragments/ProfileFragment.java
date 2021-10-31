package com.dream.grabngo.MainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dream.grabngo.ImageLoadTask;
import com.dream.grabngo.ProfileChildFragments.EditProfileFragment;
import com.dream.grabngo.ProfileChildFragments.HistoryFragment;
import com.dream.grabngo.ProfileChildFragments.SettingsFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.SingletonClass;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;

public class ProfileFragment extends Fragment {

    private View groupFragmentView;
    private TextView userName, emailID;
    private ImageView profile_image;
    private ShimmerFrameLayout shimmerFrameLayout;

    private LinearLayout editProfileBtn, historyBtn, settingsBtn;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = groupFragmentView.findViewById(R.id.user_name);
        emailID = groupFragmentView.findViewById(R.id.user_mailID);
        profile_image = groupFragmentView.findViewById(R.id.profile_image);
        shimmerFrameLayout = groupFragmentView.findViewById(R.id.profile_shimmer_view_container);
        editProfileBtn = groupFragmentView.findViewById(R.id.editProfileBtn);
        historyBtn = groupFragmentView.findViewById(R.id.transactionHistoryBtn);
        settingsBtn = groupFragmentView.findViewById(R.id.settingsBtn);

        SingletonClass singleToneClass = com.dream.grabngo.SingletonClass.getInstance();
        IdentityToken identityToken = singleToneClass.getIdentityToken();

        new ImageLoadTask(getContext(), identityToken.getPicture(), profile_image, shimmerFrameLayout).execute();

        userName.setText(identityToken.getName());
        emailID.setText(identityToken.getEmail());

        getChildFragmentManager().beginTransaction().add(R.id.profile_child_fragments_container, new EditProfileFragment()).commit();
        editProfileBtn.setOnClickListener(view -> {
            getChildFragmentManager().beginTransaction().replace(R.id.profile_child_fragments_container, new EditProfileFragment()).commit();
            editProfileBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_active, null));
            historyBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_inactive, null));
            settingsBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_inactive, null));
        });

        historyBtn.setOnClickListener(view -> {
            getChildFragmentManager().beginTransaction().replace(R.id.profile_child_fragments_container, new HistoryFragment()).commit();
            editProfileBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_inactive, null));
            historyBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_active, null));
            settingsBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_inactive, null));
        });

        settingsBtn.setOnClickListener(view -> {
            getChildFragmentManager().beginTransaction().replace(R.id.profile_child_fragments_container, new SettingsFragment()).commit();
            editProfileBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_inactive, null));
            historyBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_inactive, null));
            settingsBtn.setBackgroundTintList(getResources().getColorStateList(R.color.child_tabs_active, null));
        });

        return groupFragmentView;
    }
}