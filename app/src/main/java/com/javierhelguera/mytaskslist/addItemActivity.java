package com.javierhelguera.mytaskslist;


import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class addItemActivity extends AppCompatActivity {

    DBHelper dbhelper = new DBHelper(this);
    Spinner spinner;
    Switch switchdate;
    Button datePicker;
    Button timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setTitle("Add task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinner = (Spinner) findViewById(R.id.spinner);
        switchdate = (Switch) findViewById(R.id.switchdate);
        datePicker = (Button) findViewById(R.id.datePicker);
        timePicker = (Button) findViewById(R.id.timePicker);

        datePicker.animate().alpha(0.0f).setDuration(0);
        timePicker.animate().alpha(0.0f).setDuration(0);

        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        /* ----------------------- GENERAR EL DATE PICKER Y EL TIME PICKER ----------------------- */

        switchdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    datePicker.animate().alpha(1.0f).setDuration(200);
                    timePicker.animate().alpha(1.0f).setDuration(200);
                }else{
                    datePicker.animate().alpha(0.0f).setDuration(200);
                    timePicker.animate().alpha(0.0f).setDuration(200);
                }
            }
        });


        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        datePicker.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                }, year, month, day);

                datePickerDialog.show(getFragmentManager(), "Date picker dialog");
            }
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        if (minute<10){
                            timePicker.setText(hourOfDay+":"+"0"+minute);
                        }else{
                            timePicker.setText(hourOfDay+":"+minute);
                        }

                    }
                }, 12,00, true);
                timePickerDialog.show(getFragmentManager(), "Date picker dialog");
            }
        });

        /* ----------------------- OBTENER LAS LISTAS DE LA BD ----------------------- */

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getLists());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private List<String> getLists() {
        List<String> lists = new ArrayList<String>();
        SQLiteDatabase db=dbhelper.getReadableDatabase();

        try {
            Cursor cursor=db.rawQuery("SELECT * FROM list",null);
            long count = DatabaseUtils.queryNumEntries(db, ListsContract.TABLE);
            cursor.moveToFirst();
            for (int i = 0; i<count; i++){
                cursor.moveToPosition(i);
                lists.add(cursor.getString(0));
            }
            return lists;


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"ERROR loading lists",Toast.LENGTH_LONG).show();
        }
        return null;
    }


}
