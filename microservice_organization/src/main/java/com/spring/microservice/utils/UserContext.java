package com.spring.microservice.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class UserContext {
	public static final String CORRELATION_ID	=		"tmx-correlation-id";
	public static final String AUTH_TOKEN	 	=		"Authorization";
	public static final String USER_ID		 	=		"tmx-user-id";
	public static final String ORG_ID			=		"tmx-org-id";
	
	private static final ThreadLocal<String> correlationId = new ThreadLocal<String>();
	private static final ThreadLocal<String> authToken =  new ThreadLocal<String>();
	private static final ThreadLocal<String> userId	= new ThreadLocal<String>();
	private static final ThreadLocal<String> orgId = new ThreadLocal<String>();
	
	public static String getCorrelationId() {
		return correlationId.get();
	}
	public static void setCorrelationId(String cId) {
		correlationId.set(cId);
	}
	public static String getAuthToken() {
		return authToken.get();
	}
	public static void setAuthToken(String aToken) {
		authToken.set(aToken);
	}
	public static String getUserid() {
		return userId.get();
	}
	public static void setUserId(String uId) {
		userId.set(uId);
	}
	public static String getOrgid() {
		return orgId.get();
	}
	public static void setOrgId(String oId) {
		orgId.set(oId);
	}
	public static HttpHeaders getHttpHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CORRELATION_ID, getCorrelationId());
		
		return httpHeaders;
	}
}
