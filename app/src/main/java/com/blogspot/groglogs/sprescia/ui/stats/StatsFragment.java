package com.blogspot.groglogs.sprescia.ui.stats;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.ui.fragment.AbstractFragment;

public class StatsFragment extends AbstractFragment {

    private StatsAdapter statsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.stats, container, false);

        BarChartView barChartView = rootView.findViewById(R.id.bar_chart);

        statsAdapter = new StatsAdapter(getActivity().getApplication(), barChartView);

        statsAdapter.loadAllItems();

        //get the height of the bottom navigation menu
        int bottomNavigationHeight = getBottomNavigationHeight();

        //pass the height to the BarChartView
        barChartView.setBottomNavigationHeight(bottomNavigationHeight);

        return rootView;
    }

    private int getBottomNavigationHeight() {
        TypedValue typedValue = new TypedValue();

        if (requireActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
            return TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }

        //default on error
        return 0;
    }
}