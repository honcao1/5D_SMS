package com.example.car_5d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.car_5d.api.Login;
import com.example.car_5d.api.PostApi;
import com.example.car_5d.api.PostModel;
import com.example.car_5d.api.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private static String pre_name = "info_user";

    Button btnLogin;
    EditText edtUser, edtPass;
    TextView tvSignup, tvStartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.editPass);

        tvSignup = findViewById(R.id.tvSignup);
        tvStartTime = findViewById(R.id.tvStartTime);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUser.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();

                if ((!user.isEmpty()) && (!pass.isEmpty())) {
                    System.out.println(TAG + " Login: " + user + "\t" + pass);

                    LoginApi(user, pass);
                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Register();
            }
        });
    }

    private void LoginApi(String user, String pass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.REST_ACCOUNT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostApi postApi = retrofit.create(PostApi.class);

        Login login = new Login(user, pass);

        Call<User> call = postApi.loginUser(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "Callback: " + response.toString());

                if (response.code() == 200) {
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();

                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String username = response.body().getUsername();
                            int user_id = response.body().getId();
                            String token = response.body().getToken();

                            Log.d(TAG, "Callback: " + username + "\t" + token);

                            savePreferences(String.valueOf(user_id), username, token);
                        }
                    }

                    Intent nav = new Intent(MainActivity.this, NavActivity.class);
                    startActivity(nav);
                } else {
                    Toast.makeText(MainActivity.this, "Đăng nhập thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Cacllback err: " + t.toString());
                Toast.makeText(MainActivity.this, "Login that bai", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CreateUserApi(String username, String pass, String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PostApi.REST_ACCOUNT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostApi postApi = retrofit.create(PostApi.class);

        User user = new User(username, pass, email);

        Call<User> call = postApi.CreateUserApi(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println(TAG + " CreateUser: " + response.toString());
                if (response.code() == 201) {
                    Toast.makeText(MainActivity.this, "Tạo tài thành công.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Tạo tài khoản thất bại.", Toast.LENGTH_LONG).show();
                System.out.println(TAG + " CreateUser: " + t.toString());
            }
        });
    }

    private void dialog_Register() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_register);

        final EditText edtRegisterUsername = dialog.findViewById(R.id.edtRegisterUsername);
        final EditText edtRegisterPassword = dialog.findViewById(R.id.edtRegisterPass);
        final EditText edtRegisterEmail = dialog.findViewById(R.id.edtRegisterEmail);

        Button btnRegisterCreate = dialog.findViewById(R.id.btnRegisterCreate);
        Button btnRegisterClose = dialog.findViewById(R.id.btnRegisterHuy);

        btnRegisterCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String RegisterUsername = edtRegisterUsername.getText().toString();
                String RegisterPassword = edtRegisterPassword.getText().toString();
                String RegisterEmail = edtRegisterEmail.getText().toString();

                if ((!RegisterUsername.isEmpty()) && (!RegisterPassword.isEmpty()) && (!RegisterEmail.isEmpty())) {
                    CreateUserApi(RegisterUsername, RegisterPassword, RegisterEmail);

                    dialog.cancel();
                } else {
                    Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRegisterClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void savePreferences(String user_id, String username, String token) {
        SharedPreferences pre = getSharedPreferences(pre_name, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("user_id", user_id);
        editor.putString("username", username);
        editor.putString("token", token);
        editor.commit();
    }

    private String getPreferences() {
        SharedPreferences pre = getSharedPreferences(pre_name, MODE_PRIVATE);

        String username = pre.getString("username", "");

        System.out.println(TAG + " username: " + username);

        return username;
    }

    @Override
    protected void onResume() {
        super.onResume();

        edtUser.setText(getPreferences());
    }
}

