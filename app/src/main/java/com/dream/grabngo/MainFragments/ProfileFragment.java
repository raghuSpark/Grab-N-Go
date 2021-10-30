package com.dream.grabngo.MainFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dream.grabngo.ImageLoadTask;
import com.dream.grabngo.Activities.GetStartedActivity;
import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;
import com.dream.grabngo.SingletonClass;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ibm.cloud.appid.android.api.AppID;
import com.ibm.cloud.appid.android.api.tokens.IdentityToken;

public class ProfileFragment extends Fragment {

    private View groupFragmentView;
    private TextView userName, emailID;
    private Button logoutButton;
    private ImageView profile_image;
    private ShimmerFrameLayout shimmerFrameLayout;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        userName = groupFragmentView.findViewById(R.id.user_name);
        emailID = groupFragmentView.findViewById(R.id.user_mailID);
        logoutButton = groupFragmentView.findViewById(R.id.logout);
        profile_image = groupFragmentView.findViewById(R.id.profile_image);
        shimmerFrameLayout = groupFragmentView.findViewById(R.id.profile_shimmer_view_container);

        SingletonClass singleToneClass = com.dream.grabngo.SingletonClass.getInstance();
        IdentityToken identityToken = singleToneClass.getIdentityToken();

        new ImageLoadTask(identityToken.getPicture(), profile_image, shimmerFrameLayout).execute();

        userName.setText(identityToken.getName());
        emailID.setText(identityToken.getEmail());

        logoutButton.setOnClickListener(view -> {
            SharedPrefConfig.writeIsLoggedIn(getContext(), false);
            AppID.getInstance().logout();
            requireActivity().startActivity(new Intent(getContext(), GetStartedActivity.class));
            requireActivity().finish();
        });
        return groupFragmentView;
    }
}