package com.dream.grabngo.CartChildFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;

import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentDetailsFragment extends Fragment {

    private final Context context;
    private final FragmentManager supportFragmentManager, childFragmentManager;
    private final ArrayList<ShopWiseCartItemsDetails> orderedItemsArrayList;
    private CardView backButton;
    private Button placeOrderButton;
    private JSONObject userDetails;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupFragmentView = inflater.inflate(R.layout.fragment_payment_details, container, false);
        placeOrderButton = groupFragmentView.findViewById(R.id.payment_place_order_button);
        backButton = groupFragmentView.findViewById(R.id.payment_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new CartListFragment(context, supportFragmentManager, childFragmentManager)).commit();
            }
        });

        placeOrderButton.setOnClickListener(v -> placeOrder(groupFragmentView));

        return groupFragmentView;
    }

    private void placeOrder(View groupFragmentView) {
        childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new ThanksFragment(context, supportFragmentManager, childFragmentManager)).commit();
//        String URL = "http://192.168.43.54:3001/gng/v1/place-order";
//        JSONObject postData = new JSONObject();
//
//        ArrayList<JSONObject> orderedItemsJSONArrayList = new ArrayList<>();
//        for (CartItemDetails item : orderedItemsArrayList) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("item_name", item.getItemName());
//                jsonObject.put("item_quantity", item.getOrderQuantity());
//                jsonObject.put("item_price", item.getItemPrice());
//                orderedItemsJSONArrayList.add(jsonObject);
//            } catch (Exception e) {
//                Log.d("TAG", "placeOrder: " + e.getMessage());
//            }
//        }
//
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//
////            2017-07-04*13:23:55
//            postData.put("customer_id", userDetails.getString("CUSTOMER_ID"));
//            postData.put("shop_id", "d43b885c-5864-4029-a584-28cfd28d4cc2");
//            postData.put("payment_method_id", "11111");
//            postData.put("order_time", sdf.format(new Date()));
//            postData.put("orders", orderedItemsJSONArrayList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, response -> {
//            try {
//                SharedPrefConfig.writeCartItems(context, new ArrayList<>());
//                childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new ThanksFragment(context, supportFragmentManager, childFragmentManager)).commit();
//            } catch (Exception e) {
//                // TODO: Show snackbar
//                e.printStackTrace();
//            }
//        }, error -> {
//            // TODO: Show snackbar
//            Log.d("TAG", "onErrorResponse: " + error.getMessage());
//        });
//        requestQueue.add(jsonObjectRequest);
    }
}