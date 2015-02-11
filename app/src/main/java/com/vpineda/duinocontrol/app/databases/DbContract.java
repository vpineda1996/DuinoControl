package com.vpineda.duinocontrol.app.databases;

import android.provider.BaseColumns;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by vpineda1996 on 2015-01-31.
 */
public final class DbContract {
    public static abstract class ServerEntry implements BaseColumns {
        public static final String SERVER_TABLE_NAME = "server_table";
        public static final String COLUMN_SERVER_UUID = "uuid";
        public static final String COLUMN_SERVER_TITLE = "title";
        public static final String COLUMN_SERVER_URI = "protocol";
    }

    public static abstract class RoomEntry implements BaseColumns{
        public static final String ROOM_TABLE_NAME = "room_table";
        public static final String COLUMN_ROOM_UUID = "uuid";
        public static final String COLUMN_ROOM_TITLE = "title";

    }

    public static abstract class ToggleEntry implements BaseColumns {
        public static final String TOGGLE_TABLE_NAME = "toggle_table";
        public static final String COLUMN_TOGGLE_UUID = "uuid";
        public static final String COLUMN_TOGGLE_TITLE = "title";
        public static final String COLUMN_TOGGLE_PIN = "pin";
        public static final String COLUMN_TOGGLE_TYPE = "type";
        public static final String COLUMN_TOGGLE_ROOM_UUID = "room_uuid";
        public static final String COLUMN_TOGGLE_SERVER_UUID = "server_uuid";

    }
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";


    public static final String SQL_CREATE_TABLE_SERVERS =
            "CREATE TABLE " + ServerEntry.SERVER_TABLE_NAME + " (" +
                    ServerEntry._ID + " INTEGER PRIMARY KEY," +
                    ServerEntry.COLUMN_SERVER_UUID + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_SERVER_TITLE + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.COLUMN_SERVER_URI + TEXT_TYPE + " )";

    public static final String SQL_CREATE_TABLE_ROOM =
            "CREATE TABLE " + RoomEntry.ROOM_TABLE_NAME + " (" +
                    RoomEntry._ID + " INTEGER PRIMARY KEY," +
                    RoomEntry.COLUMN_ROOM_UUID + TEXT_TYPE + COMMA_SEP +
                    RoomEntry.COLUMN_ROOM_TITLE + TEXT_TYPE + " )";

    public static final String SQL_CREATE_TABLE_TOGGLE =
            "CREATE TABLE " + ToggleEntry.TOGGLE_TABLE_NAME + " (" +
                    ToggleEntry._ID + " INTEGER PRIMARY KEY," +
                    ToggleEntry.COLUMN_TOGGLE_UUID + TEXT_TYPE + COMMA_SEP +
                    ToggleEntry.COLUMN_TOGGLE_TITLE + TEXT_TYPE + COMMA_SEP +
                    ToggleEntry.COLUMN_TOGGLE_PIN + INT_TYPE + COMMA_SEP +
                    ToggleEntry.COLUMN_TOGGLE_TYPE + TEXT_TYPE + COMMA_SEP +
                    ToggleEntry.COLUMN_TOGGLE_ROOM_UUID + TEXT_TYPE + COMMA_SEP +
                    ToggleEntry.COLUMN_TOGGLE_SERVER_UUID + TEXT_TYPE + " )";

    public static final String SQL_DELETE_SERVERS_TABLE =
            "DROP TABLE IF EXISTS " + ServerEntry.SERVER_TABLE_NAME;
    public static final String SQL_DELETE_ROOM_TABLE =
            "DROP TABLE IF EXISTS " + RoomEntry.ROOM_TABLE_NAME;
    public static final String SQL_DELETE_TOOGLE_TABLE =
            "DROP TABLE IF EXISTS " + ToggleEntry.TOGGLE_TABLE_NAME;

}
