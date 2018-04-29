package com.h4413.recyclyon.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.h4413.recyclyon.Activities.Connection.AssociationAdapterCallback;
import com.h4413.recyclyon.Model.Association;
import com.h4413.recyclyon.Utilities.DownLoadImageTask;

import com.h4413.recyclyon.R;

public class ChooseAssociationRecyclerViewAdapter extends RecyclerView.Adapter<ChooseAssociationRecyclerViewAdapter.ViewHolder> {

    private Association[] mDataset;
    private AssociationAdapterCallback mCallback;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitle;
        public TextView mDescription;
        public ImageView mLogo;
        public LinearLayout mLayout;

        public ViewHolder(final View v) {
            super(v);
            mTitle = (TextView) itemView.findViewById(R.id.choose_association_view_title);
            mDescription = (TextView) itemView.findViewById(R.id.choose_association_view_description);
            mLogo = (ImageView) itemView.findViewById(R.id.choose_association_view_logo);
            mLayout = (LinearLayout) itemView.findViewById(R.id.choose_association_view_layout);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChooseAssociationRecyclerViewAdapter(Association[] myDataset, AssociationAdapterCallback callback) {
        mDataset = myDataset;
        mCallback = callback;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChooseAssociationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.view_choose_association_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTitle.setText(mDataset[position].nom);
        holder.mDescription.setText(mDataset[position].description);
        if(mDataset[position].logoUrl == null || mDataset[position].logoUrl.equals(""))
            mDataset[position].logoUrl = "http://www.gstatic.com/webp/gallery/1.jpg";
        new DownLoadImageTask(holder.mLogo).execute(mDataset[position].logoUrl);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClickCallback(mDataset[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
