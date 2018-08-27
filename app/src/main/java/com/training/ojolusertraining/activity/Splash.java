package com.training.ojolusertraining.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.training.ojolusertraining.R;
import com.training.ojolusertraining.helper.SessionManager;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class Splash extends AppCompatActivity  {

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        //Activity hanya tampil 3 detik, pindaj ke Login
        // TODO 1 : Buat Object Handler
        Handler handlerSplash = new Handler();

        //TODO 2 : Panggil post delayed function
        handlerSplash.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(sessionManager.isLogin()){
                    startActivity(new Intent(Splash.this,MapsActivity.class));
                }else{
                    startActivity(new Intent(Splash.this,LoginActivity.class));
                }
                //TODO 3 : Pindah kel login

                //Akhiri Activity
                finish();
            }
        }, 3000);

        //1000 = 1 detik

    }


}
