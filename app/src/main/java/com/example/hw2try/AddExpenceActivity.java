package com.example.hw2try;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddExpenceActivity extends AppCompatActivity {

    EditText exName;
    EditText dateShow;
    Spinner catDropDown;
    AlertDialog.Builder alertDialog;
    boolean flag = false;
    ImageView date;
    int day, month, year;
    ImageView browseImage;
    EditText amount;
    private static final int SELECT_PICTURE = 1, REQUEST_IMAGE_GET = 1;
    private String selectedImagePath;
    String[] items;
    ArrayAdapter<String> adapter;
    Expence currentExpence;
    Button addEx;
    Uri fullPhotoUri=Uri.parse("android.resource://com.example.hw2try/"+R.drawable.browse_img);;
    ArrayList<Expence> exList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expence);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Expense");
        actionBar.setDisplayShowHomeEnabled(true);
        exName = (EditText) findViewById(R.id.ExNameAddExID);
        amount = (EditText) findViewById(R.id.amountShowAddExID);
        catDropDown = (Spinner) findViewById(R.id.dropDownAddExID);
        items =getResources().getStringArray(R.array.category_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catDropDown.setAdapter(adapter);
        dateShow = (EditText) findViewById(R.id.dateShowAddExID);
        date = (ImageView) findViewById(R.id.datelabelDelExID);
        Log.d("demo","categories length"+items.length);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo", "entered date image onclick");
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                System.out.println("the selected " + mDay);
                Log.d("demo", "the selected " + mDay);
                DatePickerDialog dialog = new DatePickerDialog(AddExpenceActivity.this, new mDateSetListener(dateShow), mYear, mMonth, mDay);
                dialog.show();
            }
        });

        browseImage = (ImageView) findViewById(R.id.browseImageAddExID);
        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });
        addEx=(Button)findViewById(R.id.AddExpenceID);
                addEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exList = (ArrayList<Expence>) getIntent().getExtras().getSerializable(MainActivity.EX_LIST);
                if (!exName.getText().toString().isEmpty() && !exName.getText().toString().equals("")) {
                    if (exName.getText().toString().length() < 50) {
                        if (catDropDown.getSelectedItemPosition() != 0) {
                            if (!amount.getText().toString().isEmpty() && !amount.getText().toString().equals("")) {
                                if (!dateShow.getText().toString().isEmpty() && !dateShow.getText().toString().equals("")) {
                                    if (!exList.isEmpty()) {
                                        for (Expence expence : exList) {
                                            Log.d("demo", "" + exName.getText().toString().equals(expence.name));
                                            if (!exName.getText().toString().equals(expence.name)) {
                                                flag = true;
                                            }
                                        }
                                    } else {
                                        flag = true;
                                    }
                                    if (flag) {
                                        try {
                                            Log.d("demo","image null uri"+fullPhotoUri.toString());
                                            currentExpence = new Expence(exName.getText().toString(), catDropDown.getSelectedItem().toString(), dateShow.getText().toString(), new java.net.URI(fullPhotoUri.toString()), Double.parseDouble(amount.getText().toString()));
                                        } catch (URISyntaxException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("demo", currentExpence.name + " " + currentExpence.category + " " + currentExpence.amount + " " + currentExpence.date + "" + currentExpence.receiptImage);
                                        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        resultIntent.putExtra(MainActivity.EX_OBJ_KEY, currentExpence);
                                        setResult(RESULT_OK, resultIntent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "You already have this expense saved, Please click on edit to change", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    alertDialog = new AlertDialog.Builder(AddExpenceActivity.this)
                                            .setTitle("Error")
                                            .setMessage("Please select a date")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                }
                                            });
                                    alertDialog.create().show();
                                }
                            } else {
                                alertDialog = new AlertDialog.Builder(AddExpenceActivity.this)
                                        .setTitle("Error")
                                        .setMessage("Enter Amount")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        });
                                alertDialog.create().show();
                            }
                        } else {
                            alertDialog = new AlertDialog.Builder(AddExpenceActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Select any Category")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    });
                            alertDialog.create().show();
                        }
                    } else {
                        alertDialog = new AlertDialog.Builder(AddExpenceActivity.this)
                                .setTitle("Error")
                                .setMessage("Expense Name cannot exceed 50 characters")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                });
                        alertDialog.create().show();
                    }
                }else{
                    alertDialog=new AlertDialog.Builder(AddExpenceActivity.this)
                            .setTitle("Error")
                            .setMessage("Enter an Expence Name")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            });
                    alertDialog.create().show();
                }

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            //Bitmap thumbnail = data.getParcelable("data");
            fullPhotoUri = data.getData();
            Toast.makeText(AddExpenceActivity.this, ""+fullPhotoUri, Toast.LENGTH_SHORT).show();
            Bitmap bitmapImage = null;
            try {
                DecodeBitmap obj=new DecodeBitmap();
                bitmapImage = obj.decodeBitmap (this,fullPhotoUri );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Show the Selected Image on ImageView
            ImageView imageView = (ImageView) findViewById(R.id.browseImageAddExID);
            imageView.setImageBitmap(bitmapImage);
            // Do work with photo saved at fullPhotoUri
        }

    }

}