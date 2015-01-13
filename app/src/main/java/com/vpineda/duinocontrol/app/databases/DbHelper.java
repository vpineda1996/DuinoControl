package com.vpineda.duinocontrol.app.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-10.
 */
public class DbHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Server.db";

    public DbHelper(Context context){
        super (context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_TABLE_SERVERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.SQL_DELETE_SERVERS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /*
    ALL SERVER RELATED METHODS OF INSERTING DELETING AND GETTING DATA FROM DATABASE
     */

    public long addDuServer (DuServer server){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ServerEntry.SERVER_TITLE, server.getServerName());
        values.put(DbContract.ServerEntry.SERVER_UUID, server.getServerID().toString());
        values.put(DbContract.ServerEntry.SERVER_PROTOCOL, server.getProtocol());
        values.put(DbContract.ServerEntry.SERVER_HOST, server.getHost());
        values.put(DbContract.ServerEntry.SERVER_PORT, server.getPort());
        values.put(DbContract.ServerEntry.SERVER_PATH, server.getPath());

        return db.insert(
                DbContract.ServerEntry.TABLE_NAME,
                null,
                values);
    }

    public List<DuServer> getAllDuServers (){
        ArrayList<DuServer> servers = new ArrayList<DuServer>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DbContract.ServerEntry._ID,
                DbContract.ServerEntry.SERVER_TITLE,
                DbContract.ServerEntry.SERVER_UUID,
                DbContract.ServerEntry.SERVER_PROTOCOL,
                DbContract.ServerEntry.SERVER_HOST,
                DbContract.ServerEntry.SERVER_PORT,
                DbContract.ServerEntry.SERVER_PATH
        };

           // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DbContract.ServerEntry._ID + " DESC";

        Cursor c = db.query(
                DbContract.ServerEntry.TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasVal = c.moveToFirst();

        while (hasVal){
            UUID id = UUID.fromString(c.getString(c.getColumnIndex(DbContract.ServerEntry.SERVER_UUID)));
            String title, host, path;
            int protocol, port;
            title = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_TITLE));
            protocol = c.getInt(c.getColumnIndex(DbContract.ServerEntry.SERVER_PROTOCOL));
            host = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_HOST));
            port = c.getInt(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_PORT));
            path = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_PATH));

            DuServer server = new DuServer(id, title, protocol, host, port, path);

            servers.add(server);

            hasVal = c.moveToNext();
        }

        db.close();

        Collections.reverse(servers);
        return servers;
    }

    public DuServer getDuServer (DuServer dServer) {

        UUID id = dServer.getServerID();
        DuServer server = null;
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DbContract.ServerEntry._ID,
                DbContract.ServerEntry.SERVER_TITLE,
                DbContract.ServerEntry.SERVER_UUID,
                DbContract.ServerEntry.SERVER_PROTOCOL,
                DbContract.ServerEntry.SERVER_HOST,
                DbContract.ServerEntry.SERVER_PORT,
                DbContract.ServerEntry.SERVER_PATH
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DbContract.ServerEntry._ID + " DESC";

        String selection = DbContract.ServerEntry.SERVER_UUID + " ASC";
        String[] selectionArgs = {id.toString()};

        Cursor c = db.query(
                DbContract.ServerEntry.TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                selection,                                     // The columns for the WHERE clause
                selectionArgs,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasVal = c.moveToFirst();

        if (hasVal){
            String title, host, path;
            int protocol, port;
            title = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_TITLE));
            protocol = c.getInt(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_PROTOCOL));
            host = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_HOST));
            port = c.getInt(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_PORT));
            path = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.SERVER_PATH));

            server = new DuServer(id, title, protocol, host, port, path);

        }

        db.close();

        return server;
    }

    public void removeServer (UUID uuid){
        SQLiteDatabase db = getReadableDatabase();

        String selection = DbContract.ServerEntry.SERVER_UUID +" LIKE ?";
        String[] selectionArgs = { uuid.toString() };

        db.delete(DbContract.ServerEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    public void updateServer(DuServer server, String updateField, String newValue){

        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        String selection = DbContract.ServerEntry.SERVER_UUID +" LIKE ?";
        String[] selectionArgs = { server.getServerID().toString() };

        try {
            if (updateField.equals(DbContract.ServerEntry.SERVER_TITLE)) {
                values.put(DbContract.ServerEntry.SERVER_TITLE, newValue);
                db.update(
                        DbContract.ServerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.SERVER_PROTOCOL)) {
                values.put(DbContract.ServerEntry.SERVER_PROTOCOL, Integer.valueOf(newValue));
                db.update(
                        DbContract.ServerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.SERVER_HOST)) {
                values.put(DbContract.ServerEntry.SERVER_HOST, newValue);
                db.update(
                        DbContract.ServerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.SERVER_PORT)) {
                values.put(DbContract.ServerEntry.SERVER_PORT, Integer.valueOf(newValue));
                db.update(
                        DbContract.ServerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.SERVER_PATH)) {
                values.put(DbContract.ServerEntry.SERVER_PATH, newValue);
                db.update(
                        DbContract.ServerEntry.TABLE_NAME,
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

    public String[] getAllServersProperty (String property) {
        List<DuServer> duServer = getAllDuServers();
        String[] propertyList = new String[duServer.size()];
        for(int i = 0; i < duServer.size(); i++){
            DuServer server = duServer.get(i);
            if (property.equals(DbContract.ServerEntry.SERVER_UUID)) {
                propertyList[i] = server.getServerID().toString();
            }else if (property.equals(DbContract.ServerEntry.SERVER_TITLE)) {
                propertyList[i] = server.getServerName();
            }else if (property.equals(DbContract.ServerEntry._ID)) {
                propertyList[i] = String.valueOf(i);
            } else if (property.equals(DbContract.ServerEntry.SERVER_PROTOCOL)) {
                propertyList[i] = String.valueOf(server.getProtocol());
            } else if (property.equals(DbContract.ServerEntry.SERVER_HOST)) {
                propertyList[i] = server.getHost();
            } else if (property.equals(DbContract.ServerEntry.SERVER_PORT)) {
                propertyList[i] = String.valueOf(server.getPort());
            } else if (property.equals(DbContract.ServerEntry.SERVER_PATH)) {
                propertyList[i] = server.getPath();
            }
        }
        return propertyList;
    }
}