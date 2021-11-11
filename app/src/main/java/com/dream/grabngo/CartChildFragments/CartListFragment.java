package com.dream.grabngo.CartChildFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.dream.grabngo.CartItemDetails;
import com.dream.grabngo.CartItemsListRecyclerViewAdapter;
import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartListFragment extends Fragment {

    private final ArrayList<CartItemDetails> cartItemDetailsArrayList = new ArrayList<>();
    private final FragmentManager fragmentManager;
    private final Context context;
    private View groupFragmentView;
    private RecyclerView cartListItemsRecyclerView;
    private ImageView emptyCartImageView;
    private Button proceedButton;
    private CartItemsListRecyclerViewAdapter cartItemsListRecyclerViewAdapter;
    private RequestQueue requestQueue;
    private JSONObject userDetails, cartItemsJsonResponse;
    private final HorizontalStepView orderProgressStepsView;

    public CartListFragment(Context context, FragmentManager fragmentManager, HorizontalStepView orderProgressStepsView) {
        this.fragmentManager = fragmentManager;
        this.context = context;
        this.orderProgressStepsView = orderProgressStepsView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
        userDetails = SharedPrefConfig.readUserDetails(requireContext());
//        try {
//            getOrderDetails(userDetails.getString("CUSTOMER_ID"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void getOrderDetails(String customer_id) {
        String URL = "http://192.168.43.54:3001/gng/v1/get-customer-orders";
        JSONObject postData = new JSONObject();
        try {
            postData.put("CUSTOMER_ID", customer_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
            cartItemsJsonResponse = response;
            Log.d("TAG", "getOrderDetails: " + response);
            handleCart();
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Unable to save the changes! Try again!", Toast.LENGTH_SHORT).show();
                handleCart();
            }
        });
        requestQueue.add(jsonObjectRequest);
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

        try {
            if (cartItemsJsonResponse != null && cartItemsJsonResponse.getString("STATUS").equals("FAILURE")) {
                proceedButton.setVisibility(View.GONE);
                cartListItemsRecyclerView.setVisibility(View.GONE);
                emptyCartImageView.setVisibility(View.VISIBLE);
            } else {
                // TODO
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cartItemsListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupFragmentView = inflater.inflate(R.layout.fragment_cart_list, container, false);
        cartListItemsRecyclerView = groupFragmentView.findViewById(R.id.cart_list_items_recycler_view);
        proceedButton = groupFragmentView.findViewById(R.id.proceed_button);
        emptyCartImageView = groupFragmentView.findViewById(R.id.empty_cart_image_view);

        List<StepBean> steps = new ArrayList<>();
        steps.add(new StepBean("Order", 0));
        steps.add(new StepBean("Payment", -1));
        steps.add(new StepBean("Thanks", -1));

        orderProgressStepsView.setStepViewTexts(steps)
                .ondrawIndicator();

        if (cartItemDetailsArrayList.isEmpty()) {
            proceedButton.setVisibility(View.GONE);
            cartListItemsRecyclerView.setVisibility(View.GONE);
            emptyCartImageView.setVisibility(View.VISIBLE);
        } else {
            proceedButton.setVisibility(View.VISIBLE);
            cartListItemsRecyclerView.setVisibility(View.VISIBLE);
            emptyCartImageView.setVisibility(View.GONE);
        }

        cartListItemsRecyclerView.setHasFixedSize(true);
        cartListItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItemsListRecyclerViewAdapter = new CartItemsListRecyclerViewAdapter(requireContext(), cartItemDetailsArrayList);
        cartListItemsRecyclerView.setAdapter(cartItemsListRecyclerViewAdapter);
        cartItemsListRecyclerViewAdapter.notifyDataSetChanged();

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                fragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container,new PaymentDetailsFragment(context,fragmentManager,orderProgressStepsView)).commit();
            }
        });

        return groupFragmentView;
    }
}