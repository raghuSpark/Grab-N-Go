package com.dream.grabngo.ProfileSubFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dream.grabngo.Adapters.HistoryRecyclerViewAdapter;
import com.dream.grabngo.Adapters.ShopWiseCartItemsListRecyclerViewAdapter;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;
import com.dream.grabngo.MainFragments.ProfileFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;

import java.util.ArrayList;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupFragmentView = inflater.inflate(R.layout.fragment_history, container, false);
        backButton = groupFragmentView.findViewById(R.id.history_back_button);
        historyItemsRecyclerView = groupFragmentView.findViewById(R.id.history_items_recycler_view);
        noResultsTextView = groupFragmentView.findViewById(R.id.history_no_results_text_View);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ProfileFragment(context, supportFragmentManager)).commit();
            }
        });

        ArrayList<ShopWiseCartItemsDetails> historyItemsList = new ArrayList<>();
        historyItemsList = SharedPrefConfig.readShopWiseCartItems(context);
        //TODO: get these details from the web

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