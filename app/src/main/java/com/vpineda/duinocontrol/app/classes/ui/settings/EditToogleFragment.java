package com.vpineda.duinocontrol.app.classes.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.vpineda.duinocontrol.app.R;

import java.util.UUID;

/**
 * Created by vpineda1996 on 2015-02-16.
 */
public class EditToogleFragment extends DialogFragment {
    private UUID roomUUID, serverUUId;
    public static String ROOMBUNDLEKEY = "roomUUID";

    public static EditToogleFragment newInstance(@Nullable UUID roomID){
        EditToogleFragment edf = new EditToogleFragment();
        // Add params to the bundle
        if (roomID != null) {
            Bundle b = new Bundle();
            b.putString(ROOMBUNDLEKEY, roomID.toString());
            edf.setArguments(b);
        }
        return edf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            roomUUID = UUID.fromString(getArguments().getString(ROOMBUNDLEKEY));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.classes_ui_settings_edit_toggle,
                (ViewGroup) getActivity().getWindow().getDecorView().getRootView(),false);
        return new AlertDialog.Builder(getActivity()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setView(v)
                .create();
    }
}
