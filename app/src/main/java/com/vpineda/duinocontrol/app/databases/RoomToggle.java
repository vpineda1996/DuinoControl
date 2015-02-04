package com.vpineda.duinocontrol.app.databases;

import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-01-15.
 */
public abstract class RoomToggle {

    public enum ToggleType {
        SWITCH,
        ANALOG_IN,
        ANALOG_OUT,
        PWM
    }

    private UUID uuid;
    private ToggleType type;
    private String name;
    private UUID roomID;
    private UUID serverID;


    public RoomToggle(UUID toggleID, String nameInput, ToggleType toggleType, Room roomInput, DuServer serverInput){

    }

    public RoomToggle(String nameInput, ToggleType toggleType, Room roomInput, DuServer serverInput) {
        this(UUID.randomUUID(), nameInput, toggleType, roomInput, serverInput);
    }

    public RoomToggle(UUID toggleID, String nameInput, ToggleType toggleType, UUID roomUUID, UUID serverUUID) {
        this.type = toggleType;
        this.uuid = toggleID;
        this.name = nameInput;
        this.roomID = roomUUID;
        this.serverID = serverUUID;
    }


    // =========== GETTERS AND SETTERS =================


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ToggleType getType() {
        return type;
    }

    public void setType(ToggleType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getRoomID() {
        return roomID;
    }

    public void setRoomID(UUID roomID) {
        this.roomID = roomID;
    }

    public UUID getServerID() {
        return serverID;
    }

    public void setServerID(UUID serverID) {
        this.serverID = serverID;
    }
}
