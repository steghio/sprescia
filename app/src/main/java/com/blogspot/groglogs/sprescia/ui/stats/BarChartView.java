package com.blogspot.groglogs.sprescia.ui.stats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blogspot.groglogs.sprescia.util.StringUtils;

import lombok.Setter;

public class BarChartView extends View {

    private Paint barPaint;
    private Paint greenBarPaint;
    private Paint redBarPaint;
    private Paint textPaint;

    @Setter
    private BarChartType barChartType;
    @Setter
    private float[] values;
    @Setter
    private String[] labels;
    @Setter
    private int bottomNavigationHeight;

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.bottomNavigationHeight = 0;
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(Color.BLUE);
        barPaint.setStyle(Paint.Style.FILL);

        greenBarPaint = new Paint();
        greenBarPaint.setColor(Color.GREEN);
        greenBarPaint.setStyle(Paint.Style.FILL);

        redBarPaint = new Paint();
        redBarPaint.setColor(Color.RED);
        redBarPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float barWidth = getWidth() / (values.length * 2f); //space for each bar
        float maxHeight = getHeight() * 0.6f; //reserve 60% of the height for bars
        float maxValue = getMaxValue(values); //get the maximum value in the dataset

        //calculate the height available for drawing, considering the bottom navigation
        float reservedBottomSpace = bottomNavigationHeight + 100; //spece for labels and padding
        float chartBottom = getHeight() - reservedBottomSpace;

        for (int i = 0; i < values.length; i++) {
            //calculate bar dimensions
            float left = i * 2 * barWidth + barWidth / 4;
            float right = left + barWidth;
            float top = chartBottom - ((values[i] / maxValue) * maxHeight); //top of the bar
            //bottom is already set by the caller considering the navigation menu space

            Paint paint = barPaint;
            if(i > 0) {
                if (values[i - 1] < values[i]) {
                    paint = greenBarPaint;
                } else if (values[i - 1] > values[i]) {
                    paint =  redBarPaint;
                }
            }

            canvas.drawRect(left, top, right, chartBottom, paint);

            //top of bar label (value)
            float textX = left + barWidth / 2; //center the text on the bar
            float textY = top - 20; //position the text slightly above the bar
            String label = "";
            switch (barChartType){
                case SPEED -> label = " km/h";
                case DISTANCE -> label = " km";
                case TIME -> label = " hours";
            }

            canvas.drawText(StringUtils.decimal2String2Precision(values[i]) + label, textX, textY, textPaint);

            //bottom of bar label
            textX = left + barWidth / 2; //center the text on the bar
            textY = getHeight() - bottomNavigationHeight - 50; //position the text slightly below the bar, considering bottom menu

            canvas.drawText(labels[i], textX, textY, textPaint);
        }
    }

    private float getMaxValue(float[] values) {
        float max = Float.MIN_VALUE;

        for (float value : values) {
            if (value > max) max = value;
        }

        return max;
    }
}
