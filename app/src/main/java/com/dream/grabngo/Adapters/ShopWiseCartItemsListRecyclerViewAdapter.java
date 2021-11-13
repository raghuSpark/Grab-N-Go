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

import com.dream.grabngo.R;
import com.dream.grabngo.CustomClasses.CartItemDetails;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;

import java.util.ArrayList;

public class ShopWiseCartItemsListRecyclerViewAdapter extends RecyclerView.Adapter<ShopWiseCartItemsListRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final FragmentManager supportFragmentManager;
    private final ArrayList<ShopWiseCartItemsDetails> shopWiseCartItemsDetailsArrayList;
    private CartItemsListRecyclerViewAdapter.OnItemClickListener itemClickListener;

    public ShopWiseCartItemsListRecyclerViewAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<ShopWiseCartItemsDetails> shopWiseCartItemsDetailsArrayList) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.shopWiseCartItemsDetailsArrayList = shopWiseCartItemsDetailsArrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private void setOnItemClickListener(CartItemsListRecyclerViewAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ShopWiseCartItemsListRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cart_shop_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ShopWiseCartItemsListRecyclerViewAdapter.ViewHolder holder, int position) {
        if (shopWiseCartItemsDetailsArrayList.isEmpty()) {
            holder.shopName.setVisibility(View.GONE);
            holder.cartItemsRV.setVisibility(View.GONE);
        } else {
            holder.shopName.setVisibility(View.VISIBLE);
            holder.cartItemsRV.setVisibility(View.VISIBLE);

            ShopWiseCartItemsDetails shopWiseCartItemsDetails = shopWiseCartItemsDetailsArrayList.get(position);
            holder.shopName.setText(shopWiseCartItemsDetails.getShopName());

            ArrayList<CartItemDetails> cartItemDetailsArrayList = shopWiseCartItemsDetails.getCartItemDetailsArrayList();

            holder.cartItemsRV.setHasFixedSize(true);
            holder.cartItemsRV.setLayoutManager(new LinearLayoutManager(context));

            CartItemsListRecyclerViewAdapter cartItemsRVA = new CartItemsListRecyclerViewAdapter(context,supportFragmentManager,cartItemDetailsArrayList);
            holder.cartItemsRV.setAdapter(cartItemsRVA);
            cartItemsRVA.notifyDataSetChanged();

            cartItemsRVA.setOnItemClickListener(new CartItemsListRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String shopId, String shopName, int position) {
                    if (itemClickListener!=null) {
                        if (position!=RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(shopId,shopName,position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return shopWiseCartItemsDetailsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;
        RecyclerView cartItemsRV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.cart_shop_name);
            cartItemsRV = itemView.findViewById(R.id.cart_shop_recycler_view);
        }
    }
}

//public class CartItemsListRecyclerViewAdapter extends RecyclerView.Adapter<CartItemsListRecyclerViewAdapter.ViewHolder> {
//
//    private final ArrayList<CartItemDetails> cartItemDetailsArrayList;
//    private final Context context;
//    private final LayoutInflater layoutInflater;
//    private final FragmentManager supportFragmentManager;
//    private CartItemsListRecyclerViewAdapter.OnItemClickListener itemClickListener;
//
//    public CartItemsListRecyclerViewAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<CartItemDetails> cartItemDetailsArrayList) {
//        this.context = context;
//        this.supportFragmentManager = supportFragmentManager;
//        this.cartItemDetailsArrayList = cartItemDetailsArrayList;
//        this.layoutInflater = LayoutInflater.from(context);
//    }
//
//    public void setOnClickListener(CartItemsListRecyclerViewAdapter.OnItemClickListener listener) {
//        itemClickListener = listener;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = layoutInflater.inflate(R.layout.cart_shop_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.shopName.setText(cartItemDetailsArrayList.get(position).getItemName());
//    }
//
//    @Override
//    public int getItemCount() {
//        return cartItemDetailsArrayList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView shopName;
//        RecyclerView cartItemsRV;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            shopName = itemView.findViewById(R.id.cart_shop_name);
//            cartItemsRV = itemView.findViewById(R.id.cart_shop_recycler_view);
//        }
//    }
//}
