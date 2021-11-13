package com.dream.grabngo.CartChildFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dream.grabngo.Adapters.ShopWiseCartItemsListRecyclerViewAdapter;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;

import java.util.ArrayList;

public class CartListFragment extends Fragment {

    private final Context context;
    private final FragmentManager supportFragmentManager, childFragmentManager;
    private ArrayList<ShopWiseCartItemsDetails> shopWiseCartItemsDetailsArrayList = new ArrayList<>();
    private RecyclerView cartListItemsRecyclerView;
    private ImageView emptyCartImageView;
    private Button proceedButton;
    private ShopWiseCartItemsListRecyclerViewAdapter shopWiseCartItemsListRecyclerViewAdapter;

    public CartListFragment(Context context, FragmentManager supportFragmentManager, FragmentManager childFragmentManager) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.childFragmentManager = childFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleCart() {
        if (shopWiseCartItemsDetailsArrayList.isEmpty()) {
            proceedButton.setVisibility(View.GONE);
            cartListItemsRecyclerView.setVisibility(View.GONE);
            emptyCartImageView.setVisibility(View.VISIBLE);
        } else {
            proceedButton.setVisibility(View.VISIBLE);
            cartListItemsRecyclerView.setVisibility(View.VISIBLE);
            emptyCartImageView.setVisibility(View.GONE);
        }
        shopWiseCartItemsListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_cart_list, container, false);
        cartListItemsRecyclerView = groupFragmentView.findViewById(R.id.cart_list_items_recycler_view);
        proceedButton = groupFragmentView.findViewById(R.id.cart_list_proceed_button);
        emptyCartImageView = groupFragmentView.findViewById(R.id.empty_cart_image_view);

        shopWiseCartItemsDetailsArrayList = SharedPrefConfig.readShopWiseCartItems(requireContext());

        if (shopWiseCartItemsDetailsArrayList.isEmpty()) {
            proceedButton.setVisibility(View.GONE);
            cartListItemsRecyclerView.setVisibility(View.GONE);
            emptyCartImageView.setVisibility(View.VISIBLE);
        } else {
            proceedButton.setVisibility(View.VISIBLE);
            cartListItemsRecyclerView.setVisibility(View.VISIBLE);
            emptyCartImageView.setVisibility(View.GONE);
        }

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new PaymentDetailsFragment(context, supportFragmentManager, childFragmentManager, shopWiseCartItemsDetailsArrayList)).commit();
            }
        });

        cartListItemsRecyclerView.setHasFixedSize(true);
        cartListItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        shopWiseCartItemsListRecyclerViewAdapter = new ShopWiseCartItemsListRecyclerViewAdapter(context, supportFragmentManager, shopWiseCartItemsDetailsArrayList);
        cartListItemsRecyclerView.setAdapter(shopWiseCartItemsListRecyclerViewAdapter);
        shopWiseCartItemsListRecyclerViewAdapter.notifyDataSetChanged();

        return groupFragmentView;
    }
}