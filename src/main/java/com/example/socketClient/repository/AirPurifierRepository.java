package com.example.socketClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.socketClient.entity.AirPurifier;
import com.example.socketClient.vo.AirPurifierVo;

public interface AirPurifierRepository extends JpaRepository<AirPurifier, Long>{

    AirPurifier findByDeviceId(Long id);

    @Query(value="SELECT new com.example.socketClient.vo.AirPurifierVo(d.type, d.id, ap.air_quality, ap.fan_speed,  ap.operating_time, d.status) FROM Device as d JOIN AirPurifier as ap ON d.id = ap.id where d.type='空氣清淨機'")
    List<AirPurifierVo> findByType();


}
