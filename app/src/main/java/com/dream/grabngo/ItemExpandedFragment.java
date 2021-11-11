package com.dream.grabngo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.MainFragments.HomeFragment;

public class ItemExpandedFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private View groupFragmentView;
    private CardView backButton;

    public ItemExpandedFragment(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupFragmentView = inflater.inflate(R.layout.fragment_item_expanded, container, false);
        backButton = groupFragmentView.findViewById(R.id.expanded_item_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new HomeFragment(supportFragmentManager, 1)).commit();
            }
        });
        return groupFragmentView;
    }
}