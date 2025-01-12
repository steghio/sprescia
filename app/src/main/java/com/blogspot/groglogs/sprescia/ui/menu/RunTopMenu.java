package com.blogspot.groglogs.sprescia.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.activity.CreateDocumentActivity;
import com.blogspot.groglogs.sprescia.activity.ReadDocumentActivity;
import com.blogspot.groglogs.sprescia.ui.run.RunAdapter;

import java.util.ArrayList;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RunTopMenu implements MenuProvider {

    private Context context;
    private RunAdapter adapter;

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_import) {
            Toast.makeText(context, "MENU IMPORT", Toast.LENGTH_SHORT).show();
            importFromFile();
            return true;
        } else if (id == R.id.action_export) {
            Toast.makeText(context, "MENU EXPORT", Toast.LENGTH_SHORT).show();
            exportToFile();
            return true;
        }
        return false;
    }

    private void importFromFile(){
        Intent intent = new Intent(context, ReadDocumentActivity.class);
        context.startActivity(intent);
    }

    private void exportToFile(){
        Intent intent = new Intent(context, CreateDocumentActivity.class);
        intent.putParcelableArrayListExtra(CreateDocumentActivity.DATA, (ArrayList<? extends Parcelable>) adapter.getItems());
        context.startActivity(intent);
    }
}
