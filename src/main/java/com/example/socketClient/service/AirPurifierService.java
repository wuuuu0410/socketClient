package com.example.socketClient.service;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

import com.example.socketClient.entity.AirPurifier;

public interface AirPurifierService {

    List<AirPurifier> getAllAirPurifiers();

    AirPurifier getAirPurifierById(Long id);

    ResponseEntity<?> saveAirPurifier(AirPurifier airPurifier);

    ResponseEntity<?> patchAirPurifier(Long id, Map<String, Object> updates);

    ResponseEntity<?> batchPatchAirPurifiers(List<Map<String, Object>> updates);

    void deleteAirPurifier(Long id);
}
