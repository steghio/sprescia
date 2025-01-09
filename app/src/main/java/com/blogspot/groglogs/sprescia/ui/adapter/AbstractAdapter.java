package com.blogspot.groglogs.sprescia.ui.adapter;

import android.content.Context;
import android.os.Parcelable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class AbstractAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    protected RecyclerView recyclerView;

    public abstract List<? extends Parcelable> getItems();

    public abstract void showInsertDialog(Context context);

    public abstract void loadAllItems();

    public abstract void deleteAllItems();

    public abstract void saveEntity(Object e);

    public abstract void updateEntity(Object e, int position);

    public abstract void saveEntityAndRefreshView(Object e);

}
