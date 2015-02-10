package com.vpineda.duinocontrol.app.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.vpineda.duinocontrol.app.classes.model.Room;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.classes.model.toggles.Toggle;
import com.vpineda.duinocontrol.app.classes.model.toggles.ToggleTypes;

import java.net.URI;
import java.util.ArrayList;
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

    /**
     * ===================================================================================
     * ALL SERVER RELATED METHODS OF INSERTING DELETING AND GETTING DATA FROM SERVER TABLE
     * ===================================================================================
     */

    /**
     * adds server to the SERVER TABLE
     * @param server a valid server
     * @return returns the row ID of the newly inserted row, or -1 if an error occurred
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

    /**
     * gets all servers and store them in a List
     * @return a list of all the serverx
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

    /**
     * Gets the url and everything of that specific server
     * @param wantedServer UUID of that specific server
     * @return the server object or null if it doesn't exist
     */
    public Server getServer(UUID wantedServer){
        Server server = null;
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
        String selection = DbContract.ServerEntry.COLUMN_SERVER_UUID + " LIKE ?";
        String[] selectionArgs = {wantedServer.toString()};

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

        while (hasVal) {
            UUID id = UUID.fromString(c.getString(c.getColumnIndex(DbContract.ServerEntry.COLUMN_SERVER_UUID)));
            String title = c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_TITLE));
            URI uri = URI.create(c.getString(c.getColumnIndexOrThrow(DbContract.ServerEntry.COLUMN_SERVER_URI)));
            server = new Server(id, title, uri);
            // assign to hasVal if moveToNext is valid
            hasVal = c.moveToNext();
        }
        db.close();
        if(server != null) {
            return server;
        }else return null;
    }
    public Server getServer(String wantedServerUUID){
        return this.getServer(UUID.fromString(wantedServerUUID));
    }

    /**
     * removes the server from the database
     * TODO: Remove the toggles that use the server when it is removed
     * @param uuid uuid of the server or the Server object
     */
    public void removeServer (UUID uuid){
        SQLiteDatabase db = getReadableDatabase();

        String selection = DbContract.ServerEntry.COLUMN_SERVER_UUID +" LIKE ?";
        String[] selectionArgs = { uuid.toString() };

        db.delete(DbContract.ServerEntry.SERVER_TABLE_NAME, selection, selectionArgs);

        db.close();
    }
    public void removeServer (Server server){
        this.removeServer(server.getUuid());
    }

    /**
     * updates a specific value of that server
     * @param server UUID of the server that you want to update
     * @param updateField update fields (listed on DbContract.ServerEntry)
     * @param newValue new value as a string that will be instead of the old value
     */
    public void updateServer(UUID server, String updateField, String newValue){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        String selection = DbContract.ServerEntry.COLUMN_SERVER_UUID +" LIKE ?";
        String[] selectionArgs = { server.toString() };

        try {
            if (updateField.equals(DbContract.ServerEntry.COLUMN_SERVER_TITLE)) {
                values.put(DbContract.ServerEntry.COLUMN_SERVER_TITLE, newValue);
                db.update(
                        DbContract.ServerEntry.SERVER_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            } else if (updateField.equals(DbContract.ServerEntry.COLUMN_SERVER_URI)) {
                values.put(DbContract.ServerEntry.COLUMN_SERVER_URI, Integer.valueOf(newValue));
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
    public void updateServer(Server server, String updateField, String newValue){
        this.updateServer(server.getUuid(),updateField,newValue);
    }

    /**
     * gets an array containing strings of that specific property
     * @param property can be any of the properties in DbContract.ServerEntry
     * @return an array containing all of the properties
     */
    public String[] getAllServersProperty (String property) {
        List<Server> servers = getAllServers();
        String[] propertyList = new String[servers.size()];
        int i = 0;
        for(Server server : servers){
            if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_UUID)) {
                propertyList[i] = server.getUuid().toString();
            } else if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_TITLE)) {
                propertyList[i] = server.getName();
            } else if (property.equals(DbContract.ServerEntry.COLUMN_SERVER_URI)) {
                propertyList[i] = String.valueOf(server.getUri());
            }
            i++;
        }
        return propertyList;
    }

    //TODO implement all of that code below in the newly refactored way :D (FUN)

    /**
     * ================================================================================
     * ALL ROOM RELATED METHODS OF INSERTING DELETING AND GETTING DATA FROM ROOM TABLE
     * ================================================================================
     */

    /**
     * Adds a room to the Room table
     * @param room the room you want to add
     * @return returns the row ID of the newly inserted row, or -1 if an error occurred
     */
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

    /**
     * gets all of the rooms in the database
     * @return list containing all of the rooms
     */
    public List<Room> getAllRooms(){
        ArrayList<Room> rooms = new ArrayList<>();
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
            Room room = new Room(id,title,getAllRoomToggles(id));
            rooms.add(0,room);
            hasVal = c.moveToNext();
        }
        db.close();
        return rooms;
    }

    /**
     * Removes a room with that specific UUID
     * @param uuid uuid of the room that will be deleted
     */
    public void removeRoom (UUID uuid){
        SQLiteDatabase db = getReadableDatabase();

        String selection = DbContract.RoomEntry.COLUMN_ROOM_UUID +" LIKE ?";
        String[] selectionArgs = { uuid.toString() };

        db.delete(DbContract.RoomEntry.ROOM_TABLE_NAME, selection, selectionArgs);

        db.close();
    }
    public void removeRoom (Room room){
        this.removeRoom(room.getUuid());
        //TODO: when removing a room, remove the UUID from all the toggles
    }

    /**
     * updates the room in the specific update field with the specific
     * new value
     * @param roomUUID the room UUID that will be updated
     * @param updateField the update field that you want to update, all of this values
     *                    are on DbContract.RoomEntry
     * @param newValue it is the new string value that you will assign to the updateField
     */
    public void updateRoom(UUID roomUUID, String updateField, String newValue){

        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();

        String selection = DbContract.RoomEntry.COLUMN_ROOM_UUID +" LIKE ?";
        String[] selectionArgs = { roomUUID.toString() };

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
    public void updateRoom(Room room, String updateField, String newValue){
        this.updateRoom(room.getUuid(), updateField, newValue);
    }

    /**
     * Preferable method to the get rooms since this only goes once to the database instead of two times
     * because it doesn't add the toggles to Room
     * @param property a property in DbContract.RoomEntry
     * @return list containing the specific property of the room
     */
    public List<String> getAllRoomsProperty (String property) {
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
        List<String> propertyList = new ArrayList<>();
        boolean hasVal = c.moveToFirst();
        while (hasVal) {
            if (property.equals(DbContract.RoomEntry.COLUMN_ROOM_UUID)) {
                propertyList.add(0,c.getString(c.getColumnIndexOrThrow(DbContract.RoomEntry.COLUMN_ROOM_UUID)));
            } else if (property.equals(DbContract.RoomEntry.COLUMN_ROOM_TITLE)) {
                propertyList.add(0,c.getString(c.getColumnIndexOrThrow(DbContract.RoomEntry.COLUMN_ROOM_TITLE)));
            }
        }
        db.close();
        return propertyList;
    }

    /**
     * ===================================================================================
     * ALL TOGGLE RELATED METHODS OF INSERTING DELETING AND GETTING DATA FROM TOGGLE TABLE
     * ===================================================================================
     */

    /**
     * Adds a toggle to the Toggle database
     * (something really important is that the toggle must have a valid
     * server saved on the SERVER TABLE assigned to it)
     * @param toggle toggle that you want to add to the database
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long addToggle (Toggle toggle){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_TITLE, toggle.getName());
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_UUID, toggle.getUuid().toString());
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE, toggle.getType().toString());
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID, toggle.getRoomsUUIDAsJSON().toString());
        values.put(DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID, toggle.getServer().getUuid().toString());

        return db.insert(
                DbContract.ToggleEntry.TOGGLE_TABLE_NAME,
                null,
                values);
    }

    /**
     * Returns the list of toggles of with all of their characteristics
     * (something really important is that the toggle must have a valid
     * server saved on the SERVER TABLE assigned to it)
     * TODO: maybe check if the server is on the table and if it isn't throw an error
     * @param roomID uuid of the room that wants toggles
     * @return a list of the toggles of the room
     */
    public List<Toggle> getAllRoomToggles (UUID roomID){
        ArrayList<Toggle> toggles = new ArrayList<>();
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

        String selection = DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID + " LIKE ?";
        String[] selectionArgs = {"%" + roomID.toString() + "%"};

        Cursor c = db.query(
                DbContract.ToggleEntry.TOGGLE_TABLE_NAME,    // The table to query
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
            ToggleTypes toggleType =
                    ToggleTypes.valueOf(c.getString(c.getColumnIndexOrThrow(DbContract.ToggleEntry.COLUMN_TOGGLE_TYPE)));
            title = c.getString(c.getColumnIndexOrThrow(DbContract.RoomEntry.COLUMN_ROOM_TITLE));
            uuid = c.getString(c.getColumnIndexOrThrow(DbContract.RoomEntry.COLUMN_ROOM_UUID));
            roomUUID = c.getString(c.getColumnIndexOrThrow(DbContract.ToggleEntry.COLUMN_TOGGLE_ROOM_UUID));
            serverUUID = c.getString(c.getColumnIndexOrThrow(DbContract.ToggleEntry.COLUMN_TOGGLE_SERVER_UUID));

            toggles.add(toggleType.getToggleObject(UUID.fromString(uuid),
                    title,
                    getServer(serverUUID),
                    Toggle.getRoomsUUIDListFromRoomJSON(roomUUID)));
            hasVal = c.moveToNext();
        }
        db.close();
        return toggles;
    }
    public List<Toggle> getAllRoomToggles (Room room){
        return this.getAllRoomToggles(room.getUuid());
    }

    /**
     * Removes the toggle from the database
     * @param uuid
     */
    public void removeToggle (UUID uuid){
        SQLiteDatabase db = getReadableDatabase();

        String selection = DbContract.ToggleEntry.COLUMN_TOGGLE_UUID +" LIKE ?";
        String[] selectionArgs = { uuid.toString() };

        db.delete(DbContract.ToggleEntry.TOGGLE_TABLE_NAME, selection, selectionArgs);

        db.close();
    }
    /*
    public void updateToggle(Toggle toggle, RoomToggle.ToggleType newValue){
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
    public String[] getAllTogglesPropertyByRoom (String property, UUID roomID) {
        List<Toggle> toggles = getAllRoomToggles(roomID);
        String[] propertyList = new String[toggles.size()];
        for(int i = 0; i < toggles.size(); i++){
             toogle = toggles.get(i);
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