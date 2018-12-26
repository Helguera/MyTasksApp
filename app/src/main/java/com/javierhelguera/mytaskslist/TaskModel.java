package com.javierhelguera.mytaskslist;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by arthonsystechnologiesllp on 10/03/17.
 */

public class TaskModel {

    int isSelected;
    String taskName;
    String hour;
    String date;
    String category;
    int internalID;
    DBHelper dbhelper;

    //now create constructor and getter setter method using shortcut like command+n for mac & Alt+Insert for window.


    public TaskModel(int internalID, String category, String taskName, String date, String hour, int isSelected) {
        this.internalID = internalID;
        this.category = category;
        this.taskName = taskName;
        this.date = date;
        this.hour = hour;
        this.isSelected = isSelected;
    }

    public TaskModel(String category, String taskName, String date, String hour, int isSelected) {
        this.category = category;
        this.taskName = taskName;
        this.date = date;
        this.hour = hour;
        this.isSelected = isSelected;
    }

    public int isSelected() {
        return isSelected;
    }

    public void setSelected(int selected) {
        isSelected = selected;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getHour() {
        return hour;
    }

    public String getDate() {
        return date;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTaskName(String userName) {
        this.taskName = userName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getInternalID() {
        return internalID;
    }

    public void setInternalID(int internalID) {
        this.internalID = internalID;
    }

    public void saveTask(Context context) {
        dbhelper = new DBHelper(context);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql;

        sql = String.format("INSERT INTO %s(%s,%s,%s,%s,%s) VALUES (\"%s\", \"%s\", \"%s\", \"%s\", %s)",
                TaskContract.TABLE,
                TaskContract.Column.ID_LIST,
                TaskContract.Column.DESCRIPTION,
                TaskContract.Column.DUEDATE,
                TaskContract.Column.DUETIME,
                TaskContract.Column.IS_SELECTED,
                category,
                taskName,
                date,
                hour,
                isSelected);


        db.execSQL(sql);
    }
}
