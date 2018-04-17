package com.training.ojoluser.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.training.ojoluser.R;
import com.training.ojoluser.helper.DirectionMapsV2;
import com.training.ojoluser.helper.GPSTracker;
import com.training.ojoluser.helper.HeroHelper;
import com.training.ojoluser.helper.SessionManager;
import com.training.ojoluser.model.Distance;
import com.training.ojoluser.model.Duration;
import com.training.ojoluser.model.LegsItem;
import com.training.ojoluser.model.ModelBooking;
import com.training.ojoluser.model.ModelWaypoint;
import com.training.ojoluser.model.RoutesItem;
import com.training.ojoluser.network.ApiService;
import com.training.ojoluser.network.InitRetrofit;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 1;
    private static final int REQAWAL = 2;
    private static final int REQAKHIR = 3;
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
    private GoogleApiClient googleApiClient;
    private GPSTracker gps;
    private double latawal;
    private double lonawal;
    String name_location;
    private double latakhir;
    private double lonakkhir;
    private String namalokasiawal;
    private String jarak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // cek sttus gps aktif atau tidak
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            Toast.makeText(this, "Gps already enabled", Toast.LENGTH_SHORT).show();
            //     finish();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(this)) {
            Toast.makeText(this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            Toast.makeText(this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        }
    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MapsActivity.this, REQUEST_LOCATION);

                                finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    private boolean hasGPSDevice(MapsActivity mapsActivity) {
        final LocationManager mgr = (LocationManager) mapsActivity
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gps= new GPSTracker(MapsActivity.this);
        //todo 3 cek permission GPS
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        110);


            }
            return;

        }
        if (gps.canGetLocation()) {
            //getkoordinat jika gps aktif
            latawal = gps.getLatitude();
            lonawal = gps.getLongitude();
            //ubah koordinat jadi nama lokasi
            name_location = posisiku(latawal, lonawal);

            lokasiawal.setText(name_location);

        }

        // Add a marker in Sydney and move the camera

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latawal, lonawal);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in slipi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        // menampilkan control zoom in zoom out
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // menampilkan compas
        mMap.getUiSettings().setCompassEnabled(true);
        // mengatur jenis peta
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //padding maps
        mMap.setPadding(40, 150, 50, 120);

        //auto zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        //button current location
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // mMap.setMyLocationEnabled(true);

    }

    private String posisiku(double latawal, double lonawal) {
        name_location = null;
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(latawal,lonawal, 1);
            if(list != null&&list.size()>0) {
                name_location = list.get(0).getAddressLine(0) + "" + list.get(0).getCountryName();

                //fetch data from addresses
            }else{
                Toast.makeText(this, "kosong", Toast.LENGTH_SHORT).show();
                //display Toast message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name_location;
    }

    @OnClick({R.id.lokasiawal, R.id.lokasitujuan, R.id.requestorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lokasiawal:
                setlokasi(REQAWAL);
                break;
            case R.id.lokasitujuan:
                setlokasi(REQAKHIR);
                break;
            case R.id.requestorder:
                caridriver();
                break;
        }
    }

    private void caridriver() {
        String iduser = new SessionManager(this).getIdUser();
        String token = new SessionManager(this).getToken();
        String awal = lokasiawal.getText().toString();
        String tujuan = lokasitujuan.getText().toString();
        String ltawal = String.valueOf(latawal);
        String lngawal = String.valueOf(lonawal);
        String ltakhir = String.valueOf(latakhir);
        String lngakhir = String.valueOf(lonakkhir);
        String alamat = "asdas";
        String tarif = txtharga.getText().toString();
        String device = HeroHelper.getDeviceUUID(this);

        ApiService service =InitRetrofit.getInstance();
        Call<ModelBooking> bookingCall =service.bookingdriver(
                iduser,ltawal,lngawal,awal,ltakhir,lngakhir,tujuan,alamat,jarak,token,device
        );
        bookingCall.enqueue(new Callback<ModelBooking>() {
            @Override
            public void onResponse(Call<ModelBooking> call, Response<ModelBooking> response) {
                int idbooking =response.body().getIdBooking();
                Toast.makeText(MapsActivity.this, "idboo" + idbooking, Toast.LENGTH_SHORT).show();
                String result = response.body().getResult();
                String pesan = response.body().getMsg();
                if (result.equals("true")) {
                    HeroHelper.pesan(MapsActivity.this, pesan);
                    Intent kirim = new Intent(MapsActivity.this, FindDriverActivity.class);
                    kirim.putExtra("idbooking", idbooking);
                    startActivity(kirim);
                } else {
                    HeroHelper.pesan(MapsActivity.this, pesan);

                }

            }

            @Override
            public void onFailure(Call<ModelBooking> call, Throwable t) {

            }
        });
    }

    //menentukan lokasi menggunakan google place
    private void setlokasi(int i) {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("ID") // set filter negara
                .build();

        //
        Intent intent = null;
        try {
            intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter)
                    .build(MapsActivity.this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        startActivityForResult(intent, i);
    }
//untuk menangkap resnpose atau result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQAWAL) {
            if (resultCode == RESULT_OK&& data!=null) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                latawal = place.getLatLng().latitude;
                lonawal = place.getLatLng().longitude;

                //make marker berdasarkan koordinat
                LatLng posisi1 = new LatLng(latawal, lonawal);

                mMap.clear();

                if (lokasitujuan.getText().toString().length() != 0) {

                    mMap.clear();

                    mMap.addMarker(new MarkerOptions().position(new LatLng(latakhir, lonakkhir)).title(namalokasiawal));

                }


                //     mMap.addMarker(new MarkerOptions().position(posisi1).title(place.getAddress().toString()));

                //auto zoom
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi1, 15));
                //

                namalokasiawal = place.getAddress().toString();
                lokasiawal.setText(namalokasiawal);


                //get koordinat

                //  Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                //   Log.i(TAG, status.getStatusMessage());
                //  HeroHelper.pesan(MapsActivity.this,status);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else {

            Place place = PlaceAutocomplete.getPlace(this, data);
            latakhir = place.getLatLng().latitude;
            lonakkhir = place.getLatLng().longitude;

            //make marker berdasarkan koordinat
            LatLng posisi1 = new LatLng(latawal, lonawal);

            if (lokasiawal.getText().toString().length() != 0) {

                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(new LatLng(latawal, lonawal)).title(namalokasiawal));

            }


            mMap.addMarker(new MarkerOptions().position(posisi1).title(place.getAddress().toString()));

            //auto zoom
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi1, 15));
            //
            lokasitujuan.setText(place.getAddress().toString());


            actionRoute();


            //get koordinat

            //  Log.i(TAG, "Place: " + place.getName());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void actionRoute() {
        //get koordinat
        String origin = String.valueOf(latawal) + "," + String.valueOf(lonawal);
        String desti = String.valueOf(latakhir) + "," + String.valueOf(lonakkhir);


        LatLngBounds.Builder bound = LatLngBounds.builder();
        bound.include(new LatLng(latawal, lonawal));
        bound.include(new LatLng(latakhir, lonakkhir));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bound.build(), 16));


        ApiService service = InitRetrofit.getInstance2();
        Call<ModelWaypoint> modelWaypointCall =service.getwaypoint(origin,desti);
        modelWaypointCall.enqueue(new Callback<ModelWaypoint>() {
            @Override
            public void onResponse(Call<ModelWaypoint> call, Response<ModelWaypoint> response) {
                List<RoutesItem> routes = response.body().getRoutes();
                List<LegsItem> legsItems = null;
                //    quesList=db.getAllQuestions();
                if (routes != null && routes.size() != 0) {
                    legsItems = routes.get(0).getLegs();
                }
                Distance distance = legsItems.get(0).getDistance();
                Duration duration = legsItems.get(0).getDuration();
                jarak = distance.getText();
                double valuejarak = Double.valueOf(distance.getValue());
                String waktu = duration.getText();
                txtjarak.setText(jarak);
                txtdurasi.setText(waktu);
                //harga
                double harga = Math.ceil(valuejarak / 1000);
                double total = harga * 1000;
                txtharga.setText("Rp." + HeroHelper.toRupiahFormat2(String.valueOf(total)));
                String points = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                DirectionMapsV2 direction = new DirectionMapsV2(MapsActivity.this);
                direction.gambarRoute(mMap, points);
            }

            @Override
            public void onFailure(Call<ModelWaypoint> call, Throwable t) {

            }
        });
    }
}
