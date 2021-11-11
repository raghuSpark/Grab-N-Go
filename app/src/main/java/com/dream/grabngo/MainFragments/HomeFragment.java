package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.R;
import com.dream.grabngo.ShoppingItemDetails;
import com.dream.grabngo.ShoppingItemsRecyclerViewAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private final ArrayList<ShoppingItemDetails> shoppingItemDetailsArrayList = new ArrayList<>();
    private CardView searchBackButton, mapsButton;
    private TextInputEditText searchEditText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView homeShoppingItemsRecyclerView;
    private ShoppingItemsRecyclerViewAdapter shoppingItemsRecyclerViewAdapter;

    private RequestQueue requestQueue;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        shimmerFrameLayout = groupFragmentView.findViewById(R.id.home_shopping_items_shimmer_layout);
        searchEditText = groupFragmentView.findViewById(R.id.home_search_edit_text);
        swipeRefreshLayout = groupFragmentView.findViewById(R.id.home_fragment_swipe);
        homeShoppingItemsRecyclerView = groupFragmentView.findViewById(R.id.home_shopping_items_recycler_view);
        searchBackButton = groupFragmentView.findViewById(R.id.home_search_back_card);
        mapsButton = groupFragmentView.findViewById(R.id.home_maps_card);

        // Get the items form the database and add to the shoppingItemDetailsArrayList
        getAvailableItems();

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

        homeShoppingItemsRecyclerView.setHasFixedSize(true);
        homeShoppingItemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        shoppingItemsRecyclerViewAdapter = new ShoppingItemsRecyclerViewAdapter(requireContext(), shoppingItemDetailsArrayList);
        homeShoppingItemsRecyclerView.setAdapter(shoppingItemsRecyclerViewAdapter);
        shoppingItemsRecyclerViewAdapter.notifyDataSetChanged();

        return groupFragmentView;
    }

    private void getAvailableItems() {
        String URL = "http://192.168.43.54:3001/gng/v1/get-available-items";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, response -> {
            try {
                shoppingItemDetailsArrayList.clear();
                for (int i = 1; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);
                    ShoppingItemDetails item = new ShoppingItemDetails(
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
        shoppingItemsRecyclerViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}