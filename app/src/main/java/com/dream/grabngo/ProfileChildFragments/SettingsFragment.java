package com.dream.grabngo.ProfileChildFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.dream.grabngo.Activities.GetStartedActivity;
import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;
import com.ibm.cloud.appid.android.api.AppID;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    private View groupFragmentView;
    private Button logoutButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppID.getInstance().initialize(requireContext(), TENANT_ID, REGION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

        logoutButton = groupFragmentView.findViewById(R.id.logoutBtn);

        logoutButton.setOnClickListener(v -> {
            SharedPrefConfig.writeIsLoggedIn(requireContext(),false);
            AppID.getInstance().logout();
            startActivity(new Intent(requireActivity(), GetStartedActivity.class));
            requireActivity().finish();
        });

        return groupFragmentView;
    }
}