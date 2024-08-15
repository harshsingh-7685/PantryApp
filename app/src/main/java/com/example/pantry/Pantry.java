package com.example.pantry;

import java.util.ArrayList;
import java.util.Date;

public class Pantry {
    public static ArrayList<Pantry> pantryArrayList = new ArrayList<Pantry>();
    public static String PANTRY_EDIT_EXTRA = "pantryEdit";

    private int id;
    private String name;
    private int quantity;
    private Date expiration;
    private Date deleted;

    public Pantry(int id, String name, int quantity, Date expiration, Date deleted) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiration = expiration;
        this.deleted = deleted;
    }

    public Pantry(int id, String name, int quantity, Date expiration) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiration = expiration;
        deleted = null;
    }

    public static Pantry getPantryForID(int passedPantryID) {
        for(Pantry pantry : pantryArrayList){
            if(pantry.getId() == passedPantryID)
                return pantry;
        }

        return null;
    }

    public static ArrayList<Pantry> nonDeletedItems(){
        ArrayList<Pantry> nonDeleted = new ArrayList<Pantry>();
        for(Pantry pantry : pantryArrayList){
            if(pantry.deleted == null)
                nonDeleted.add(pantry);
        }

        return nonDeleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
