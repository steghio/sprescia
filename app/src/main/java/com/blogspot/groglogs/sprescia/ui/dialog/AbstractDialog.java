package com.blogspot.groglogs.sprescia.ui.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.ui.adapter.AbstractAdapter;
import com.blogspot.groglogs.sprescia.util.DateTimeUtils;

import java.time.LocalDate;

import lombok.Getter;

public abstract class AbstractDialog {
    @Getter
    protected final View dialogView;
    protected long id;
    protected int position;
    protected final EditText editTextKm;
    protected final EditText editTextDate;
    protected final EditText editTextTime;
    protected final AbstractAdapter adapter;

    public AbstractDialog(Context context, AbstractAdapter adapter, int dialog){
        this.id = -1;
        this.position = -1;

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        this.dialogView = layoutInflater.inflate(dialog, null);

        this.editTextKm = dialogView.findViewById(R.id.editTextKm);
        this.editTextDate = dialogView.findViewById(R.id.editTextDate);
        this.editTextTime = dialogView.findViewById(R.id.editTextTime);

        this.adapter = adapter;
    }

    public double getKm(){
        return Double.parseDouble(this.editTextKm.getText().toString());
    }

    public LocalDate getDate(){
        return DateTimeUtils.fromString(this.editTextDate.getText().toString());
    }

    public int getHours(){
        return DateTimeUtils.hoursFromString(this.editTextTime.getText().toString());
    }

    public int getMinutes(){
        return DateTimeUtils.minutesFromString(this.editTextTime.getText().toString());
    }

    /**
     * month in date picker is 0-based so we need to handle +/- 1 from LocalDate
     * @param context
     * @param date default date to display
     */
    public void addDatePicker(Context context, LocalDate date){
        this.editTextDate.setText(date.toString());

        this.editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        this.editTextDate.setText(DateTimeUtils.stringFrom(selectedYear, selectedMonth + 1, selectedDay));
                    },
                    date.getYear(),
                    date.getMonthValue() - 1,
                    date.getDayOfMonth()
            );
            datePickerDialog.show();
        });
    }

    /**
     * @param context
     * @param hours
     * @param minutes
     */
    public void addTimePicker(Context context, int hours, int minutes){
        this.editTextTime.setText(DateTimeUtils.timeStringFrom(hours, minutes));

        this.editTextTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view, hourOfDay, minute) -> this.editTextTime.setText(DateTimeUtils.timeStringFrom(hourOfDay, minute)),
                    hours,
                    minutes,
                    true
            );
            timePickerDialog.show();
        });
    }

    public abstract View.OnClickListener getSubmitButtonWithValidation(AlertDialog d, boolean isUpdate);
}
