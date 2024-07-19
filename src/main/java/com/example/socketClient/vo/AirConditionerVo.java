package com.example.socketClient.vo;

import com.example.socketClient.constant.AirConditionerConstants.FanSpeed;
import com.example.socketClient.constant.AirConditionerConstants.Mode;

public class AirConditionerVo {

    // 用於判斷傳入的資料是哪種機器
    private String type;

    private Long id;

    private Double current_temp;

    private Double target_temp;
    
    private String mode;

    private String fanSpeed;

    private boolean status;

    public AirConditionerVo() {
    }

    public AirConditionerVo(String type, Long id, Double current_temp, Double target_temp, Mode mode, FanSpeed fanSpeed,
            boolean status) {
        this.type = type;
        this.id = id;
        this.current_temp = current_temp;
        this.target_temp = target_temp;
        this.mode = mode.name();
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

    public Double getCurrent_temp() {
        return current_temp;
    }

    public void setCurrent_temp(Double current_temp) {
        this.current_temp = current_temp;
    }

    public Double getTarget_temp() {
        return target_temp;
    }

    public void setTarget_temp(Double target_temp) {
        this.target_temp = target_temp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
