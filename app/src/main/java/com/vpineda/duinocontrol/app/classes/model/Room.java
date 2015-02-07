package com.vpineda.duinocontrol.app.classes.model;

import android.content.Context;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Victor on 2/3/2015.
 */
public class Room {
    private UUID uuid;
    private String name;
    private JSONObject toggles;

    /* =====================================
     * ===========CONSTRUCTORS==============
     * =====================================
     */

    public Room (UUID id, String name, JSONObject toggles) {
        this.uuid = id;
        this.name = name;
        this.toggles = toggles;
    }
    public Room (String name){
        this(UUID.randomUUID(),name, new JSONObject());
    }

    public Room (UUID id, String name){
        this(id, name, new JSONObject());
    }

    public List<Toggle> getToggles(Context context){
     //TODO: get all toggles
        return null;
    }


    /*
     * =====================================
     * ========GETTERS AND SETTERS==========
     * =====================================
     */

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getToggles() {
        return toggles;
    }

    public void setToggles(JSONObject toggles) {
        this.toggles = toggles;
    }
    public void setToggles(ArrayList<Toggle> toggles){

    }
}
