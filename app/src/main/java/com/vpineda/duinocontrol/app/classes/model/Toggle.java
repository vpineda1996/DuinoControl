package com.vpineda.duinocontrol.app.classes.model;


import android.content.Context;
import android.view.View;
import com.vpineda.duinocontrol.app.databases.DbContract;
import com.vpineda.duinocontrol.app.databases.DbHelper;

import java.util.List;
import java.util.UUID;

/**
 * Created by Victor on 2/3/2015.
 */
public abstract class Toggle {
    private UUID uuid;
    private String name;
    private Server server;
    private TOGGLE_TYPE type;

    // the possible types the toggle can be
    // TODO: implement servos and IR
    public enum TOGGLE_TYPE{
        ANALOG_READ,
        ANALOG_WRITE,
        DIGITAL_READ,
        DIGITAL_WRITE}

    /* =====================================
     * ===========CONSTRUCTORS==============
     * =====================================
     */

    public Toggle (UUID id, String name, Server server, TOGGLE_TYPE type) {
        this.uuid = id;
        this.name = name;
        this.server = server;
        this.type = type;
    }
    public Toggle (String name, Server server, TOGGLE_TYPE type){
        this(UUID.randomUUID(),name, server, type);
    }
    // TODO: find a way to get all of the assigned toggles to this Toggle

    abstract List<Room> getAssignedRooms(Context context);
    abstract View getView();

}
