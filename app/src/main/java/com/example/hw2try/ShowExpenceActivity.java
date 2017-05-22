package com.example.hw2try;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

public class ShowExpenceActivity extends AppCompatActivity {

    TextView exName,exCategory,exAmt,exDate;
    ImageView exReceipt;
    Button finish;
    ImageButton first,last,previous,next;
    ArrayList<Expence> expences;
    DecodeBitmap obj=new DecodeBitmap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expence);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Show Expense");
        actionBar.setDisplayShowHomeEnabled(true);
        expences=(ArrayList<Expence>)getIntent().getExtras().getSerializable(MainActivity.EX_LIST);
        exName=(TextView)findViewById(R.id.nameShowID);
        exCategory=(TextView)findViewById(R.id.categoryShowID);
        exAmt=(TextView)findViewById(R.id.amountShowID);
        exDate=(TextView)findViewById(R.id.dateShowID);
        exReceipt=(ImageView)findViewById(R.id.receiptShowID);
        finish=(Button)findViewById(R.id.finishID);
        first=(ImageButton)findViewById(R.id.firstDetailID);
        last=(ImageButton)findViewById(R.id.lastDetailID);
        previous=(ImageButton)findViewById(R.id.previousDetailID);
        next=(ImageButton)findViewById(R.id.nextDetailID);

        Collections.sort(expences, new Comparator<Expence>() {
            @Override
            public int compare(Expence lhs, Expence rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);
            }

        });
        exName.setText(expences.get(0).name);
        exCategory.setText(expences.get(0).category);
        exAmt.setText(String.valueOf(expences.get(0).amount));
        exDate.setText(expences.get(0).date);
        try {
            exReceipt.setImageBitmap(obj.decodeBitmap(ShowExpenceActivity.this, Uri.parse(expences.get(0).receiptImage.toString())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exName.setText(expences.get(0).name);
                exCategory.setText(expences.get(0).category);
                exAmt.setText(String.valueOf(expences.get(0).amount));
                exDate.setText(expences.get(0).date);
                try {
                    exReceipt.setImageBitmap(obj.decodeBitmap(ShowExpenceActivity.this, Uri.parse(expences.get(0).receiptImage.toString())));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exName.setText(expences.get(expences.size()-1).name);
                exCategory.setText(expences.get(expences.size()-1).category);
                exAmt.setText(String.valueOf(expences.get(expences.size()-1).amount));
                exDate.setText(expences.get(expences.size()-1).date);
                try {
                    exReceipt.setImageBitmap(obj.decodeBitmap(ShowExpenceActivity.this, Uri.parse(expences.get(expences.size()-1).receiptImage.toString())));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            Expence displayedEx;
            @Override
            public void onClick(View v) {
                for(Expence ex:expences){
                    if(ex.name.equals(exName.getText().toString())){
                        displayedEx=ex;
                    }
                }
                Log.d("demo","current expence"+expences.indexOf(displayedEx));
                ListIterator<Expence> it = expences.listIterator(expences.indexOf(displayedEx));
                if(it.hasPrevious()){
                    Expence previousEx=it.previous();
                        exName.setText(previousEx.name);
                        exCategory.setText(previousEx.category);
                        exAmt.setText(String.valueOf(previousEx.amount));
                        exDate.setText(previousEx.date);
                        try {
                            exReceipt.setImageBitmap(obj.decodeBitmap(ShowExpenceActivity.this, Uri.parse(previousEx.receiptImage.toString())));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                }else {
                    Toast.makeText(getApplicationContext(),"There are no previous elements",Toast.LENGTH_LONG).show();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            Expence displayedEx;
            @Override
            public void onClick(View v) {
                for(Expence ex:expences){
                    if(ex.name.equals(exName.getText().toString())){
                        displayedEx=ex;
                    }
                }
                  ListIterator<Expence> iterator=expences.listIterator(expences.indexOf(displayedEx));
                  Log.d("demo","next elem"+iterator.next().name);
                  if(iterator.hasNext()){
                      Expence nextExpence=iterator.next();
                      exName.setText(nextExpence.name);
                      exCategory.setText(nextExpence.category);
                      exAmt.setText(String.valueOf(nextExpence.amount));
                      exDate.setText(nextExpence.date);
                      try {
                          exReceipt.setImageBitmap(obj.decodeBitmap(ShowExpenceActivity.this, Uri.parse(nextExpence.receiptImage.toString())));
                      } catch (FileNotFoundException e) {
                          e.printStackTrace();
                      }

                  }else {
                      Toast.makeText(getApplicationContext(),"No next Elements",Toast.LENGTH_LONG).show();
                  }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShowExpenceActivity.this, MainActivity.class);
                finish();
            }
        });
    }
}
