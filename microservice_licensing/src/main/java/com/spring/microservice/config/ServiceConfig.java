package com.spring.microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "example")
public class ServiceConfig {
	private String property;
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	
	@Value("${redis.server}")
	private String redisServer = "";
	
	@Value("${redis.port}")
	private String redisPort = "";
	
	public String getRedisServer() {
		return redisServer;
	}

	public String getRedisPort() {
		return redisPort;
	}
	
	
}
