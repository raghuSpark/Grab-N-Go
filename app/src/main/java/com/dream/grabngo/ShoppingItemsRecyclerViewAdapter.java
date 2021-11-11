package com.dream.grabngo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ShoppingItemsRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingItemsRecyclerViewAdapter.ViewHolder> {

    ArrayList<ShoppingItemDetails> shoppingItemDetailsArrayList;
    Context context;
    LayoutInflater layoutInflater;
    FragmentManager supportFragmentManager;

    public ShoppingItemsRecyclerViewAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<ShoppingItemDetails> shoppingItemDetailsArrayList) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.shoppingItemDetailsArrayList = shoppingItemDetailsArrayList;
        this.layoutInflater = LayoutInflater.from(context);
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
        holder.item_price.setText(MessageFormat.format("₹ {0}", shoppingItemDetailsArrayList.get(position).getItemPrice().toString()));
    }

    @Override
    public int getItemCount() {
        return shoppingItemDetailsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_price;
        ImageView item_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.shopping_item_name);
            item_price = itemView.findViewById(R.id.shopping_item_price);
            item_image = itemView.findViewById(R.id.shopping_item_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container,new ItemExpandedFragment(supportFragmentManager)).commit();
                }
            });
        }
    }
}
