package com.example.pantry;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SQLiteManager extends SQLiteOpenHelper {
    private static SQLiteManager sqLiteManager;

    private static final String DATABASE_NAME = "PantryDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Pantry";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String QUANT_FIELD = "quantity";
    private static final String EXP_FIELD = "expiration";
    private static final String DEL_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    public static final DateFormat dateFormat = new SimpleDateFormat("MM--dd-yyy HH:mm:ss");

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context){
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }


    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT,")
                .append(NAME_FIELD)
                .append(" TEXT, ")
                .append(QUANT_FIELD)
                .append(" TEXT, ")
                .append(EXP_FIELD)
                .append(" TEXT")  // Closing parenthesis added here
                .append(")");  // You might have missed this one as well

        sqLiteDatabase.execSQL(sql.toString());
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        switch(oldVersion){
//            case 1:
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN" + NEW_COLUMN + " TEXT");
//            case 2:
//                sqLiteDatabase.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN" + NEW_COLUMN + " TEXT");
//        }
    }

//    public void addPantryToDatabase(Pantry pantry){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ID_FIELD, pantry.getId());
//        contentValues.put(NAME_FIELD, pantry.getName());
//        contentValues.put(QUANT_FIELD, pantry.getQuantity());
//        contentValues.put(EXP_FIELD, getStringFromDate(pantry.getExpiration()));
//        contentValues.put(DEL_FIELD, getStringFromDate(pantry.getDeleted()));
//
//        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
//    }

    public void addPantryToDatabase(Pantry pantry){
        Log.d("SQLiteManager", "Adding pantry: " + pantry.toString());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD,
                pantry.getId());
        contentValues.put(NAME_FIELD, pantry.getName());
        contentValues.put(QUANT_FIELD, pantry.getQuantity());
        contentValues.put(EXP_FIELD, getStringFromDate(pantry.getExpiration()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);


        //
        long rowId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (rowId == -1) {
            Log.e("SQLiteManager", "Error inserting pantry");
            // Handle insertion failure (e.g., show an error message)
        }
    }

    public void updatePantryInDB(Pantry pantry){
        Log.d("SQLiteManager", "Updating pantry: " + pantry.toString());

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_FIELD,
                pantry.getName());
        contentValues.put(QUANT_FIELD, pantry.getQuantity());
        contentValues.put(EXP_FIELD, getStringFromDate(pantry.getExpiration()));

        sqLiteDatabase.update(TABLE_NAME, contentValues,
                ID_FIELD + " =? ", new String[]{String.valueOf(pantry.getId())});


        //
        long rowId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if (rowId == -1) {
            Log.e("SQLiteManager", "Error inserting pantry");
            // Handle insertion failure (e.g., show an error message)
        }
    }

    public void populatePantryListArray(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try(Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(1);
                    String name = result.getString(2);
                    int quantity = result.getInt(3);
                    String stringExpiry = result.getString(4);
                    String stringDeleted = result.getString(5);

                    Date expiry = getDateFromString(stringExpiry);
                    Date deleted = getDateFromString(stringDeleted);

                    Pantry pantry = new Pantry(id, name, quantity, expiry, deleted);
                    Pantry.pantryArrayList.add(pantry);
                }
            }
        }
    }

//    public void updatePantryInDB(Pantry pantry){
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(ID_FIELD, pantry.getId());
//        contentValues.put(NAME_FIELD, pantry.getName());
//        contentValues.put(QUANT_FIELD, pantry.getQuantity());
//        contentValues.put(EXP_FIELD, getStringFromDate(pantry.getExpiration()));
//        contentValues.put(DEL_FIELD, getStringFromDate(pantry.getDeleted()));
//
//        sqLiteDatabase.update(TABLE_NAME, contentValues,
//                ID_FIELD + " =? ", new String[]{String.valueOf(pantry.getId())});
//    }
    private String getStringFromDate(Date date) {
        if(date == null)
            return null;

        return dateFormat.format(date);
    }

    private Date getDateFromString(String string){
        try{
            return dateFormat.parse(string);
        }
        catch (ParseException | NullPointerException e){
            return null;
        }
    }
}
