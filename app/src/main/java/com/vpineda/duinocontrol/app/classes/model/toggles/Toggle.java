package com.vpineda.duinocontrol.app.classes.model.toggles;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.classes.ui.adapters.ToggleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Victor on 2/3/2015.
 */
public abstract class Toggle {
    private UUID uuid;
    private String name;
    private Server server;
    private ToggleTypes type;
    private List<UUID> roomsUUID;

    // TODO: implement servos and IR

    /**
     * =====================================
     * ===========CONSTRUCTORS==============
     * =====================================
     */

    public Toggle (UUID id, String name, Server server, ToggleTypes type, List<UUID> roomsUUID) {
        this.uuid = id;
        this.name = name;
        this.server = server;
        this.type = type;
        this.roomsUUID = roomsUUID;
    }
    public Toggle (String name, Server server, ToggleTypes type, List<UUID> roomsUUID){
        this(UUID.randomUUID(),name, server, type, roomsUUID);
    }

    /**
     * Gives the toggle's view to the RecyclerView
     * @param viewGroup view group of the RecyclerView
     * @param inflater inflater to inflate the xml file
     * @return the current view of the toggle
     */
    public abstract View getView(ViewGroup viewGroup,LayoutInflater inflater);

    /**
     * Set listeners for that special item toggle
     * @param viewHolder view holder in the RecyclerView of toggles
     * @param position position of the listener
     */
    public abstract void setListeners(ToggleAdapter.ToggleRecycleViewViewHolder viewHolder, final int position);
    //public abstract void updateState();
    public abstract JSONObject getJSONMessage();
    public void sendJSONMessage(){
        getJSONMessage();
    }

    /**
     * =====================================
     * ======GETTERS AND SETTERS============
     * =====================================
     */

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

    public List<UUID> getRoomsUUID() {
        return roomsUUID;
    }

    public JSONArray getRoomsUUIDAsJSON() {
        JSONArray array = new JSONArray();
        for (UUID roomID : roomsUUID){
            array.put(roomID.toString());
        }
        return array;
    }
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
}
