package com.training.ojolusertraining.helper;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.training.ojolusertraining.R;

import java.util.List;


/**
 * Created by nandoseptianhusni on 8/30/17.
 */

//ini class untuk memindahkan data ke recylerview dan juga custom recylerview
public class  CustomRecycler extends RecyclerView.Adapter<CustomRecycler.MyHolder> {

    FragmentActivity c;
    OnItemClicked clicked;

    public CustomRecycler(FragmentActivity c, OnItemClicked onItemClicked) {
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

     //   holder.itemView.setOnClickListener(new );

    }

    @Override
    public int getItemCount() {
        return 10;
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
