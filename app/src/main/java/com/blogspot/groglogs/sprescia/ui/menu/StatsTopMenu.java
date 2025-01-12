package com.blogspot.groglogs.sprescia.ui.menu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.ui.adapter.AbstractStatsAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StatsTopMenu implements MenuProvider {

    private final Context context;
    private final AbstractStatsAdapter adapter;
    private boolean isAscending = true;

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_menu_stats, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_speed) {
            Toast.makeText(context, "MENU SPEED", Toast.LENGTH_SHORT).show();
            adapter.showSpeedChart();
            return true;
        } else if (id == R.id.action_dist) {
            Toast.makeText(context, "MENU DIST", Toast.LENGTH_SHORT).show();
            adapter.showDistChart();
            return true;
        } else if (id == R.id.action_time) {
            Toast.makeText(context, "MENU TIME", Toast.LENGTH_SHORT).show();
            adapter.showTimeChart();
            return true;
        } else if (id == R.id.action_steps) {
            Toast.makeText(context, "MENU STEPS", Toast.LENGTH_SHORT).show();
            adapter.showStepsChart();
            return true;
        } else if (id == R.id.action_sort) {
            Toast.makeText(context, "MENU SORT", Toast.LENGTH_SHORT).show();

            menuItem.setIcon(isAscending ? R.drawable.ic_sort_desc_24dp : R.drawable.ic_sort_asc_24dp);
            isAscending = !isAscending;

            adapter.invertSort();
            return true;
        }
        return false;
    }
}
