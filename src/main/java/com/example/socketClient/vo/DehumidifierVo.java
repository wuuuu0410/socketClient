package com.example.socketClient.vo;

import com.example.socketClient.constant.AirConditionerConstants.FanSpeed;

public class DehumidifierVo {
    
    private String type;

    private Long id;

    private Double current_humidity;

    private Double target_humidity;

    private Double tank_capacity;
    
    private String fanSpeed;

    private boolean status;

    public DehumidifierVo() {
    }

    public DehumidifierVo(String type, Long id, Double current_humidity, Double target_humidity, Double tank_capacity,
            FanSpeed fanSpeed, boolean status) {
        this.type = type;
        this.id = id;
        this.current_humidity = current_humidity;
        this.target_humidity = target_humidity;
        this.tank_capacity = tank_capacity;
        this.fanSpeed = fanSpeed.name();
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

    public Double getCurrent_humidity() {
        return current_humidity;
    }

    public void setCurrent_humidity(Double current_humidity) {
        this.current_humidity = current_humidity;
    }

    public Double getTarget_humidity() {
        return target_humidity;
    }

    public void setTarget_humidity(Double target_humidity) {
        this.target_humidity = target_humidity;
    }

    public Double getTank_capacity() {
        return tank_capacity;
    }

    public void setTank_capacity(Double tank_capacity) {
        this.tank_capacity = tank_capacity;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    
}
