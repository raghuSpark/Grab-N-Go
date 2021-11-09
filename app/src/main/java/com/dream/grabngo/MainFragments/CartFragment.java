package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.dream.grabngo.CartChildFragments.CartListFragment;
import com.dream.grabngo.R;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private View groupFragmentView;
    private HorizontalStepView orderProgressStepsView;

    public CartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_cart, container, false);
        orderProgressStepsView = groupFragmentView.findViewById(R.id.order_progress_steps);

        List<StepBean> steps = new ArrayList<>();
        steps.add(new StepBean("Order", -1));
        steps.add(new StepBean("Payment", -1));
        steps.add(new StepBean("Thanks", -1));

        orderProgressStepsView
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#AAAAAA"))
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#AAAAAA"))
                .setStepsViewIndicatorDefaultIcon(getResources().getDrawable(R.drawable.ic_in_complete_step, null))
                .setStepsViewIndicatorCompleteIcon(getResources().getDrawable(R.drawable.ic_added_to_cart, null))
                .setStepViewTexts(steps)
                .ondrawIndicator();

//        Working with child fragments
        getChildFragmentManager().beginTransaction().add(R.id.cart_child_fragments_container, new CartListFragment(getContext(), getChildFragmentManager(), orderProgressStepsView)).commit();

        return groupFragmentView;
    }
}