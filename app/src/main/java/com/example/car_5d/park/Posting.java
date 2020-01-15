package com.example.car_5d.park;

import java.util.ArrayList;
import java.util.List;

public class Posting {
    private Integer user;
    private String bienso;
    private List<Park> parks;

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getBienso() {
        return bienso;
    }

    public void setBienso(String bienso) {
        this.bienso = bienso;
    }

    public List<Park> getParks() {
        return parks;
    }

    public void setParks(List<Park> parks) {
        this.parks = parks;
    }
}
