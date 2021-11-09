package com.dream.grabngo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShoppingItemsRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingItemsRecyclerViewAdapter.ViewHolder> {

    ArrayList<ShoppingItemDetails> shoppingItemDetailsArrayList;
    Context context;
    LayoutInflater layoutInflater;
    CartItemsListRecyclerViewAdapter.OnItemClickListener itemClickListener;

    public ShoppingItemsRecyclerViewAdapter(Context context, ArrayList<ShoppingItemDetails> shoppingItemDetailsArrayList) {
        this.context = context;
        this.shoppingItemDetailsArrayList = shoppingItemDetailsArrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(CartItemsListRecyclerViewAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.shopping_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_name.setText(shoppingItemDetailsArrayList.get(position).getItemName());
        holder.item_price.setText("\u20B9 " + shoppingItemDetailsArrayList.get(position).getItemPrice().toString());
    }

    @Override
    public int getItemCount() {
        return shoppingItemDetailsArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String itemName, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_price;
        ImageView item_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.shopping_item_name);
            item_price = itemView.findViewById(R.id.shopping_item_price);
            item_image = itemView.findViewById(R.id.shopping_item_image_view);
        }
    }
}
