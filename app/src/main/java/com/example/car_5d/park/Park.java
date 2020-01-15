package com.example.car_5d.park;

public class Park {
    private Integer id;
    private String bienso;
    private String tenduong;
    private Boolean status;
    private String timestart;
    private String datestamp;
    private String timeend;

    public Park(String bienso, String tenduong, String timestart, String datestamp, String timeend) {
        this.bienso = bienso;
        this.tenduong = tenduong;
        this.timestart = timestart;
        this.datestamp = datestamp;
        this.timeend = timeend;
    }

    public String getBienso() {
        return bienso;
    }

    public void setBienso(String bienso) {
        this.bienso = bienso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenduong() {
        return tenduong;
    }

    public void setTenduong(String tenduong) {
        this.tenduong = tenduong;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public String getDatestamp() {
        return datestamp;
    }

    public void setDatestamp(String datestamp) {
        this.datestamp = datestamp;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }
}
