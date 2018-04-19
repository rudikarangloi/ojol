package com.training.ojoluser.helper;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.training.ojoluser.R;
import com.training.ojoluser.model.DataProses;

import java.util.List;


/**
 * Created by nandoseptianhusni on 8/30/17.
 */

//ini class untuk memindahkan data ke recylerview dan juga custom recylerview
public class  CustomRecycler extends RecyclerView.Adapter<CustomRecycler.MyHolder> {

    List<DataProses> data;
    FragmentActivity c;

    OnItemClicked clicked;


    public CustomRecycler(List<DataProses> data, FragmentActivity c, OnItemClicked onItemClicked) {
        this.data = data;
        this.c = c;
        clicked=onItemClicked;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(c).inflate(R.layout.custom_recyclerview, parent, false);

        return new MyHolder(inflater);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.texttgl.setText(data.get(position).getBookingTanggal());
        holder.txtawal.setText(data.get(position).getBookingFrom());
        holder.txtakhir.setText(data.get(position).getBookingTujuan());
        holder.txtharga.setText(data.get(position).getBookingBiayaUser());

     //   holder.itemView.setOnClickListener(new );


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {


        TextView texttgl;

        TextView txtawal;

        TextView txtakhir;

        TextView txtharga;

        public MyHolder(View itemView) {
            super(itemView);

            texttgl =(TextView) itemView.findViewById(R.id.texttgl);
            txtawal =(TextView) itemView.findViewById(R.id.txtawal);
            txtakhir =(TextView) itemView.findViewById(R.id.txtakhir);
            txtharga =(TextView) itemView.findViewById(R.id.txtharga);



        }
    }

    public interface OnItemClicked{
        void onItemClick(int position);
    }
    public void setOnClick(OnItemClicked onClick){
        clicked=onClick;
    }

}
