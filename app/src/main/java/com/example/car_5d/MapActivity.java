package com.example.car_5d;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.car_5d.api.GetModel;
import com.example.car_5d.api.PostApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String TAG = "MapActivity";
    private static String pre_name = "info_user";

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermission) {
            getDevicesLocation();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final float DEFAULT_ZOOM = 15f;

    private EditText edtSearch, edtTenDuong;
    private ImageView mGps;
    Spinner mSpinnerTime, mSpinnerXe;

    private Boolean mLocationPermission = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        edtTenDuong = findViewById(R.id.edtTenDuong);
        edtSearch = findViewById(R.id.edtSearch);
        mGps = findViewById(R.id.ic_gps);

        mSpinnerTime = findViewById(R.id.spinnerTime);
        SpinnerTime(mSpinnerTime, 0);

        mSpinnerXe = findViewById(R.id.spinnerXe);
        SpinnerListXe(mSpinnerXe, 0);

        getLocationPermission();

        init();
    }

    private void init() {
        Log.d(TAG, "init: initalizing");

        /*
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH
                            || actionId== EditorInfo.IME_ACTION_DONE
                            || event.getAction() == KeyEvent.ACTION_DOWN
                            || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }

                return false;
            }
        });
        */

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked gps");
                getDevicesLocation();
            }
        });

        hideSoftKeyboard();
    }

    private void SpinnerTime(Spinner spinner, int selectedIdx) {
        ArrayAdapter<String> dataset = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        dataset.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dataset.add("30m");
        dataset.add("60m");
        dataset.add("90m");
        dataset.add("120m");

        spinner.setAdapter(dataset);
        spinner.setSelection(selectedIdx);
    }

    private void SpinnerListXe(Spinner spinner, int selectedIdx) {
        final ArrayAdapter<String> dataset = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());
        dataset.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.My_Post)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String user_id = getPreferences();

        PostApi postApi = retrofit.create(PostApi.class);

        Call<List<GetModel>> call = postApi.getPost(user_id);

        call.enqueue(new Callback<List<GetModel>>() {
            @Override
            public void onResponse(Call<List<GetModel>> call, Response<List<GetModel>> response) {
                Log.d(TAG, "Callback: "+ response.toString());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<GetModel> getModelList = response.body();

                        for (GetModel h: getModelList) {
                            Integer id      = h.getId();
                            String bienso   = h.getBienso();
                            String mauxe    = h.getMauxe();
                            String loaixe   = h.getLoaixe();

                            Log.d(TAG, "Callback: "+ id+"\t"+"\t"+bienso+"\n"+mauxe+"\n"+loaixe);

                            dataset.add(bienso);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetModel>> call, Throwable t) {
                Log.e(TAG, "Callback Err: "+ t.getMessage());
            }
        });

        spinner.setAdapter(dataset);
        spinner.setSelection(selectedIdx);
    }

    private void geoLocate(double lat, double lng) {
        Log.d(TAG, "geoLocate: geolocating");

//        String searchString = edtSearch.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try {
//            list = geocoder.getFromLocationName(searchString, 1);
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOException"+e.getMessage());
        }

        if (list.size()>0) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: "+address.toString());

            edtTenDuong.setText(address.getThoroughfare());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private void getDevicesLocation() {
        Log.d(TAG, "getDevicesLocation");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermission) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onCompleteListener: found location");
                            Location currentLocation = (Location) task.getResult();

                            geoLocate(currentLocation.getLatitude(), currentLocation.getLongitude());
//                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        } else {
                            Log.d(TAG, "onCompleteListener: current location is null");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDevicesLocation: SecurityException" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: moving the camera to lat: "+ latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermission = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private String getPreferences() {
        SharedPreferences pre = getSharedPreferences(pre_name, MODE_PRIVATE);

        String user_id     = pre.getString("user_id", "");

        System.out.println(TAG+" user_id: "+user_id);

        return user_id;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermission = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length>0) {
                    for (int i=0; i<grantResults.length;i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermission = false;
                            return;
                        }
                    }
                    mLocationPermission = true;
                    initMap();
                }
        }
    }
    
}
