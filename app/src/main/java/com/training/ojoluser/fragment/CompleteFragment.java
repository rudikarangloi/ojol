package com.training.ojoluser.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.ojoluser.R;
import com.training.ojoluser.helper.CustomRecycler;
import com.training.ojoluser.helper.HeroHelper;
import com.training.ojoluser.helper.SessionManager;
import com.training.ojoluser.model.DataProses;
import com.training.ojoluser.model.ModelHistory;
import com.training.ojoluser.network.ApiService;
import com.training.ojoluser.network.InitRetrofit;

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
public class CompleteFragment extends Fragment {


    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Unbinder unbinder;

    public CompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_complete, container, false);

        unbinder = ButterKnife.bind(this,v);
        gethistory();
        return v;
    }

    private void gethistory() {
        ApiService service = InitRetrofit.getInstance();
        SessionManager sessionManager = new SessionManager(getActivity());
        String token = sessionManager.getToken();
        String device = HeroHelper.getDeviceUUID(getActivity());
        Call<ModelHistory> historyproses =service.historyproses("4",sessionManager.getIdUser(),token,device);
        historyproses.enqueue(new Callback<ModelHistory>() {
            @Override
            public void onResponse(Call<ModelHistory> call, Response<ModelHistory> response) {

                //response succes
                if(response.isSuccessful()){
                    String result = response.body().getResult();
                    String message = response.body().getMsg();
                    //check response bernilai true /false
                    if(result.equals("true")){

                        //get data history user
                        List<DataProses> data = response.body().getData();
                        //pindahkan data yang sudah di dapatkan dari server k recyclerview

                        CustomRecycler adapter = new CustomRecycler(data,getActivity(), new CustomRecycler.OnItemClicked() {
                            @Override
                            public void onItemClick(int position) {
                            }
                        });
                        recyclerview.setAdapter(adapter);
                        LinearLayoutManager linear = new LinearLayoutManager(getActivity());
                        recyclerview.setLayoutManager(linear);




                    }
                    else{
                        //bkin toast kalau seandai hasil resultnya nggak true
                        HeroHelper.pesan(getActivity(),message);

                    }

                }
                else{

                }
            }

            @Override
            public void onFailure(Call<ModelHistory> call, Throwable t) {

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
