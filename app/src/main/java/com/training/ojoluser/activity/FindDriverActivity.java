package com.training.ojoluser.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.training.ojoluser.R;
import com.training.ojoluser.helper.HeroHelper;
import com.training.ojoluser.helper.SessionManager;
import com.training.ojoluser.model.ModelCancel;
import com.training.ojoluser.model.ModelCekBooking;
import com.training.ojoluser.network.InitRetrofit;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindDriverActivity extends AppCompatActivity {

    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;
    @BindView(R.id.buttoncancel)
    Button buttoncancel;
    private SessionManager sesi;
    int id;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finddriver);
        ButterKnife.bind(this);
            timer = new Timer();
        sesi = new SessionManager(this);
        pulsator.start();
        id =getIntent().getIntExtra("idbooking",0);
        checkbooking();

    }

    private void checkbooking() {
        String idbooking = String.valueOf(id);
        InitRetrofit.getInstance().cekstatusbooking(idbooking).enqueue(new Callback<ModelCekBooking>() {
            @Override
            public void onResponse(Call<ModelCekBooking> call, Response<ModelCekBooking> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getResult();
                    String pesan = response.body().getMsg();
                    String iddriver = response.body().getDriver();
                    if (result.equals("true")) {
                        Intent intent = new Intent(FindDriverActivity.this, PosisiDriver.class);
                        intent.putExtra("driver", iddriver);
                        Toast.makeText(FindDriverActivity.this, "driver "+iddriver, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        HeroHelper.pesan(FindDriverActivity.this,pesan);
                       finish();
                    }else{
                        HeroHelper.pesan(FindDriverActivity.this,pesan);
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelCekBooking> call, Throwable t) {
            HeroHelper.pesan(FindDriverActivity.this,"gagal"+t.getMessage());
            }
        });
;    }

    @OnClick(R.id.buttoncancel)
    public void onViewClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Yakin mau cancel order ojek ini ?");
        builder.setPositiveButton("ya yakin ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //action cancel
                actionCancel();
            }
        });
        builder.setNegativeButton("bimbang", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            };
        });

        builder.show();


    }

    private void actionCancel() {
        String token = sesi.getToken();
        String device = HeroHelper.getDeviceUUID(this);
        String idbooking = String.valueOf(id);
        Toast.makeText(this, "cek id"+idbooking, Toast.LENGTH_SHORT).show();
        InitRetrofit.getInstance().cancelbooking(idbooking,token,device).enqueue(new Callback<ModelCancel>() {
            @Override
            public void onResponse(Call<ModelCancel> call, Response<ModelCancel> response) {
            String result = response.body().getResult();
            String pesan = response.body().getMsg();

                if (response.isSuccessful()){
                 if (result.equals("true")){
                     finish();
                     HeroHelper.pesan(FindDriverActivity.this,pesan);
                 }else{
                     HeroHelper.pesan(FindDriverActivity.this,pesan);

                 }
            }
            }

            @Override
            public void onFailure(Call<ModelCancel> call, Throwable t) {
                HeroHelper.pesan(FindDriverActivity.this,"gagal"+t.getMessage());

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkbooking();
                        HeroHelper.pesan(FindDriverActivity.this,"refresh");
                    }
                });

            }
        },0,3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}
