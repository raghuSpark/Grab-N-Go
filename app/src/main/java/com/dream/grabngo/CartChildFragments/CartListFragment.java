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

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.dream.grabngo.CartItemDetails;
import com.dream.grabngo.CartItemsListRecyclerViewAdapter;
import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;

import java.util.ArrayList;
import java.util.List;

public class CartListFragment extends Fragment {

    private final Context context;
    private final HorizontalStepView orderProgressStepsView;
    private final FragmentManager supportFragmentManager, childFragmentManager;
    private ArrayList<CartItemDetails> cartItemDetailsArrayList = new ArrayList<>();
    private RecyclerView cartListItemsRecyclerView;
    private ImageView emptyCartImageView;
    private Button proceedButton;
    private CartItemsListRecyclerViewAdapter cartItemsListRecyclerViewAdapter;

    public CartListFragment(Context context, FragmentManager supportFragmentManager, FragmentManager childFragmentManager, HorizontalStepView orderProgressStepsView) {
        this.supportFragmentManager = supportFragmentManager;
        this.childFragmentManager = childFragmentManager;
        this.context = context;
        this.orderProgressStepsView = orderProgressStepsView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleCart() {
        if (cartItemDetailsArrayList.isEmpty()) {
            proceedButton.setVisibility(View.GONE);
            cartListItemsRecyclerView.setVisibility(View.GONE);
            emptyCartImageView.setVisibility(View.VISIBLE);
        } else {
            proceedButton.setVisibility(View.VISIBLE);
            cartListItemsRecyclerView.setVisibility(View.VISIBLE);
            emptyCartImageView.setVisibility(View.GONE);
        }
        cartItemsListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_cart_list, container, false);
        cartListItemsRecyclerView = groupFragmentView.findViewById(R.id.cart_list_items_recycler_view);
        proceedButton = groupFragmentView.findViewById(R.id.cart_list_proceed_button);
        emptyCartImageView = groupFragmentView.findViewById(R.id.empty_cart_image_view);

        List<StepBean> steps = new ArrayList<>();
        steps.add(new StepBean("Order", 0));
        steps.add(new StepBean("Payment", -1));
        steps.add(new StepBean("Thanks", -1));
        orderProgressStepsView.setStepViewTexts(steps).ondrawIndicator();

        cartItemDetailsArrayList = SharedPrefConfig.readCartItems(requireContext());

        if (cartItemDetailsArrayList.isEmpty()) {
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
                childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new PaymentDetailsFragment(context, supportFragmentManager, childFragmentManager, orderProgressStepsView, cartItemDetailsArrayList)).commit();
            }
        });

        cartListItemsRecyclerView.setHasFixedSize(true);
        cartListItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        cartItemsListRecyclerViewAdapter = new CartItemsListRecyclerViewAdapter(context, supportFragmentManager, cartItemDetailsArrayList);
        cartListItemsRecyclerView.setAdapter(cartItemsListRecyclerViewAdapter);
        cartItemsListRecyclerViewAdapter.notifyDataSetChanged();

        return groupFragmentView;
    }
}