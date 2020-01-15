package com.example.car_5d;


import android.Manifest;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.car_5d.api.GetModel;
import com.example.car_5d.api.PostApi;
import com.example.car_5d.api.PostPark;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.DoubleDateAndTimePickerDialog;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private static String TAG = "MapActivity";
    private static String pre_name = "info_user";

    long time1, time2;

    private String start, end;

    @Override
    public void onMapReady(GoogleMap mMap) {
        Toast.makeText(getActivity(), "Map is Ready", Toast.LENGTH_SHORT).show();
        googleMap = mMap;

        if (mLocationPermission) {
            getDevicesLocation();

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

//            init();
        }
    }

    View root;

    MapView mapView;
    private GoogleMap googleMap;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final float DEFAULT_ZOOM = 15f;

    private TextView tvLocation, tvStartTime, tvEndTime, tvTime;
    Spinner mSpinnerList;
    Button btnMua;

    private Boolean mLocationPermission = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_location, container, false);

        LinearLayout layoutTime = root.findViewById(R.id.layoutTime);

        tvLocation = root.findViewById(R.id.tvLocation);
        tvStartTime = root.findViewById(R.id.tvStartTime);
        tvEndTime = root.findViewById(R.id.tvEndTime);
        tvTime = root.findViewById(R.id.tvTime);

        btnMua = root.findViewById(R.id.btnMua);

        mSpinnerList = root.findViewById(R.id.spinnerList);
        SpinnerListXe(mSpinnerList, 0);

        mapView = root.findViewById(R.id.TestMap);
        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLocationPermission();

        tvStartTime.setOnClickListener(ClickStartTime);
        tvEndTime.setOnClickListener(ClickEndTime);

        btnMua.setOnClickListener(ClickButtonMua);

        return root;
    }

    private TextView.OnClickListener ClickStartTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SingleDateAndTimePickerDialog.Builder(getContext())
                    .minutesStep(30)
                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                        @Override
                        public void onDisplayed(SingleDateAndTimePicker picker) {

                        }
                    })
                    .title("Selected Start Time")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            tvStartTime.setText("Start Time\nToday\n"+ simpleDateFormat.format(date));

                            start = simpleDateFormat.format(date);
                            time1 = date.getTime();
                            System.out.println(date.getTime());

                            if (time2 - time1 > 0) {
                                long time = (time2-time1)/(1000*60);
                                tvTime.setText(time+"m");
                            }
                        }
                    }).display();
        }
    };

    private TextView.OnClickListener ClickEndTime = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SingleDateAndTimePickerDialog.Builder(getContext())
                    .minutesStep(30)
                    .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                        @Override
                        public void onDisplayed(SingleDateAndTimePicker picker) {

                        }
                    })
                    .title("Selected End Time")
                    .listener(new SingleDateAndTimePickerDialog.Listener() {
                        @Override
                        public void onDateSelected(Date date) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            tvEndTime.setText("End Time\n Today\n "+ simpleDateFormat.format(date));

                            end = simpleDateFormat.format(date);
                            time2 = date.getTime();
                            System.out.println(date.getTime());

                            if (time2 - time1 > 0) {
                                long time = (time2-time1)/(1000*60);
                                tvTime.setText(time+"m");
                            }
                        }
                    }).display();
        }
    };

    private Button.OnClickListener ClickButtonMua = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer quesion = Integer.valueOf(getPreferencesBienso(mSpinnerList.getSelectedItemId()));
            String tenduong = tvLocation.getText().toString().trim();

            Log.d(TAG, "ClickButtonMua: "+quesion);
            Log.d(TAG, "ClickButtonMua: "+start);
            Log.d(TAG, "ClickButtonMua: "+end);
            Log.d(TAG, "ClickButtonMua: "+tenduong);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(PostApi.My_Post)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PostPark postPark = new PostPark(quesion, tenduong, false, start, end);

            PostApi postApi = retrofit.create(PostApi.class);
            Call<PostPark> call = postApi.postParkAPI(postPark);
            call.enqueue(new Callback<PostPark>() {
                @Override
                public void onResponse(Call<PostPark> call, Response<PostPark> response) {
                    Log.d(TAG, "Callback: "+response.toString());
                    if (response.code()==201) {
                        Toast.makeText(getActivity(), "Đăng kí chỗ thành công.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PostPark> call, Throwable t) {
                    Log.e(TAG, "Callback err: "+t.toString());
                    Toast.makeText(getActivity(), "Đăng kí chỗ thất bại.", Toast.LENGTH_SHORT).show();
                }
            });

        }
    };

    private void SpinnerListXe(Spinner spinner, int selectedIdx) {
        final ArrayAdapter<String> dataset = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>());
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

                        String spiner_id = "0,";
                        String spiner_bienso = "0,";

                        for (GetModel h: getModelList) {
                            Integer id      = h.getId();
                            String bienso   = h.getBienso();
                            String mauxe    = h.getMauxe();
                            String loaixe   = h.getLoaixe();

                            Log.d(TAG, "Callback: "+ id+"\t"+"\t"+bienso+"\t"+mauxe+"\t"+loaixe);

                            dataset.add(bienso);

                            spiner_id += id+",";
                            spiner_bienso += bienso+",";
                        }
                        savePreferencesBienso(spiner_id, spiner_bienso);
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

        Geocoder geocoder = new Geocoder(getActivity().getApplicationContext());
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

            tvLocation.setText(address.getThoroughfare());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private void getDevicesLocation() {
        Log.d(TAG, "getDevicesLocation");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);

            googleMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initMapView() {
        mapView.getMapAsync(LocationFragment.this);
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermission = true;
                initMapView();
            } else {
                ActivityCompat.requestPermissions(getActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void savePreferencesBienso(String id, String biendo) {
        SharedPreferences pre = getActivity().getSharedPreferences("spiner", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("id", id);
        editor.putString("bienso", biendo);
        editor.commit();
    }

    private String getPreferencesBienso(long postion) {
        SharedPreferences pre = getActivity().getSharedPreferences("spiner", getActivity().MODE_PRIVATE);

        String[] id = pre.getString("id", "").split(",");

        return id[(int) (postion+1)];
    }

    private String getPreferences() {
        SharedPreferences pre = getActivity().getSharedPreferences(pre_name, getActivity().MODE_PRIVATE);

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
                    initMapView();
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
