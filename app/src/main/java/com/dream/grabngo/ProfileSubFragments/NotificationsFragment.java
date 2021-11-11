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

public class NotificationsFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private View groupFragmentView;
    private CardView backButton;

    public NotificationsFragment(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupFragmentView = inflater.inflate(R.layout.fragment_notifications, container, false);
        backButton = groupFragmentView.findViewById(R.id.notifications_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ProfileFragment(supportFragmentManager)).commit();
            }
        });
        return groupFragmentView;
    }
}