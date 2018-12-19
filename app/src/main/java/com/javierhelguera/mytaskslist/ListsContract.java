package com.javierhelguera.mytaskslist;

import android.provider.BaseColumns;

public class ListsContract {

    public static final String DB_NAME = "mytasklist.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "list";
    //public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";

    public class Column {

        public static final String ID = "idList";

    }

}
