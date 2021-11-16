package com.dream.grabngo.MainFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.R;

public class PayFragment extends Fragment {

    private final Context context;
    private final FragmentManager supportFragmentManager;

    public PayFragment(Context context, FragmentManager supportFragmentManager) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupFragmentView = inflater.inflate(R.layout.fragment_pay, container, false);
        return groupFragmentView;
    }
}