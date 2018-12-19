package com.javierhelguera.mytaskslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Console;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context){
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT, %s TEXT, FOREIGN KEY (%s) references list(%s) )",
                TaskContract.TABLE,
                TaskContract.Column.ID,
                TaskContract.Column.ID_LIST,
                TaskContract.Column.DESCRIPTION,
                TaskContract.Column.DUEDATE,
                TaskContract.Column.DUETIME,
                TaskContract.Column.ID_LIST,
                TaskContract.Column.ID_LIST);

        db.execSQL(sql);

        sql = String.format("CREATE TABLE %s ( %s TEXT NOT NULL, PRIMARY KEY (%s) )",
                ListsContract.TABLE,
                ListsContract.Column.ID,
                ListsContract.Column.ID);

        db.execSQL(sql);

        ContentValues cv = new ContentValues();
        cv.put(ListsContract.Column.ID, "Uncategorized");

        db.insert(ListsContract.TABLE, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TaskContract.TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ListsContract.TABLE);
        onCreate(db);
    }

}
