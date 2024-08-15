package com.example.pantry;

import static com.example.pantry.Pantry.pantryArrayList;
import static com.example.pantry.SQLiteManager.dateFormat;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.pantry.databinding.ActivityPantryDetailBinding;
import java.text.ParseException;
import java.util.Date;

import android.widget.Toast;

public class PantryDetailActivity extends AppCompatActivity {
    private EditText nameEditText, quantityEditText, expirationEditText;
    private Button deleteButton;
    private AppBarConfiguration appBarConfiguration;
    private ActivityPantryDetailBinding binding;

    private Pantry selectedPantry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_detail);
        initWidgets();
        checkForEditPantry();
    }

    private void initWidgets() {
        nameEditText = findViewById(R.id.nameEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        expirationEditText = findViewById(R.id.expirationEditText);
        deleteButton = findViewById(R.id.deletePantryButton);
    }

    private void checkForEditPantry() {
        Intent previousIntent = getIntent();

        int passedPantryID = previousIntent.getIntExtra(Pantry.PANTRY_EDIT_EXTRA, -1);
        selectedPantry = Pantry.getPantryForID(passedPantryID);

        if(selectedPantry != null){
            nameEditText.setText(selectedPantry.getName());
            quantityEditText.setText(selectedPantry.getQuantity());
            expirationEditText.setText(getStringFromDate(selectedPantry.getExpiration()));
        }
        else{
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

//    public void savePantry(View view) {
//        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
//        String name = String.valueOf(nameEditText.getText());
//        int quantity = Integer.valueOf(String.valueOf(quantityEditText));
//        Date expiration = getDateFromString(String.valueOf(expirationEditText.getText()));
//
//        if(selectedPantry == null){
//            int id = Pantry.pantryArrayList.size();
//            Pantry newPantry = new Pantry(id, name, quantity, expiration);
//            Pantry.pantryArrayList.add(newPantry);
//            sqLiteManager.addPantryToDatabase(newPantry);
//        }
//        else{
//            selectedPantry.setName(name);
//            selectedPantry.setQuantity(quantity);
//            sqLiteManager.updatePantryInDB(selectedPantry);
//        }
//
//        finish();
//    }

    public void savePantry(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String name = String.valueOf(nameEditText.getText());

        String quantityString = String.valueOf(quantityEditText.getText());
        int quantity = 0; // Default quantity if validation fails

        // Validate quantity input
        if (quantityString.isEmpty()) {
            // Handle empty quantity
            showError("Quantity cannot be empty");
            return;
        } else if (!quantityString.matches("^\\d+$")) {
            // Handle invalid quantity format
            showError("Quantity must be a positive number");
            return;
        } else {
            try {
                quantity = Integer.parseInt(quantityString);
            } catch (NumberFormatException e) {
                // Handle conversion failure
                showError("Error parsing quantity");
                return;
            }
        }

        Date expiration = getDateFromString(String.valueOf(expirationEditText.getText()));

        if (selectedPantry == null) {
            int id = pantryArrayList.size();
            Pantry newPantry = new Pantry(id, name, quantity, expiration);
            pantryArrayList.add(newPantry);
            sqLiteManager.addPantryToDatabase(newPantry);
        } else {
            selectedPantry.setName(name);
            selectedPantry.setQuantity(quantity);
            sqLiteManager.updatePantryInDB(selectedPantry);
        }

        //
        Pantry newPantry = Pantry.getPantryForID(pantryArrayList.size());
        Log.d("PantryDetailActivity", "Saved pantry: " + newPantry.toString());


        finish();
    }

    private void showError(String message) {
        // Implement your error handling logic here, e.g., display a Toast, Snackbar, or dialog
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void deletePantry(View view) {
        selectedPantry.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updatePantryInDB(selectedPantry);

        finish();
    }

    private Date getDateFromString(String string){
        try{
            return dateFormat.parse(string);
        }
        catch (ParseException | NullPointerException e){
            return null;
        }
    }

    private String getStringFromDate(Date date) {
        if(date == null)
            return null;

        return dateFormat.format(date);
    }
}