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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //calculate height based on the number of bars and bar height
        int desiredHeight = values.length * 150; //include gaps between bars
        int height = resolveSize(desiredHeight, heightMeasureSpec);

        //set measured dimensions
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float maxBarWidth = getWidth() * 0.8f; //reserve 80% of the width for bars
        float maxValue = getMaxValue(values); //get the maximum value in the dataset

        //calculate the height available for drawing
        float reservedBottomSpace = bottomNavigationHeight + 50; //bottom menu + padding
        float chartHeight = getHeight() - reservedBottomSpace;
        float barHeight = chartHeight / (values.length * 2f); //space for each bar (and gaps)

        for (int i = 0; i < values.length; i++) {
            //calculate bar dimensions
            float top = i * 2 * barHeight;
            float left = 0; //bars start from the left edge
            float right = (values[i] / maxValue) * maxBarWidth; //width of the bar
            float bottom = top + barHeight;

            Paint paint = barPaint;
            if(i > 0) {
                if (values[i - 1] < values[i]) {
                    paint = greenBarPaint;
                } else if (values[i - 1] > values[i]) {
                    paint =  redBarPaint;
                }
            }

            canvas.drawRect(left, top, right, bottom, paint);

            //show label on the left of the bar
            float labelX = left + 100;
            float textY = top + barHeight / 2 + 15; //center the text vertically in the bar

            canvas.drawText(labels[i], labelX, textY, textPaint);

            //show value label inside or at the end of the bar
            float textX = Math.max(right + 50, 300); //text to the right of the bar or spaced from the label
            String label = "";
            switch (barChartType){
                case SPEED -> label = " km/h";
                case DISTANCE -> label = " km";
                case TIME -> label = " hours";
            }

            canvas.drawText(StringUtils.decimal2String2Precision(values[i]) + label, textX, textY, textPaint);
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
