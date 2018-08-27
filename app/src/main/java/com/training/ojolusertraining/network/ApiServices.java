package com.training.ojolusertraining.network;

import com.training.ojolusertraining.network.response.ResponseBooking;
import com.training.ojolusertraining.network.response.ResponseCancel;
import com.training.ojolusertraining.network.response.ResponseDaftar;
import com.training.ojolusertraining.network.response.checkbooking.ResponseCheckBooking;
import com.training.ojolusertraining.network.response.driver.ResponseDriver;
import com.training.ojolusertraining.network.response.history.ResponseHistory;
import com.training.ojolusertraining.network.response.login.ResponseLogin;
import com.training.ojolusertraining.network.response.route.ResponseRoute;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by balqis on 5/14/2018.
 */

public interface ApiServices {

    //Buat Request untuk daftar
    @POST("daftar")
    @FormUrlEncoded
    Call<ResponseDaftar> request_daftar(
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password
    );

    //Untuk Login
    @POST("login")
    @FormUrlEncoded
    Call<ResponseLogin> request_login(
            @Field("device") String device,
            @Field("f_email") String email,
            @Field("f_password") String password
    );


    //Untuk Routing
    //https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyC4_OWsvMNaoExK-VHE-HgqM5SL5l57XW4
    @GET("json")
    Call<ResponseRoute> request_route(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("avoid") String avoid,
            @Query("key") String key
    );


    //Booking
    @POST("insert_booking")
    @FormUrlEncoded
    Call<ResponseBooking> request_booking(
            @Field("f_idUser") String id_user,
            @Field("f_awal") String alamat_tempat_awal,
            @Field("f_latAwal") String latitude_awal,
            @Field("f_lngAwal") String longitude_awal,
            @Field("f_akhir") String alamat_tempat_akhir,
            @Field("f_latAkhir") String latitude_tujuan,
            @Field("f_lngAkhir") String longitude_tujuan,
            @Field("f_alamat") String alamat,
            @Field("f_jarak") String jarak,
            @Field("f_tarif") String tarif
    );


    //Check Booking
    @POST("check_booking")
    @FormUrlEncoded
    Call<ResponseCheckBooking> request_check_booking(
            @Field("id_booking") int id_booking
    );

    //CAncel Booking
    @POST("cancel_booking")
    @FormUrlEncoded
    Call<ResponseCancel> request_cancel_booking(
            @Field("id_booking") int id_booking
    );

    //Cek Posisi Driver
    @POST("get_driver")
    @FormUrlEncoded
    Call<ResponseDriver> request_get_driver(
            @Field("id") String id_driver
    );

    @FormUrlEncoded
    @POST("get_handle_booking_user")
    Call<ResponseHistory> request_process_booking(
            @Field("f_token") String token,
            @Field("f_device") String device,
            @Field("f_idUser") String iduser);

    @FormUrlEncoded
    @POST("get_complete_booking_user")
    Call<ResponseHistory> request_complete_booking(
            @Field("f_token") String token,
            @Field("f_device") String device,
            @Field("f_idUser") String iduser);



}
