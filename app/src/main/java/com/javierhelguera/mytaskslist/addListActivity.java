package com.javierhelguera.mytaskslist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class addListActivity extends AppCompatActivity {

    EditText listField;
    Button addListButton;
    DBHelper dbhelper = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Add list");

        listField = (EditText) findViewById(R.id.listField);
        addListButton = (Button) findViewById(R.id.addListButton);

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addList(listField.getText().toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    private void addList(String list) {
        SQLiteDatabase db=dbhelper.getReadableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(ListsContract.Column.ID, list);
            db.insert(ListsContract.TABLE, null, values);
            listField.setText("");
            Snackbar.make(findViewById(R.id.tempView), "List "+list+" added", Snackbar.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR loading lists",Toast.LENGTH_LONG).show();
        }
    }
}
