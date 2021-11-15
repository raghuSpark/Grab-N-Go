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

import com.dream.grabngo.CustomClasses.CartItemDetails;
import com.dream.grabngo.CustomClasses.ShopWiseCartItemsDetails;
import com.dream.grabngo.CustomClasses.ShoppingItemDetails;
import com.dream.grabngo.R;
import com.dream.grabngo.utils.SharedPrefConfig;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ItemExpandedFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private final ShoppingItemDetails shoppingItem;
    private final CartItemDetails cartItem;
    private View groupFragmentView;
    private CardView backButton, increaseQuantityButton, decreaseQuantityButton;
    private Button addToCartButton;
    private TextView itemNameTextView, itemInStockQuantity, itemPriceTextView, orderQuantityTextView, shopRatingTextView, shopNameTextView, shop_details_button;
    private RatingBar ratingBar;

    public ItemExpandedFragment(FragmentManager supportFragmentManager, ShoppingItemDetails shoppingItem, CartItemDetails cartItem) {
        this.supportFragmentManager = supportFragmentManager;
        this.shoppingItem = shoppingItem;
        this.cartItem = cartItem;
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
        shopNameTextView = groupFragmentView.findViewById(R.id.expanded_item_store_name_text_view);
        shop_details_button = groupFragmentView.findViewById(R.id.expanded_item_shop_details_button);
        increaseQuantityButton = groupFragmentView.findViewById(R.id.expanded_item_quantity_increase_button);
        decreaseQuantityButton = groupFragmentView.findViewById(R.id.expanded_item_quantity_decrease_button);
        orderQuantityTextView = groupFragmentView.findViewById(R.id.expanded_item_add_to_cart_quantity);
        addToCartButton = groupFragmentView.findViewById(R.id.expanded_item_add_to_cart_button);

        setDetailsToViewsAccordingly(shoppingItem, cartItem);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new HomeFragment(supportFragmentManager, 1)).commit();
            }
        });

        shop_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopID, shopName;
                if (shoppingItem == null) {
                    shopID = cartItem.getShopId();
                    shopName = cartItem.getShopName();
                } else {
                    shopID = shoppingItem.getShopId();
                    shopName = shoppingItem.getShopName();
                }
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new ShopDetailsFragment(getContext(), supportFragmentManager, shopID, shopName)).commit();
            }
        });

        addToCartButton.setOnClickListener(v -> {
            Double itemPrice = Double.parseDouble(itemPriceTextView.getText().toString().substring(1));
            int orderQuantity = Integer.parseInt(orderQuantityTextView.getText().toString());
            ArrayList<ShopWiseCartItemsDetails> shopWiseCartItemsDetailsArrayList = SharedPrefConfig.readShopWiseCartItems(requireContext());

            boolean isShopAlreadyPresent = false;
            ArrayList<CartItemDetails> cartItemDetailsArrayList = new ArrayList<>();
            int pos = 0;
            if (shoppingItem == null) {
                for (int i = 0; i < shopWiseCartItemsDetailsArrayList.size(); i++) {
                    ShopWiseCartItemsDetails item = shopWiseCartItemsDetailsArrayList.get(i);
                    if (item.getShopId().equals(cartItem.getShopId())) {
                        cartItemDetailsArrayList = item.getCartItemDetailsArrayList();
                        pos = i;
                        isShopAlreadyPresent = true;
                        break;
                    }
                }
                cartItemDetailsArrayList.add(new CartItemDetails(cartItem.getShopId(), cartItem.getShopName(), itemNameTextView.getText().toString(), itemPrice, cartItem.getAvailableQuantity(), orderQuantity));
                if (!isShopAlreadyPresent) {
                    shopWiseCartItemsDetailsArrayList.add(new ShopWiseCartItemsDetails(cartItem.getShopId(), cartItem.getShopName(), cartItemDetailsArrayList));
                } else {
                    shopWiseCartItemsDetailsArrayList.get(pos).setCartItemDetailsArrayList(cartItemDetailsArrayList);
                }
            } else {
                for (int i = 0; i < shopWiseCartItemsDetailsArrayList.size(); i++) {
                    ShopWiseCartItemsDetails item = shopWiseCartItemsDetailsArrayList.get(i);
                    if (item.getShopId().equals(shoppingItem.getShopId())) {
                        cartItemDetailsArrayList = item.getCartItemDetailsArrayList();
                        pos = i;
                        isShopAlreadyPresent = true;
                        break;
                    }
                }
                cartItemDetailsArrayList.add(new CartItemDetails(shoppingItem.getShopId(), shoppingItem.getShopName(), itemNameTextView.getText().toString(), itemPrice, shoppingItem.getAvailableQuantity(), orderQuantity));
                if (!isShopAlreadyPresent) {
                    shopWiseCartItemsDetailsArrayList.add(new ShopWiseCartItemsDetails(shoppingItem.getShopId(), shoppingItem.getShopName(), cartItemDetailsArrayList));
                } else {
                    shopWiseCartItemsDetailsArrayList.get(pos).setCartItemDetailsArrayList(cartItemDetailsArrayList);
                }
            }

            SharedPrefConfig.writeShopWiseCartItems(requireContext(), shopWiseCartItemsDetailsArrayList);
            Snackbar.make(groupFragmentView, "Item added to cart!", Snackbar.LENGTH_SHORT).show();
        });

        decreaseQuantityButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(orderQuantityTextView.getText().toString());
            if (quantity > 1) {
                quantity--;
            }
            orderQuantityTextView.setText(String.valueOf(quantity));
        });

        increaseQuantityButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(orderQuantityTextView.getText().toString());
            if (quantity < shoppingItem.getAvailableQuantity()) {
                quantity++;
            }
            orderQuantityTextView.setText(String.valueOf(quantity));
        });

        return groupFragmentView;
    }

    private void setDetailsToViewsAccordingly(ShoppingItemDetails shoppingItem, CartItemDetails cartItem) {
        if (shoppingItem != null) {
            itemNameTextView.setText(shoppingItem.getItemName());
            itemInStockQuantity.setText(String.valueOf(shoppingItem.getAvailableQuantity()));
            itemPriceTextView.setText(String.format("₹ %s", shoppingItem.getItemPrice()));
            shopNameTextView.setText(shoppingItem.getShopName());
        } else {
            itemNameTextView.setText(cartItem.getItemName());
            itemInStockQuantity.setText(String.valueOf(cartItem.getAvailableQuantity()));
            itemPriceTextView.setText(String.format("₹ %s", cartItem.getItemPrice()));
            shopNameTextView.setText(cartItem.getShopName());
        }
    }
}