package com.javierhelguera.mytaskslist;

import com.getbase.floatingactionbutton.FloatingActionButton;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.sql.StatementEvent;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private NavigationView navmenu;
    private String listToShow = "All tasks";
    private Context context = this;

    DBHelper dbhelper = new DBHelper(this);

    tasksRepository repositorio = new tasksRepository(this);  //ALMACEN DONDE ESTARAN TODOS LAS TASKSMODELS


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("All tasks");


        /* ----------------------- PARA EL FAB DESPLEGABLE ----------------------- */

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton add_list = findViewById(R.id.add_list);
        FloatingActionButton add_task = findViewById(R.id.add_task);

        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(v.getContext(), addListActivity.class));
                new addListDialog(MainActivity.this, new addListDialog.addListDialogListener() {
                    @Override
                    public void onOK(final String list) {
                        ListModel temp = new ListModel(list);
                        String result = temp.saveList(context);
                        updateNavMenu();
                        Snackbar.make(mDrawerLayout, result, Snackbar.LENGTH_LONG).show();
                    }
                    @Override
                    public void onCancel(String list) {

                    }
                }).show();
            }
        });

        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), addItemActivity.class));
            }
        });


        /* ----------------------- PARA LA LISTA DE CHECKBOXES ----------------------- */

        showTasks(listToShow);

        /* ----------------------- PARA AÑADIR LAS LISTAS AL MENU LATERAL ----------------------- */

        updateNavMenu();

        navmenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                setTitle(item.getTitle().toString());
                listToShow = item.getTitle().toString();
                showTasks(listToShow);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    private List<String> getLists() {
        List<String> lists = new ArrayList<String>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM list", null);
            long count = DatabaseUtils.queryNumEntries(db, ListsContract.TABLE);
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                lists.add(cursor.getString(0));
            }
            return lists;


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "ERROR loading lists", Toast.LENGTH_LONG).show();
        }
        return null;
    }


    private void showTasks(final String listToShow) {
        ListView listView = (ListView) findViewById(R.id.listview);

        repositorio.refresh();

        final ArrayList<TaskModel> temp = repositorio.getRepository(listToShow);

        final CustomAdapter adapter = new CustomAdapter(this, temp);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TaskModel model = temp.get(i);

                if (model.isSelected() == 1) {
                    model.setSelected(0);
                    repositorio.updateTask(model);
                } else {
                    model.setSelected(1);
                    repositorio.updateTask(model);
                }


                adapter.updateRecords(temp);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToogle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.clearDone:

                try {
                    repositorio.removeSelectedTasksFromList(listToShow);
                    showTasks(listToShow);
                    Snackbar.make(mDrawerLayout, "Deleted done tasks", Snackbar.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error deleting done tasks", Toast.LENGTH_LONG).show();
                }

                return true;

            case R.id.deleteList:

                List<String> lists = getLists();
                lists.remove(0);
                String[] listsArray = new String[lists.size()];
                listsArray = lists.toArray(listsArray);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete a list");
                builder.setItems(listsArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> lists = getLists();
                        lists.remove(0);
                        String[] listsArray = new String[lists.size()];
                        listsArray = lists.toArray(listsArray);
                        ListModel temp = new ListModel(listsArray[which]);
                        String result = temp.deleteList(context);

                        Snackbar.make(mDrawerLayout, result, Snackbar.LENGTH_LONG).show();
                        updateNavMenu();
                    }
                });
                builder.show();

                return  true;
            case R.id.about:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle(R.string.about);
                alertDialog.setMessage(getResources().getString(R.string.about_content) + "\n\n" + getResources().getString(R.string.source_code));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    public void updateNavMenu(){
        navmenu = (NavigationView) findViewById(R.id.navigation_menu);
        navmenu.bringToFront();

        Menu menu = navmenu.getMenu();
        menu.clear();
        MenuItem item2 = menu.add(R.id.grp1,R.id.all_tasks, Menu.NONE, "All tasks");
        item2.setIcon(R.drawable.ic_list_black_24dp);
        List<String> lists = getLists();

        for (int i = 0; i < lists.size(); i++) {
            MenuItem item = menu.add(lists.get(i));
            item.setIcon(R.drawable.ic_list_black_24dp);
        }

        menu.performIdentifierAction(R.id.all_tasks,0);
    }

}
