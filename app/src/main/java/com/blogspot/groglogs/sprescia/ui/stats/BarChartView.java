package com.blogspot.groglogs.sprescia.ui.stats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.blogspot.groglogs.sprescia.util.StringUtils;

import lombok.Setter;

//todo button to change stat displayed
public class BarChartView extends View {

    private Paint barPaint;
    private Paint textPaint;

    @Setter
    private float[] values;
    @Setter
    private String[] labels;
    private int bottomNavigationHeight = 0; // Height of the bottom navigation menu

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Paint for bars
        barPaint = new Paint();
        barPaint.setColor(Color.BLUE);
        barPaint.setStyle(Paint.Style.FILL);

        // Paint for text
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50f); // Increase text size for better visibility
        textPaint.setTextAlign(Paint.Align.CENTER); // Center align text
    }

    // Method to set the bottom navigation menu height
    public void setBottomNavigationHeight(int height) {
        this.bottomNavigationHeight = height;
        invalidate(); // Redraw the view when the height is set
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float barWidth = getWidth() / (values.length * 2f); // Space for each bar
        float maxHeight = getHeight() * 0.6f; // Reserve 60% of the height for bars
        float maxValue = getMaxValue(values); // Get the maximum value in the dataset

        // Calculate the height available for drawing, considering the bottom navigation
        float reservedBottomSpace = bottomNavigationHeight + 100; // 100 for labels and padding
        float chartBottom = getHeight() - reservedBottomSpace;

        for (int i = 0; i < values.length; i++) {
            // Calculate bar dimensions
            float left = i * 2 * barWidth + barWidth / 4;
            float top = chartBottom - ((values[i] / maxValue) * maxHeight); // Top of the bar
            float right = left + barWidth;
            float bottom = chartBottom; // Bottom of the bar

            // Draw the bar
            canvas.drawRect(left, top, right, bottom, barPaint);

            // Draw the value on top of the bar
            float textX = left + barWidth / 2; // Center the text on the bar
            float textY = top - 20; // Position the text slightly above the bar
            canvas.drawText(StringUtils.decimal2String2Precision((double) values[i]) + " km/h", textX, textY, textPaint);

            // Draw the label below the bar
            textX = left + barWidth / 2; // Center of the bar
            textY = getHeight() - bottomNavigationHeight - 50; // Adjust position below the bar
            canvas.drawText(labels[i], textX, textY, textPaint);
        }
    }

    // Helper method to get the maximum value from the dataset
    private float getMaxValue(float[] values) {
        float max = Float.MIN_VALUE;
        for (float value : values) {
            if (value > max) max = value;
        }
        return max;
    }
}
