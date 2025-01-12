package com.blogspot.groglogs.sprescia.ui.stats.general;

import android.app.Application;

import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.ui.adapter.AbstractStatsAdapter;
import com.blogspot.groglogs.sprescia.ui.stats.BarChartType;
import com.blogspot.groglogs.sprescia.ui.stats.BarChartView;

public class StatsAdapter extends AbstractStatsAdapter {

    public StatsAdapter(Application application, BarChartView barChartView) {
        super(application, barChartView);
    }

    public void loadAllItems(){
        super.loadAllItems();
        showStepsChart();
    }

    @Override
    public void showSpeedChart(){
        float[] values = new float[items.size()];
        String[] labels = new String[items.size()];

        for(int i = 0; i < items.size(); i++){
            RunViewItem e = items.get(i);

            values[i] = e.getKmh();
            labels[i] = e.getDate().toString();
        }

        setChartDataAndRefresh(BarChartType.SPEED, values, labels);
    }

    @Override
    public void showDistChart(){
        float[] values = new float[items.size()];
        String[] labels = new String[items.size()];

        for(int i = 0; i < items.size(); i++){
            RunViewItem e = items.get(i);

            values[i] = e.getKm();
            labels[i] = e.getDate().toString();
        }

        setChartDataAndRefresh(BarChartType.DISTANCE, values, labels);
    }

    @Override
    public void showTimeChart(){
        float[] values = new float[items.size()];
        String[] labels = new String[items.size()];

        for(int i = 0; i < items.size(); i++){
            RunViewItem e = items.get(i);

            values[i] = e.getTimeAsDecimal();
            labels[i] = e.getDate().toString();
        }

        setChartDataAndRefresh(BarChartType.TIME, values, labels);
    }

    @Override
    public void showStepsChart(){
        float[] values = new float[items.size()];
        String[] labels = new String[items.size()];

        for(int i = 0; i < items.size(); i++){
            RunViewItem e = items.get(i);

            values[i] = e.getSteps();
            labels[i] = e.getDate().toString();
        }

        setChartDataAndRefresh(BarChartType.STEPS, values, labels);
    }
}
