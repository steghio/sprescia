package com.blogspot.groglogs.sprescia.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.groglogs.sprescia.model.view.AbstractViewItem;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

public class CreateDocumentActivity extends AppCompatActivity {

    public static final String CSV_SEPARATOR=";";
    public static final String DATA = "DATA";
    private ActivityResultLauncher<Intent> createDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            // Once the user selects a file, write content to it
                            if (uri != null) {
                                writeToFile(uri);
                            }
                        }
                    }
                    else {
                        Toast.makeText(CreateDocumentActivity.this, "Error creating file", Toast.LENGTH_SHORT).show();
                    }

                    //finish the current activity and return to the previous screen
                    finish();
                });

        createDocument();
    }

    private void createDocument() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "sprescia_export_" +
                LocalDateTime.now().toString() +
                ".csv");

        createDocumentLauncher.launch(intent);
    }

    private void writeToFile(Uri uri) {
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);

            List<AbstractViewItem> items = getIntent().getParcelableArrayListExtra(DATA, AbstractViewItem.class);

            for(AbstractViewItem item : items){
                outputStream.write(item.toCsv().getBytes());
            }

            outputStream.close();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to write the file", Toast.LENGTH_SHORT).show();
        }
    }
}
