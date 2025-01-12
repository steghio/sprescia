package com.blogspot.groglogs.sprescia.ui.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.ui.adapter.AbstractStatsAdapter;
import com.blogspot.groglogs.sprescia.ui.menu.StatsTopMenu;
import com.blogspot.groglogs.sprescia.ui.stats.BarChartView;
import com.blogspot.groglogs.sprescia.ui.stats.general.StatsAdapter;
import com.blogspot.groglogs.sprescia.ui.stats.month.MonthStatsAdapter;

//todo sort by date asc/desc
public class AbstractStatsFragment extends Fragment {

    protected AbstractStatsAdapter adapter;
    private final int fragmentResId;
    private final boolean isGeneralStats;

    public AbstractStatsFragment(int fragmentResId, boolean isGeneralStats){
        this.fragmentResId = fragmentResId;
        this.isGeneralStats = isGeneralStats;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(fragmentResId, container, false);

        BarChartView barChartView = rootView.findViewById(R.id.bar_chart);

        adapter = isGeneralStats ?
                new StatsAdapter(getActivity().getApplication(), barChartView) :
                new MonthStatsAdapter(getActivity().getApplication(), barChartView);

        requireActivity().addMenuProvider(new StatsTopMenu(requireContext(), adapter), getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        requireActivity().invalidateOptionsMenu();

        //get the height of the bottom navigation menu
        int bottomNavigationHeight = getBottomNavigationHeight();

        //pass the height to the BarChartView
        barChartView.setBottomNavigationHeight(bottomNavigationHeight);

        adapter.loadAllItems();
        adapter.showSpeedChart();

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
