package com.example.socketClient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.socketClient.entity.Dehumidifier;
import com.example.socketClient.vo.DehumidifierVo;

public interface DehumidifierRepository extends JpaRepository<Dehumidifier, Long> {

    Dehumidifier findByDeviceId(Long id);

    @Query(value = "SELECT new com.example.socketClient.vo.DehumidifierVo(d.type, d.id, de.current_humidity, de.target_humidity,  de.tank_capacity, de.fanSpeed, d.status) FROM Device as d JOIN Dehumidifier as de ON d.id = de.id where d.type='除濕機'")
    List<DehumidifierVo> findByType();

}
