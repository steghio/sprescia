package com.blogspot.groglogs.sprescia.ui.adapter;

import android.app.Application;
import android.widget.Toast;

import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.storage.db.repository.RunRepository;
import com.blogspot.groglogs.sprescia.ui.stats.BarChartType;
import com.blogspot.groglogs.sprescia.ui.stats.BarChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.Getter;

public abstract class AbstractStatsAdapter {

    @Getter
    protected final List<RunViewItem> items;

    private final RunRepository runRepository;

    private final BarChartView barChartView;

    public AbstractStatsAdapter(Application application, BarChartView barChartView) {
        this.items = new ArrayList<>();
        this.runRepository = new RunRepository(application);
        this.barChartView = barChartView;
    }

    public void loadAllItems(){
        List<RunItem> entities;

        try {
            entities = runRepository.getAllItemsByDateAsc();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(barChartView.getContext(), "Error loading items", Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }

        for(RunItem e : entities){
            items.add(new RunViewItem(e.getId(), e.getKm(), e.getSteps(), e.getHours(), e.getMinutes(), e.getDate()));
        }
    }

    public void setChartDataAndRefresh(BarChartType barChartType, float[] values, String[] labels){
        barChartView.setBarChartType(barChartType);
        barChartView.setValues(values);
        barChartView.setLabels(labels);
        barChartView.invalidate();
    }

    public abstract void showSpeedChart();

    public abstract void showDistChart();

    public abstract void showTimeChart();

    public abstract void showStepsChart();
}
