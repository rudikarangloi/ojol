package com.training.ojoluser.network;

import com.training.ojoluser.model.ModelBooking;
import com.training.ojoluser.model.ModelLogin;
import com.training.ojoluser.model.ModelRegister;
import com.training.ojoluser.model.ModelWaypoint;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

//sesuaikan dengan nama function di file Api.php
    @FormUrlEncoded
    //nama function
    @POST("daftar")
    //get response dari webservice
    Call<ModelRegister> registeruser(
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("phone") String nohp,
            @Field("password") String password);

    @FormUrlEncoded
    //nama function
    @POST("login")
    //get response dari webservice
    Call<ModelLogin> loginuser(
            @Field("device") String device,
            @Field("f_email") String email,
            @Field("f_password") String password);


    @GET("json")

    Call<ModelWaypoint> getwaypoint(
            @Query("origin") String origin,
            @Query("destination") String destination
    );

    @FormUrlEncoded
    //nama function
    @POST("insert_booking")
        //get response dari webservice
    Call<ModelBooking> bookingdriver(
            @Field("f_idUser") String iduser,
            @Field("f_latAwal") String latAwal,
            @Field("f_lngAwal") String lngawal,
            @Field("f_awal") String awal,
            @Field("f_latAkhir") String latakhir,
            @Field("f_lngAkhir") String lngakhir,
            @Field("f_akhir") String akhir,
            @Field("f_alamat") String alamat,
            @Field("f_jarak") String jarak,
            @Field("f_token") String token,
            @Field("f_device") String device);


}
