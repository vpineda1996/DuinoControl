package com.vpineda.duinocontrol.app.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.vpineda.duinocontrol.app.classes.model.Server;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-10.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "main.db";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbContract.SQL_CREATE_TABLE_SERVERS);
        db.execSQL(DbContract.SQL_CREATE_TABLE_ROOM);
        db.execSQL(DbContract.SQL_CREATE_TABLE_TOGGLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbContract.SQL_DELETE_SERVERS_TABLE);
        db.execSQL(DbContract.SQL_DELETE_ROOM_TABLE);
        db.execSQL(DbContract.SQL_DELETE_TOOGLE_TABLE);
        //TODO: DO SOMETHING WHEN UPGRADING INSTEAD OF JUST DELETING
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /*
     * ===================================================================================
     * ALL SERVER RELATED METHODS OF INSERTING DELETING AND GETTING DATA FROM SERVER TABLE
     * ===================================================================================
     */

    /* adds a server to the SERVER TABLE
     * REQUIRES: that a SERVER TABLE exists on the database
     * EFFECTS: adds a server to the SERVER TABLE
     */
    public long addServer(Server server) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ServerEntry.COLUMN_SERVER_TITLE, server.getName());
        values.put(DbContract.ServerEntry.COLUMN_SERVER_UUID, server.getUuid().toString());
        values.put(DbContract.ServerEntry.COLUMN_SERVER_URI, server.getUri().toString());
        return db.insert(
                DbContract.ServerEntry.SERVER_TABLE_NAME,
                null,
                values);
    }

    /*
     * gets all servers and store them in a List
     */

    public List<Server> getAllServers() {
        // start the array where all of the servers will be stored
        ArrayList<Server> servers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DbContract.ServerEntry._ID,
                DbContract.ServerEntry.COLUMN_SERVER_TITLE,
                DbContract.ServerEntry.COLUMN_SERVER_UUID,
                DbContract.ServerEntry.COLUMN_SERVER_URI
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DbContract.ServerEntry._ID + " DESC";

        Cursor c = db.query(
                DbContract.ServerEntry.SERVER_TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasVal = c.moveToFirst();

        while (hasVal) {
            UUID id = UUID.fromString(c.getString(c.getColumnIndex(DbContract.ServerEntry.COLUMN_SERVER_UUID)));
            String title = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_TITLE));
            URI uri = URI.create(c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_URI)));
            Server server = new Server(id, title, uri);
            servers.add(0,server);
            // assign to hasVal if moveToNext is valid
            hasVal = c.moveToNext();
        }
        db.close();
        //Reverse the server
        //Collections.reverse(servers);
        return servers;
    }
    /* Get

    public DuServer getDuServer (DuServer dServer) {

        UUID id = dServer.getServerID();
        DuServer server = null;
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DbContract.ServerEntry._ID,
                DbContract.ServerEntry.COLUMN_SERVER_TITLE,
                DbContract.ServerEntry.COLUMN_SERVER_UUID,
                DbContract.ServerEntry.COLUMN_SERVER_PROTOCOL,
                DbContract.ServerEntry.COLUMN_SERVER_HOST,
                DbContract.ServerEntry.COLUMN_SERVER_PORT,
                DbContract.ServerEntry.COLUMN_SERVER_PATH
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DbContract.ServerEntry._ID + " DESC";

        String selection = DbContract.ServerEntry.COLUMN_SERVER_UUID + " ASC";
        String[] selectionArgs = {id.toString()};

        Cursor c = db.query(
                DbContract.ServerEntry.SERVER_TABLE_NAME,    // The table to query
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
            title = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_TITLE));
            protocol = c.getInt(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_PROTOCOL));
            host = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_HOST));
            port = c.getInt(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_PORT));
            path = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_PATH));

            server = new DuServer(id, title, protocol, host, port, path);

        }

        db.close();

        return server;
    }

    public void removeServer (UUID uuid){
        SQLiteDatabase db = getReadableDatabase();

        String selection = DbContract.ServerEntry.COLUMN_SERVER_UUID +" LIKE ?";
        String[] selectionArgs = { uuid.toString() };

        db.delete(DbContract.ServerEntry.SERVER_TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    public void updateServer(DuServer server, String updateField, String newValue){

        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        String selection = DbContract.ServerEntry.COLUMN_SERVER_UUID +" LIKE ?";
        String[] selectionArgs = { server.getServerID().toString() };

        try {
            if (updateField.equals(DbContract.ServerEntry.COLUMN_SERVER_TITLE)) {
                values.put(DbContract.ServerEntry.COLUMN_SERVER_TITLE, newValue);
                db.update(
                        DbContract.ServerEntry.SERVER_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.COLUMN_SERVER_PROTOCOL)) {
                values.put(DbContract.ServerEntry.COLUMN_SERVER_PROTOCOL, Integer.valueOf(newValue));
                db.update(
                        DbContract.ServerEntry.SERVER_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.COLUMN_SERVER_HOST)) {
                values.put(DbContract.ServerEntry.COLUMN_SERVER_HOST, newValue);
                db.update(
                        DbContract.ServerEntry.SERVER_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.COLUMN_SERVER_PORT)) {
                values.put(DbContract.ServerEntry.COLUMN_SERVER_PORT, Integer.valueOf(newValue));
                db.update(
                        DbContract.ServerEntry.SERVER_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.COLUMN_SERVER_PATH)) {
                values.put(DbContract.ServerEntry.COLUMN_SERVER_PATH, newValue);
                db.update(
                        DbContract.ServerEntry.SERVER_TABLE_NAME,
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
        List<DuServer> duServer = getAllServers();
        String[] propertyList = new String[duServer.size()];
        for(int i = 0; i < duServer.size(); i++){
            DuServer server = duServer.get(i);
            if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_UUID)) {
                propertyList[i] = server.getServerID().toString();
            }else if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_TITLE)) {
                propertyList[i] = server.getServerName();
            }else if (property.equals(DbContract.ServerEntry._ID)) {
                propertyList[i] = String.valueOf(i);
            } else if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_PROTOCOL)) {
                propertyList[i] = String.valueOf(server.getProtocol());
            } else if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_HOST)) {
                propertyList[i] = server.getHost();
            } else if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_PORT)) {
                propertyList[i] = String.valueOf(server.getPort());
            } else if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_PATH)) {
                propertyList[i] = server.getPath();
            }
        }
        return propertyList;
    }

    // ===================================================
    // =====METHODS REGARDING MANAGING ROOMS TABLE========
    // ===================================================

    public long addRoom (Room room){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.RoomEntry.COLUMN_ROOM_TITLE, room.getName());
        values.put(DbContract.RoomEntry.COLUMN_ROOM_UUID, room.getUuid().toString());

        return db.insert(
                DbContract.RoomEntry.ROOM_TABLE_NAME,
                null,
                values);
    }

    public List<Room> getAllRooms (){
        ArrayList<Room> rooms = new ArrayList<Room>();
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                DbContract.ServerEntry._ID,
                DbContract.RoomEntry.COLUMN_ROOM_TITLE,
                DbContract.RoomEntry.COLUMN_ROOM_UUID
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DbContract.RoomEntry._ID + " DESC";

        Cursor c = db.query(
                DbContract.RoomEntry.ROOM_TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasVal = c.moveToFirst();

        while (hasVal){
            String title = c.getString(c.getColumnIndexOrThrow(DbContract.RoomEntry.COLUMN_ROOM_TITLE));
            UUID id = UUID.fromString(c.getString(c.getColumnIndex(DbContract.RoomEntry.COLUMN_ROOM_UUID)));
            Room room = new Room(title,id);

            rooms.add(room);
            hasVal = c.moveToNext();
        }
        db.close();
        Collections.reverse(rooms);
        return rooms;
    }


    public void removeRoom (UUID uuid){
        SQLiteDatabase db = getReadableDatabase();

        String selection = DbContract.RoomEntry.COLUMN_ROOM_UUID +" LIKE ?";
        String[] selectionArgs = { uuid.toString() };

        db.delete(DbContract.RoomEntry.ROOM_TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    public void updateRoom(Room room, String updateField, String newValue){

        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        String selection = DbContract.RoomEntry.COLUMN_ROOM_UUID +" LIKE ?";
        String[] selectionArgs = { room.getUuid().toString() };

        try {
            if (updateField.equals(DbContract.RoomEntry.COLUMN_ROOM_TITLE)) {
                values.put(DbContract.RoomEntry.COLUMN_ROOM_TITLE, newValue);
                db.update(
                        DbContract.RoomEntry.ROOM_TABLE_NAME,
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

    public String[] getAllRoomsProperty (String property) {
        List<Room> rooms = getAllRooms();
        String[] propertyList = new String[rooms.size()];
        for(int i = 0; i < rooms.size(); i++){
            Room room = rooms.get(i);
            if (property.equals(DbContract.RoomEntry.COLUMN_ROOM_UUID)) {
                propertyList[i] = room.getUuid().toString();
            }else if (property.equals(DbContract.RoomEntry.COLUMN_ROOM_TITLE)) {
                propertyList[i] = room.getName();
            }else if (property.equals(DbContract.RoomEntry._ID)) {
                propertyList[i] = String.valueOf(i);
            }
        }
        return propertyList;
    }

    // ===================================================
    // =====METHODS MANAGING TOGGLE TABLE=================
    // ===================================================

    public long addToggle (RoomToggle toggle){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_TITLE, toggle.getName());
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_UUID, toggle.getUuid().toString());
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE, toggle.getType().name());
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID, toggle.getRoomID().toString());
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID, toggle.getServerID().toString());

        return db.insert(
                DbContract.ToggleEntry.TOGGLE_TABLE_NAME,
                null,
                values);
    }

    public List<RoomToggle> getAllRoomToggles (UUID roomID){
        ArrayList<RoomToggle> toggles = new ArrayList<RoomToggle>();
        SQLiteDatabase db = getReadableDatabase();
        //TODO
        String[] projection = {
                DbContract.ServerEntry._ID,
                DbContract.RoomEntry.COLUMN_ROOM_TITLE,
                DbContract.RoomEntry.COLUMN_ROOM_UUID,
                DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE,
                DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID,
                DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID
        };


        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DbContract.ServerEntry._ID + " DESC";

        String selection = DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID + " ASC";
        String[] selectionArgs = {roomID.toString()};

        Cursor c = db.query(
                DbContract.RoomEntry.ROOM_TABLE_NAME,    // The table to query
                projection,                               // The columns to return
                selection,                                     // The columns for the WHERE clause
                selectionArgs,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        boolean hasVal = c.moveToFirst();

        while (hasVal){
            String title, uuid, roomUUID, serverUUID ;
            RoomToggle.ToggleType toggleType;
            title = c.getString(c.getColumnIndexOrThrow(DbContract.RoomEntry.COLUMN_ROOM_TITLE));
            uuid = c.getString(c.getColumnIndexOrThrow(DbContract.RoomEntry.COLUMN_ROOM_UUID));
            toggleType = RoomToggle.ToggleType.valueOf(c.getString(c.getColumnIndexOrThrow(DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE)));
            roomUUID = c.getString(c.getColumnIndexOrThrow(DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID));
            serverUUID = c.getString(c.getColumnIndexOrThrow(DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID));

            RoomToggle toggle = new RoomToggle(UUID.fromString(uuid),
                    title,
                    toggleType,
                    UUID.fromString(roomUUID),
                    UUID.fromString(serverUUID));
            hasVal = c.moveToNext();

            toggles.add(toggle);
        }

        db.close();
        Collections.reverse(toggles);
        return toggles;
    }

    public void removeToggle (UUID uuid){
        SQLiteDatabase db = getReadableDatabase();

        String selection = DbContract.ToggleEntry.COLUMN_TOGGLE_UUID +" LIKE ?";
        String[] selectionArgs = { uuid.toString() };

        db.delete(DbContract.ToggleEntry.TOGGLE_TABLE_NAME, selection, selectionArgs);

        db.close();
    }
    public void updateToggle(RoomToggle toggle, RoomToggle.ToggleType newValue){
        updateToggle(toggle, DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE, newValue.name());
    }

    public void updateToggle(RoomToggle toggle, String updateField, String newValue){

        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        String selection = DbContract.ToggleEntry.COLUMN_TOGGLE_UUID +" LIKE ?";
        String[] selectionArgs = { toggle.getUuid().toString() };

        try {
            if (updateField.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_TITLE)) {
                values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_TITLE, newValue);
                db.update(
                        DbContract.ToggleEntry.TOGGLE_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if(updateField.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE)){
                values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_TITLE, newValue);
                db.update(
                        DbContract.ToggleEntry.TOGGLE_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if(updateField.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID)){
                values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID, newValue);
                db.update(
                        DbContract.ToggleEntry.TOGGLE_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            }else if(updateField.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID)){
                values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID, newValue);
                db.update(
                        DbContract.ToggleEntry.TOGGLE_TABLE_NAME,
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

    public String[] getAllTogglesProperty (String property, UUID roomID) {
        List<RoomToggle> toggles = getAllRoomToggles(roomID);
        String[] propertyList = new String[toggles.size()];
        for(int i = 0; i < toggles.size(); i++){
            RoomToggle toogle = toggles.get(i);
            if (property.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_UUID)) {
                propertyList[i] = toogle.getUuid().toString();
            }else if (property.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_TITLE)) {
                propertyList[i] = toogle.getName();
            }else if (property.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE)) {
                propertyList[i] = toogle.getType().name();
            }else if (property.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID)) {
                propertyList[i] = toogle.getRoomID().toString();
            }else if (property.equals(DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID)) {
                propertyList[i] = toogle.getServerID().toString();
            }else if (property.equals(DbContract.ToggleEntry._ID)) {
                propertyList[i] = String.valueOf(i);
            }
        }
        return propertyList;
    }*/


}