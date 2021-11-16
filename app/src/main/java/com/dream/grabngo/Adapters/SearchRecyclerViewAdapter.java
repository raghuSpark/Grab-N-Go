package com.dream.grabngo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dream.grabngo.CustomClasses.ShoppingItemDetails;
import com.dream.grabngo.R;

import java.util.ArrayList;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<ShoppingItemDetails> searchResultItemsList;
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final FragmentManager supportFragmentManager;

    public SearchRecyclerViewAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<ShoppingItemDetails> searchResultItemsList) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.searchResultItemsList = searchResultItemsList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.search_result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerViewAdapter.ViewHolder holder, int position) {
        ShoppingItemDetails currentItem = searchResultItemsList.get(position);
        holder.itemName.setText(currentItem.getItemName());
        holder.itemQuantity.setText(currentItem.getAvailableQuantity());
        holder.cityName.setText(currentItem.getCityName());
        holder.itemPrice.setText(String.valueOf(currentItem.getItemPrice()));
        holder.ratingBar.setRating(currentItem.getShopRating());
    }

    @Override
    public int getItemCount() {
        return searchResultItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQuantity, cityName, itemPrice;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.search_result_item_name);
            itemQuantity = itemView.findViewById(R.id.search_result_available_quantity);
            cityName = itemView.findViewById(R.id.search_result_city_name);
            itemPrice = itemView.findViewById(R.id.search_result_item_price);
            ratingBar = itemView.findViewById(R.id.search_result_rating_bar);
        }
    }
}
