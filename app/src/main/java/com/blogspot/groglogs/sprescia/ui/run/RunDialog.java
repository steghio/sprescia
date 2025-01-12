package com.blogspot.groglogs.sprescia.ui.run;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.util.DateTimeUtils;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class RunDialog {

    @Getter
    protected final View dialogView;
    protected long id;
    protected int position;
    protected final EditText editTextSteps;
    protected final EditText editTextKm;
    protected final EditText editTextDate;
    protected final EditText editTextTime;
    protected final RunAdapter adapter;

    public RunDialog(Context context, RunAdapter adapter){
        this.id = -1;
        this.position = -1;

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        this.dialogView = layoutInflater.inflate(R.layout.dialog_run, null);

        this.editTextSteps = dialogView.findViewById(R.id.editTextSteps);
        this.editTextKm = dialogView.findViewById(R.id.editTextKm);
        this.editTextDate = dialogView.findViewById(R.id.editTextDate);
        this.editTextTime = dialogView.findViewById(R.id.editTextTime);

        this.adapter = adapter;
    }

    public void fillDialog(Context context, RunViewItem f, int position){
        this.id = f.getId();
        this.position = position;

        this.editTextSteps.setText(String.valueOf(f.getSteps()));
        this.editTextKm.setText(String.valueOf(f.getKm()));

        this.addDatePicker(context, f.getDate());
        this.addTimePicker(context, f.getHours(), f.getMinutes());
    }

    public View.OnClickListener getSubmitButtonWithValidation(AlertDialog d, boolean isUpdate){
        return v -> {
            double km = -1;
            int steps = 0;
            int hours = 0;
            int minutes = 0;
            LocalDate date = null;

            boolean isError = false;

            try {
                steps = this.getSteps();
            } catch (NumberFormatException e) {
                this.editTextSteps.setError("Please enter a positive value for Steps");
                isError = true;
            }

            try {
                km = this.getKm();
            } catch (NumberFormatException e) {
                this.editTextKm.setError("Please enter a positive value for Km");
                isError = true;
            }

            try{
                date = this.getDate();
            } catch (Exception e){
                this.editTextDate.setError("Invalid Date");
                isError = true;
            }

            try{
                hours = this.getHours();
                minutes = this.getMinutes();
            } catch (Exception e){
                this.editTextTime.setError("Invalid Time");
                isError = true;
            }

            if(hours <= 0 && minutes <= 0){
                this.editTextTime.setError("Invalid Time");
                isError = true;
            }

            if(!isError){
                if(isUpdate){
                    if(this.id == -1){
                        Toast.makeText(d.getContext(), "Error update row with no ID!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        RunItem i = new RunItem(km, steps, hours, minutes, date);
                        i.setId(this.id);
                        adapter.updateEntity(i, this.position);

                        Toast.makeText(d.getContext(), "Run updated", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    RunItem i = new RunItem(km, steps, hours, minutes, date);
                    adapter.saveEntityAndRefreshView(i);

                    Toast.makeText(d.getContext(), "Run added", Toast.LENGTH_SHORT).show();
                }

                d.dismiss();
            }
        };
    }



    public int getSteps(){
        return Integer.parseInt(this.editTextSteps.getText().toString());
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
     * @param context the context
     * @param date default date to display
     */
    public void addDatePicker(Context context, LocalDate date){
        this.editTextDate.setText(date.toString());

        this.editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, selectedYear, selectedMonth, selectedDay) -> this.editTextDate.setText(
                            DateTimeUtils.stringFrom(selectedYear,
                                    selectedMonth + 1,
                                    selectedDay)),
                    date.getYear(),
                    date.getMonthValue() - 1,
                    date.getDayOfMonth()
            );
            datePickerDialog.show();
        });
    }

    /**
     * @param context the context
     * @param hours default hour to display
     * @param minutes default minute to display
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
}
