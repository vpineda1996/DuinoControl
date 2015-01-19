package com.vpineda.duinocontrol.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by vpineda1996 on 2015-01-15.
 */
public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private int mSwitchType;

    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    @Override
    public RoomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(RoomAdapter.ViewHolder viewHolder, int i) {
        //viewHolder.mTextView.setText();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}


