package com.dream.grabngo.MainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.R;

public class ShopDetailsFragment extends Fragment {

    private View groupFragmentView;

    public ShopDetailsFragment(FragmentManager supportFragmentManager) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_shop_details, container, false);
        return groupFragmentView;
    }
}