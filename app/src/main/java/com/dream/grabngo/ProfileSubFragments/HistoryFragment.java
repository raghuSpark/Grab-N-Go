package com.dream.grabngo.ProfileSubFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dream.grabngo.Adapters.HistoryRecyclerViewAdapter;
import com.dream.grabngo.Adapters.ShopWiseCartItemsListRecyclerViewAdapter;
import com.dream.grabngo.CartChildFragments.ThanksFragment;
import com.dream.grabngo.CustomClasses.CartItemDetails;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;
import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private final Context context;
    private TextView noResultsTextView;
    private CardView backButton;
    private RecyclerView historyItemsRecyclerView;
    private HistoryRecyclerViewAdapter historyItemsRecyclerViewAdapter;

    public HistoryFragment(Context context, FragmentManager supportFragmentManager) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        backButton = groupFragmentView.findViewById(R.id.history_back_button);
        historyItemsRecyclerView = groupFragmentView.findViewById(R.id.history_items_recycler_view);
        noResultsTextView = groupFragmentView.findViewById(R.id.history_no_results_text_View);

        backButton.setOnClickListener(v -> supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ProfileFragment(context, supportFragmentManager)).commit());

        ArrayList<ShopWiseCartItemsDetails> historyItemsList;
        historyItemsList = SharedPrefConfig.readShopWiseCartItemsHistory(context);

        if (historyItemsList.isEmpty()) {
            historyItemsRecyclerView.setVisibility(View.GONE);
            noResultsTextView.setVisibility(View.VISIBLE);
        } else {
            historyItemsRecyclerView.setVisibility(View.VISIBLE);
            noResultsTextView.setVisibility(View.GONE);
        }

        historyItemsRecyclerView.setHasFixedSize(true);
        historyItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        historyItemsRecyclerViewAdapter = new HistoryRecyclerViewAdapter(context, supportFragmentManager, historyItemsList);
        historyItemsRecyclerView.setAdapter(historyItemsRecyclerViewAdapter);
        historyItemsRecyclerViewAdapter.notifyDataSetChanged();

        return groupFragmentView;
    }
}