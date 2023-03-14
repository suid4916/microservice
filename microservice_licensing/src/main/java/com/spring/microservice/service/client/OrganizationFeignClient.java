package com.spring.microservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.spring.microservice.model.Organization;

@FeignClient("organization-service")
public interface OrganizationFeignClient {
	@GetMapping(value = "/v1/organization/{organizationId}", consumes = "application/json")
	Organization getOrganization(@PathVariable("organizationId") String organizationId);
}
