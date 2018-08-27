package com.training.ojolusertraining.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by balqis on 5/14/2018.
 */

public class InitRetrofit {


    public static  String URL_API = "http://hafidzputra.com/ojeg_server/api/";
    public static  String URL_GOOGLE_API = "https://maps.googleapis.com/maps/api/directions/";

    //Init retrofit
    public static Retrofit setInit(){
        return new Retrofit.Builder().baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //Instance Interface APiServices (tempat semua request ditampung)
    public static ApiServices getInstance(){
        return setInit().create(ApiServices.class);
    }




    //https://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Los+Angeles,CA&waypoints=Joplin,MO|Oklahoma+City,OK&key=AIzaSyDeSo9nGCkgn5vo--Kp43bBaB19FPq1f2o
    //setting retrofit
    public static Retrofit setInitMap(){
        return new Retrofit.Builder().baseUrl(URL_GOOGLE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    // instance interface ApiServices (tmpat semua request ditampung)
    public static ApiServices getIntanceMap(){
        return setInitMap().create(ApiServices.class);
    }

}
