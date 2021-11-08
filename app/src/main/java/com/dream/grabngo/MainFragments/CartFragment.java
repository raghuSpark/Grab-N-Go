package com.dream.grabngo.MainFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.anton46.stepsview.StepsView;
import com.dream.grabngo.CartChildFragments.CartListFragment;
import com.dream.grabngo.R;

public class CartFragment extends Fragment {

    private View groupFragmentView;

    public CartFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView = inflater.inflate(R.layout.fragment_cart, container, false);
        StepsView progressSteps = groupFragmentView.findViewById(R.id.progress_steps);

//        ListView mListView = groupFragmentView.findViewById(R.id.progress_steps);
        MyAdapter adapter = new MyAdapter(requireContext(), 0);
        adapter.addAll("");
//        mListView.setAdapter(adapter);

//        Working with child fragments

        getChildFragmentManager().beginTransaction().add(R.id.cart_child_fragments_container, new CartListFragment(getContext(), getChildFragmentManager())).commit();

        return groupFragmentView;
    }

    public static class MyAdapter extends ArrayAdapter<String> {

        private final String[] steps = {"1", "2", "3"};

        public MyAdapter(Context context, int resource) {
            super(context, resource);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.step_view, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.mStepsView.setCompletedPosition(position % steps.length)
                    .setLabels(steps)
                    .setBarColorIndicator(
                            getContext().getColor(R.color.steps_bar_color))
                    .setProgressColorIndicator(getContext().getColor(R.color.steps_progress_color))
                    .setLabelColorIndicator(getContext().getColor(R.color.primary_blue))
                    .drawView();
            return convertView;
        }

        static class ViewHolder {
            StepsView mStepsView;

            public ViewHolder(View view) {
                mStepsView = view.findViewById(R.id.stepsView);
            }
        }
    }
}