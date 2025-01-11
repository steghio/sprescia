package com.blogspot.groglogs.sprescia.ui.run;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.ui.dialog.AbstractDialog;
import com.blogspot.groglogs.sprescia.util.DateTimeUtils;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class RunDialog extends AbstractDialog {

    public RunDialog(Context context, RunAdapter runAdapter){
        super(context, runAdapter, R.layout.dialog_run);
    }

    public void fillDialog(Context context, RunViewItem f, int position){
        this.id = f.getId();
        this.position = position;

        this.editTextKm.setText(String.valueOf(f.getKm()));

        this.addDatePicker(context, f.getDate());
        this.addTimePicker(context, f.getHours(), f.getMinutes());
    }

    public View.OnClickListener getSubmitButtonWithValidation(AlertDialog d, boolean isUpdate){
        return v -> {
            double km = -1;
            int hours = 0;
            int minutes = 0;
            LocalDate date = null;

            boolean isError = false;

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
                        RunItem i = new RunItem(km, hours, minutes, date);
                        i.setId(this.id);
                        adapter.updateEntity(i, this.position);

                        Toast.makeText(d.getContext(), "Run updated", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    RunItem i = new RunItem(km, hours, minutes, date);
                    adapter.saveEntityAndRefreshView(i);

                    Toast.makeText(d.getContext(), "Run added", Toast.LENGTH_SHORT).show();
                }

                d.dismiss();
            }
        };
    }
}
