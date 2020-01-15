package com.example.car_5d;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.car_5d.api.PostApi;
import com.example.car_5d.api.PostModel;
import com.example.car_5d.profile.Profile;
import com.example.car_5d.profile.ProfileAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    ListView profileList, inforList;
    ArrayList<Profile> arrayList, arrayInfor;
    ProfileAdapter adapter, adapterInfor;

    View view;

    private static String TAG = "MainActivity";
    private static String pre_name = "info_user";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_profile, container, false);

        profileList = view.findViewById(R.id.profileListView);
        arrayList = new ArrayList<>();

        arrayList.add(new Profile("Thêm xe", R.drawable.ic_car));
        arrayList.add(new Profile("Thanh toán", R.drawable.ic_credit_card));
        arrayList.add(new Profile("Sign Out", R.drawable.ic_signout));

        adapter = new ProfileAdapter(getContext(), R.layout.custom_profile, arrayList);
        profileList.setAdapter(adapter);

        profileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        dialog_addXe();
                        break;

                    case 2:
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        return view;
    }

    private void dialog_addXe() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add);

        final EditText edtAddBienso    = dialog.findViewById(R.id.edtAddBienSo);
        final EditText edtAddMauxe     = dialog.findViewById(R.id.edtAddMauXe);
        final EditText edtAddLoaixe    = dialog.findViewById(R.id.edtAddLoaiXe);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnHuy = dialog.findViewById(R.id.btnHuy);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bienso = edtAddBienso.getText().toString().trim();
                final String mauxe  = edtAddMauxe.getText().toString().trim();
                final String loaixe = edtAddLoaixe.getText().toString().trim();

                if ((!bienso.isEmpty()) && (!mauxe.isEmpty()) && (!loaixe.isEmpty())) {
                    System.out.println(bienso+"\t"+mauxe+"\t"+loaixe);

                    PostApiXe(bienso, mauxe, loaixe);

                    dialog.cancel();
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void PostApiXe(String bienso, String mauxe, String loaixe) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.My_Post)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Integer user = Integer.valueOf(getPreferences());

        PostApi postApi = retrofit.create(PostApi.class);

        PostModel postModel = new PostModel(user, bienso, mauxe, loaixe);

        Call<PostModel> call = postApi.addPost(postModel);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                Toast.makeText(getActivity(), "Dang ki thanh cong", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Callback: " + response.toString());
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Log.e(TAG, "Callback err: " + t.getMessage());
            }
        });
    }

    private String getPreferences() {
        SharedPreferences pre = getActivity().getSharedPreferences(pre_name, getActivity().MODE_PRIVATE);

        String user_id = pre.getString("user_id", "");

        System.out.println(TAG + " user_id: " + user_id);

        return user_id;
    }
}
