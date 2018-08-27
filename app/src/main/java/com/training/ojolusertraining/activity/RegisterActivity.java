package com.training.ojolusertraining.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.training.ojolusertraining.R;
import com.training.ojolusertraining.network.ApiServices;
import com.training.ojolusertraining.network.InitRetrofit;
import com.training.ojolusertraining.network.response.ResponseDaftar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.daftarusername)
    EditText daftarusername;
    @BindView(R.id.daftaremail)
    EditText daftaremail;
    @BindView(R.id.daftarhp)
    EditText daftarhp;
    @BindView(R.id.daftarpassword)
    EditText daftarpassword;
    @BindView(R.id.daftarconfirmasipass)
    EditText daftarconfirmasipass;
    @BindView(R.id.btnsignup)
    Button btnsignup;

    ApiServices apiServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        ButterKnife.bind(this);
        apiServices = InitRetrofit.getInstance();
    }

    @OnClick(R.id.btnsignup)
    public void onViewClicked() {
        //TODO 1 : Get isi dari EditText
        String namaUser = daftarusername.getText().toString();
        String emailUser = daftaremail.getText().toString();
        String telpUser = daftarhp.getText().toString();
        String passUser = daftarpassword.getText().toString();
        String confirPassUser = daftarconfirmasipass.getText().toString();

        //TODO 2 Cek validasi input
        if(namaUser.isEmpty() || emailUser.isEmpty() || telpUser.isEmpty() || passUser.isEmpty() || confirPassUser.isEmpty()){
            Toast.makeText(this, "Semua Input harus terisi", Toast.LENGTH_SHORT).show();
        }else if(!passUser.equals(confirPassUser)){
            Toast.makeText(this, "Konfirmasi katasandi tidak sama", Toast.LENGTH_SHORT).show();
        }else{
            //TODO 3 : Kirim data ke API
            //Siapkan Request
            Call<ResponseDaftar> requestDaftar  = apiServices.request_daftar(namaUser,emailUser,telpUser,passUser);
            //Kirim request
            requestDaftar.enqueue(new Callback<ResponseDaftar>() {
                @Override
                public void onResponse(Call<ResponseDaftar> call, Response<ResponseDaftar> response) {
                    //Dijalankan ketika Request berhasil
                    if(response.isSuccessful()){
                        //Tampung ke variabel
                        String result = response.body().getResult();
                        String msg = response.body().getMsg();
                        //Tampilkan Toast
                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                        //Jika Result JSON  = 1, amak berhasil, pindah ke login
                        if(result.equals("1")){
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Gagal Brooo", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<ResponseDaftar> call, Throwable t) {
                    //Dijalankan ketika Request gagal. (Contoh : Tidak ada internet)
                    //Cetak Error
                    t.printStackTrace();

                }
            });
        }

    }
}
