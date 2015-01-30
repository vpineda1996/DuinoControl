package com.vpineda.duinocontrol.app.toggles;

import com.vpineda.duinocontrol.app.databases.DuServer;
import com.vpineda.duinocontrol.app.databases.Room;
import com.vpineda.duinocontrol.app.databases.RoomToggle;

import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-27.
 */
public class AnalogToggle extends RoomToggle {

    public AnalogToggle(UUID toggleID, String nameInput, ToggleType toggleType, Room roomInput, DuServer serverInput) {
        super(toggleID, nameInput, toggleType, roomInput, serverInput);
    }


}
