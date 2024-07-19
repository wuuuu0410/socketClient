package com.example.socketClient.socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.socketClient.repository.AirPurifierRepository;
import com.example.socketClient.vo.AirPurifierVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@EnableScheduling
@Component
public class AirPurifierSimulatorImpl {

    @Autowired
    private AirPurifierRepository airPurifierRepository;

    private Socket client;
    private BufferedOutputStream out;
    private String ip = "26.115.194.94";
    private int port = 81;
    private ObjectMapper mapper = new ObjectMapper();

    // 儲存空氣清淨機的狀態
    Map<Long, AirPurifierVo> apStatusMap = new HashMap<>();

    // 假資料的因數
    // private static final double AIR_QUALITY_INERTIA_FACTOR = 0.9; //
    // 慣性因子，越高越不容易改變
    // private static final int MAX_AIR_QUALITY_CHANGE = 5; // 最大變化
    // private static final int BASE_WORST_AIR_QUALITY = 200; // 最差空氣品質
    // private static final int WORST_AIR_QUALITY_RANGE = 20; // 最差空氣品質範圍

    public AirPurifierSimulatorImpl() {
        super();
    }

    @PostConstruct
    public void init() {
        List<AirPurifierVo> res = airPurifierRepository.findByType();
        for (AirPurifierVo item : res) {
            apStatusMap.put(item.getId(), item);
        }
    }

    // @Scheduled(fixedRate = 5000) // 每5秒更新一次
    public void produceBuildData() {
        List<AirPurifierVo> fakeData = new ArrayList<>();
        for (Entry<Long, AirPurifierVo> item : apStatusMap.entrySet()) {
            if (!item.getValue().isStatus()) {
                continue;
            }
            updateAirQuality(item.getKey());
            updateOperatingTime(item.getKey());
            fakeData.add(item.getValue());
        }
        if (CollectionUtils.isEmpty(fakeData)) {
            return;
        }
        try {
            startClient(mapper.writeValueAsString(fakeData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("JSON ERROR");
        }
    }

    // 傳送假資料
    public void startClient(String dataStr) {
        try {
            System.out.println("Start send 空氣清淨機 data");
            // Step1：連接 server
            client = new Socket(ip, port);
            System.out.println("空氣清淨機 Connected");

            // Step2：輸出資料
            out = new BufferedOutputStream(client.getOutputStream());
            out.write(dataStr.getBytes());

            // Step3：關閉 socket
            out.flush();
            out.close();
            out = null;
            client.close();
            client = null;

        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    // 底下為變換狀況
    private void updateAirQuality(long id) {
        int currentAirQuality = apStatusMap.get(id).getAir_quality();
        int fanSpeed = apStatusMap.get(id).getFan_speed();

        // 根據風速改善空氣品質
        // 假設風速越高，空氣品質改善越快
        int improvement = (int) (fanSpeed * 0.1); // 0-10 的改善
        apStatusMap.get(id).setAir_quality(Math.max(0, currentAirQuality - improvement));
    }

    private void updateOperatingTime(long id) {
        // 增加運行時間，假設每次更新增加5秒（因為更新間隔是5秒）
        apStatusMap.get(id).setOperating_time(apStatusMap.get(id).getOperating_time() + 5.0 / 3600); // 轉換為小時
    }
}
