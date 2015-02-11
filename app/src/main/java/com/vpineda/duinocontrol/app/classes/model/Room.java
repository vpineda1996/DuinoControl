package com.vpineda.duinocontrol.app.classes.model;

import android.content.Context;
import com.vpineda.duinocontrol.app.classes.model.toggles.Toggle;
import com.vpineda.duinocontrol.app.databases.DbHelper;

import java.util.List;
import java.util.UUID;

/**
 * Created by Victor on 2/3/2015.
 */
public class Room {
    private UUID uuid;
    private String name;
    private List<Toggle> toggles;

    /**
     * =====================================
     * ===========CONSTRUCTORS==============
     * =====================================
     */
    // This constructor should only be used by the database class, it is preferable
    // that you dont use it to initialize a Room object
    public Room (UUID id, String name, List<Toggle> toggles) {
        this.uuid = id;
        this.name = name;
        this.toggles = toggles;
    }

    public Room (UUID id, String name, final Context context) {
        this.uuid = id;
        this.name = name;
        new Thread(new Runnable() {
            @Override
            public void run() {
                toggles = getTogglesFromDatabase(context);
            }
        }).run();
    }
    public Room (String name, Context context){
        this(UUID.randomUUID(),name,context);
    }

    /**
     * Gets the toggles from the database so we can display it to the user
     * @param context
     * @return list of toggle in current room
     */
    public List<Toggle> getTogglesFromDatabase(Context context){
        DbHelper helper = new DbHelper(context);
        List<Toggle> toggleList = helper.getAllRoomToggles(uuid);
        helper.close();
        return toggleList;
    }


    /**
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

    public List<Toggle> getToggles() {
        return toggles;
    }

    public void addToggle(Toggle toggle) {
        toggles.add(toggle);
    }
}
