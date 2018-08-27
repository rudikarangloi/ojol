package com.training.ojolusertraining.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;
import com.training.ojolusertraining.MainActivity;
import com.training.ojolusertraining.R;
import com.training.ojolusertraining.network.ApiServices;
import com.training.ojolusertraining.network.InitRetrofit;
import com.training.ojolusertraining.network.response.driver.DataItem;
import com.training.ojolusertraining.network.response.driver.ResponseDriver;
import com.training.ojolusertraining.network.response.route.Distance;
import com.training.ojolusertraining.network.response.route.Duration;
import com.training.ojolusertraining.network.response.route.LegsItem;
import com.training.ojolusertraining.network.response.route.ResponseRoute;
import com.training.ojolusertraining.network.response.route.RoutesItem;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PosisiDriver extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    String id_driver;
    String polylinePoint;
    ApiServices apiServices;
    ApiServices apiServicesMap;

    Timer myTimer;
    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.btnCall)
    Button btnCall;
    @BindView(R.id.btnSms)
    Button btnSms;
    @BindView(R.id.txtnamadriver)
    TextView txtnamadriver;
    @BindView(R.id.linear2)
    LinearLayout linear2;
    @BindView(R.id.txthpdriver)
    TextView txthpdriver;
    @BindView(R.id.linear1)
    LinearLayout linear1;

    private String namaDriver, noHpDriver, idDriver;
    FusedLocationProviderClient mFusedLocation;
    private LatLng posisiAnda;
    private LatLng koordinatDriver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posisi_driver);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Dapatka Intent
        id_driver = getIntent().getStringExtra("ID_DRIVER");
        polylinePoint = getIntent().getStringExtra("RUTE_POLYLINE");

        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        apiServices = InitRetrofit.getInstance();
        apiServicesMap = InitRetrofit.getIntanceMap();

        myTimer = new Timer();

        //Set Timer
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                trackingDriver();
            }
        }, 0, 3000);

    }

    //method untuk setting maps
    //sample: setting zoom,type maps ,etc
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(koordinatDriver));
        //Menalpilkan kontrol zoom in dan zoom out
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //Menampilkan Kompas
        mMap.getUiSettings().setCompassEnabled(true);
        //mengatur jenis peta
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //Padding Map
       // mMap.setPadding(40, 150, 50, 120);

        //drawRoute();

        //Dapatkan Lokasi saat ini
        mFusedLocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Pastikan Variabel tidak kosong
                if (location != null) {
                    // Add a marker in Sydney and move the camera
                    posisiAnda = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(posisiAnda).title("Lokasi Saat ini"));

                    //belom di zoom
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    //Di zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisiAnda, 17));
                }
            }
        });

    }

    private void drawRoute() {

        //decode
        List<LatLng> decodePath = PolyUtil.decode(polylinePoint);
        //Gambar garis Map
        mMap.addPolyline(new PolylineOptions().addAll(decodePath)
                .width(8f)
                .color(Color.argb(255, 56, 167, 252)))
                .setGeodesic(true);
    }

    @Override
    protected void onPause() {
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
                    myTimer.cancel();
                    finish();
                }
            });

            alert.setNegativeButton("Home", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(PosisiDriver.this, MainActivity.class));
                    myTimer.cancel();
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

    //Method Check Posisi driver

    public void trackingDriver() {

        Call<ResponseDriver> requestTrackDriver = apiServices.request_get_driver(id_driver);
        requestTrackDriver.enqueue(new Callback<ResponseDriver>() {
            @Override
            public void onResponse(Call<ResponseDriver> call, Response<ResponseDriver> response) {
                if (response.isSuccessful()) {
                    String msg = response.body().getMsg();
                    String result = response.body().getResult();
                    List<DataItem> dataDriver = response.body().getData();

                    if (result.equals("true")) {
                        //Pecah data
                        namaDriver = dataDriver.get(0).getUserNama();
                        noHpDriver = dataDriver.get(0).getUserHp();

                        txthpdriver.setText(noHpDriver);
                        txtnamadriver.setText(namaDriver);

                        //idDriver dikirim dari Activity sebelumnya
                        //idDriver = dataDriver.get(0).getIdUser();

                        Double latitude_lokasiDriver = Double.parseDouble(dataDriver.get(0).getTrackingLat());
                        Double longitude_lokasiDriver = Double.parseDouble(dataDriver.get(0).getTrackingLng());

                        koordinatDriver = new LatLng(latitude_lokasiDriver, longitude_lokasiDriver);

                        //set marker goggle map

                        //Marker dibuat icon sendiri
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.motorbike3);
                        MarkerOptions customMarker = new MarkerOptions().position(koordinatDriver)
                                .title("Lokasi Driver")
                                .icon(icon);
                        // set marker ke google map
                        mMap.clear();

                        //Tambahkan Marker / posisi Driver dan user
                        mMap.addMarker(customMarker);
                        mMap.addMarker(new MarkerOptions().position(posisiAnda).title("Lokasi Saat ini"));



                        //Auto zoom
                        // ------ Layar berada di tengah 2 kordinat [antara Driver dan user] ---------
                        LatLngBounds.Builder latLongBulider = new LatLngBounds.Builder();
                        latLongBulider.include(koordinatDriver);
                        latLongBulider.include(posisiAnda);

                        //Bounce Koordinat
                        LatLngBounds bounds = latLongBulider.build();

                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int paddingMap = (int) (width * 0.2); //Jarak dari
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, paddingMap);
                        mMap.animateCamera(cu);
                        // ---------------------------------------------------------
                        //drawRoute();
                        //BUAT ROUTE PADA PETA
                        actionRoute();
                        //Button Current locatiom



                    } else {
                        Toast.makeText(PosisiDriver.this, "Driver Tidak ada", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseDriver> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.btnCall)
    public void onBtnCallClicked() {
        //Intent dialPnoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+noHpDriver));
        //startActivity(dialPnoneIntent);

        String uri = "tel:" + noHpDriver.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: ketika tidak ada permission
            Toast.makeText(this, "Izin panggilan belum diberikan", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }


    @OnClick(R.id.btnSms)
    public void onBtnSmsClicked() {
        Uri uri = Uri.parse("smsto:" + noHpDriver);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "Here you can set the SMS text to be sent");
        startActivity(it);
    }

    //Method buat route
    private void actionRoute() {

        //Siapkan request yg akan dikirim
        String latLngAwal = koordinatDriver.latitude + "," + koordinatDriver.longitude;
        String latLngAkhir = posisiAnda.latitude + "," + posisiAnda.longitude;
        //Pilihan rute
        //tolls menunjukkan rute yang dihitung harus menghindari jalan/jembatan tol.
        //highways menunjukkan rute yang dihitung harus menghindari jalan raya.
        //ferries menunjukkan rute yang dihitung harus menghindari penyeberangan feri.
        //indoor menunjukkan rute yang dihitung harus menghindari tangga dalam ruangan untuk arah berjalan dan arah angkutan umum.
        //      Hanya permintaan yang menyertakan kunci API atau ID klien Google Maps APIs Premium Plan yang akan menerima tangga dalam ruangan secara default.
        String hidariRoute = "tolls|highways";
        //Jika jalan tidak dibatasi
        //String hidariRoute=null;
        String apiKey = "AIzaSyC4_OWsvMNaoExK-VHE-HgqM5SL5l57XW4";

        //Siapkan request yg dikirim

        Call<ResponseRoute> requestRouting = apiServicesMap.request_route(latLngAwal, latLngAkhir, hidariRoute, apiKey);
        //Kirim request
        requestRouting.enqueue(new Callback<ResponseRoute>() {
            @Override
            public void onResponse(Call<ResponseRoute> call, Response<ResponseRoute> response) {
                if (response.isSuccessful()) {
                    Log.d("hasil routing", response.body().toString());
                    String status = response.body().getStatus();
                    List<RoutesItem> dataRoute = response.body().getRoutes();

                    if (status.equals("OK")) {
                        polylinePoint = dataRoute.get(0).getOverviewPolyline().getPoints();
                        //decode
                        List<LatLng> decodePath = PolyUtil.decode(polylinePoint);
                        //Gambar garis Map
                        mMap.addPolyline(new PolylineOptions().addAll(decodePath)
                                .width(8f)
                                .color(Color.argb(255, 56, 167, 252)))
                                .setGeodesic(true);

                        //Perhitungan jarak dan waktu
                        List<LegsItem> dataLegs = dataRoute.get(0).getLegs();
                        Distance jarakTempuh = dataLegs.get(0).getDistance();
                        Duration WaktuTempuh = dataLegs.get(0).getDuration();


                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRoute> call, Throwable t) {

            }
        });

    }
}
