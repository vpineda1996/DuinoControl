package com.vpineda.duinocontrol.app.databases;

import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-13.
 */
public class Room {
    String name;
    UUID uuid;

    public Room (String nameInput, UUID roomUUID){
        name = nameInput;
        uuid = roomUUID;
    }
    public Room (String nameInput){
        name = nameInput;
        uuid = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
