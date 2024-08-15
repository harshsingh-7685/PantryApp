package com.example.pantry;

import static com.example.pantry.SQLiteManager.dateFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;

public class PantryAdapter extends ArrayAdapter<Pantry> {
    public PantryAdapter(Context context, ArrayList<Pantry> items){
        //needs to be super(context, 0, items);
        super(context, 0);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Pantry pantry = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pantry_cell, parent, false);

        TextView name = convertView.findViewById(R.id.cellTitle);
        TextView quantity = convertView.findViewById(R.id.cellDesc);
        TextView expiration = convertView.findViewById(R.id.cellExp);

        name.setText(pantry.getName());
        quantity.setText(pantry.getQuantity());
        expiration.setText(getStringFromDate(pantry.getExpiration()));

        return convertView;
    }

    private String getStringFromDate(Date date) {
        if(date == null)
            return null;

        return dateFormat.format(date);
    }
}
