package com.dream.grabngo.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dream.grabngo.CustomClasses.CartItemDetails;
import com.dream.grabngo.CustomClasses.RatingDetails;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;
import com.dream.grabngo.MainFragments.ItemExpandedFragment;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;

import java.text.MessageFormat;
import java.util.ArrayList;

public class CartItemsListRecyclerViewAdapter extends RecyclerView.Adapter<CartItemsListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<CartItemDetails> cartItemDetailsArrayList;
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final FragmentManager supportFragmentManager;
    private final boolean fromHistory;
    private CartItemsListRecyclerViewAdapter.OnItemClickListener itemClickListener;

    public CartItemsListRecyclerViewAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<CartItemDetails> cartItemDetailsArrayList, boolean fromHistory) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.cartItemDetailsArrayList = cartItemDetailsArrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.fromHistory = fromHistory;
    }

    public void setOnItemClickListener(CartItemsListRecyclerViewAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItemDetails currentItem = cartItemDetailsArrayList.get(position);
        holder.item_name.setText(currentItem.getItemName());
        holder.item_price.setText(MessageFormat.format("₹ {0}", currentItem.getItemPrice().toString()));
        holder.item_quantity.setText(String.valueOf(currentItem.getOrderQuantity()));

        if (fromHistory) {
            holder.deleteItem.setVisibility(View.GONE);
        }

        holder.deleteItem.setOnClickListener(v -> {
            ArrayList<ShopWiseCartItemsDetails> shopWiseCartItemsDetailsArrayList = SharedPrefConfig.readShopWiseCartItems(context);
            for (int j = 0; j < shopWiseCartItemsDetailsArrayList.size(); j++) {
                ShopWiseCartItemsDetails item = shopWiseCartItemsDetailsArrayList.get(j);
                if (item.getShopId().equals(currentItem.getShopId())) {
                    cartItemDetailsArrayList.remove(position);
                    if (!cartItemDetailsArrayList.isEmpty()) shopWiseCartItemsDetailsArrayList.set(j,new ShopWiseCartItemsDetails(item.getShopId(), item.getShopName(), cartItemDetailsArrayList));
                    else shopWiseCartItemsDetailsArrayList.remove(j);
                    break;
                }
            }
            SharedPrefConfig.writeShopWiseCartItems(context, shopWiseCartItemsDetailsArrayList);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItemDetailsArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(RatingDetails currentItem, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_price, item_quantity;
        CardView deleteItem;
        ImageView item_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.cart_item_name);
            item_price = itemView.findViewById(R.id.cart_item_price);
            item_image = itemView.findViewById(R.id.cart_item_image_view);
            item_quantity = itemView.findViewById(R.id.cart_item_quantity_text_view);
            deleteItem = itemView.findViewById(R.id.delete_cart_item);

            itemView.setOnClickListener(v -> supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ItemExpandedFragment(supportFragmentManager, null, cartItemDetailsArrayList.get(getAdapterPosition()))).commit());
        }
    }
}
