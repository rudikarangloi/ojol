package com.training.ojolusertraining.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.training.ojolusertraining.R;
import com.training.ojolusertraining.helper.HeroHelper;
import com.training.ojolusertraining.network.ApiServices;
import com.training.ojolusertraining.network.InitRetrofit;
import com.training.ojolusertraining.network.response.ResponseCancel;
import com.training.ojolusertraining.network.response.checkbooking.DataItem;
import com.training.ojolusertraining.network.response.checkbooking.ResponseCheckBooking;

import java.util.List;
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

    Timer timer;

    //Tampung Intent
    int id_booking;
    String polylinePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finddriver);
        ButterKnife.bind(this);

        //Inisiasi Timer
        timer=new Timer();

        //Dapatkan Intent
        id_booking = getIntent().getIntExtra("ID_BOOKING",0);
        polylinePoint = getIntent().getStringExtra("RUTE_POLYLINE");

        //Animasi dijalankan
        pulsator.start();

        //Set Timer
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //HeroHelper.pesan(FindDriverActivity.this,"Mencari Driver .....");
                //Toast.makeText(FindDriverActivity.this, "mencari Driver ....", Toast.LENGTH_SHORT).show();
                checkBooking();
            }
        },0,3000);

    }

    private void checkBooking() {

        ApiServices apiServices= InitRetrofit.getInstance();
        Call<ResponseCheckBooking> requestCheckBooking = apiServices.request_check_booking(id_booking);
        requestCheckBooking.enqueue(new Callback<ResponseCheckBooking>() {
            @Override
            public void onResponse(Call<ResponseCheckBooking> call, Response<ResponseCheckBooking> response) {
                if(response.isSuccessful()){
                    Log.d("ResponseCheck",response.body().toString());

                    String msg=response.body().getMsg();
                    String result=response.body().getResult();
                    List<DataItem> dataBooking = response.body().getData();

                    if(result.equals("true")){
                        String booking_status=dataBooking.get(0).getBookingStatus();
                        //String bb=response.body().getData().get(0).getBookingStatus();

                        //Jika Driver / pengemudi Klik OK, maka Booking status = 2
                        if(booking_status.equals("2")){

                            timer.cancel();
                            //HeroHelper.pindahclass(FindDriverActivity.this,PosisiDriver.class);
                            Intent intent=new Intent(FindDriverActivity.this,PosisiDriver.class);
                            intent.putExtra("ID_DRIVER",dataBooking.get(0).getBookingDriver());
                            //Bawa data polylinePoint ke Activity PosisiDriver
                            intent.putExtra("RUTE_POLYLINE",polylinePoint);
                            startActivity(intent);
                            finish();


                        }else if(booking_status.equals("3")){

                            timer.cancel();
                            //Akhiri Activity saat ini
                            finish();

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCheckBooking> call, Throwable t) {

            }
        });

    }

    @OnClick(R.id.buttoncancel)
    public void onViewClicked() {

        //---------------------------Buat Alert Dilaog
        AlertDialog.Builder alertCOnfirm = new AlertDialog.Builder(this);

        alertCOnfirm.setTitle("Konfirmasi");
        alertCOnfirm.setMessage("Anda yakin Membatalkan Order ?");

        alertCOnfirm.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelBooking();
            }
        });

        alertCOnfirm.setNegativeButton("tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertCOnfirm.show();

        //---------------------------------------------



    }

    private void cancelBooking() {

        int id_booking = getIntent().getIntExtra("ID_BOOKING",0);

        ApiServices apiServices= InitRetrofit.getInstance();

        Call<ResponseCancel> requestCancel=apiServices.request_cancel_booking(id_booking);
        requestCancel.enqueue(new Callback<ResponseCancel>() {
            @Override
            public void onResponse(Call<ResponseCancel> call, Response<ResponseCancel> response) {
                if(response.isSuccessful()){

                    String msg=response.body().getMsg();
                    String result=response.body().getResult();

                    if(result.equals("true")){
                        //Oindah ke activity MapActivity
                        startActivity(new Intent(FindDriverActivity.this,MapsActivity.class));
                        finish();
                        timer.cancel();
                    }

                    HeroHelper.pesan(FindDriverActivity.this,msg);

                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponseCancel> call, Throwable t) {
                t.printStackTrace();
                HeroHelper.pesan(FindDriverActivity.this,"Terjadi kesalahan");

            }
        });
    }
}
