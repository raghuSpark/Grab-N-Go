package com.dream.grabngo.CartChildFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.CustomClasses.CartItemDetails;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;
import com.dream.grabngo.CustomClasses.UserDetails;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PaymentDetailsFragment extends Fragment {

    private final Context context;
    private final FragmentManager supportFragmentManager, childFragmentManager;
    private final ArrayList<ShopWiseCartItemsDetails> orderedItemsArrayList;
    private CardView backButton;
    private Button placeOrderButton;
    private UserDetails userDetails;
    private ProgressBar progressBar;
    private RequestQueue requestQueue;

    public PaymentDetailsFragment(Context context, FragmentManager supportFragmentManager, FragmentManager childFragmentManager, ArrayList<ShopWiseCartItemsDetails> orderedItemsArrayList) {
        this.context = context;
        this.childFragmentManager = childFragmentManager;
        this.supportFragmentManager = supportFragmentManager;
        this.orderedItemsArrayList = orderedItemsArrayList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(context);
        userDetails = SharedPrefConfig.readUserDetails(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_payment_details, container, false);
        placeOrderButton = groupFragmentView.findViewById(R.id.payment_place_order_button);
        backButton = groupFragmentView.findViewById(R.id.payment_back_button);
        progressBar = groupFragmentView.findViewById(R.id.place_order_progress_bar);

        backButton.setOnClickListener(v -> childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new CartListFragment(context, supportFragmentManager, childFragmentManager)).commit());
        placeOrderButton.setOnClickListener(v -> placeOrder());

        return groupFragmentView;
    }

    private void placeOrder() {
        progressBar.setVisibility(View.VISIBLE);
        placeOrderButton.setVisibility(View.GONE);
        String URL = "http://192.168.43.54:3001/gng/v1/place-order";
        JSONObject postData = new JSONObject();

        ArrayList<JSONObject> orderedItemsJSONArrayList = new ArrayList<>();
        for (ShopWiseCartItemsDetails cartItem : orderedItemsArrayList) {
            for (CartItemDetails item : cartItem.getCartItemDetailsArrayList()) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("item_name", item.getItemName());
                    jsonObject.put("item_quantity", item.getOrderQuantity());
                    jsonObject.put("item_price", item.getItemPrice());
                    orderedItemsJSONArrayList.add(jsonObject);
                } catch (Exception e) {
                    Log.d("TAG", "placeOrder: " + e.getMessage());
                }
            }
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            postData.put("customer_id", userDetails.getCustomerId());
            postData.put("shop_id", orderedItemsArrayList.get(0).getShopId());
            postData.put("payment_method_id", "5");
            postData.put("order_time", sdf.format(new Date()));
            postData.put("orders", orderedItemsJSONArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
            ArrayList<ShopWiseCartItemsDetails> currentList = SharedPrefConfig.readShopWiseCartItemsHistory(context);
            for (ShopWiseCartItemsDetails itemsDetails:SharedPrefConfig.readShopWiseCartItems(context)) {
                currentList.add(new ShopWiseCartItemsDetails(
                        itemsDetails.getShopId(),
                        itemsDetails.getShopName(),
                        itemsDetails.getCartItemDetailsArrayList()));
            }
            SharedPrefConfig.writeShopWiseCartItemsHistory(context,currentList);
            SharedPrefConfig.writeShopWiseCartItems(context, new ArrayList<>());
            childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new ThanksFragment(context, supportFragmentManager, childFragmentManager)).commit();
        }, error -> {
            progressBar.setVisibility(View.GONE);
            placeOrderButton.setVisibility(View.VISIBLE);
            Log.d("TAG", "onErrorResponses: " + error.getMessage());
        });
        requestQueue.add(jsonObjectRequest);
    }
}