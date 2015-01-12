package com.vpineda.duinocontrol.app.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-10.
 */
public class ServerDbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Server.db";

    public ServerDbHelper (Context context){
        super (context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ServerContract.SQL_CREATE_TABLE_SERVERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ServerContract.SQL_DELETE_SERVERS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public long addDuServer (DuServer server){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ServerContract.ServerEntry.SERVER_TITLE, server.getServerName());
        values.put(ServerContract.ServerEntry.SERVER_UUID, server.getServerID().toString());
        values.put(ServerContract.ServerEntry.SERVER_PROTOCOL, server.getProtocol());
        values.put(ServerContract.ServerEntry.SERVER_HOST, server.getHost());
        values.put(ServerContract.ServerEntry.SERVER_PORT, server.getPort());
        values.put(ServerContract.ServerEntry.SERVER_PATH, server.getPath());

        return db.insert(
                ServerContract.ServerEntry.TABLE_NAME,
                null,
                values);
    }

    public List<DuServer> getAllDuServers (){
        ArrayList<DuServer> servers = new ArrayList<DuServer>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                ServerContract.ServerEntry._ID,
                ServerContract.ServerEntry.SERVER_TITLE,
                ServerContract.ServerEntry.SERVER_UUID,
                ServerContract.ServerEntry.SERVER_PROTOCOL,
                ServerContract.ServerEntry.SERVER_HOST,
                ServerContract.ServerEntry.SERVER_PORT,
                ServerContract.ServerEntry.SERVER_PATH
        };

           // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ServerContract.ServerEntry._ID + " DESC";

        Cursor c = db.query(
                ServerContract.ServerEntry.TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasVal = c.moveToFirst();

        while (hasVal){
            UUID id = UUID.fromString(c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_UUID)));
            String title, protocol, host, port, path;
            title = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_TITLE));
            protocol = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_PROTOCOL));
            host = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_HOST));
            port = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_PORT));
            path = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_PATH));

            DuServer server = new DuServer(id, title, protocol, host, port, path);

            servers.add(server);

            hasVal = c.moveToNext();
        }

        db.close();
        return servers;
    }

    public DuServer getDuServer (DuServer dServer) {

        UUID id = dServer.getServerID();
        DuServer server = null;
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                ServerContract.ServerEntry._ID,
                ServerContract.ServerEntry.SERVER_TITLE,
                ServerContract.ServerEntry.SERVER_UUID,
                ServerContract.ServerEntry.SERVER_PROTOCOL,
                ServerContract.ServerEntry.SERVER_HOST,
                ServerContract.ServerEntry.SERVER_PORT,
                ServerContract.ServerEntry.SERVER_PATH
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ServerContract.ServerEntry._ID + " DESC";

        String selection = ServerContract.ServerEntry.SERVER_UUID + " ASC";
        String[] selectionArgs = {id.toString()};

        Cursor c = db.query(
                ServerContract.ServerEntry.TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                selection,                                     // The columns for the WHERE clause
                selectionArgs,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasVal = c.moveToFirst();

        if (hasVal){
            String title, protocol, host, port, path;
            title = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_TITLE));
            protocol = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_PROTOCOL));
            host = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_HOST));
            port = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_PORT));
            path = c.getString(c.getColumnIndex(ServerContract.ServerEntry.SERVER_PATH));

            server = new DuServer(id, title, protocol, host, port, path);

        }

        db.close();

        return server;
    }

    public void removeServer (DuServer server){
        SQLiteDatabase db = getReadableDatabase();

        String selection = ServerContract.ServerEntry.SERVER_UUID +" LIKE ?";
        String[] selectionArgs = { server.getServerID().toString() };

        db.delete(DATABASE_NAME, selection, selectionArgs);

        db.close();
    }

    public void updateServer(DuServer server, String updateField, String newValue){

        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        String selection = ServerContract.ServerEntry.SERVER_UUID +" LIKE ?";
        String[] selectionArgs = { server.getServerID().toString() };

        try {
            if (updateField.equals(ServerContract.ServerEntry.SERVER_TITLE)) {
                values.put(ServerContract.ServerEntry.SERVER_TITLE, newValue);
                db.update(
                        DATABASE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(ServerContract.ServerEntry.SERVER_PROTOCOL)) {
                values.put(ServerContract.ServerEntry.SERVER_PROTOCOL, newValue);
                db.update(
                        DATABASE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(ServerContract.ServerEntry.SERVER_HOST)) {
                values.put(ServerContract.ServerEntry.SERVER_HOST, newValue);
                db.update(
                        DATABASE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(ServerContract.ServerEntry.SERVER_PORT)) {
                values.put(ServerContract.ServerEntry.SERVER_PORT, newValue);
                db.update(
                        DATABASE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(ServerContract.ServerEntry.SERVER_PATH)) {
                values.put(ServerContract.ServerEntry.SERVER_PATH, newValue);
                db.update(
                        DATABASE_NAME,
                        values,
                        selection,
                        selectionArgs);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("db error", "Couldn't find the resource");
        }
        db.close();
    }
}