package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.Activities.SearchActivity;
import com.dream.grabngo.Adapters.ShoppingItemsRecyclerViewAdapter;
import com.dream.grabngo.CustomClasses.ShoppingItemDetails;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.LocationService;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private final ArrayList<ShoppingItemDetails> shoppingItemDetailsArrayList = new ArrayList<>();
    private final FragmentManager supportFragmentManager;
    private TextView homeSearchButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView homeShoppingItemsRecyclerView;
    private ShoppingItemsRecyclerViewAdapter shoppingItemsRecyclerViewAdapter;
    private RequestQueue requestQueue;

    public HomeFragment(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
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

        getAvailableItems();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            homeShoppingItemsRecyclerView.setVisibility(View.GONE);
            getAvailableItems();
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
                            "city_name",
                            object.getString("ITEM_NAME"),
                            object.getInt("ITEM_QUANTITY"),
                            0,
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

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(serviceInfo.service.getClassName())) {
                    if (serviceInfo.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(requireContext(), LocationService.class);
            intent.setAction("startLocationService");
            requireContext().startService(intent);
            Toast.makeText(requireContext(), "Location service started!", Toast.LENGTH_SHORT).show();
        } else {
            stopLocationService();
        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(requireContext(), LocationService.class);
            intent.setAction("stopLocationService");
            requireContext().startService(intent);
            Toast.makeText(requireContext(), "Location service stopped!", Toast.LENGTH_SHORT).show();
        }
    }
}