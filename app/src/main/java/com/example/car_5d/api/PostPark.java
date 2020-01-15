package com.example.car_5d.api;

public class PostPark {
    private Integer quesion;
    private String tenduong;
    private Boolean status;
    private String timestart;
    private String timeend;

    public PostPark(Integer quesion, String tenduong, Boolean status, String timestart, String timeend) {
        this.quesion = quesion;
        this.tenduong = tenduong;
        this.status = status;
        this.timestart = timestart;
        this.timeend = timeend;
    }
}
