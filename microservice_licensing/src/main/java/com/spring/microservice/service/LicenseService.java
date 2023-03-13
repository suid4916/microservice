package com.spring.microservice.service;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.spring.microservice.config.ServiceConfig;
import com.spring.microservice.model.License;
import com.spring.microservice.repository.LicenseRepository;

@Service
public class LicenseService {
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Autowired
	ServiceConfig config;
	
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
