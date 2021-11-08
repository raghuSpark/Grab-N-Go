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

public class CartItemsListRecyclerViewAdapter extends RecyclerView.Adapter<CartItemsListRecyclerViewAdapter.ViewHolder> {

    ArrayList<CartItemDetails> cartItemDetailsArrayList;
    Context context;
    CartItemsListRecyclerViewAdapter.OnItemClickListener itemClickListener;

    public CartItemsListRecyclerViewAdapter(Context context, ArrayList<CartItemDetails> cartItemDetailsArrayList) {
        this.cartItemDetailsArrayList = cartItemDetailsArrayList;
        this.context = context;
    }

    public void setOnItemClickListener(CartItemsListRecyclerViewAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_list_item, parent, false);
        return new ViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO
    }

    @Override
    public int getItemCount() {
        return cartItemDetailsArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String itemName, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_price;
        ImageView item_image;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            item_name = itemView.findViewById(R.id.cart_item_name);
            item_price = itemView.findViewById(R.id.cart_item_price);
            item_image = itemView.findViewById(R.id.cart_item_image_view);
        }
    }
}