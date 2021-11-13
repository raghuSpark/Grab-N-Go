package com.dream.grabngo.CartChildFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.R;

public class ThanksFragment extends Fragment {

    private final Context context;
    private final FragmentManager childFragmentManager, supportFragmentManager;
    private Button backToCartButton;

    public ThanksFragment(Context context, FragmentManager supportFragmentManager, FragmentManager childFragmentManager) {
        this.context = context;
        this.childFragmentManager = childFragmentManager;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_thanks, container, false);
        backToCartButton = groupFragmentView.findViewById(R.id.back_to_cart_button);

        backToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new CartListFragment(context, supportFragmentManager, childFragmentManager)).commit();
            }
        });

        return groupFragmentView;
    }
}