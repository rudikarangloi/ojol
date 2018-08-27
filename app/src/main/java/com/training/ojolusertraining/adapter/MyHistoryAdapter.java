package com.training.ojolusertraining.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.training.ojolusertraining.R;
import com.training.ojolusertraining.network.response.history.DataItem;

import java.util.List;

/**
 * Created by balqis on 5/17/2018.
 */

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.ViewHolder> {

    int status;

    List<DataItem> dataHistory;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tanggal, awal, tujuan, harga;

        public ViewHolder(View v) {
            super(v);
            tanggal = v.findViewById(R.id.texttgl);
            awal = v.findViewById(R.id.txtawal);
            tujuan = v.findViewById(R.id.txtakhir);
            harga = v.findViewById(R.id.txtharga);
            //mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyHistoryAdapter(List<DataItem> dataHistory, int status) {
        this.status = status;
        this.dataHistory = dataHistory;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.awal.setText(dataHistory.get(position).getBookingFrom());
        holder.tujuan.setText(dataHistory.get(position).getBookingTujuan());
        holder.tanggal.setText(dataHistory.get(position).getBookingTanggal());
        holder.harga.setText(dataHistory.get(position).getBookingBiayaDriver());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataHistory.size();
    }
}
