package com.example.car_5d.api;

public class GetModel {
    private Integer id;
    private Integer user_id;
    private String bienso;
    private String mauxe;
    private String loaixe;

    public GetModel(Integer id, String bienso, String mauxe, String loaixe) {
        this.id = id;
        this.bienso = bienso;
        this.mauxe = mauxe;
        this.loaixe = loaixe;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getBienso() {
        return bienso;
    }

    public String getMauxe() {
        return mauxe;
    }

    public String getLoaixe() {
        return loaixe;
    }
}
