package com.blogspot.groglogs.sprescia.ui.stats;

import android.app.Application;
import android.widget.Toast;

import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.storage.db.repository.RunRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.Getter;

public class StatsAdapter {

    @Getter
    private final List<RunViewItem> items;

    private final RunRepository runRepository;

    private final BarChartView barChartView;

    public StatsAdapter(Application application, BarChartView barChartView) {
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

        showStepsChart();
    }

    public void setChartDataAndRefresh(BarChartType barChartType, float[] values, String[] labels){
        barChartView.setBarChartType(barChartType);
        barChartView.setValues(values);
        barChartView.setLabels(labels);
        barChartView.invalidate();
    }

    public void showSpeedChart(){
        float[] values = new float[items.size()];
        String[] labels = new String[items.size()];

        for(int i = 0; i < items.size(); i++){
            RunViewItem e = items.get(i);

            values[i] = (float) e.getKmh();
            labels[i] = e.getDate().toString();
        }

        setChartDataAndRefresh(BarChartType.SPEED, values, labels);
    }

    public void showDistChart(){
        float[] values = new float[items.size()];
        String[] labels = new String[items.size()];

        for(int i = 0; i < items.size(); i++){
            RunViewItem e = items.get(i);

            values[i] = (float) e.getKm();
            labels[i] = e.getDate().toString();
        }

        setChartDataAndRefresh(BarChartType.DISTANCE, values, labels);
    }

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
