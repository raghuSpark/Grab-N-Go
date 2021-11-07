package com.dream.grabngo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartItemsListRecyclerViewAdapter extends RecyclerView.Adapter {

    private final ArrayList<CartItemDetails> cartItemDetailsArrayList;
    Context context;
    private CartItemsListRecyclerViewAdapter.OnItemClickListener itemClickListener;

    public CartItemsListRecyclerViewAdapter(ArrayList<CartItemDetails> cartItemDetailsArrayList) {
        this.cartItemDetailsArrayList = cartItemDetailsArrayList;
    }

    public void setOnItemClickListener(CartItemsListRecyclerViewAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_list_item, parent, false);
        return new MyViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartItemsListRecyclerViewAdapter.MyViewHolder myViewHolder = (MyViewHolder) holder;
    }

    @Override
    public int getItemCount() {
        return cartItemDetailsArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String itemName, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
        }
    }
}
