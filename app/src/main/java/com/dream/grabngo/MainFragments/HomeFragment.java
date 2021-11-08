package com.dream.grabngo.MainFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.R;
import com.dream.grabngo.ShoppingItemDetails;
import com.dream.grabngo.ShoppingItemsRecyclerViewAdapter;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private View groupFragmentView;
    private ImageView routesButton;
    private TextInputEditText searchEditText;

    private RecyclerView homeShoppingItemsRecyclerView;
    private final ArrayList<ShoppingItemDetails> shoppingItemDetailsArrayList = new ArrayList<>();
    private ShoppingItemsRecyclerViewAdapter shoppingItemsRecyclerViewAdapter;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Get the items form the database and add to the shoppingItemDetailsArrayList
        getAvailableItems();
    }

    private void getAvailableItems() {
        Log.d("TAG", "getAvailableItems: ");
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String URL = "http://192.168.43.54:3001/gng/v1/get-available-items";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("TAG", "getAvailableDetails: " + response);
                try {
                    for(int i = 1;i<response.length();i++) {
                        JSONObject object = response.getJSONObject(i);
                        ShoppingItemDetails item = new ShoppingItemDetails(
                                object.getString("ITEM_NAME"),
                                object.getInt("AVAILABLE_QUANTITY"),
                                object.getDouble("PRICE"),
                                false
                        );
                        shoppingItemDetailsArrayList.add(item);
                    }
                    shoppingItemsRecyclerViewAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                                Log.d("TAG", "onErrorResponse: "+error.getMessage());
                Toast.makeText(getContext(), "Error occurred! Try again!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        routesButton = groupFragmentView.findViewById(R.id.home_map_button);
        searchEditText = groupFragmentView.findViewById(R.id.home_search_edit_text);
        homeShoppingItemsRecyclerView = groupFragmentView.findViewById(R.id.home_shopping_items_recycler_view);

        routesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: routes list should be shown here
//                getAvailableItems();
            }
        });

        homeShoppingItemsRecyclerView.setHasFixedSize(true);
        homeShoppingItemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,GridLayoutManager.VERTICAL,false));
        shoppingItemsRecyclerViewAdapter = new ShoppingItemsRecyclerViewAdapter(requireContext(), shoppingItemDetailsArrayList);
        homeShoppingItemsRecyclerView.setAdapter(shoppingItemsRecyclerViewAdapter);
        shoppingItemsRecyclerViewAdapter.notifyDataSetChanged();

        return groupFragmentView;
    }
}