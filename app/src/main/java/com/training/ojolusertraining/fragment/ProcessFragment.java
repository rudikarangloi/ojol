package com.training.ojolusertraining.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.training.ojolusertraining.R;
import com.training.ojolusertraining.adapter.MyHistoryAdapter;
import com.training.ojolusertraining.helper.HeroHelper;
import com.training.ojolusertraining.helper.SessionManager;
import com.training.ojolusertraining.network.ApiServices;
import com.training.ojolusertraining.network.InitRetrofit;
import com.training.ojolusertraining.network.response.history.DataItem;
import com.training.ojolusertraining.network.response.history.ResponseHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProcessFragment extends Fragment {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;


    MyHistoryAdapter adapter ;
    public ProcessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_proses, container, false);
        unbinder = ButterKnife.bind(this, view);

        /*
        // Buat adapter untuk recyceler view
        adapter = new MyHistoryAdapter(1);
        // Beritahu adapter bahwa ada perubahan data
        adapter.notifyDataSetChanged();
        recyclerview.setAdapter(adapter);
        */

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        getHistory();
        return view;
    }

    private void getHistory() {

        SessionManager sessionManager = new SessionManager(getActivity());
        ApiServices apiServices = InitRetrofit.getInstance();
        String idUser=sessionManager.getIdUser().toLowerCase();
        String device= HeroHelper.getDeviceUUID(getActivity());
        String token=sessionManager.getToken().toLowerCase();

        Call<ResponseHistory> request=apiServices.request_process_booking(token ,device,idUser);
        request.enqueue(new Callback<ResponseHistory>() {
            @Override
            public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                String result = response.body().getResult();
                if(result.equals("true")){
                    List<DataItem> dataHistory = response.body().getData();

                    adapter = new MyHistoryAdapter(dataHistory,1);

                    adapter.notifyDataSetChanged();
                    recyclerview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseHistory> call, Throwable t) {

            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
