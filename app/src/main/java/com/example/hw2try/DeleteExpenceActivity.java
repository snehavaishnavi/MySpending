package com.example.hw2try;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class DeleteExpenceActivity extends AppCompatActivity {
    ArrayList<Expence> exList;
    Button selectExpence,cancelButton,deleteButton;
    EditText exName,exAmt,exDate;
    Spinner exCategory;
    ImageView exReceipt;
    ArrayAdapter<String> adapter;
    ImageView date;
    Uri fullPhotoUri;
    String expenceName;
    private static String EX_DEL_KEY="EX_DEL_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expence);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Delete Expense");
        actionBar.setDisplayShowHomeEnabled(true);
        selectExpence=(Button)findViewById(R.id.selectExDelExID);
        exCategory=(Spinner)findViewById(R.id.dropDownDelExID);
        date=(ImageView)findViewById(R.id.datelabelDelExID);
        cancelButton=(Button)findViewById(R.id.CancelDelExID);
        String[] items =getResources().getStringArray(R.array.category_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exCategory.setAdapter(adapter);
        exName=(EditText)findViewById(R.id.ExNameDelExID);
        exAmt=(EditText)findViewById(R.id.amountShowDelExID);
        exDate=(EditText)findViewById(R.id.dateShowDelExID);
        exReceipt=(ImageView)findViewById(R.id.browseImageDelExID);
        deleteButton=(Button)findViewById(R.id.deleteID);
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
                Arrays.sort(exListNames);
                AlertDialog.Builder builder=new AlertDialog.Builder(DeleteExpenceActivity.this);
                Log.d("demo","alert dialog");
                builder.setTitle("Pick an Expense")
                        .setItems(exListNames, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(Expence ex:exList){
                                    if(ex.name.equals(exListNames[which])){
                                        expenceName=ex.name;
                                        exName.setText(ex.name);
                                        exCategory.setSelection(adapter.getPosition(ex.category));
                                        exAmt.setText(String.valueOf(ex.amount));
                                        exDate.setText(ex.date);
                                        try {
                                            exReceipt.setImageBitmap(obj.decodeBitmap(DeleteExpenceActivity.this, Uri.parse(ex.receiptImage.toString())));
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
                exName.setEnabled(false);
                exName.setFocusable(false);
                exAmt.setEnabled(false);
                exAmt.setFocusable(false);
                exDate.setEnabled(false);
                exDate.setFocusable(false);
                exCategory.setEnabled(false);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DeleteExpenceActivity.this,MainActivity.class);
                intent.putExtra(MainActivity.EX_OBJ_KEY,expenceName);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DeleteExpenceActivity.this, MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }
}
