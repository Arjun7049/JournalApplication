package com.journal.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {
	
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Test
	void redisTest() {
		redisTemplate.opsForValue().set("email", "arjun@gmail.com");
		Object email = redisTemplate.opsForValue().get("salary");
		int a=1;
	}

}
