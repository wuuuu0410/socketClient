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
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.socketClient.repository.DehumidifierRepository;
import com.example.socketClient.vo.DehumidifierVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@EnableScheduling
@Component
public class DehumidifierSimulatorImpl {

    // 進資料庫
    @Autowired
    private DehumidifierRepository dehumidifierRepository;

    // 宣告變數
    private Socket client;
    private BufferedOutputStream out;
    private String ip = "26.115.194.94";
    private int port = 82;
    private ObjectMapper mapper = new ObjectMapper();
    private Random random = new Random();

    // 儲存所有除濕機的狀態：
    Map<Long, DehumidifierVo> deStatusMap = new HashMap<>();

    // 假資料的因數
    private static final double HUMIDITY_INERTIA_FACTOR = 0.9; // 慣性因子，越高越不容易改變
    private static final double MAX_HUMIDITY_CHANGE = 0.5; // 最大變化
    private static final double DEFAULT_HUMIDITY = 80.0; // 預設濕度
    private static final double MAX_HUMIDITY = 90.0; // 最大濕度

    public DehumidifierSimulatorImpl() {
        super();
    }

    // 取得ID
    @PostConstruct
    public void init() {
        List<DehumidifierVo> res = dehumidifierRepository.findByType();
        for (DehumidifierVo item : res) {
            deStatusMap.put(item.getId(), item);
        }
    }

    // 建立假資料
    // @Scheduled(fixedRate = 5000) // 每5秒更新一次
    public void produceBuildData() {
        List<DehumidifierVo> fakeData = new ArrayList<>();
        for (Entry<Long, DehumidifierVo> item : deStatusMap.entrySet()) {
            if (!item.getValue().isStatus()) {
                continue;
            }
            // 修改假資料的值
            updateCurrentHumidity(item.getKey());
            updateTankCapacity(item.getKey());
            // 預計要傳送的假資料
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
            System.out.println("Start send 除濕機 data");
            // Step1：連接 server
            client = new Socket(ip, port);
            System.out.println("除濕機 Connected");

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
    private void updateCurrentHumidity(Long id) {
        double currentHumidity = deStatusMap.get(id).getCurrent_humidity();
        double targetHumidity = deStatusMap.get(id).getTarget_humidity();

        // 計算濕度差
        double humidityDifference = targetHumidity - currentHumidity;
        // 基礎降低速率
        double baseReduction = 0.1 + random.nextDouble() * 0.4;
        // 應用慣性因子
        double reduction = baseReduction * HUMIDITY_INERTIA_FACTOR + Math.abs(humidityDifference) * 0.01;
        // 限制最大變化
        reduction = Math.min(reduction, MAX_HUMIDITY_CHANGE);

        if (currentHumidity > targetHumidity) {
            deStatusMap.get(id).setCurrent_humidity(Math.max(targetHumidity, currentHumidity - reduction));
        }
    }

    private void updateTankCapacity(Long id) {
        // 假設每5秒，水箱容量增加0.01-0.05升
        double increase = 0.01 + random.nextDouble() * 0.04;
        double newCapacity = deStatusMap.get(id).getTank_capacity() + increase;
        // 假設最大水箱容量為5升
        deStatusMap.get(id).setTank_capacity(Math.min(5.0, newCapacity));
    }
}
