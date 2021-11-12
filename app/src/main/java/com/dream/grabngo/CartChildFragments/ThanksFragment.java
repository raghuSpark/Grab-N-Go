package com.dream.grabngo.CartChildFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.dream.grabngo.CartItemDetails;
import com.dream.grabngo.R;

import java.util.ArrayList;
import java.util.List;

public class ThanksFragment extends Fragment {

    private final Context context;
    private final FragmentManager childFragmentManager,supportFragmentManager;
    private Button backToCartButton;
    private final HorizontalStepView orderProgressStepsView;

    public ThanksFragment(Context context, FragmentManager supportFragmentManager,FragmentManager childFragmentManager, HorizontalStepView orderProgressStepsView) {
        this.context = context;
        this.childFragmentManager = childFragmentManager;
        this.supportFragmentManager = supportFragmentManager;
        this.orderProgressStepsView = orderProgressStepsView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupFragmentView = inflater.inflate(R.layout.fragment_thanks, container, false);
        backToCartButton = groupFragmentView.findViewById(R.id.back_to_cart_button);

        List<StepBean> steps = new ArrayList<>();
        steps.add(new StepBean("Order", 1));
        steps.add(new StepBean("Payment", 1));
        steps.add(new StepBean("Thanks", 0));

        orderProgressStepsView.setStepViewTexts(steps)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#373E8B"))
                .ondrawIndicator();

        backToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childFragmentManager.beginTransaction().replace(R.id.cart_child_fragments_container, new CartListFragment(context,supportFragmentManager,childFragmentManager,orderProgressStepsView)).commit();
            }
        });

        return groupFragmentView;
    }
}