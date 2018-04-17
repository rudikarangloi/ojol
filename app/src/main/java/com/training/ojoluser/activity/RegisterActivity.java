package com.training.ojoluser.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.training.ojoluser.R;
import com.training.ojoluser.helper.HeroHelper;
import com.training.ojoluser.model.ModelRegister;
import com.training.ojoluser.network.ApiService;
import com.training.ojoluser.network.InitRetrofit;

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

    //todo 1 generate butterknife
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnsignup)
    public void onViewClicked() {
        //Todo 2 cek inputan user tidak boleh kosong


        if (HeroHelper.isEmpty(daftarusername)
                || HeroHelper.isEmpty(daftaremail)
                || HeroHelper.isEmpty(daftarhp)
                || HeroHelper.isEmpty(daftarpassword)
                || HeroHelper.isEmpty(daftarconfirmasipass)) {


            HeroHelper.pesan(this, "harus isi semuanya");
        } else if (!HeroHelper.isEmailValid(daftaremail)) {
            HeroHelper.pesan(this, "email tidak valid,periksa lagi");
        } else if (HeroHelper.isCompare(daftarpassword, daftarconfirmasipass)) {
            HeroHelper.pesan(this, "confirm password tidak sama");
        } else if (HeroHelper.minLength(daftarpassword, 6)) {
            HeroHelper.pesan(this, "min passowrd 6 karakter");

        }
        //todo 3 parsing  view (Edittext) ke string1126
        else {
            String username = daftarusername.getText().toString();
            String nohp = daftarhp.getText().toString();
            String password = daftarpassword.getText().toString();
            String email = daftaremail.getText().toString();
            //tampilan loading
            ProgressDialog dialog = ProgressDialog.show(this, "proses register", "loading...");

            ApiService service = InitRetrofit.getInstance();

            Call<ModelRegister> modelRegisterCall = service.registeruser(username, email, nohp, password);
            //panggil response
            modelRegisterCall.enqueue(new Callback<ModelRegister>() {
                @Override
                public void onResponse(Call<ModelRegister> call, Response<ModelRegister> response) {
                    if (response.isSuccessful()) {
                        String result = response.body().getResult();
                        String msg = response.body().getMsg();
                        if (result.equals("true")) {
                            HeroHelper.pesan(RegisterActivity.this,msg);
                            HeroHelper.pindahclass(RegisterActivity.this,LoginActivity.class);
                            finish();
                        }else{
                            HeroHelper.pesan(RegisterActivity.this,msg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelRegister> call, Throwable t) {
                    HeroHelper.pesan(RegisterActivity.this,"Gagal koneksi"+t.getMessage());
                }
            });
        }
    }


}
