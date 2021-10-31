package com.dream.grabngo.ProfileChildFragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dream.grabngo.Activities.GetStartedActivity;
import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;
import com.ibm.cloud.appid.android.api.AppID;

public class SettingsFragment extends Fragment {

    private static final String TENANT_ID = "71cdaba0-0cf0-4487-9705-f06bf644dec4";
    private static final String REGION = AppID.REGION_UK;

    private View groupFragmentView;
    private Button logoutButton;
    private AlertDialog dialog;

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
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            View view = getLayoutInflater().inflate(R.layout.dialog_logout_confirmation, null);
            builder.setView(view);

            view.findViewById(R.id.yes_quit_button).setOnClickListener(view1 -> {
                SharedPrefConfig.writeIsLoggedIn(requireContext(),false);
                AppID.getInstance().logout();
                dialog.cancel();
                startActivity(new Intent(requireActivity(), GetStartedActivity.class));
                requireActivity().finish();
            });

            view.findViewById(R.id.no_stay_button).setOnClickListener(view12 -> dialog.cancel());

            dialog = builder.create();
            dialog.show();
        });

        return groupFragmentView;
    }
}