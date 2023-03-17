package com.spring.gatewayserver.filter;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Order(1)
@Component
public class TrackingFilter implements GlobalFilter{
	private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);
	
	@Autowired
	FilterUtils filterUtils;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
		if(isCorrelationIdPresent(requestHeaders)) {
			logger.debug("tmx-correlation_id found in tracking filter: {}", filterUtils.getCorrelationId(requestHeaders));
		}else {
			String correlationID = generateCorrelationId();
			exchange = filterUtils.setCorrelationID(exchange, correlationID);
			logger.debug("tmx-correlation-id generated in tracking filter: {}", correlationID);
		}
		
		return chain.filter(exchange);
	}

	private String generateCorrelationId() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString();
	}

	private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
		// TODO Auto-generated method stub
		if(filterUtils.getCorrelationId(requestHeaders) != null) 
			return true;
		else
			return false;
	}

}
