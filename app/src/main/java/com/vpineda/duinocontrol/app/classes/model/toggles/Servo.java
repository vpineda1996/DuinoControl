package com.vpineda.duinocontrol.app.classes.model.toggles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Server;
import com.vpineda.duinocontrol.app.networking.Commands;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-03-26.
 */
public class Servo extends Toggle {
    private TextView mNameTextView;
    private SeekBar mSeekBar;
    public Servo(UUID id, String name, int pin, Server server, List<UUID> roomsUUID) {
        super(id, name, pin, server, ToggleTypes.PWM, roomsUUID);
    }

    public Servo(String name, int pin, Server server, List<UUID> roomUUID) {
        super(name, pin, server, ToggleTypes.PWM, roomUUID);
    }

    @Override
    public View getInflatedView(ViewGroup viewGroup, LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.classes_model_toggles_servo,viewGroup,false);
        mNameTextView = (TextView) v.findViewById(R.id.toggles_servo_card_view_text_view);
        mSeekBar = (SeekBar) v.findViewById(R.id.toggles_servo_card_view_seek_bar);
        mNameTextView.setText(getName());
        return v;
    }

    @Override
    protected JSONObject getJSONMessage(Commands typeOfMessage) {
        return null;
    }

    @Override
    protected void setCurrentToggleValue(int state) {

    }

    @Override
    public void onServerResponseSuccess(JSONObject response) {

    }

    @Override
    public void onServerResponseFailure(Exception e, byte[] response) {

    }
}
