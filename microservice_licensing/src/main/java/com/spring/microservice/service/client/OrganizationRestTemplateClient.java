package com.spring.microservice.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.spring.microservice.model.Organization;

@Component
public class OrganizationRestTemplateClient {
	
	@Autowired
	RestTemplate restTemplate;
	
	public Organization getOrganization(String organizationId) {
		ResponseEntity<Organization> restExchange = restTemplate.exchange(
				"http://organization-service/v1/organization/{organizationId}", HttpMethod.GET, null,
				Organization.class, organizationId);
		return restExchange.getBody();
	}

	public Organization getOrganization(String organizationId, String requestMethod) {
		// TODO Auto-generated method stub
		ResponseEntity<Organization> restExchange = restTemplate.exchange(
				"http://organization-service/v1/organization/{organizationId}/post", HttpMethod.POST, null,
				Organization.class, organizationId);
		return restExchange.getBody();
	}
	
}
