package com.javierhelguera.mytaskslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

public class ListModel {

    private String list_name;
    private DBHelper dbhelper;

    public ListModel (String name){
        this.list_name = name;
    }

    public String saveList(Context context){
        dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            if (!list_name.equals("")){
                ContentValues values = new ContentValues();
                values.put(ListsContract.Column.ID, list_name);
                db.insert(ListsContract.TABLE, null, values);
                return "List "+list_name+" added";
            }else{
                return "List name can not be empty";
            }

        }catch (Exception e){
            return "ERROR adding list";
        }
    }

    public String deleteList(Context context){
        dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            String sql = String.format("DELETE FROM %s WHERE %s = \"%s\"",
                    ListsContract.TABLE,
                    ListsContract.Column.ID,
                    list_name);

            db.execSQL(sql);
            return "List "+list_name+" deleted";

        }catch (Exception e){
            return "ERROR deleting list";
        }
    }
}
