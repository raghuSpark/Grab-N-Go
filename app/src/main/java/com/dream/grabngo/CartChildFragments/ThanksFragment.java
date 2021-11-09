package com.dream.grabngo.CartChildFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.dream.grabngo.R;

import java.util.ArrayList;
import java.util.List;

public class ThanksFragment extends Fragment {

    private final Context context;
    private final FragmentManager fragmentManager;
    private final HorizontalStepView orderProgressStepsView;
    private View groupFragmentView;

    public ThanksFragment(Context context, FragmentManager fragmentManager, HorizontalStepView orderProgressStepsView) {
        this.fragmentManager = fragmentManager;
        this.context = context;
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
        groupFragmentView = inflater.inflate(R.layout.fragment_thanks, container, false);

        List<StepBean> steps = new ArrayList<>();
        steps.add(new StepBean("Order", 1));
        steps.add(new StepBean("Payment", 1));
        steps.add(new StepBean("Thanks", 0));

        orderProgressStepsView.setStepViewTexts(steps)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#373E8B"))
                .ondrawIndicator();

        return groupFragmentView;
    }
}