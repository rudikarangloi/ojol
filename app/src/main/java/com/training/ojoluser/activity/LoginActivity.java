package com.training.ojoluser.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.training.ojoluser.MainActivity;
import com.training.ojoluser.R;
import com.training.ojoluser.helper.HeroHelper;
import com.training.ojoluser.helper.SessionManager;
import com.training.ojoluser.model.DataLogin;
import com.training.ojoluser.model.ModelLogin;
import com.training.ojoluser.network.ApiService;
import com.training.ojoluser.network.InitRetrofit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.loginemail)
    EditText loginemail;
    @BindView(R.id.loginpassword)
    EditText loginpassword;
    @BindView(R.id.signin)
    Button signin;
    @BindView(R.id.textlink)
    TextView textlink;
    SessionManager sesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signin)
    public void onViewClicked() {
        //Todo 2 cek inputan user tidak boleh kosong


        if (HeroHelper.isEmpty(loginemail)
                || HeroHelper.isEmpty(loginpassword)) {


            HeroHelper.pesan(this, "harus isi semuanya");
        } else if (!HeroHelper.isEmailValid(loginemail)) {
            HeroHelper.pesan(this, "email tidak valid,periksa lagi");
        } else if (HeroHelper.minLength(loginpassword, 6)) {
            HeroHelper.pesan(this, "min passowrd 6 karakter");

        }
        //todo 3 parsing  view (Edittext) ke string1126
        else {
            String password = loginpassword.getText().toString();
            String email = loginemail.getText().toString();
            String device = HeroHelper.getDeviceUUID(LoginActivity.this);
            //tampilan loading
            final ProgressDialog dialog = ProgressDialog.show(this, "proses register", "loading...");

            ApiService service = InitRetrofit.getInstance();
            Call<ModelLogin> loginCall = service.loginuser(device, email, password);
            loginCall.enqueue(new Callback<ModelLogin>() {
                @Override
                public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                    if (response.isSuccessful()) {

                        String result = response.body().getResult();
                        String msg = response.body().getMsg();
                        String token = response.body().getToken();
                        sesi = new SessionManager(LoginActivity.this);
                        if (result.equals("true")) {
                            HeroHelper.pindahclass(LoginActivity.this, MapsActivity.class);
                            HeroHelper.pesan(LoginActivity.this, msg);
                            DataLogin login = response.body().getData();
                            sesi.setIduser(login.getIdUser());
                            sesi.setEmail(login.getUserEmail());
                            sesi.setPhone(login.getUserHp());
                            sesi.createLoginSession(token);
                            finish();
                            dialog.dismiss();
                        } else {
                            HeroHelper.pesan(LoginActivity.this, msg);
                    dialog.dismiss();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ModelLogin> call, Throwable t) {
                    HeroHelper.pesan(LoginActivity.this, "gagal koneksi");
                }
            });

        }
    }

    public void onSignup(View view) {
        HeroHelper.pindahclass(LoginActivity.this,RegisterActivity.class);
    }
}
