package com.vpineda.duinocontrol.app.classes.model.toggles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.networking.Commands;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-02-14.
 */
public class DimmableLight extends Toggle {
    public DimmableLight(UUID id, String name, int pin, Server server, List<UUID> roomsUUID) {
        super(id, name, pin, server, ToggleTypes.DIMMABLE_LED, roomsUUID);
    }

    public DimmableLight(String name, int pin, Server server, List<UUID> roomsUUID) {
        super(name, pin, server, ToggleTypes.DIMMABLE_LED, roomsUUID);
    }

    @Override
    public View getInflatedView(ViewGroup viewGroup, LayoutInflater inflater) {
        return null;
    }

    @Override
    protected JSONObject getJSONMessage(Commands typeOfMessage) {
        return null;
    }

    @Override
    protected void setToggleState(int state) {

    }

    @Override
    public void onServerResponseSuccess(JSONObject response) {

    }

    @Override
    public void onServerResponseFailure(Exception e, byte[] response) {

    }
}
