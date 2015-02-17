package com.vpineda.duinocontrol.app.classes.model.toggles;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.classes.ui.adapters.ToggleAdapter;
import com.vpineda.duinocontrol.app.networking.Commands;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Victor on 2/3/2015.
 */
public abstract class Toggle implements Server.OnResponseListener{
    private UUID uuid;
    private String name;
    private Server server;
    private ToggleTypes type;
    private List<UUID> roomsUUID;
    private View view;
    private int pin;

    // TODO: implement servos and IR

    /**
     * =====================================
     * ===========CONSTRUCTORS==============
     * =====================================
     */

    public Toggle (UUID id, String name, int pin, Server server, ToggleTypes type, List<UUID> roomsUUID) {
        this.uuid = id;
        this.name = name;
        this.pin = pin;
        this.server = server;
        this.type = type;
        this.roomsUUID = roomsUUID;
    }
    public Toggle (String name, int pin, Server server, ToggleTypes type, List<UUID> roomsUUID){
        this(UUID.randomUUID(),name, pin, server, type, roomsUUID);
    }

    /**
     * Gives the toggle's view to the RecyclerView
     * @param viewGroup view group of the RecyclerView
     * @param inflater inflater to inflate the xml file
     * @return the current view of the toggle
     */
    public abstract View getInflatedView(ViewGroup viewGroup, LayoutInflater inflater);

    /**
     * Set listeners for that special item toggle
     * @param viewHolder view holder in the RecyclerView of toggles
     * @param position position of the listener
     */
    public void setListeners(ToggleAdapter.ToggleRecycleViewViewHolder viewHolder, final int position){
        view =viewHolder.getView();
    }

    /**
     * Creates the message that will be sent to the server
     * @return the jsonobject that will be sent
     */
    protected abstract JSONObject getJSONMessage(Commands typeOfMessage);

    /**
     * Changes the state of the toggle
     * @param state the integer of the current state
     *              it can go from 0 to 255 but there is a special case
     *              where it can be -1 that means the opposite of the current value
     *              (useful for booleans)
     */
    protected abstract void setToggleState(int state);

    /**
     * Sends the message to the server and then returns the response from the server
     * @param typeOfMessage one of the commands that are in Commands section
     */
    public void sendJSONMessage(Commands typeOfMessage){
        try {
            server.sendCommand(getJSONMessage(typeOfMessage), this);
        } catch (UnsupportedEncodingException e) {
            Log.e("JSON", "Genereated JSONMessage doesn't make sense");
        }
    }
/**
     * =====================================
     * ======GETTERS AND SETTERS============
     * =====================================
     */

    /**
     * Method regarding saving rooms in the database
     * @return the JSON array containing the UUID of the room
     */
    public JSONArray getRoomsUUIDAsJSON() {
        JSONArray array = new JSONArray();
        for (UUID roomID : roomsUUID){
            array.put(roomID.toString());
        }
        return array;
    }

    /**
     * Parse the json from the database and return the list of Rooms
     * @param jsonArrayAsString string from the server
     * @return the list of roomUUID
     */
    public static List<UUID> getRoomsUUIDListFromRoomJSON(String jsonArrayAsString){
        List<UUID> rooms = null;
        try {
            // First decode the JSONArray that is given to you
            // if it isn't fallback to catch thus throwing null
            JSONArray jsonArray = new JSONArray(jsonArrayAsString);
            rooms = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++){
                rooms.add(UUID.fromString(jsonArray.getString(i)));
            }
        } catch (JSONException e) {
            Log.e("jsonParsing", "Error parsing JSON from database");
            e.printStackTrace();
        }
        return rooms;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ToggleTypes getType() {
        return type;
    }

    public void setType(ToggleTypes type) {
        this.type = type;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    protected View getToggleView() {
        return view;
    }

    public List<UUID> getRoomsUUID() {
        return roomsUUID;
    }
}
