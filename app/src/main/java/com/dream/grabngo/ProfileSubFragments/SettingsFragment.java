package com.dream.grabngo.ProfileSubFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;

public class SettingsFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private CardView backButton;
    private View groupFragmentView;

    public SettingsFragment(androidx.fragment.app.FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        backButton = groupFragmentView.findViewById(R.id.settings_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ProfileFragment(supportFragmentManager)).commit();
            }
        });
        return groupFragmentView;
    }
}