package com.example.car_5d.api;

import java.util.List;

public class GetParkModel {
    private Integer id;
    private Integer user_id;

    public GetParkModel(Integer id, Integer user_id, String bienso, List<PostModel> postModels) {
        this.id = id;
        this.user_id = user_id;
        this.bienso = bienso;
        this.postModels = postModels;
    }

    private String bienso;
    private List<PostModel> postModels;

}
