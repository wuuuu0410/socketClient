package com.example.socketClient;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.socketClient.repository.AirConditionerRepository;
import com.example.socketClient.vo.AirConditionerVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class SocketClientApplicationTests {

	@Autowired
    private AirConditionerRepository AirConditionerRepository;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	void contextLoads() {
	}

	@Test
	void daoTest() throws JsonProcessingException{
		List<AirConditionerVo> res = AirConditionerRepository.findByType();
		System.out.println(mapper.writeValueAsString(res));
	}

}
