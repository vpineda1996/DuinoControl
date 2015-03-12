package com.vpineda.duinocontrol.app.classes.model.toggles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.classes.ui.adapters.ToggleAdapter;
import com.vpineda.duinocontrol.app.networking.Commands;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-02-14.
 */
public class DimmableLight extends Toggle {
    private SeekBar mSeekBar;

    private int currentToggleValue = 0;
    private boolean haveRecievedServerResponse = true;


    public DimmableLight(UUID id, String name, int pin, Server server, List<UUID> roomsUUID) {
        super(id, name, pin, server, ToggleTypes.DIMMABLE_LED, roomsUUID);
    }

    public DimmableLight(String name, int pin, Server server, List<UUID> roomsUUID) {
        super(name, pin, server, ToggleTypes.DIMMABLE_LED, roomsUUID);
    }

    /**
     * Inflates the view and assigns the respective views to the elements in the view
     * @param viewGroup view group of the RecyclerView
     * @param inflater inflater to inflate the xml file
     * @return
     */
    @Override
    public View getInflatedView(ViewGroup viewGroup, LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.classes_model_toggles_dimmable_lights,viewGroup,false);
        // Link all of the elements in the view to the code
        mSeekBar = (SeekBar) v.findViewById(R.id.toggles_dimmable_lights_card_view_seek_bar);
        return v;
    }

    /**
     * Sets the listierns for this toggle
     * @param viewHolder view holder in the RecyclerView of toggles
     * @param position position of the listener
     */
    @Override
    public void setListeners(ToggleAdapter.ToggleRecycleViewViewHolder viewHolder, int position) {
        super.setListeners(viewHolder, position);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // If we haven't received a response from the server do nothing
                if(haveRecievedServerResponse){
                    setCurrentToggleValue(Double.valueOf(seekBar.getProgress() * 2.55).intValue());
                    sendJSONMessage(Commands.ANALOG_WRITE);
                    // Once we send the message set the haveReceivedServerResponse to false since we are waiting
                    // for a new response from the server
                    haveRecievedServerResponse = false;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // When we stop tracking the finger send the message, it doesn't matter if the server has responded
                setCurrentToggleValue(Double.valueOf(seekBar.getProgress() * 2.55).intValue());
                sendJSONMessage(Commands.ANALOG_WRITE);
            }
        });
    }

    /**
     * Prepares the json message that we will send to the server
     * @param typeOfMessage the type of message we will send
     *                      go to Commands enum to see posibilities
     * @return the created json message or null if there was a exception
     */
    @Override
    protected JSONObject getJSONMessage(Commands typeOfMessage) {
        try {
            JSONObject jsonObject = null;
            switch (typeOfMessage) {
                case ANALOG_WRITE:
                    jsonObject = new JSONObject();
                    jsonObject.put("command", typeOfMessage.getCommand());
                    jsonObject.put("pin",getPin());
                    jsonObject.put("value", currentToggleValue);
                    break;
            }
            return jsonObject;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param state the integer of the current state
     *              it can go from 0 to 255 but there is a special case
     *              where it can be -1 that means the opposite of the current value
     */
    @Override
    protected void setCurrentToggleValue(int state) {
        currentToggleValue = state;
    }

    @Override
    public void onServerResponseSuccess(JSONObject response) {
        haveRecievedServerResponse = true;
        //TODO
    }

    @Override
    public void onServerResponseFailure(Exception e, byte[] response) {
        haveRecievedServerResponse = true;
        //TODO
    }
}
