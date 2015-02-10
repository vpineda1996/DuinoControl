package com.vpineda.duinocontrol.app.classes.model.toggles;

/**
 * Created by vpineda1996 on 2015-02-08.
 */

import com.vpineda.duinocontrol.app.classes.model.Server;

import java.util.List;
import java.util.UUID;

/**
 * This are the possible toggle types that have been implemented
 * Once we create one, we must add it to the list so that
 * the program can know that it exists
 */
public enum ToggleTypes {
    LIGHTS;


    ToggleTypes(){
    }

    /**
     * Method necessary for DbHelper to create new objects depending on the ToggleType
     * every time we add a new ToggleType, we need to add a case ont the switch
     * @param uuid
     * @param name
     * @param server
     * @param roomUUID
     * @return returns the new toggle
     */
    public Toggle getToggleObject(UUID uuid, String name, Server server, List<UUID> roomUUID){
        switch (this){
            case LIGHTS:
                return new Lights(uuid,name,server,roomUUID);
        }
        return null;
    }
}
