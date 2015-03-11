package com.vpineda.duinocontrol.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vpineda.duinocontrol.app.R;
import com.vpineda.duinocontrol.app.classes.model.Room;

import java.util.Collections;
import java.util.List;

/**
 * Created by vpineda1996 on 2015-01-17.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerRecycleViewViewHolder> {
    private LayoutInflater inflater;
    List<String> data = Collections.emptyList();
    private OnItemClickListener mListener;

    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        public void onClick(View view, int position);
        public void onCreateContextMenu(ContextMenu menu);
    }


    public DrawerAdapter(Context context, OnItemClickListener listener, List<String> listOfRoomsNames) {
        inflater=LayoutInflater.from(context);
        mListener = listener;
        this.data = listOfRoomsNames;
    }

    @Override
    public DrawerRecycleViewViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = inflater.inflate(R.layout.recycler_view_drawer_layout_old,viewGroup,false);
        DrawerRecycleViewViewHolder holder = new DrawerRecycleViewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DrawerRecycleViewViewHolder viewHolder, final int i) {
        final String current = data.get(i);
        viewHolder.title.setText(current);
        //Set listners for clicking
        viewHolder.getmView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(v, i);
            }
        });
        viewHolder.getmView().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                mListener.onCreateContextMenu(menu);
            }
        });

    }

    public void updateData(List<String> roomNames) {
        this.data = roomNames;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DrawerRecycleViewViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        View mView;
        public DrawerRecycleViewViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.drawer_single_item_text_view);
            mView = itemView;
        }

        public View getmView() {
            return mView;
        }
    }
}
