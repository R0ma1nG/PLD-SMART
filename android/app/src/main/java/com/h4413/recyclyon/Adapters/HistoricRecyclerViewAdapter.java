package com.h4413.recyclyon.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h4413.recyclyon.Model.Depot;
import com.h4413.recyclyon.Model.Historic;
import com.h4413.recyclyon.Model.HistoricEntry;
import com.h4413.recyclyon.R;

public class HistoricRecyclerViewAdapter extends RecyclerView.Adapter<HistoricRecyclerViewAdapter.ViewHolder> {

    private Depot[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mDescription;

        public ViewHolder(final View v) {
            super(v);
            mTitle = (TextView) itemView.findViewById(R.id.historic_view_infos);
            mDescription = (TextView) itemView.findViewById(R.id.historic_view_description);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoricRecyclerViewAdapter(Depot[] dataset) {
        mDataset = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoricRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_historic_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Depot entry = mDataset[position];
        holder.mTitle.setText(String.valueOf(entry.montant)+"€ pour "+entry.nom);
        holder.mDescription.setText(entry.date.toString());
    }

    @Override
    public int getItemCount() {
        if(mDataset == null) return 0;
        return mDataset.length;
    }
}
