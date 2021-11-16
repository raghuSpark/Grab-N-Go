package com.dream.grabngo.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dream.grabngo.CustomClasses.CartItemDetails;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;
import com.dream.grabngo.R;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final FragmentManager supportFragmentManager;
    private final ArrayList<ShopWiseCartItemsDetails> historyItemArrayList;

    public HistoryRecyclerViewAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<ShopWiseCartItemsDetails> historyItemArrayList) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.historyItemArrayList = historyItemArrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerViewAdapter.ViewHolder holder, int position) {
        if (historyItemArrayList.isEmpty()) {
            holder.shopName.setVisibility(View.GONE);
            holder.cartItemsRV.setVisibility(View.GONE);
        } else {
            holder.shopName.setVisibility(View.VISIBLE);
            holder.cartItemsRV.setVisibility(View.VISIBLE);

            ShopWiseCartItemsDetails shopWiseCartItemsDetails = historyItemArrayList.get(position);
            holder.shopName.setText(shopWiseCartItemsDetails.getShopName());

            ArrayList<CartItemDetails> cartItemDetailsArrayList = shopWiseCartItemsDetails.getCartItemDetailsArrayList();

            holder.cartItemsRV.setHasFixedSize(true);
            holder.cartItemsRV.setLayoutManager(new LinearLayoutManager(context));

            CartItemsListRecyclerViewAdapter cartItemsRVA = new CartItemsListRecyclerViewAdapter(context, supportFragmentManager, cartItemDetailsArrayList, true);
            holder.cartItemsRV.setAdapter(cartItemsRVA);
            cartItemsRVA.notifyDataSetChanged();

            cartItemsRVA.setOnItemClickListener((currentItem, position1) -> {
            });
        }
    }

    @Override
    public int getItemCount() {
        return historyItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;
        RecyclerView cartItemsRV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.history_shop_name);
            cartItemsRV = itemView.findViewById(R.id.history_shop_recycler_view);
        }
    }
}