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

import com.dream.grabngo.CustomClasses.RatingDetails;
import com.dream.grabngo.R;

import java.util.ArrayList;

public class RatingsListRecyclerViewAdapter extends RecyclerView.Adapter<RatingsListRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<RatingDetails> ratingsArrayList;
    private final Context context;
    private final FragmentManager supportFragmentManager;
    private final LayoutInflater layoutInflater;

    private RatingsListRecyclerViewAdapter.OnItemClickListener itemClickListener;

    public RatingsListRecyclerViewAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RatingDetails> ratingsArrayList) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.ratingsArrayList = ratingsArrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(RatingsListRecyclerViewAdapter.OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rating_item, parent, false);
        return new ViewHolder(view, itemClickListener, supportFragmentManager);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RatingDetails currentItem = ratingsArrayList.get(position);
        holder.ratingBar.setRating(currentItem.getRatingValue());
        holder.shopName.setText(currentItem.getShopName());
        holder.ratingTimeStamp.setText(currentItem.getRatingTimeStamp());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:

//                        RatingDetails currentItem = ratingDetailsArrayList.get(pos);
//                        AlertDialog dialog;
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                        View view = dialogInflater.inflate(R.layout.dialog_rating_pop_up, null);
//                        builder.setView(view);
//
//                        TextView popupShopName = view.findViewById(R.id.rating_popup_shop_name);
//                        TextView popupCustomerName = view.findViewById(R.id.rating_popup_customer_name);
//                        TextView popupReview = view.findViewById(R.id.rating_popup_review);
//                        RatingBar popupRatingBar = view.findViewById(R.id.rating_popup_rating_bar);
//
//                        popupShopName.setText(currentItem.getShopName());
//                        popupCustomerName.setText(MessageFormat.format("- {0}", currentItem.getCustomerName()));
//                        if (currentItem.getCustomerReview() != null)
//                            popupReview.setText(currentItem.getCustomerReview());
//                        popupRatingBar.setRating(currentItem.getRatingValue());
//
//                        dialog = builder.create();
//                        dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ratingsArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String currentItem, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView shopName, ratingTimeStamp;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener, FragmentManager supportFragmentManager) {
            super(itemView);

            itemView.setOnClickListener(v -> {
//                CartItemDetails temp_item = new CartItemDetails("123","pushpa General Stores","Water",36.00,2,1);
//                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ItemExpandedFragment(supportFragmentManager, null, temp_item)).commit();
            });

            shopName = itemView.findViewById(R.id.rating_shop_name);
            ratingTimeStamp = itemView.findViewById(R.id.rating_item_time_stamp);
            ratingBar = itemView.findViewById(R.id.rating_item_rating_bar);
        }
    }
}