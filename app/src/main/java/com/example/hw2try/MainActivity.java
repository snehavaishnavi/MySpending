package com.example.hw2try;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
ArrayList<Expence> exList=new ArrayList<Expence>();
    static final String EX_LIST="EX_LIST";
    static final String EX_OBJ_KEY = "EX_OBJ_KEY";
    static final int REQ_ADD_CODE=1;
    static final int REQ_EDIT_CODE=2;
    static final int REQ_DEL_CODE=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Expense App");
        actionBar.setDisplayShowHomeEnabled(true);
        Button add=(Button)findViewById(R.id.AddExID);
        Button edit=(Button)findViewById(R.id.EditExID);
        Button delete=(Button)findViewById(R.id.DeleteExID);
        Button show=(Button)findViewById(R.id.ShowExID);
        Button finish=(Button)findViewById(R.id.FinishMainID);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddExpenceActivity.class);
                intent.putExtra(EX_LIST,exList);
                startActivityForResult(intent,REQ_ADD_CODE);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!exList.isEmpty()){
                    Intent intent=new Intent(MainActivity.this,EditExpenceActivity.class);
                    intent.putExtra(EX_LIST,exList);
                    startActivityForResult(intent,REQ_EDIT_CODE);
                }else
                    Toast.makeText(getApplicationContext(),"NO EXPENSES WERE SAVED",Toast.LENGTH_LONG).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!exList.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, DeleteExpenceActivity.class);
                    intent.putExtra(EX_LIST, exList);
                    startActivityForResult(intent, REQ_DEL_CODE);
                }else
                    Toast.makeText(getApplicationContext(),"NO EXPENSES WERE SAVED",Toast.LENGTH_LONG).show();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!exList.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, ShowExpenceActivity.class);
                    intent.putExtra(EX_LIST, exList);
                    startActivity(intent);
                }else
                    Toast.makeText(getApplicationContext(),"NO EXPENSES WERE SAVED",Toast.LENGTH_LONG).show();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Expence expence;
        if(requestCode==REQ_ADD_CODE && resultCode==RESULT_OK) {
            Log.d("demo", "entered activtyresult");
            if (data.getExtras() != null) {
                Log.d("demo", "intent not null");
//            getIntent().getExtras().getSerializable("obj_key");
                expence = (Expence) data.getExtras().getSerializable(MainActivity.EX_OBJ_KEY);
                Log.d("demo", expence.name);
                exList.add(expence);
                if (!exList.isEmpty()) {
                    Log.d("demo", "list not empty");
                    for (Expence ex : exList) {
                        Log.d("demo", ex.name + " " + ex.category);
                    }
                }

            } else {
                Log.d("demo","intent null");
            }
        }else if(requestCode==REQ_EDIT_CODE && resultCode==RESULT_OK) {
            if(data.getExtras()!=null){
                expence=(Expence)data.getExtras().getSerializable(MainActivity.EX_OBJ_KEY);
                for (Expence e:exList){
                    if(e.name.equals(expence.name)){
                        e.amount=expence.amount;
                        e.category=expence.category;
                        e.date=expence.date;
                        e.receiptImage=expence.receiptImage;
                    }
                }

            }
        }else if(requestCode==REQ_DEL_CODE && resultCode==RESULT_OK){

            if(data.getExtras()!=null){
                String exName=data.getExtras().getString(MainActivity.EX_OBJ_KEY);
                Iterator<Expence> it=exList.iterator();
                while(it.hasNext()) {
                    if(it.next().name.equals(exName)){
                        it.remove();
                    }

                }
            }else{
                Log.d("demo","data empty");
            }
        }


    }
}
