package com.javierhelguera.mytaskslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class tasksRepository {

    ArrayList<TaskModel> repository;
    DBHelper dbhelper;
    Context context;

    public tasksRepository(Context context){
        repository = new ArrayList<>();
        dbhelper = new DBHelper(context);
        this.context = context;
    }


    public ArrayList<TaskModel> getRepository() {
        return repository;
    }

    public ArrayList<TaskModel> getRepository(String category){
        if (category.equals("All tasks")){
            return repository;
        }else{
            ArrayList<TaskModel> temp = new ArrayList<>();

            for (int i=0; i<repository.size(); i++){
                if(repository.get(i).getCategory().equals(category)){
                    temp.add(repository.get(i));
                }
            }

            return temp;
        }
    }

    public void setRepository(ArrayList<TaskModel> repository) {
        this.repository = repository;
    }

    public void addTask(TaskModel task) {
        repository.add(task);
    }

    public ArrayList<TaskModel> getAllTasks(){
        return repository;
    }

    public TaskModel getTask(int i) {
        return repository.get(i);
    }

    public void refresh(){

        SQLiteDatabase db=dbhelper.getReadableDatabase();
        repository = new ArrayList<>();

        try {
            Cursor cursor;
            cursor = db.rawQuery("SELECT * FROM task ORDER BY dueDate ASC", null);

            long count = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i<count; i++){
                cursor.moveToPosition(i);
                TaskModel temp = new TaskModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5));

                repository.add(temp);
            }

        }catch (Exception e){
            Toast.makeText(context,"ERROR loading lists",Toast.LENGTH_LONG).show();
        }

    }

    public void updateTask(TaskModel model){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql = String.format("UPDATE %s SET %s = %s WHERE %s = %s",
                TaskContract.TABLE,
                TaskContract.Column.IS_SELECTED,
                model.isSelected(),
                TaskContract.Column.ID,
                model.getInternalID());
        db.execSQL(sql);
    }

    public void removeSelectedTasksFromList(String list){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String sql;
        if (list.equals("All tasks")){
            sql = String.format("DELETE FROM %s WHERE %s = 1",
                    TaskContract.TABLE,
                    TaskContract.Column.IS_SELECTED);
        }else{
            sql = String.format("DELETE FROM %s WHERE ( %s = \"%s\" AND %s = 1)",
                    TaskContract.TABLE,
                    TaskContract.Column.ID_LIST,
                    list,
                    TaskContract.Column.IS_SELECTED);
        }
        db.execSQL(sql);
    }




}
