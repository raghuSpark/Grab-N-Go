package com.dream.grabngo.MainFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dream.grabngo.R;

public class ShopDetailsFragment extends Fragment {

    private final FragmentManager supportFragmentManager;
    private CardView backButton, callButton, mailButton;
    private ImageView shopImageView;
    private TextView shopName, aboutShop, address1, address2, cityAndPinCode, ratingTextView;
    private RatingBar ratingBar;

    public ShopDetailsFragment(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View groupFragmentView = inflater.inflate(R.layout.fragment_shop_details, container, false);
        backButton = groupFragmentView.findViewById(R.id.shop_details_back_button);
        callButton = groupFragmentView.findViewById(R.id.shop_details_call_button);
        mailButton = groupFragmentView.findViewById(R.id.shop_details_mail_button);
        shopImageView = groupFragmentView.findViewById(R.id.shop_details_image_view);
        shopName = groupFragmentView.findViewById(R.id.shop_details_name_text_view);
        aboutShop = groupFragmentView.findViewById(R.id.shop_details_about_text_view);
        address1 = groupFragmentView.findViewById(R.id.shop_details_address_1);
        address2 = groupFragmentView.findViewById(R.id.shop_details_address_2);
        cityAndPinCode = groupFragmentView.findViewById(R.id.shop_details_city_pin_code);
        ratingBar = groupFragmentView.findViewById(R.id.shop_details_store_rating_bar);
        ratingTextView = groupFragmentView.findViewById(R.id.shop_details_rating_text_view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragments_container, new HomeFragment(supportFragmentManager, 1)).commit();
            }
        });

        return groupFragmentView;
    }
}