package com.training.ojoluser.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.training.ojoluser.MainActivity;
import com.training.ojoluser.R;
import com.training.ojoluser.model.DataDriver;
import com.training.ojoluser.model.ModelDriver;
import com.training.ojoluser.network.ApiService;
import com.training.ojoluser.network.InitRetrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PosisiDriver extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.txtnamadriver)
    TextView txtnamadriver;
    @BindView(R.id.txthpdriver)
    TextView txthpdriver;
    String id;
    GoogleMap mMap;
    private LatLng posisi;
    private List<DataDriver> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posisi_driver);
        ButterKnife.bind(this);


        //get data berdasarkan yang kirim oleh class finddriver
        String lokasiawall = getIntent().getStringExtra("lokasi1");
        String lokasiakhir = getIntent().getStringExtra("lokasi2");
        id = getIntent().getStringExtra("driver");


        //tambahan di di webinar untuk ambil posisi driver yang tak booking penggunanya
        //ok ?
        //getDriver(id);


        //pindahin data ke textview
        lokasiawal.setText(lokasiawall);
        lokasitujuan.setText(lokasiakhir);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    //method untuk setting maps
    //sample: setting zoom,type maps ,etc
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        getDriver(mMap);


    }

    private void getDriver(final GoogleMap mMap) {

        ApiService api = InitRetrofit.getInstance();
        //final ProgressDialog dialog = ProgressDialog.show(PosisiDriver.this, "get posisi driver", "loading...");

        //saat request
        Call<ModelDriver> call = api.getdataDriver(id);

        call.enqueue(new Callback<ModelDriver>() {
            @Override
            public void onResponse(Call<ModelDriver> call, Response<ModelDriver> response) {

mMap.clear();
                //ini saat server kasi data tracking driver
                if (response.isSuccessful()) {
              //      dialog.dismiss();
                    data = new ArrayList<>();
                    data = response.body().getData();

                    String lat = data.get(0).getTrackingLat();
                    String lon = data.get(0).getTrackingLng();

                    //coordinat ubah nilainya ke double
                    //convert type data dari string menuju double
                    //kenapa double ?
                    //karena kalau type datanya string coordinat nggak masuk map android
                    txtnamadriver.setText(data.get(0).getUserNama());
                    txthpdriver.setText(data.get(0).getUserHp());
                    Double lat1 = Double.parseDouble(lat);
                    Double lon1 = Double.parseDouble(lon);

                    //pindahkan ke maps
                    posisi = new LatLng(lat1, lon1);


                    mMap.addMarker(new MarkerOptions().position(posisi).title("Your Driver"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(posisi));
                    // menampilkan control zoom in zoom out
                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    // menampilkan compas
                    mMap.getUiSettings().setCompassEnabled(true);
                    // mengatur jenis peta
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    //padding maps
                    mMap.setPadding(40, 150, 50, 120);

                    //auto zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi, 16));

                    //button current location
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);


                }
            }

            @Override
            public void onFailure(Call<ModelDriver> call, Throwable t) {
           //     dialog.dismiss();
            }
        });


    }


    Timer autoUpdate;

    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        getDriver(mMap);


                        //RbHelper.pesan(c,"ngulang");


                    }
                });
            }
        }, 0, 3000); // updates each 40 secs
    }

    @Override
    protected void onPause() {
        autoUpdate.cancel();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Where you go ?");
            alert.setPositiveButton("Exit App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            alert.setNegativeButton("Home", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(PosisiDriver.this, MainActivity.class));
                    finish();
                }
            });
            alert.show();

        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.history) {
            //    startActivity(new Intent(PosisiDriver.this,History.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void oncall(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.get(0).getUserHp())));
    }
}
