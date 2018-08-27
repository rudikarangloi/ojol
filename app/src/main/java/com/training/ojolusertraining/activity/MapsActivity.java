package com.training.ojolusertraining.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;
import com.training.ojolusertraining.R;
import com.training.ojolusertraining.helper.HeroHelper;
import com.training.ojolusertraining.helper.SessionManager;
import com.training.ojolusertraining.network.ApiServices;
import com.training.ojolusertraining.network.InitRetrofit;
import com.training.ojolusertraining.network.response.ResponseBooking;
import com.training.ojolusertraining.network.response.route.Distance;
import com.training.ojolusertraining.network.response.route.Duration;
import com.training.ojolusertraining.network.response.route.LegsItem;
import com.training.ojolusertraining.network.response.route.ResponseRoute;
import com.training.ojolusertraining.network.response.route.RoutesItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;


//Activity pertama yang dipanggil setelah login

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int RC_AWAL = 99;
    private static final int RC_AKHIR = 999;

    LatLng koordinatAwal;
    LatLng koordinatAkhir;
    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.txtharga)
    TextView txtharga;
    @BindView(R.id.txtjarak)
    TextView txtjarak;
    @BindView(R.id.txtdurasi)
    TextView txtdurasi;
    @BindView(R.id.requestorder)
    Button requestorder;

    private GoogleMap mMap;

    FusedLocationProviderClient mFusedLocation;     ////Dapatkan Lokasi saat ini

    ApiServices apiServicesMap;

    String STATE_AWAL,STATE_TUJUAN;

    private String polyLinePoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*
        if(savedInstanceState != null){
            String awal = savedInstanceState.getString(STATE_AWAL);
            String tujuan = savedInstanceState.getString(STATE_TUJUAN);
            lokasiawal.setText(awal);
            lokasitujuan.setText(tujuan);
        }
        */

        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Inisialisasi Fused Location: Medndapatkan lokasi saat ini
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        apiServicesMap = InitRetrofit.getIntanceMap();


    }

    /*
    public void onSaveInstanceState(Bundle outstate) {
        outstate.putString(STATE_AWAL,lokasiawal.getText().toString());
        outstate.putString(STATE_TUJUAN,lokasitujuan.getText().toString());
        super.onSaveInstanceState(outstate);
    }
    */

    /*
    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        mFusedLocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Pastikan Variabel tidak kosong
                if (location != null) {
                    // Add a marker in Sydney and move the camera
                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Lokasi Saat ini"));

                    //belom di zoom
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    //Di zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                }
            }
        });
    }
    */

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //MAP_TYPE_NORMAL , MAP_TYPE_HYBRID, MAP_TYPE_SATELLITE, MAP_TYPE_TERRAIN
        mMap.setMapType(MAP_TYPE_TERRAIN);

        //Dapatkan Lokasi saat ini
        mFusedLocation.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Pastikan Variabel tidak kosong
                if (location != null) {
                    // Add a marker in Sydney and move the camera
                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Lokasi Saat ini"));

                    //belom di zoom
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                    //Di zoom
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menu_id = item.getItemId();

        if(menu_id==R.id.history){
            startActivity(new Intent(this,History.class));
        }else if(menu_id==R.id.profil){
            final SessionManager sessionManager = new SessionManager(this);
            AlertDialog.Builder alertProfile = new AlertDialog.Builder(this);
            alertProfile.setTitle("Profile");
            alertProfile.setMessage(
                            "Nama : " + sessionManager.getNama() +
                            "\nEmail : " + sessionManager.getEmail() +
                            "\nPhone : " + sessionManager.getPhone()  );

            alertProfile.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sessionManager.logout();
                    startActivity(new Intent(MapsActivity.this,LoginActivity.class));
                    finish();
                }
            });

            alertProfile.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alertProfile.show();

        }


        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.lokasiawal)
    public void onLokasiawalClicked() {
        showAutoComplete(RC_AWAL);
    }

    @OnClick(R.id.lokasitujuan)
    public void onLokasitujuanClicked() {
        showAutoComplete(RC_AKHIR);
    }

    @OnClick(R.id.requestorder)
    public void onRequestorderClicked() {

        if(koordinatAkhir==null || koordinatAwal==null){
            HeroHelper.pesan(this,"Silahkan Pilih Lokasi awal dan tujuan");
            return;
        }

        String idUser = new SessionManager(this).getIdUser();
        //String token = new SessionManager(this).getToken();
        String alamatAwal = lokasiawal.getText().toString();
        String alamatTujuan = lokasitujuan.getText().toString();
        String alamat = "Alamat Dummy Tak terpakai";
        String latAwal = String.valueOf(koordinatAwal.latitude);
        String lngAwal = String.valueOf(koordinatAwal.longitude);
        String latAkhir = String.valueOf(koordinatAkhir.latitude);
        String lngAkhir = String.valueOf(koordinatAkhir.longitude);
        String tarif=txtharga.getText().toString();
        String jarak=txtharga.getText().toString();
        //String deviceID= HeroHelper.getDeviceUUID(this);

        ApiServices apiServices=InitRetrofit.getInstance();
        Call<ResponseBooking> requestBooking = apiServices
                .request_booking(
                                idUser,
                                alamatAwal,
                                latAwal,
                                lngAwal,
                                alamatTujuan,
                                latAkhir,
                                lngAkhir,
                                alamat,
                                jarak,
                                tarif
                                );

       requestBooking.enqueue(new Callback<ResponseBooking>() {
           @Override
           public void onResponse(Call<ResponseBooking> call, Response<ResponseBooking> response) {
               if(response.isSuccessful()){
                   int idBooking=response.body().getIdBooking();
                   Toast.makeText(MapsActivity.this, "idBook : " + idBooking, Toast.LENGTH_SHORT).show();
                   String result=response.body().getResult();
                   String pesan = response.body().getMsg();

                   //Lihat result di json hasilnya true
                   if(result.equals("true")){
                       Toast.makeText(MapsActivity.this, pesan, Toast.LENGTH_SHORT).show();

                       //HeroHelper.pindahclass(MapsActivity.this,FindDriverActivity.class);
                       Intent   pergiKeFindDriver=new Intent(MapsActivity.this,FindDriverActivity.class);
                       pergiKeFindDriver.putExtra("ID_BOOKING",idBooking);
                       pergiKeFindDriver.putExtra("RUTE_POLYLINE",polyLinePoint);
                       startActivity(pergiKeFindDriver);
                   }
               }
           }

           @Override
           public void onFailure(Call<ResponseBooking> call, Throwable t) {

           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK){

            mMap.clear();

            //Dapatkan data dari variabel data
            Place dataPlace = PlaceAutocomplete.getPlace(this,data);
            //Dapatkan koodinat tempat terpilih
            LatLng koordinatPlace=dataPlace.getLatLng();

            if(requestCode == RC_AWAL){
                koordinatAwal = koordinatPlace;
                lokasiawal.setText(dataPlace.getAddress());
            }else if(requestCode == RC_AKHIR){
                koordinatAkhir=koordinatPlace;
                lokasitujuan.setText(dataPlace.getAddress());
            }

            if(koordinatAwal!=null && koordinatAkhir!=null){
                //set marker ke map
                mMap.addMarker(new MarkerOptions().position(koordinatAwal).title("Lokasi Awal"));
                mMap.addMarker(new MarkerOptions().position(koordinatAkhir).title("Lokasi Akhir"));
                           // .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_account_circle_black_24dp)));

               // mMap.addMarker(new MarkerOptions().position(koordinatAkhir).title(dataPlace.getName().toString()));
                //Zoom
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koordinatPlace,15F));

                // ------ Layar berada di tengah 2 kordinat ---------
                LatLngBounds.Builder latLongBulider= new LatLngBounds.Builder();
                latLongBulider.include(koordinatAwal);
                latLongBulider.include(koordinatAkhir);

                //Bounce Koordinat
                LatLngBounds bounds=latLongBulider.build();

                int width=getResources().getDisplayMetrics().widthPixels;
                int height=getResources().getDisplayMetrics().heightPixels;
                int paddingMap = (int) (width*0.2); //Jarak dari
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,paddingMap);
                mMap.animateCamera(cu);
                // ---------------------------------------------------------

                //BUAT ROUTE PADA PETA
                actionRoute();

            }else{
                //set marker ke map
                mMap.addMarker(new MarkerOptions().position(koordinatPlace).title(dataPlace.getName().toString()));
                //Zoom
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koordinatPlace,15F));
            }


        }
    }

    public void showAutoComplete(int requestCode){

        //Filter hanya negara tertentu
        AutocompleteFilter typeFilter=new AutocompleteFilter.Builder().setCountry("ID").build();
        try {
            Intent intentPlaceAuto = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).setFilter(typeFilter)
                    .build(MapsActivity.this);
            startActivityForResult(intentPlaceAuto,requestCode);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    //Method buat route
    private void actionRoute() {

        //Siapkan request yg akan dikirim
        String latLngAwal=koordinatAwal.latitude+","+koordinatAwal.longitude;
        String latLngAkhir=koordinatAkhir.latitude+","+koordinatAkhir.longitude;
        //Pilihan rute
        //tolls menunjukkan rute yang dihitung harus menghindari jalan/jembatan tol.
        //highways menunjukkan rute yang dihitung harus menghindari jalan raya.
        //ferries menunjukkan rute yang dihitung harus menghindari penyeberangan feri.
        //indoor menunjukkan rute yang dihitung harus menghindari tangga dalam ruangan untuk arah berjalan dan arah angkutan umum.
        //      Hanya permintaan yang menyertakan kunci API atau ID klien Google Maps APIs Premium Plan yang akan menerima tangga dalam ruangan secara default.
        String hidariRoute="tolls|highways";
        //Jika jalan tidak dibatasi
        //String hidariRoute=null;
        String apiKey = "AIzaSyC4_OWsvMNaoExK-VHE-HgqM5SL5l57XW4";

        //Siapkan request yg dikirim

        Call<ResponseRoute> requestRouting=apiServicesMap.request_route(latLngAwal,latLngAkhir,hidariRoute,apiKey);
        //Kirim request
        requestRouting.enqueue(new Callback<ResponseRoute>() {
            @Override
            public void onResponse(Call<ResponseRoute> call, Response<ResponseRoute> response) {
                if(response.isSuccessful()){
                    Log.d("hasil routing",response.body().toString());
                    String status = response.body().getStatus();
                    List<RoutesItem> dataRoute = response.body().getRoutes();
                    
                    if(status.equals("OK")){
                        polyLinePoint = dataRoute.get(0).getOverviewPolyline().getPoints();
                        //decode
                        List<LatLng> decodePath= PolyUtil.decode(polyLinePoint);
                        //Gambar garis Map
                        mMap.addPolyline(new PolylineOptions().addAll(decodePath)
                                .width(8f)
                                .color(Color.argb(255,56,167,252)))
                                .setGeodesic(true);

                        //Perhitungan jarak dan waktu
                        List<LegsItem> dataLegs = dataRoute.get(0).getLegs();
                        Distance jarakTempuh=dataLegs.get(0).getDistance();
                        Duration WaktuTempuh=dataLegs.get(0).getDuration();

                        //Perhitungan Harga
                        double HargaPermeter=250;
                        double tarifTotal=HargaPermeter * jarakTempuh.getValue();

                        //Set widget
                        txtjarak.setText(jarakTempuh.getText().toString());
                        txtdurasi.setText(WaktuTempuh.getText().toString());
                        txtharga.setText("Rp."+String.valueOf(tarifTotal));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRoute> call, Throwable t) {

            }
        });

    }
}
