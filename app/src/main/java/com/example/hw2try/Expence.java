package com.example.hw2try;


import java.io.Serializable;
import java.net.URI;

/**
 * Created by USER on 11/09/2016.
 */
public class Expence implements Serializable{
    String name, category, date;
     URI receiptImage;
    double amount;

    public Expence(){
        name="";
        category="";
        date="";
        amount=0.0;
    }
    public Expence(String name, String category, String date,URI receiptImage, double amount) {
        this.name = name;
        this.category = category;
        this.date = date;
        this.receiptImage = receiptImage;
        this.amount = amount;
    }
}
