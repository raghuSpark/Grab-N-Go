package com.dream.grabngo.ProfileSubFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.Adapters.RatingsListRecyclerViewAdapter;
import com.dream.grabngo.CustomClasses.RatingDetails;
import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RatingsFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private final Context context;
    private ArrayList<RatingDetails> ratingDetailsArrayList = new ArrayList<>();
    private CardView backButton;
    private ShimmerFrameLayout ratingsShimmerLayout;
    private RecyclerView ratingsRecyclerView;
    private RatingsListRecyclerViewAdapter ratingsListRecyclerViewAdapter;
    private RequestQueue requestQueue;

    public RatingsFragment(Context context, FragmentManager supportFragmentManager) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_ratings, container, false);
        backButton = groupFragmentView.findViewById(R.id.ratings_back_button);
        ratingsShimmerLayout = groupFragmentView.findViewById(R.id.rating_items_shimmer_layout);
        ratingsRecyclerView = groupFragmentView.findViewById(R.id.ratings_recycler_view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ProfileFragment(context, supportFragmentManager)).commit();
            }
        });

        getUsersRatings();

        ratingsRecyclerView.setHasFixedSize(true);
        ratingsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        ratingsListRecyclerViewAdapter = new RatingsListRecyclerViewAdapter(context, supportFragmentManager, ratingDetailsArrayList);
        ratingsRecyclerView.setAdapter(ratingsListRecyclerViewAdapter);
        ratingsListRecyclerViewAdapter.notifyDataSetChanged();

        return groupFragmentView;
    }

    private void getUsersRatings() {
        String URL = "http://192.168.43.54:3001/gng/v1/getShopRatingsByUser";

        JSONArray postDataArray = new JSONArray();
        JSONObject postData = new JSONObject();
        try {
            postData.put("customer_id", SharedPrefConfig.readUserDetails(context).getString("CUSTOMER_ID"));
            postDataArray.put(postData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        @SuppressLint("NotifyDataSetChanged")
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, URL, postDataArray, response -> {
            if (response.length() != 0) {
                ratingDetailsArrayList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        ratingDetailsArrayList.add(
                                new RatingDetails(
                                        item.getString("SHOP_ID"),
                                        item.getString("SHOP_NAME"),
                                        item.getString("CUSTOMER_ID"),
                                        item.getString("CUSTOMER_NAME"),
                                        item.getString("REVIEW_TEXT"),
                                        item.getInt("RATING"),
                                        item.getString("REVIEW_TIME")
                                )
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ratingsListRecyclerViewAdapter = new RatingsListRecyclerViewAdapter(context, supportFragmentManager, ratingDetailsArrayList);
                ratingsRecyclerView.setAdapter(ratingsListRecyclerViewAdapter);
                ratingsListRecyclerViewAdapter.notifyDataSetChanged();

                ratingsShimmerLayout.stopShimmer();
                ratingsShimmerLayout.setVisibility(View.GONE);
                ratingsRecyclerView.setVisibility(View.VISIBLE);
            }
        }, error -> {
            Log.d("TAG", "onErrorResponse: " + error.getMessage());
            ratingsShimmerLayout.stopShimmer();
            ratingsShimmerLayout.setVisibility(View.GONE);
            ratingsRecyclerView.setVisibility(View.VISIBLE);
        });
        requestQueue.add(jsonArrayRequest);
    }
}