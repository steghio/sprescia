package com.blogspot.groglogs.sprescia.ui.run;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.storage.db.repository.RunRepository;
import com.blogspot.groglogs.sprescia.util.DateTimeUtils;
import com.blogspot.groglogs.sprescia.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import lombok.Getter;

public class RunAdapter extends RecyclerView.Adapter<RunViewHolder> {

    private final RecyclerView recyclerView;

    @Getter
    private List<RunViewItem> items;

    //todo if possible to track better structure to modify only what necessary on data changes to display correct kmh comparison
    //key = item position, value = kmh for item
    private Map<Integer,Double> kmhMap;

    private final RunRepository runRepository;

    public RunAdapter(Application application, RecyclerView recyclerView) {
        this.items = new ArrayList<>();
        this.kmhMap = new HashMap<>();
        this.runRepository = new RunRepository(application);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_run, parent, false);

        return new RunViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RunViewHolder holder, int position) {
        RunViewItem item = items.get(position);
        holder.getStepsIconImageView().setImageResource(item.getStepsIconResId());
        holder.getStepsTextView().setText(StringUtils.formatIntegerWithThousandSeparator(item.getSteps()));
        holder.getKmIconImageView().setImageResource(item.getKmIconResId());
        holder.getKmTextView().setText(recyclerView.getContext().getString(R.string.display_km, item.getKm()));
        holder.getTimeIconImageView().setImageResource(item.getTimeIconResId());
        holder.getTimeTextView().setText(DateTimeUtils.timeStringFrom(item.getHours(), item.getMinutes()));
        holder.getDateTextView().setText(item.getDate().toString());

        int img;
        Double kmh = kmhMap.get(position);
        Double prevKmh = kmhMap.get(position + 1);

        if(prevKmh == null || Math.abs(prevKmh - kmh) == 0.0){
            img = R.drawable.ic_chart_flat_24dp;
        }
        else if(prevKmh - kmh < 0.1){
            img = R.drawable.ic_chart_up_24dp;
        }
        else{
            img = R.drawable.ic_chart_down_24dp;
        }

        holder.getKmhIconImageView().setImageResource(img);
        holder.getKmhTextView().setText(recyclerView.getContext().getString(R.string.display_kmh, kmh));

        holder.getEditButton().setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "EDIT ID: " + item.getId() + " - POS: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                showUpdateDialog(view.getContext(), item, holder.getAdapterPosition());
            });

        holder.getDeleteButton().setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "DELETE ID: " + item.getId() + " - POS: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                deleteItem(item);
            });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //load asc but display desc
    public void loadAllItems(){
        this.items = new ArrayList<>();
        this.kmhMap = new HashMap<>();

        this.recyclerView.post(this::notifyDataSetChanged);

        List<RunItem> entities = loadAndCalculateKmh();

        for(int i = 0; i < entities.size(); i++){
            addEntity(entities.get(i));
        }
    }

    private List<RunItem> loadAndCalculateKmh(){
        List<RunItem> entities;

        try {
            entities = runRepository.getAllItemsByDateDesc();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(recyclerView.getContext(), "Error loading items", Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }

        for(int i = 0; i < entities.size(); i++) {
            RunItem item = entities.get(i);

            kmhMap.put(i, item.calculateKmH());
        }

        return entities;
    }

    public void deleteItem(RunViewItem item){
        runRepository.delete(item.getId());

        //kmh comparison is between adjacent elements, indexed by position
        //we need to recalc all the kmh map for all positions when structure changes
        loadAllItems();
    }

    public void deleteAllItems(){
        runRepository.deleteAll();
        items.clear();
        recyclerView.post(this::notifyDataSetChanged);
    }

    public void saveEntity(Object entity) {
        try {
            runRepository.insert((RunItem) entity);
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(recyclerView.getContext(), "Error saving item", Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }
    }

    public void saveEntityAndRefreshView(Object entity) {
        saveEntity(entity);
        loadAllItems();
    }

    public void updateEntity(Object o, int position){
        boolean isFullRefresh = false;

        RunItem entity = (RunItem) o;

        try{
            RunItem old = runRepository.findById(entity.getId());
            isFullRefresh = old != null && (
                    !old.getDate().isEqual(entity.getDate()) ||
                            old.getKm() != entity.getKm() ||
                            old.getHours() != entity.getHours() ||
                            old.getMinutes() != entity.getMinutes()
            );
        } catch (ExecutionException | InterruptedException e) {
            //ignore, bad luck on refresh
        }

        runRepository.update(entity);

        //if anything used for mpg calc has been changed, reload, otherwise simple refresh
        if(isFullRefresh){
            loadAllItems();
        }
        else{
            items.set(position, new RunViewItem(entity.getId(), entity.getKm(), entity.getSteps(), entity.getHours(), entity.getMinutes(), entity.getDate()));
            recyclerView.post(() -> notifyItemChanged(position));
        }
    }

    public void addEntity(RunItem entity) {
        items.add(new RunViewItem(entity.getId(), entity.getKm(), entity.getSteps(), entity.getHours(), entity.getMinutes(), entity.getDate()));
        recyclerView.post(() -> notifyItemInserted(items.size() - 1));
    }

    public void showInsertDialog(Context context) {
        RunDialog runDialog = new RunDialog(context, this);

        runDialog.addDatePicker(context, LocalDate.now());
        runDialog.addTimePicker(context, 0, 0);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Enter Run Details")
                .setView(runDialog.getDialogView())
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            final AlertDialog d = (AlertDialog) dialogInterface;
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(runDialog.getSubmitButtonWithValidation(d, false));
        });

        dialog.show();
    }

    public void showUpdateDialog(Context context, RunViewItem f, int position) {
        RunDialog runDialog = new RunDialog(context, this);

        runDialog.fillDialog(context, f, position);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Edit Run Details")
                .setView(runDialog.getDialogView())
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            final AlertDialog d = (AlertDialog) dialogInterface;
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(runDialog.getSubmitButtonWithValidation(d, true));
        });

        dialog.show();
    }
}
