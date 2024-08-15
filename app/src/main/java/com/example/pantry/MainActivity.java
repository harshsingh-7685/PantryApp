package com.example.pantry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView pantryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        loadFromDBToMemory();
        setNodeAdapter();
        setOnClickListener();
    }

    private void initWidgets() {
        pantryListView = findViewById(R.id.pantryListView);
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populatePantryListArray();
    }

    private void setNodeAdapter() {
        if (Pantry.pantryArrayList.isEmpty()) {
            // Handle no data scenario (e.g., display a message)
            Log.e("MainActivity", "No data scenario");
        } else {
            PantryAdapter pantryAdapter = new PantryAdapter(getApplicationContext(), Pantry.nonDeletedItems());
            pantryListView.setAdapter(pantryAdapter);
        }
        PantryAdapter pantryAdapter = new PantryAdapter(getApplicationContext(), Pantry.nonDeletedItems());
        pantryListView.setAdapter(pantryAdapter);
    }

    private void setOnClickListener(){
        pantryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Pantry selectedPantry = (Pantry)pantryListView.getItemAtPosition(position);
                Intent editPantryIntent = new Intent(getApplicationContext(), PantryDetailActivity.class);
                editPantryIntent.putExtra(Pantry.PANTRY_EDIT_EXTRA, selectedPantry.getId());
                startActivity(editPantryIntent);
            }
        });
    }

    public void newEntry(View view) {
        Intent newPantryIntent = new Intent(this, PantryDetailActivity.class);
        startActivity(newPantryIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNodeAdapter();
    }
}