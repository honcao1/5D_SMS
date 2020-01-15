package com.example.car_5d.api;

import com.example.car_5d.park.Park;
import com.example.car_5d.park.Posting;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PostApi {
//    String root = "http://192.168.0.133:8000/";
    String root = "https://rest-apiandroid.herokuapp.com/";
//    String root = "http://192.168.1.124:8000/";

    String REST_ACCOUNT = root + "api/restaccount/";
    String My_Post      = root + "api/mypost/";

    @GET("{id}/")
    Call<List<GetModel>> getPost(@Path(value = "id", encoded = true) String id);

    @POST("create/")
    Call<PostModel> addPost(@Body PostModel postModel);

    @POST("api-token-auth/")
    Call<User> loginUser(@Body Login login);

    @POST("register/")
    Call<User> CreateUserApi(@Body User user);

    @POST("parkcreate/")
    Call<PostPark> postParkAPI(@Body PostPark postPark);

    @GET("{id}/")
    Call<List<Posting>> getPark(@Path(value = "id", encoded = true) String id);
}
