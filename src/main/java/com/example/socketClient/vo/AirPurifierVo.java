package com.example.socketClient.vo;

public class AirPurifierVo {

    private String type;

    private Long id;

    private int air_quality;

    private int fan_speed;

    private Double operating_time;

    private boolean status;

    public AirPurifierVo() {
    }

    public AirPurifierVo(String type, Long id, int air_quality, int fan_speed, Double operating_time,
            boolean status) {
        this.type = type;
        this.id = id;
        this.air_quality = air_quality;
        this.fan_speed = fan_speed;
        this.operating_time = operating_time;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAir_quality() {
        return air_quality;
    }

    public void setAir_quality(int air_quality) {
        this.air_quality = air_quality;
    }

    public int getFan_speed() {
        return fan_speed;
    }

    public void setFan_speed(int fan_speed) {
        this.fan_speed = fan_speed;
    }

    public Double getOperating_time() {
        return operating_time;
    }

    public void setOperating_time(Double operating_time) {
        this.operating_time = operating_time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
