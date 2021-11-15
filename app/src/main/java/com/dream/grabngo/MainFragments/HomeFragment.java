package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.Adapters.ShoppingItemsRecyclerViewAdapter;
import com.dream.grabngo.CustomClasses.ShoppingItemDetails;
import com.dream.grabngo.R;
import com.dream.grabngo.Activities.SearchActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private final int FROM_ITEM_EXPANDED_FRAGMENT_CODE = 1;
    private final int from_fragment_code;

    private final ArrayList<ShoppingItemDetails> shoppingItemDetailsArrayList = new ArrayList<>();
    private final FragmentManager supportFragmentManager;
    private CardView mapsButton;
    private TextView homeSearchButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView homeShoppingItemsRecyclerView;
    private ShoppingItemsRecyclerViewAdapter shoppingItemsRecyclerViewAdapter;
    private RequestQueue requestQueue;

    public HomeFragment(FragmentManager supportFragmentManager, int from_fragment_code) {
        this.supportFragmentManager = supportFragmentManager;
        this.from_fragment_code = from_fragment_code;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        shimmerFrameLayout = groupFragmentView.findViewById(R.id.home_shopping_items_shimmer_layout);
        homeSearchButton = groupFragmentView.findViewById(R.id.home_search_button);
        swipeRefreshLayout = groupFragmentView.findViewById(R.id.home_fragment_swipe);
        homeShoppingItemsRecyclerView = groupFragmentView.findViewById(R.id.home_shopping_items_recycler_view);
        mapsButton = groupFragmentView.findViewById(R.id.home_maps_card);

        // Get the items form the database and add to the shoppingItemDetailsArrayList
        if (from_fragment_code != FROM_ITEM_EXPANDED_FRAGMENT_CODE)
            getAvailableItems();
        else handleShimmerLayout();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            homeShoppingItemsRecyclerView.setVisibility(View.GONE);
            getAvailableItems();
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: routes list should be shown here
            }
        });

        homeSearchButton.setOnClickListener(v -> startActivity(new Intent(requireContext(), SearchActivity.class)));

        homeShoppingItemsRecyclerView.setHasFixedSize(true);
        homeShoppingItemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        shoppingItemsRecyclerViewAdapter = new ShoppingItemsRecyclerViewAdapter(requireContext(), supportFragmentManager, shoppingItemDetailsArrayList);
        homeShoppingItemsRecyclerView.setAdapter(shoppingItemsRecyclerViewAdapter);
        shoppingItemsRecyclerViewAdapter.notifyDataSetChanged();

        return groupFragmentView;
    }

    private void getAvailableItems() {
        String URL = "http://192.168.43.54:3001/gng/v1/get-available-items";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, response -> {
            try {
                handleShimmerLayout();
                shoppingItemDetailsArrayList.clear();
                for (int i = 1; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);
                    ShoppingItemDetails item = new ShoppingItemDetails(
                            object.getString("SHOP_ID"),
                            object.getString("SHOP_NAME"),
                            object.getString("ITEM_NAME"),
                            object.getInt("ITEM_QUANTITY"),
                            object.getDouble("ITEM_PRICE")
                    );
                    shoppingItemDetailsArrayList.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            handleShimmerLayout();
        }, error -> {
            Log.d("TAG", "onErrorResponse: " + error.getMessage());
            handleShimmerLayout();
        });
        requestQueue.add(jsonArrayRequest);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleShimmerLayout() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        homeShoppingItemsRecyclerView.setVisibility(View.VISIBLE);
        if (from_fragment_code != FROM_ITEM_EXPANDED_FRAGMENT_CODE)
            shoppingItemsRecyclerViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}