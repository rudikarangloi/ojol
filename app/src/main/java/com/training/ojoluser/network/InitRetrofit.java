package com.training.ojoluser.network;

import com.training.ojoluser.helper.MyContants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InitRetrofit {

    //inizialisasi sebuah library untuk accesss data dari server
    //library yang digunakan adalah retrofit dari square

    public static Retrofit setInit() {

        return new Retrofit.Builder().baseUrl(MyContants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }public static Retrofit setInit2() {

        return new Retrofit.Builder().baseUrl(MyContants.BASE_MAP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static ApiService getInstance(){

        return setInit().create(ApiService.class);


    }public static ApiService getInstance2(){

        return setInit2().create(ApiService.class);


    }

}
