package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.CartChildFragments.CartListFragment;
import com.dream.grabngo.utils.NetworkChangeListener;
import com.dream.grabngo.R;

public class CartFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    public CartFragment(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_cart, container, false);

//        Working with child fragments
        getChildFragmentManager().beginTransaction().add(R.id.cart_child_fragments_container, new CartListFragment(requireContext(), supportFragmentManager, getChildFragmentManager())).commit();

        return groupFragmentView;
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireContext().registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        requireContext().unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}