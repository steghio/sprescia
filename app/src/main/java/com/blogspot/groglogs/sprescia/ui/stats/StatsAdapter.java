package com.blogspot.groglogs.sprescia.ui.stats;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.storage.db.repository.RunRepository;
import com.blogspot.groglogs.sprescia.ui.adapter.AbstractAdapter;
import com.blogspot.groglogs.sprescia.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.Getter;

public class StatsAdapter {

    @Getter
    private List<RunViewItem> items;

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

        float[] values = new float[entities.size()];
        String[] labels = new String[entities.size()];

        for(int i = 0; i < entities.size(); i++){
            RunItem e = entities.get(i);

            items.add(new RunViewItem(e.getId(), e.getKm(), e.getHours(), e.getMinutes(), e.getDate()));

            values[i] = (float) e.calculateKmH();
            labels[i] = e.getDate().toString();
        }

        setChartDataAndRefresh(BarChartType.SPEED, values, labels);
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
}
