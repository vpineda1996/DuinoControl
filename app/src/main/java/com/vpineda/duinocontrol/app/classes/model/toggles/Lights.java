package com.vpineda.duinocontrol.app.classes.model.toggles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Room;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.classes.ui.adapters.ToggleAdapter;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-02-07.
 */
public class Lights extends Toggle {
    private boolean value;

    /**
     * =====================================
     * ===========CONSTRUCTORS==============
     * =====================================
     */

    public Lights(UUID id, String name, Server server, List<UUID> roomUUID) {
        super(id, name, server, ToggleTypes.LIGHTS, roomUUID);
    }

    public Lights(String name, Server server, List<UUID> roomUUID) {
        super(name, server, ToggleTypes.LIGHTS,roomUUID);
    }

    /**
     * REQUIRES: a valid viewgroup and a layout inflater
     * EFFECTS: creates a view of a specific class
     */

    @Override
    public View getView(ViewGroup viewGroup, LayoutInflater inflater) {
        //TODO: Maybe create a preference where we can modify the height, color, etc of this view
        return inflater.inflate(R.layout.classes_mode_toggles_lights,viewGroup,false);
    }

    @Override
    public void setListeners(ToggleAdapter.ToggleRecycleViewViewHolder viewHolder, int position) {
        //TODO
        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendJSONMessage();
                //TODO
            }
        });
    }

    @Override
    public JSONObject getJSONMessage() {
        //TODO
        return null;
    }
}
