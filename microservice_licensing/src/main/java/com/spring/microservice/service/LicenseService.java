package com.spring.microservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.spring.microservice.config.ServiceConfig;
import com.spring.microservice.model.License;
import com.spring.microservice.model.Organization;
import com.spring.microservice.repository.LicenseRepository;
import com.spring.microservice.service.client.OrganizationDiscoveryClient;
import com.spring.microservice.service.client.OrganizationFeignClient;
import com.spring.microservice.service.client.OrganizationRestTemplateClient;
import com.spring.microservice.utils.UserContextHolder;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@Service
public class LicenseService {
	
	private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Autowired
	ServiceConfig config;
	
	@Autowired
	private OrganizationDiscoveryClient organizationDiscoveryClient;
	
	@Autowired
	private OrganizationRestTemplateClient organizationRestTemplateClinet;
	
	@Autowired
	private OrganizationFeignClient organizationFeignClient;
	
	public License getLicense(String licenseId, String organizationId) {
		logger.debug("LicenseService:getLicense Correlation id: {}",UserContextHolder.getContext().getCorrelationId());
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if(license == null) {
			throw new IllegalArgumentException(
				String.format(
						messageSource.getMessage("license.search.error.message", null, null), 
				licenseId, organizationId)
			);
		}
		
		return license.withComment(config.getProperty());
	}
	
	public License getLicense(String licenseId, String organizationId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if(null == license) {
			throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), licenseId, organizationId));
		}
		Organization organization = retriveOrganizationInfo(organizationId, clientType);
		
		if(null != organization) {
			license.setOrganizationName(organization.getName());
			license.setContactName(organization.getContactName());
			license.setContactEmail(organization.getContactEmail());
			license.setContactPhone(organization.getContactPhone());
		}
		return license.withComment(config.getProperty());
	}
	
	private Organization retriveOrganizationInfo(String organizationId, String clientType) {
		// TODO Auto-generated method stub
		Organization organization = null;
		
		switch(clientType) {
		case "discovery":
			System.out.println("Dicovery client called!");
			organization = organizationDiscoveryClient.getOrganization(organizationId);
			break;
		case "rest":
			System.out.println("RestTemplate client called!");
			organization = organizationRestTemplateClinet.getOrganization(organizationId);
			break;
		case "feign":
			System.out.println("Feign client called!");
			organization = organizationFeignClient.getOrganization(organizationId);
			break;
		default:
			organization = getOrganization(organizationId);
			break;
		}
		return organization;
	}

	public License createLicense(License license) {
		license.setLicenseId(UUID.randomUUID().toString());
		licenseRepository.save(license);
		return license.withComment(config.getProperty());
	}
	
	public License updateLicense(License license) {
		licenseRepository.save(license);
		return license.withComment(config.getProperty());
	}
	
	public String deleteLicense(String licenseId, String organizationId) {
		String responseMessage = null;
		License license = new License();
		license.setLicenseId(licenseId);
		licenseRepository.delete(license);
		responseMessage= String.format(messageSource.getMessage("license.delete.message", null, null), licenseId);
		return responseMessage;
	}
	
	
	@CircuitBreaker(name="licenseService", fallbackMethod = "buildFallbackLicenseList")
	@Retry(name = "retryLicenseService")
	@RateLimiter(name = "ratelimiterLicenseService")
	@Bulkhead(name = "bulkheadLicenseService")
	public List<License> getLicenseByOrganization(String organizationId) throws TimeoutException{
		logger.debug("LicenseService:getLicensesByOrganization Correlation id: {}",UserContextHolder.getContext().getCorrelationId());
		randomlyRunLong();
		return licenseRepository.findByOrganizationId(organizationId);
	}
	
	// 회복성 패턴의 순서 Retry ( CircuitBreaker ( RateLimiter ( TimeLimiter ( Bulkhead ( Function ) ) ) ) )
	// bulkhead가 스레드 풀 생성 -> timelimiter가 스레드 시간 제한 -> ratelimiter가 해당 함수의 호출 횟수 제한 -> timelimiter & ratelimiter가 발생시킨 Exception을 circuitbreaker에 기록 및 회로 차단 -> 그 후 retry로 회복 시도
	// bulkhead(threadpool 방식을 사용할 경우), timelimiter는 CompletableFuture 로 감싸줘야함
//	@CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
//	@RateLimiter(name = "ratelimiterLicenseService")
//	@Retry(name = "retryLicenseService")
//	@Bulkhead(name = "bulkheadLicenseService", type = Type.THREADPOOL)
////	@TimeLimiter(name="")
//	public CompletableFuture<List<License>> getLicenseByOrganization(String organizationId) throws TimeoutException{
//		randomlyRunLong();
//		return  CompletableFuture.completedFuture(licenseRepository.findByOrganizationId(organizationId));
//	}
	
	@CircuitBreaker(name="organizationService")
	private Organization getOrganization(String organizationId) {
		return organizationRestTemplateClinet.getOrganization(organizationId);
	}
	
//	@SuppressWarnings("unused")
//	private CompletableFuture<List<License>> buildFallbackLicenseList(String organizationId, Throwable t){
//		List<License> fallbackList = new ArrayList<>();
//		License license = new License();
//		license.setLicenseId("00000000-000-00000");
//		license.setOrganizationId(organizationId);
//		license.setProductName("Sorry. No licensing information currently available");
//		fallbackList.add(license);
//		return  CompletableFuture.completedFuture(fallbackList);
//	}
	
	@SuppressWarnings("unused")
	private List<License> buildFallbackLicenseList(String organizationId, Throwable t){
		List<License> fallbackList = new ArrayList<>();
		License license = new License();
		license.setLicenseId("00000000-000-00000");
		license.setOrganizationId(organizationId);
		license.setProductName("Sorry. No licensing information currently available");
		fallbackList.add(license);
		return  fallbackList;
	}
	
	//circuitBreaker 테스트 용
	private void randomlyRunLong() throws TimeoutException {
		Random rand = new Random();
		int randomNum = rand.nextInt(3) + 1;
		if (randomNum == 3) 
			sleep();
//		throw new java.util.concurrent.TimeoutException();
	}

	private void sleep() throws TimeoutException {
		try {
			Thread.sleep(5000);
			throw new java.util.concurrent.TimeoutException();
		}catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}
}
