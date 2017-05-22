package com.example.hw2try;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by USER on 10/09/2016.
 */
public class mDateSetListener implements DatePickerDialog.OnDateSetListener {

    int mYear;
    int mMonth;
    int mDay;
    private EditText editText;

    public mDateSetListener(EditText editText){
        this.editText=editText;
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        // TODO Auto-generated method stub
        // getCalender();
        this.mYear = year;
        this.mMonth = monthOfYear;
        this.mDay = dayOfMonth;

        editText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(mMonth + 1).append("/").append(mDay).append("/")
                .append(mYear).append(" "));
    }

}