package com.vpineda.duinocontrol.app.classes.model.toggles;

/**
 * Created by vpineda1996 on 2015-02-08.
 */

import com.vpineda.duinocontrol.app.classes.model.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This are the possible toggle types that have been implemented
 * Once we create one, we must add it to the list so that
 * the program can know that it exists
 */
public enum ToggleTypes {
    LIGHTS ("Lights"),
    DIMMABLE_LED ("Dimmable LED (PWM)");

    private String name;
    ToggleTypes(String name){
        this.name = name;
    }

    /**
     * Method necessary for DbHelper to create new objects depending on the ToggleType
     * every time we add a new ToggleType, we need to add a case ont the switch
     * @param uuid
     * @param name
     * @param pin
     * @param server
     * @param roomUUID
     * @return the toggle object or null
     */
    public Toggle getToggleObject(UUID uuid, String name, int pin, Server server, List<UUID> roomUUID){
        switch (this){
            case LIGHTS:
                return new Lights(uuid,name, pin,server,roomUUID);
            case DIMMABLE_LED:
                return new DimmableLight(uuid, name, pin, server, roomUUID);
        }
        return null;
    }
    public static List<String> getAllToggleNames() {
        List<String> toggleNames = new ArrayList<>();
        for (ToggleTypes tg : ToggleTypes.values()){
            toggleNames.add(tg.name);
        }
        return toggleNames;
    }
}
