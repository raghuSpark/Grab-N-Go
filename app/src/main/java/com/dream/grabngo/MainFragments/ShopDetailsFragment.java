package com.dream.grabngo.MainFragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

public class ShopDetailsFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private final String shopId;
    private final String shopName;
    private final Context context;
    private CardView backButton, callButton, mailButton;
    private ImageView shopImageView;
    private TextView shopNameTextView, aboutShopTextView, address1TextView, address2TextView, cityAndPinCodeTextView, ratingTextView;
    private RatingBar ratingBar;

    public ShopDetailsFragment(Context context, FragmentManager supportFragmentManager, String shopId, String shopName) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.shopName = shopName;
        this.shopId = shopId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_shop_details, container, false);
        backButton = groupFragmentView.findViewById(R.id.shop_details_back_button);
        callButton = groupFragmentView.findViewById(R.id.shop_details_call_button);
        mailButton = groupFragmentView.findViewById(R.id.shop_details_mail_button);
        shopImageView = groupFragmentView.findViewById(R.id.shop_details_image_view);
        shopNameTextView = groupFragmentView.findViewById(R.id.shop_details_name_text_view);
        aboutShopTextView = groupFragmentView.findViewById(R.id.shop_details_about_text_view);
        address1TextView = groupFragmentView.findViewById(R.id.shop_details_address_1);
        address2TextView = groupFragmentView.findViewById(R.id.shop_details_address_2);
        cityAndPinCodeTextView = groupFragmentView.findViewById(R.id.shop_details_city_pin_code);
        ratingBar = groupFragmentView.findViewById(R.id.shop_details_store_rating_bar);

        shopNameTextView.setText(shopName);
        getStoreDetails();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new HomeFragment(supportFragmentManager, 1)).commit();
            }
        });

        return groupFragmentView;
    }

    private void getStoreDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "http://192.168.1.1:3001/gng/v1/get-shop-details";
        JSONObject postData = new JSONObject();
        try {
            postData.put("shop_id", shopId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "getStoreDetails: ");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    aboutShopTextView.setText(response.getString("ABOUT"));
                    address1TextView.setText(response.getString("ADDRESS_LINE_1"));
                    address2TextView.setText(response.getString("ADDRESS_LINE_2"));
                    cityAndPinCodeTextView.setText(MessageFormat.format("{0} , {1}", response.getString("CITY"), response.getString("PIN_CODE")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> Log.d("TAG", "onErrorResponse: " + error.getMessage()));
        requestQueue.add(jsonObjectRequest);
    }
}