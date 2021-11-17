package com.dream.grabngo.MainFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.R;

public class PayFragment extends Fragment {

    private final Context context;
    private final FragmentManager supportFragmentManager;
    private Button btnAction;

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
        View groupFragmentView = inflater.inflate(R.layout.fragment_pay, container, false);
        btnAction = groupFragmentView.findViewById(R.id.btnAction);
        return groupFragmentView;
    }
}