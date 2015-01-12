package com.vpineda.duinocontrol.app.databases;

import android.provider.BaseColumns;

/**
 * Created by vpineda1996 on 2015-01-10.
 */
public final class ServerContract {

    public ServerContract(){}

    public static abstract class ServerEntry implements BaseColumns {
        public static final String TABLE_NAME = "servers";
        public static final String SERVER_UUID = "entryid";
        public static final String SERVER_TITLE = "title";
        public static final String SERVER_PROTOCOL = "protocol";
        public static final String SERVER_HOST = "host";
        public static final String SERVER_PORT = "port";
        public static final String SERVER_PATH = "path";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLE_SERVERS =
            "CREATE TABLE " + ServerEntry.TABLE_NAME + " (" +
                    ServerEntry._ID + " INTEGER PRIMARY KEY," +
                    ServerEntry.SERVER_UUID + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.SERVER_TITLE + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.SERVER_PROTOCOL + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.SERVER_HOST + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.SERVER_PORT + TEXT_TYPE + COMMA_SEP +
                    ServerEntry.SERVER_PATH  + TEXT_TYPE +
                    " )";

    public static final String SQL_DELETE_SERVERS_TABLE =
            "DROP TABLE IF EXISTS " + ServerEntry.TABLE_NAME;



}
