package com.blogspot.groglogs.sprescia.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.groglogs.sprescia.model.entity.RunItem;
import com.blogspot.groglogs.sprescia.model.view.RunViewItem;
import com.blogspot.groglogs.sprescia.ui.run.RunFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadDocumentActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> readDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri fileUri = result.getData().getData();
                        if (fileUri != null) {
                            readFromFile(fileUri);
                        }
                    }
                    else {
                        Toast.makeText(ReadDocumentActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                    }

                    //finish the current activity and return to the previous screen
                    finish();
                }
        );

        readDocument();
    }

    private void readDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        readDocumentLauncher.launch(intent);
    }

    private void readFromFile(Uri fileUri) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(fileUri)))) {
            readRunItems(reader);
        } catch (IOException e) {
            Toast.makeText(this, "Failed to read the file", Toast.LENGTH_SHORT).show();
        }
    }

    private void readRunItems(BufferedReader reader) throws IOException {

        RunFragment.getAdapter().deleteAllItems();

        String line;

        while ((line = reader.readLine()) != null) {

            RunViewItem item = RunViewItem.fromCsv(line);

            RunItem f = new RunItem(item.getKm(), item.getSteps(), item.getHours(), item.getMinutes(), item.getDate());

            RunFragment.getAdapter().saveEntity(f);
        }

        Toast.makeText(this, "Run items imported successfully", Toast.LENGTH_SHORT).show();

        RunFragment.getAdapter().loadAllItems();
    }
}
