package com.example.socketClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.socketClient.entity.AirConditioner;
import com.example.socketClient.vo.AirConditionerVo;


public interface AirConditionerRepository extends JpaRepository<AirConditioner, Long> {

    AirConditioner findByDeviceId(Long id);

    @Query(value="SELECT new com.example.socketClient.vo.AirConditionerVo(d.type, d.id, ac.current_temp, ac.target_temp, ac.mode, ac.fanSpeed, d.status) FROM Device as d JOIN AirConditioner as ac ON d.id = ac.id where d.type='冷氣機'")
    List<AirConditionerVo> findByType();
}