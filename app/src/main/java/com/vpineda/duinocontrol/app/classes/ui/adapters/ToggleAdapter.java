package com.vpineda.duinocontrol.app.classes.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vpineda.duinocontrol.app.classes.model.toggles.Toggle;

import java.util.Collections;
import java.util.List;

/**
 * Created by vpineda1996 on 2015-01-17.
 */
public class ToggleAdapter extends RecyclerView.Adapter<ToggleAdapter.ToggleRecycleViewViewHolder> {

    private LayoutInflater inflater;
    List<Toggle> data = Collections.emptyList();

    /**
     * Constructor
     * @param context context of the program
     * @param listOfToggles list of all togles
     */
    public ToggleAdapter(Context context, List<Toggle> listOfToggles) {
        inflater = LayoutInflater.from(context);
        this.data = listOfToggles;
    }

    /**
     * Gets the view that is inflated by that specific toggle
     * @param viewGroup
     * @param position the current position in the list
     * @return returns the viewHolder
     */
    @Override
    public ToggleRecycleViewViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = data.get(position).getInflatedView(viewGroup, inflater);
        return new ToggleRecycleViewViewHolder(view);
    }

    /**
     * Sends the current view to the toggle, then the toggle puts the listeners
     * and the finds the required views from the viewHolder by using
     * viewHolder.getInflatedView()
     * @param viewHolder viewHolder of a specific item on the list
     * @param position current position in the list of the element
     */
    @Override
    public void onBindViewHolder(ToggleRecycleViewViewHolder viewHolder, final int position) {
        Toggle current = data.get(position);
        current.setListeners(viewHolder, position);
    }

    /**
     * @return returns the size of the list, necessary for the layoutManager
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ToggleRecycleViewViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        public ToggleRecycleViewViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public View getView() {
            return mView;
        }
    }
}
