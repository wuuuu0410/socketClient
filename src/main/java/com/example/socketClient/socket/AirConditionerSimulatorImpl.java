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

import com.example.socketClient.repository.AirConditionerRepository;
import com.example.socketClient.vo.AirConditionerVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;




/**
 * AirConditionerSimulatorImpl
 */
@EnableScheduling
@Component
public class AirConditionerSimulatorImpl {
    
    // 進資料庫
    @Autowired
    private AirConditionerRepository airConditionerRepository;

    // 宣告變數
	private Socket client;
	private BufferedOutputStream out;
	private String ip = "26.115.194.94";
	private int port = 80;
	private ObjectMapper mapper = new ObjectMapper();

    // 儲存所有冷氣的狀態：冷氣id vs 冷氣假資料
    Map<Long, AirConditionerVo> acStatusMap = new HashMap<>();

    // 假資料的因數
    private static final double MAX_TEMP_CHANGE = 0.5; // 每次最大溫度變化
    private static final double INERTIA_FACTOR = 0.9; // 慣性因子，越高越不容易改變


    public AirConditionerSimulatorImpl(){
        super();
    }


    // 取得所有的冷氣 id
    @PostConstruct
    public void init(){
        System.out.println("init");
        List<AirConditionerVo> res = airConditionerRepository.findByType();
        for(AirConditionerVo item : res){
            acStatusMap.put(item.getId(), item);
        }
        System.out.println("init END");
    }

    

    // 建立假資料
    @Scheduled(fixedRate = 5000)  // 每5秒更新一次
    public void producebuildData(){
        List<AirConditionerVo> fakeData = new ArrayList<>();
        for( Entry<Long, AirConditionerVo> item : acStatusMap.entrySet()){
            if(!item.getValue().isStatus()){
                continue;
            }
            // 修改假資料的值
            updateTemperature(item.getKey());
            // 預計要傳送的假資料
            fakeData.add(item.getValue());
        }
        if(CollectionUtils.isEmpty(fakeData)){
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
            System.out.println("Start send 冷氣機 data");
			// Step1：連接 server
			client = new Socket(ip, port);
			System.out.println("冷氣機 Connected");

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
    private void updateTemperature(long id) {
        double currentTemp = acStatusMap.get(id).getCurrent_temp();
        double targetTemp = acStatusMap.get(id).getTarget_temp();
        String mode = acStatusMap.get(id).getMode();
        String fanSpeed = acStatusMap.get(id).getFanSpeed();

        // 如果 fanSpeed 為 null，設置一個默認值
        if (fanSpeed == null) {
            fanSpeed = "MEDIUM"; // 或其他適合的默認值
        }

        // 根據模式和風速調整溫度
        double tempChange = calculateTempChange(currentTemp, targetTemp, mode, fanSpeed);

        // 慣性因子和最大變化限制
        tempChange = tempChange * INERTIA_FACTOR;
        tempChange = Math.max(-MAX_TEMP_CHANGE, Math.min(MAX_TEMP_CHANGE, tempChange));

        acStatusMap.get(id).setCurrent_temp(currentTemp + tempChange);
    }

    private double calculateTempChange(double currentTemp, double targetTemp, String mode, String fanSpeed) {
        double baseChange = 0.05; // 基礎溫度變化率
        double fanSpeedMultiplier = getFanSpeedMultiplier(fanSpeed);
        double tempDifference = targetTemp - currentTemp;

        switch (mode) {
            case "COOL":
                return Math.max(-baseChange * fanSpeedMultiplier, tempDifference * 0.1);
            case "HEAT":
                return Math.min(baseChange * fanSpeedMultiplier, tempDifference * 0.1);
            case "AUTO":
                return tempDifference * 0.05; // 自動模式緩慢調整
            case "FAN":
            default:
                return 0; // 風扇模式不改變溫度
        }
    }

    private double getFanSpeedMultiplier(String fanSpeed) {
        switch (fanSpeed) {
            case "LOW":
                return 0.7;
            case "MEDIUM":
                return 1.0;
            case "HIGH":
                return 1.3;
            case "AUTO":
            default:
                return 1.0; // 自動風速假設為中等
        }
    }

}