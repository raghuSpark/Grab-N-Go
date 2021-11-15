package com.dream.grabngo.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

    public RatingsListRecyclerViewAdapter(Context context, FragmentManager supportFragmentManager, ArrayList<RatingDetails> ratingsArrayList) {
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
        this.ratingsArrayList = ratingsArrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rating_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RatingDetails currentItem = ratingsArrayList.get(position);
        holder.ratingBar.setRating(currentItem.getRatingValue());
        holder.shopName.setText(currentItem.getShopName());
        if (currentItem.getCustomerReview().isEmpty()) holder.customerReview.setText("No review written!");
        else holder.customerReview.setText(currentItem.getCustomerReview());
        holder.ratingTimeStamp.setText(currentItem.getRatingTimeStamp());

        holder.expandRatingItem.setOnClickListener(v -> {
            if (holder.expandRatingItem.getRotation()==0) {
                holder.expandRatingItem.setRotation(180);
                holder.reviewHeading.setVisibility(View.VISIBLE);
                holder.customerReview.setVisibility(View.VISIBLE);
            } else {
                holder.expandRatingItem.setRotation(0);
                holder.reviewHeading.setVisibility(View.GONE);
                holder.customerReview.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ratingsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView shopName, ratingTimeStamp, customerName, customerReview, reviewHeading;
        RatingBar ratingBar;
        CardView expandRatingItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.rating_shop_name);
            ratingTimeStamp = itemView.findViewById(R.id.rating_item_time_stamp);
            ratingBar = itemView.findViewById(R.id.rating_item_rating_bar);
            customerReview = itemView.findViewById(R.id.customer_review);
            reviewHeading = itemView.findViewById(R.id.review_heading);
            expandRatingItem = itemView.findViewById(R.id.rating_item_expand_button);
        }
    }
}