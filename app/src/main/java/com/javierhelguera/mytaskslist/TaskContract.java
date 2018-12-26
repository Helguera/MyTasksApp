package com.javierhelguera.mytaskslist;

import android.provider.BaseColumns;

public class TaskContract {

    public static final String DB_NAME = "mytasklist.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "task";
    //public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";

    public class Column {

        public static final String ID = "idTask";
        public static final String ID_LIST = "idList";
        public static final String DESCRIPTION = "description";
        public static final String DUEDATE = "dueDate";
        public static final String DUETIME = "dueTime";
        public static final String IS_SELECTED = "isSelected";

    }

}