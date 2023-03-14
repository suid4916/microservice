package com.spring.microservice.service;

import java.util.UUID;

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

@Service
public class LicenseService {
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
			organization = organizationRestTemplateClinet.getOrganization(organizationId);
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
}
