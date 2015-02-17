package com.vpineda.duinocontrol.app.classes.model.toggles;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.classes.ui.adapters.ToggleAdapter;
import com.vpineda.duinocontrol.app.networking.Commands;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-02-07.
 */
public class Lights extends Toggle {
    private boolean value = false;

    /**
     * =====================================
     * ===========CONSTRUCTORS==============
     * =====================================
     */

    public Lights(UUID id, String name, int pin, Server server, List<UUID> roomUUID) {
        super(id, name, pin, server, ToggleTypes.LIGHTS, roomUUID);
    }

    public Lights(String name, int pin, Server server, List<UUID> roomUUID) {
        super(name, pin, server, ToggleTypes.LIGHTS,roomUUID);
    }

    /**
     * Setup the view for this specific Toggle
     * @param viewGroup view group of the RecyclerView
     * @param inflater inflater to inflate the xml file
     * @return the view of the inflated toggle
     */
    @Override
    public View getInflatedView(ViewGroup viewGroup, LayoutInflater inflater) {
        //TODO: Maybe create a preference where we can modify the height, color, etc of this view
        return inflater.inflate(R.layout.classes_mode_toggles_lights,viewGroup,false);
    }

    /**
     * Method that the RecyclerView calls when the view has been created
     * here we set the listeners in the UI.
     * @param viewHolder view holder in the RecyclerView of toggles
     * @param position position of the listener
     */
    @Override
    public void setListeners(final ToggleAdapter.ToggleRecycleViewViewHolder viewHolder, int position) {
        //TODO
        super.setListeners(viewHolder,position);
        viewHolder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToggleState(-1);
                sendJSONMessage(Commands.DIGITAL_WRITE);
            }
        });
    }

    @Override
    public void onServerResponseSuccess(JSONObject response) {
        Toast.makeText(getToggleView().getContext(),response.toString(),Toast.LENGTH_SHORT).show();
        if (value)
            getToggleView().setBackgroundColor(getToggleView().getResources().getColor(R.color.cardview_dark_background));
        else
            getToggleView().setBackgroundColor(getToggleView().getResources().getColor(R.color.cardview_light_background));
    }

    @Override
    public void onServerResponseFailure(Exception e, byte[] response) {
        Toast.makeText(getToggleView().getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    /**
     * Create the message to send to the server
     * @param typeOfMessage this indicates what type of message does the
     *                      user wants, depending on it, this method will create it
     * @return the message that will be sent
     */
    @Override
    protected JSONObject getJSONMessage(Commands typeOfMessage) {
        try {
            JSONObject message = new JSONObject();
            switch (typeOfMessage){
                case DIGITAL_WRITE:
                    message.put("command", typeOfMessage.getCommand());
                    message.put("pin", getPin());
                    message.put("value", value ? 1 : 0);
                    return message;
                default:
                    Log.e("JSON","Error generating JSON message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Change the toggle value
     * @param state the integer of the current state
     *              it can go from 0 to 255 but there is a special case
     *              where it can be -1 that means the opposite of the current value
     */
    @Override
    protected void setToggleState(int state) {
        if (state == 0){
            value = false;
        }else if(state == -1){
            value = !value;
        }else {
            value = true;
        }
    }
}
