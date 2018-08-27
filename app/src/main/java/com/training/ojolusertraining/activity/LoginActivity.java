package com.training.ojolusertraining.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.training.ojolusertraining.R;
import com.training.ojolusertraining.helper.HeroHelper;
import com.training.ojolusertraining.helper.SessionManager;
import com.training.ojolusertraining.network.ApiServices;
import com.training.ojolusertraining.network.InitRetrofit;
import com.training.ojolusertraining.network.response.login.Data;
import com.training.ojolusertraining.network.response.login.ResponseLogin;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.loginemail)
    EditText loginemail;
    @BindView(R.id.loginpassword)
    EditText loginpassword;
    @BindView(R.id.signin)
    Button signin;
    @BindView(R.id.textlink)
    TextView textlink;

    //Deklarasi
    ApiServices apiServices;
    SessionManager sessionManager;

    private static final int RC_PERMISSION = 99 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //Inisalisasi
        apiServices = InitRetrofit.getInstance();
        //Session Manager
        sessionManager = new SessionManager(this);

        // Minta permission
        methodRequiresTwoPermission();

    }

    @OnClick(R.id.signin)
    public void onSigninClicked() {

        String email = loginemail.getText().toString();
        String password = loginpassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Semua harus terisi", Toast.LENGTH_SHORT).show();
        } else {
            //Intent IntentMaps = new Intent(this,MapsActivity.class);
            //startActivity(IntentMaps);
            //finish();

            //deviceID : IMEI HP
            //String deviceId = "10010101";
            String deviceId = HeroHelper.getDeviceUUID(this);
            Log.d("Device Imei Bro : ",deviceId);
            //Siapkan Request
            Call<ResponseLogin> requestLogin = apiServices.request_login(deviceId, email, password);
            //Kirim request
            requestLogin.enqueue(new Callback<ResponseLogin>() {
                @Override
                public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                    if (response.isSuccessful()) {

                        String result = response.body().getResult();
                        String token = response.body().getToken();

                        Data datauser = response.body().getData();

                        if (result.equals("true")) {
                            //Simpan ke Session Manager dari database Mysql
                            sessionManager.setIduser(datauser.getIdUser());
                            sessionManager.setEmail(datauser.getUserEmail());
                            sessionManager.setPhone(datauser.getUserHp());
                            sessionManager.setNama(datauser.getUserNama());
                            sessionManager.createLoginSession(response.body().getToken());


                            Intent intentMaps = new Intent(LoginActivity.this, MapsActivity.class);
                            startActivity(intentMaps);
                            finish();

                        } else {

                            String msg = response.body().getMsg();
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseLogin> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @OnClick(R.id.textlink)
    public void onTextlinkClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    //@AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.SEND_SMS };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Aplikasi membutuhkan izin lokasi dan membaca perangkat",
                    RC_PERMISSION, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //ketika user meng-klik Allow

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
