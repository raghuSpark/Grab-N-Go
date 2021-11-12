package com.dream.grabngo.MainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.CartItemDetails;
import com.dream.grabngo.R;
import com.dream.grabngo.SharedPrefConfig;

import java.util.ArrayList;

public class ItemExpandedFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private View groupFragmentView;
    private CardView backButton, increaseQuantityButton, decreaseQuantityButton;
    private Button addToCartButton;
    private TextView itemNameTextView, itemInStockQuantity, itemPriceTextView, addToCartQuantityTextView, ratingTextView, shop_details_button;
    private RatingBar ratingBar;

    public ItemExpandedFragment(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupFragmentView = inflater.inflate(R.layout.fragment_item_expanded, container, false);
        backButton = groupFragmentView.findViewById(R.id.expanded_item_back_button);
        itemNameTextView = groupFragmentView.findViewById(R.id.expanded_item_name_text_view);
        itemPriceTextView = groupFragmentView.findViewById(R.id.expanded_item_price_text_view);
        itemInStockQuantity = groupFragmentView.findViewById(R.id.expanded_item_in_stock_quantity_text_view);
        ratingBar = groupFragmentView.findViewById(R.id.expanded_item_store_rating_bar);
        ratingTextView = groupFragmentView.findViewById(R.id.expanded_item_rating_text_view);
        shop_details_button = groupFragmentView.findViewById(R.id.expanded_item_shop_details_button);
        increaseQuantityButton = groupFragmentView.findViewById(R.id.expanded_item_quantity_increase_button);
        decreaseQuantityButton = groupFragmentView.findViewById(R.id.expanded_item_quantity_decrease_button);
        addToCartQuantityTextView = groupFragmentView.findViewById(R.id.expanded_item_add_to_cart_quantity);
        addToCartButton = groupFragmentView.findViewById(R.id.expanded_item_add_to_cart_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new HomeFragment(supportFragmentManager, 1)).commit();
            }
        });

        shop_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ShopDetailsFragment(supportFragmentManager)).commit();
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double itemPrice = Double.parseDouble(itemPriceTextView.getText().toString().substring(1));
                int addToCartQuantity = Integer.parseInt(addToCartQuantityTextView.getText().toString());
                ArrayList<CartItemDetails> cartItemArrayList = SharedPrefConfig.readCartItems(requireContext());
                cartItemArrayList.add(new CartItemDetails(itemNameTextView.getText().toString(), itemPrice, addToCartQuantity));
                SharedPrefConfig.writeCartItems(requireContext(), cartItemArrayList);
                // TODO: Snackbar
            }
        });

        return groupFragmentView;
    }
}