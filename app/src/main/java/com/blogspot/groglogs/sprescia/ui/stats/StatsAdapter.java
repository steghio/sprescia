package com.blogspot.groglogs.sprescia.ui.stats;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.storage.db.repository.RunRepository;
import com.blogspot.groglogs.sprescia.ui.adapter.AbstractAdapter;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class StatsAdapter extends AbstractAdapter<StatsViewHolder> {

    @Getter
    private List<RunViewItem> items;

    private final RunRepository runRepository;

    public StatsAdapter(Application application, RecyclerView recyclerView) {
        this.items = new ArrayList<>();
        this.runRepository = new RunRepository(application);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stats, parent, false);

        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        //todo draw
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void loadAllItems(){
        //todo load
    }

    public void deleteAllItems(){}

    public void saveEntity(Object entity) {}

    public void saveEntityAndRefreshView(Object entity) {}

    public void updateEntity(Object o, int position){}

    public void showInsertDialog(Context context) {}
}
