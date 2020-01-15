package com.example.car_5d;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.car_5d.api.GetModel;
import com.example.car_5d.api.PostApi;
import com.example.car_5d.park.Park;
import com.example.car_5d.park.ParkAdapter;
import com.example.car_5d.park.Posting;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParkFragment extends Fragment {
    private static String TAG = "ParkFragment";
    private static String pre_name = "info_user";

    View view;

    ListView parkListview;
    ArrayList<Park> arraypark;
    ParkAdapter parkAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_park, container, false);

        parkListview = view.findViewById(R.id.parkListview);
        arraypark = new ArrayList<>();

        ListParkUser();

        return view;
    }

    private String getPreferences() {
        SharedPreferences pre = getActivity().getSharedPreferences(pre_name, getActivity().MODE_PRIVATE);
        String user_id     = pre.getString("user_id", "");
        System.out.println(TAG+" user_id: "+user_id);
        return user_id;
    }

    private void ListParkUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.My_Post)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String user_id = getPreferences();

        PostApi postApi = retrofit.create(PostApi.class);

        Call<List<Posting>> call = postApi.getPark(user_id);
        call.enqueue(new Callback<List<Posting>>() {
            @Override
            public void onResponse(Call<List<Posting>> call, Response<List<Posting>> response) {
                Toast.makeText(getContext(), "GET ok!", Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    if (response.body()!=null) {
                        List<Posting> postings = response.body();

                        for (Posting h:postings) {
                            Log.d(TAG, h.getBienso());

                            List<Park> parks = h.getParks();
                            for (Park p: parks) {
                                Log.d(TAG, p.getId()+"\t"+p.getTenduong());
                                arraypark.add(new Park(h.getBienso(), p.getTenduong(), p.getTimestart(), p.getDatestamp(), p.getTimeend()));
                            }
                        }
                        parkAdapter = new ParkAdapter(getActivity(), R.layout.custom_listview, arraypark);
                        parkListview.setAdapter(parkAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Posting>> call, Throwable t) {

            }
        });
    }
}
