package com.example.hw2try;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class EditExpenceActivity extends AppCompatActivity {

    ArrayList<Expence> exList;
    Button selectExpence,saveButton,cancelButton;
    EditText exName,exAmt,exDate;
    Spinner exCategory;
    ImageView exReceipt;
    ArrayAdapter<String> adapter;
    ImageView date;
    Uri fullPhotoUri;
    private static String EX_EDIT_KEY="EX_EDIT_KEY";

    private static final int REQUEST_IMAGE_GET = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expence);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Edit Expense");
        actionBar.setDisplayShowHomeEnabled(true);
        selectExpence=(Button)findViewById(R.id.selectExEditExID);
        exCategory=(Spinner)findViewById(R.id.dropDownEditExID);
        date=(ImageView)findViewById(R.id.datelabelEditExID);
        saveButton=(Button)findViewById(R.id.saveID);
        cancelButton=(Button)findViewById(R.id.CancelEditExID);
        String[] items =getResources().getStringArray(R.array.category_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exCategory.setAdapter(adapter);
        exName=(EditText)findViewById(R.id.ExNameEditExID);
        exAmt=(EditText)findViewById(R.id.amountShowEditExID);
        exDate=(EditText)findViewById(R.id.dateShowEditExID);
        exReceipt=(ImageView)findViewById(R.id.browseImageEditExID);
        exList=(ArrayList<Expence>)getIntent().getExtras().getSerializable(MainActivity.EX_LIST);


        selectExpence.setOnClickListener(new View.OnClickListener() {
            DecodeBitmap obj=new DecodeBitmap();
            @Override
            public void onClick(View v) {
                Log.d("demo","entered on click of select expence");
                Collections.sort(exList, new Comparator<Expence>() {
                    @Override
                    public int compare(Expence lhs, Expence rhs) {
                        return lhs.name.compareToIgnoreCase(rhs.name);
                    }

                });

                final CharSequence[] exListNames=new CharSequence[exList.size()];
                int i=0;
                for (Expence ex:exList){
                    exListNames[i]=ex.name;
                    i++;
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(EditExpenceActivity.this);
                Log.d("demo","alert dialog");
                builder.setTitle("Pick an Expense")
                        .setItems(exListNames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(Expence ex:exList){
                                    if(ex.name.equals(exListNames[which])){
                                        exName.setText(ex.name);
                                        exCategory.setSelection(adapter.getPosition(ex.category));
                                        exAmt.setText(String.valueOf(ex.amount));
                                        exDate.setText(ex.date);
                                        try {
                                            Log.d("demo","edit image null uri"+Uri.parse(ex.receiptImage.toString()));
                                            exReceipt.setImageBitmap(obj.decodeBitmap(EditExpenceActivity.this, Uri.parse(ex.receiptImage.toString())));
                                            fullPhotoUri=Uri.parse(ex.receiptImage.toString());
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }
                        });
                AlertDialog alert=builder.create();
                alert.show();
            }
        });

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
                DatePickerDialog dialog = new DatePickerDialog(EditExpenceActivity.this, new mDateSetListener(exDate), mYear, mMonth, mDay);
                dialog.show();
            }
        });

        exReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Expence editedEx=new Expence();
                try {
                    Log.d("demo","edited expense"+exName.getText().toString()+exCategory.getSelectedItem().toString()+exDate.getText().toString()+new java.net.URI(fullPhotoUri.toString())+Double.parseDouble(exAmt.getText().toString()));
                    editedEx =new Expence(exName.getText().toString(),exCategory.getSelectedItem().toString(),exDate.getText().toString(),new java.net.URI(fullPhotoUri.toString()),Double.parseDouble(exAmt.getText().toString()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(EditExpenceActivity.this,MainActivity.class);
                intent.putExtra(MainActivity.EX_OBJ_KEY,editedEx);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditExpenceActivity.this, MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            //Bitmap thumbnail = data.getParcelable("data");
            fullPhotoUri = data.getData();
            Toast.makeText(EditExpenceActivity.this, ""+fullPhotoUri, Toast.LENGTH_SHORT).show();
            Bitmap bitmapImage = null;
            try {
                DecodeBitmap obj=new DecodeBitmap();
                bitmapImage = obj.decodeBitmap (this,fullPhotoUri );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Show the Selected Image on ImageView

            exReceipt.setImageBitmap(bitmapImage);
            // Do work with photo saved at fullPhotoUri
        }
    }


}