package com.blogspot.groglogs.sprescia.ui.stats.month;

import android.app.Application;

import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.ui.adapter.AbstractStatsAdapter;
import com.blogspot.groglogs.sprescia.ui.stats.BarChartType;
import com.blogspot.groglogs.sprescia.ui.stats.BarChartView;
import com.blogspot.groglogs.sprescia.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class MonthStatsAdapter extends AbstractStatsAdapter {

    //sorted by key = year+month, value=avg value for metric
    private TreeMap<LocalDate, Float> monthMap;
    //key = year+month, value=number of values for that month, used to calculate rolling average
    private Map<LocalDate, Integer> monthValuesCount;

    public MonthStatsAdapter(Application application, BarChartView barChartView) {
        super(application, barChartView);
    }

    public void loadAllItems(){
        super.loadAllItems();
        showStepsChart();
    }

    private void initMaps(){
        monthMap = new TreeMap<>();
        monthValuesCount = new HashMap<>();
    }

    private LocalDate getMonthYearByDate(LocalDate date){
        return LocalDate.of(date.getYear(), date.getMonthValue(), 1);
    }

    private float addToAverage(LocalDate monthYear, float val){
        float old = monthMap.getOrDefault(monthYear, 0f);

        return old + (val - old) / monthValuesCount.get(monthYear);
    }

    private void calcAvgByMonthAndYear(BarChartType barChartType, Function<RunViewItem, Float> valueFunc){
        initMaps();

        for(RunViewItem i : items){
            LocalDate monthYear = getMonthYearByDate(i.getDate());

            monthValuesCount.put(monthYear, monthValuesCount.getOrDefault(monthYear, 0) + 1);

            monthMap.put(monthYear, addToAverage(monthYear, valueFunc.apply(i)));
        }

        float[] values = new float[monthMap.size()];
        String[] labels = new String[monthMap.size()];

        int i = 0;
        for(Map.Entry<LocalDate, Float> avg : monthMap.entrySet()){
            values[i] = avg.getValue();
            labels[i] = StringUtils.monthYearStringFromDate(avg.getKey());
            i++;
        }

        setChartDataAndRefresh(barChartType, values, labels);
    }

    @Override
    public void showSpeedChart(){
        calcAvgByMonthAndYear(BarChartType.SPEED, RunViewItem::getKmh);
    }

    @Override
    public void showDistChart(){
        calcAvgByMonthAndYear(BarChartType.DISTANCE, RunViewItem::getKm);
    }

    @Override
    public void showTimeChart(){
        calcAvgByMonthAndYear(BarChartType.TIME, RunViewItem::getTimeAsDecimal);
    }

    @Override
    public void showStepsChart(){
        calcAvgByMonthAndYear(BarChartType.STEPS, runViewItem -> {
            int someIntegerValue = runViewItem.getSteps();
            return (float) someIntegerValue;
        });
    }
}
